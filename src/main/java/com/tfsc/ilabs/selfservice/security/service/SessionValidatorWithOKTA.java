package com.tfsc.ilabs.selfservice.security.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.JsonArray;
import com.tfsc.ilabs.selfservice.common.exception.InternalException;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.models.ComponentInfo;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.utils.BaseUtil;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.HttpUtils;
import com.tfsc.ilabs.selfservice.common.utils.MockUtils;
import com.tfsc.ilabs.selfservice.configpuller.services.api.DBConfigService;
import com.tfsc.ilabs.selfservice.common.models.AccessTokenDetails;
import com.tfsc.ilabs.selfservice.common.models.UserProfile;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.util.StringUtils;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

/**
 * Created by ravi.b on 30/07/2019.
 */
public class SessionValidatorWithOKTA implements SessionValidator {

    @Autowired
    private DBConfigService dbConfigService;

    @Autowired
    Environment environment;

    private static final Logger logger =
            LoggerFactory.getLogger(SessionValidatorWithOKTA.class);
    private static final String OAUTH2 = "/oauth2/";
    private static final String INTROSPECT = "v1/introspect";
    private static final String AUTHORIZE = "v1/authorize";
    private static final String TOKENURL = "/v1/token";
    private static final String LOGOUTURL = "/v1/logout";

    private static Map<String, UserAccessTokenInfo> tokenMap = new ConcurrentHashMap<>();
    private static Map<String, Map<String,ComponentInfo>> clientComponentMap = new ConcurrentHashMap<>();
    private String umsBaseUrl;
    private String baseUrl;
    private String authorizationServerId;
    private String oidcClientId;
    private String oidcClientSecret;
    private String oktaConnectionTimeout;
    private String oktaConnectionTimeoutMultiply;
    private String oktaConnectionProxy;
    private String oktaConnectionProxyHost;
    private String oktaConnectionProxyPort;
    private String oktaConnectionProxyProtocol;
    private boolean isAuthorizationEnabled;
    private boolean isUMSEnabled;
    private ObjectMapper mapper = new ObjectMapper();
    private CloseableHttpClient httpclient;

    public SessionValidatorWithOKTA(String baseUrl, String authorizationServerId, String oidcClientId, String oidcClientSecret, String oktaConnectionTimeout,
                                    String oktaConnectionTimeoutMultiply, String oktaConnectionProxy, String oktaConnectionProxyHost, String oktaConnectionProxyPort,
                                    String oktaConnectionProxyProtocol, String umsBaseUrl, boolean isAuthorizationEnabled, boolean isUMSEnabled) {
        this.baseUrl = baseUrl;
        this.authorizationServerId = authorizationServerId;
        this.oidcClientId = oidcClientId;
        this.oidcClientSecret = oidcClientSecret;
        this.oktaConnectionTimeout = oktaConnectionTimeout;
        this.oktaConnectionTimeoutMultiply = oktaConnectionTimeoutMultiply;
        this.oktaConnectionProxy = oktaConnectionProxy;
        this.oktaConnectionProxyHost = oktaConnectionProxyHost;
        this.oktaConnectionProxyPort = oktaConnectionProxyPort;
        this.oktaConnectionProxyProtocol = oktaConnectionProxyProtocol;
        this.umsBaseUrl = umsBaseUrl;
        this.isAuthorizationEnabled = isAuthorizationEnabled;
        this.isUMSEnabled = isUMSEnabled;
    }

    public static Map<String, UserAccessTokenInfo> getTokenMap() {
        return tokenMap;
    }

    /**
     * Remove token from tokenMap
     *
     * @param accessToken
     */
    public static void removeTokenFromMap(String accessToken) {
        tokenMap.remove(accessToken);
    }

    /**
     * Method to initialize the proxy for OKTA calls
     */
    @PostConstruct
    void init() {
        httpclient = BaseUtil.getOktaHttpClient(oktaConnectionTimeoutMultiply, oktaConnectionTimeout, oktaConnectionProxy, oktaConnectionProxyHost, oktaConnectionProxyPort, oktaConnectionProxyProtocol);
    }

    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public UUID getUUID(HttpServletRequest request) {
        String accessToken = HttpUtils.getSessionCookies(request);
        return UUID.nameUUIDFromBytes(!BaseUtil.isNullOrBlank(accessToken) ? accessToken.getBytes() : Constants.DEFAULT_USER.getBytes());
    }

    /**
     * Method to Introspect the accessToken provided by front end. If the OKTA returns active token only then the APIs will be executed
     *
     * @param request
     * @return
     */
    @Override
    public UserSession validateSession(HttpServletRequest request) {
        String accessToken = HttpUtils.getSessionCookies(request);
        if(BaseUtil.isNullOrBlank(accessToken)){
            logger.error("missing access token in request " +  request.getRequestURI());
            throw new BadCredentialsException("missing access token in request "+ request.getRequestURI());
        }
        try {
            UserAccessTokenInfo userAccessToken = getTokenDetails(accessToken, request);
            if (userAccessToken == null || !userAccessToken.isActive()) {
                throw new CredentialsExpiredException("accessToken is invalid");
            }
            validateRequest(request, userAccessToken);
            UserSession userSession = new UserSession();
            userSession.setUserId(userAccessToken.getSub());
            userSession.setUserName(userAccessToken.getUsername());
            userSession.setAccessToken(accessToken);
            return userSession;

        } catch (org.springframework.security.core.AuthenticationException e) {
            throw e;
        } catch (IOException e) {
            logger.error("Error while executing the Introspect API", e);
            throw new InternalException("error while executing the Introspect API");
        } catch (ForbiddenException e) {
            logger.error("User not authorized for this client");
            throw new ForbiddenException(e.getMessage());
        } catch (Exception e) {
            logger.error("Error occured while validating OKTA Session", e);
            throw new InternalException("error occured while validating OKTA Session");
        }
    }

    private void validateRequest(HttpServletRequest request, UserAccessTokenInfo userAccessToken) {
        if (request.getRequestURI().contains("/client/")) {
            String clientId = getPathVariable(request, "/client/*");
            if (!userAccessToken.getClientIds().contains(clientId)) {
                throw new ForbiddenException("User is not authorized for the request");
            }
        }
    }

    /**
     * Returns token details from tokenMap if not expired else validates with Okta auth server
     * for the same and returns
     *
     * @param accessToken
     * @param request
     * @return
     * @throws IOException
     * @throws AuthenticationException
     */
    public UserAccessTokenInfo getTokenDetails(String accessToken, HttpServletRequest request) throws IOException, AuthenticationException {
        UserAccessTokenInfo userAccessToken = tokenMap.get(accessToken);
        if (userAccessToken != null && userAccessToken.isActive()) {
            long expMillis = userAccessToken.getExp();
            long currentMillis = Instant.now().getEpochSecond();
            if (currentMillis > expMillis) {
                tokenMap.remove(accessToken);
                userAccessToken = validateTokenWithOkta(accessToken, request);
            }
        } else {
            userAccessToken = validateTokenWithOkta(accessToken, request);
        }
        return userAccessToken;
    }

    /**
     * Validates access token with Okta auth server
     *
     * @param accessToken
     * @param request
     * @return
     * @throws IOException
     * @throws AuthenticationException
     */
    private UserAccessTokenInfo validateTokenWithOkta(String accessToken, HttpServletRequest request) throws IOException, AuthenticationException {
        if (BaseUtil.isInvalidateSessionRequest(request) || BaseUtil.isErrorRequest(request)) {
            return null;
        }

        logger.debug("Validating token with Okta Auth Server");
        HttpPost httpPost = new HttpPost(getOktaIntrospectUrl());
        httpPost.addHeader(new BasicScheme().authenticate(getCredentials(), httpPost, null));
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(Constants.TOKEN, accessToken));
        nvps.add(new BasicNameValuePair(Constants.TOKEN_TYPE_HINT, Constants.SESSION_COOKIE_NAME));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        HttpResponse response = httpclient.execute(httpPost);
        UserAccessTokenInfo userAccessTokenFromMap = tokenMap.get(accessToken);
        UserAccessTokenInfo userAccessToken = mapper.readValue(response.getEntity().getContent(), UserAccessTokenInfo.class);
        if (!userAccessToken.isActive()) {
            throw new CredentialsExpiredException("token is invalid");
        }
        userAccessToken.setToken(accessToken);
        userAccessToken.setCreatedAtMillis(Instant.now().getEpochSecond());
        if(userAccessTokenFromMap != null){
            userAccessToken.setAccessTokenDetails(userAccessTokenFromMap.getAccessTokenDetails());
        }

        if (isAuthorizationEnabled) {
            logger.debug("Fetching user roles from UMS server");
            JsonNode roleResponse = getUserRoles(userAccessToken);

            logger.debug("Store user role response");
            Map<String, List<String>> roles = getUserRolesFromResponse(roleResponse, userAccessToken);
            userAccessToken.setRoles(roles);
        }

        tokenMap.put(accessToken, userAccessToken);
        if (StringUtils.isEmpty(userAccessToken.getSub()) && userAccessToken.isActive()) {
            logger.error("userEmail not found in OKTA response. request={}, oktaIntrospectUrl={}, response from SSO={}", request, getOktaIntrospectUrl(), response);
            throw new CredentialsExpiredException("userEmail not found in OKTA response");
        }
        logger.info("User logged in with Username : {}", userAccessToken.getUsername());
        return userAccessToken;
    }

    @Override
    public void logout(String tokenId) {
        logger.info("Logout is currently handled in front end");
    }

    private String getOktaIntrospectUrl() {
        String oktaIntrospectUrl = null;
        if (StringUtils.isEmpty(authorizationServerId)) {
            oktaIntrospectUrl = baseUrl + OAUTH2 + INTROSPECT;
        } else {
            oktaIntrospectUrl = baseUrl + OAUTH2 + authorizationServerId + '/' + INTROSPECT;
        }
        return oktaIntrospectUrl;
    }

    private UsernamePasswordCredentials getCredentials() {
        return new UsernamePasswordCredentials(oidcClientId, oidcClientSecret);
    }

    public JsonNode getUserRoles(UserAccessTokenInfo userAccessTokenInfo) {
        JsonNode data;
        String url = getUMSRolesUrl(userAccessTokenInfo.getUid());
        try {
            String response = isUMSEnabled ? HttpUtils.makeGetCall(url).body() : MockUtils.getUMSRoles();
            data = mapper.readTree(response);
        } catch (Exception e) {
            throw new SelfServeException("Fetching UserRoles", e);
        }
        return data;
    }

    private String getUMSRolesUrl(String uid) {
        return umsBaseUrl + Constants.UMS_USER_RESOURCE_PATH + "/" + uid + "?product=chat";
    }

    private Map<String, List<String>> getUserRolesFromResponse(JsonNode data, UserAccessTokenInfo userAccessTokenInfo) {

        Map<String, List<String>> roleMap = new HashMap<>();
        List<String> clientIds = new ArrayList<>();
        JsonNode clients = data.get("clients");
        if (clients.isArray()) {
            clients.forEach(client -> {
                JsonNode accounts = client.get("accounts");
                if (accounts.isArray()) {
                    accounts.forEach(account -> {
                        ArrayNode products = (ArrayNode) account.get("products");
                        JsonNode chatProduct = StreamSupport.stream(products.spliterator(), false)
                                .filter(product -> ("chat").equals(product.get("productId").asText("")))
                                .findFirst()
                                .orElse(null);
                        Map<String,ComponentInfo> componentMap = new HashMap<>();
                        if (chatProduct != null) {
                            String clientId = client.get("clientId").asText();
                            clientIds.add(clientId);
                            String accountId = account.get("accountId").asText();
                            List<String> roles = new ArrayList<>();
                            JsonNode rolesJson = chatProduct.get("roles");
                            if (rolesJson != null && rolesJson.isArray()) {
                                rolesJson.forEach(role -> roles.add(role.asText()));
                            } else {
                                roles.add(chatProduct.get("role").asText());
                            }
                            roleMap.put(clientId + accountId, roles);
                            JsonNode componentsJson = chatProduct.get("components");
                            componentsJson.forEach(component -> {
                                ComponentInfo componentInfo = mapper.convertValue(component, ComponentInfo.class);
                                String supportedComponents = dbConfigService.findByCode(Constants.COMPONENT_LIST, String.class);
                                if(supportedComponents.contains(componentInfo.getComponentId().toLowerCase())){
                                    componentMap.put(componentInfo.getComponentId().toLowerCase(), componentInfo);
                                }
                            });
                            clientComponentMap.put(clientId, componentMap);
                        }
                    });
                }
            });
        }
        userAccessTokenInfo.setClientIds(clientIds);
        return roleMap;
    }

    private String getPathVariable(HttpServletRequest httpRequest, String pattern) {
        return httpRequest.getRequestURI().split(pattern)[1].split("/")[0];
    }

    public ComponentInfo getComponentInfoFromTokenMap(String clientId, String componentId){

        Map<String,ComponentInfo> componentMap = clientComponentMap.get(clientId);;
        return componentMap.get(componentId.toLowerCase());
    }
    private String getOktaAuthorizeUrl(){
        String oktaAuthorizeUrl = null;

        if (!StringUtils.isEmpty(authorizationServerId)) {
            oktaAuthorizeUrl = baseUrl + OAUTH2 + authorizationServerId + '/' + AUTHORIZE;
        }
        return oktaAuthorizeUrl;
    }

    public String getAuthRedirectUrl(HttpServletRequest request) throws URISyntaxException {
        String redirect_uri =environment.getProperty(Constants.SERVICE_BASE_URL)+ Constants.OKTA_AUTHORIZATION_CODE_CALLBACK_URI;
        
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(Constants.OKTA_URL_CLIENTID, oidcClientId));
        nvps.add(new BasicNameValuePair(Constants.OKTA_URL_RESPONSETYPE, Constants.OKTA_CODE));
        nvps.add(new BasicNameValuePair(Constants.OKTA_REDIRECT_URI, redirect_uri));
        nvps.add(new BasicNameValuePair(Constants.OKTA_SCOPE,Constants.OKTA_LOGIN_SCOPE));
        nvps.add(new BasicNameValuePair(Constants.OKTA_STATE, UUID.randomUUID().toString()));
        URI uri = new URIBuilder(getOktaAuthorizeUrl()).addParameters(nvps).build();
        return uri.toString();
    }

    private String getTokenUrl(){
        String oktaTokenUrl = null;
        if (!StringUtils.isEmpty(authorizationServerId)) {
            oktaTokenUrl = baseUrl + OAUTH2 + authorizationServerId + TOKENURL;
        }
        return oktaTokenUrl;
    }
    public UserAccessTokenInfo getTokenFromCode(String code, HttpServletRequest request) throws IOException, ServletException, AuthenticationException {
        String tokenUrl = getTokenUrl();
        HttpPost httpPost = new HttpPost(tokenUrl);
        httpPost.addHeader("content-type", "application/x-www-form-urlencoded" );
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(Constants.OKTA_GRANT_TYPE, Constants.OKTA_AUTHORIZATION_CODE));
        nvps.add(new BasicNameValuePair(Constants.OKTA_URL_CLIENTID, oidcClientId));
        nvps.add(new BasicNameValuePair(Constants.OKTA_URL_CLIENT_SECRET, oidcClientSecret));
        nvps.add(new BasicNameValuePair(Constants.OKTA_CODE, code));
        String redirect_uri = environment.getProperty(Constants.SERVICE_BASE_URL)+ Constants.OKTA_AUTHORIZATION_CODE_CALLBACK_URI;
        nvps.add(new BasicNameValuePair(Constants.OKTA_REDIRECT_URI, redirect_uri));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        HttpResponse response = httpclient.execute(httpPost);
        AccessTokenDetails accessTokenInfo = mapper.readValue(response.getEntity().getContent(), AccessTokenDetails.class);
        UserAccessTokenInfo userAccessTokenInfo = new UserAccessTokenInfo();
        userAccessTokenInfo.setAccessTokenDetails(accessTokenInfo);
        tokenMap.put(accessTokenInfo.getAccessToken(), userAccessTokenInfo);
        return userAccessTokenInfo;
    }
    public UserProfile getUserProfileFromOktaAndUMS(UserAccessTokenInfo userAccessToken){
        JsonNode clients = null;
        if (isAuthorizationEnabled) {
            logger.debug("Fetching user roles from UMS server");
            JsonNode roleResponse = getUserRoles(userAccessToken);
            clients = roleResponse.get("clients");
        }
        return new UserProfile(userAccessToken.getUsername(),userAccessToken.getUsername(),userAccessToken.getUsername(),"{}",baseUrl , clients);

    }
    private String getOktaLogoutUrl(){
        String oktaLogUrl = null;

        if (!StringUtils.isEmpty(authorizationServerId)) {
            oktaLogUrl = baseUrl + OAUTH2 + authorizationServerId + LOGOUTURL;
        }
        return oktaLogUrl;
    }
    public String getOktaLogoutRedirectUrl(HttpServletRequest request) throws URISyntaxException {
        String accessToken = HttpUtils.getSessionCookies(request);
        UserAccessTokenInfo userAccessTokenInfo  = tokenMap.get(accessToken);
        AccessTokenDetails accessTokenDetails = userAccessTokenInfo.getAccessTokenDetails();
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(Constants.OKTA_ID_TOKEN_HINT, accessTokenDetails.getIdToken()));
        nvps.add(new BasicNameValuePair(Constants.OKTA_LOGOUT_REDIRECT_URI, environment.getProperty(Constants.SERVICE_BASE_URL)));
        URI uri = new URIBuilder(getOktaLogoutUrl()).addParameters(nvps).build();
        removeTokenFromMap(accessToken);
        return uri.toString();
    }
}

