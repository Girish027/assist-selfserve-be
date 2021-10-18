package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import io.swagger.models.HttpMethod;
import javassist.bytecode.ByteArray;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpUtils {

    private static final Logger logger =
            LoggerFactory.getLogger(HttpUtils.class);


    private HttpUtils() {
        // hide implicit public constructor
    }

    public static HttpResponse<String> makeGetCall(String url) {

        return makeGetCall(url, new ObjectMapper().createObjectNode());
    }

    public static HttpResponse<String> makeGetCall(String url, JsonNode headers) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));

            setHeadersFromJsonNode(requestBuilder, headers);

            HttpRequest request = requestBuilder.build();
            logger.info("HttpUtils:: makeGetCall:: requestUrl:: " + url);
            return HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new SelfServeException(Constants.HTTP_GET_ERROR_MSG + ex.getMessage());
        }
    }

    public static HttpResponse<String> makeGetCall(String url, JsonNode headers, JsonNode queryParameters) {
        try {
            URI uri = getUriWithQueryParameters(url, queryParameters);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri);

            setHeadersFromJsonNode(requestBuilder, headers);

            HttpRequest request = requestBuilder.build();
            logger.info("HttpUtils:: makeGetCall:: requestUrl:: " + uri);
            return HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new SelfServeException(Constants.HTTP_GET_ERROR_MSG + ex.getMessage());
        }
    }

    public static HttpResponse<byte[]> makeGetCallOfArray(String url, JsonNode headers) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));

            setHeadersFromJsonNode(requestBuilder, headers);

            HttpRequest request = requestBuilder.build();
            logger.info("HttpUtils:: makeGetCallOfArray:: requestUrl:: " + url);
            return HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofByteArray());
        } catch (Exception ex) {
            throw new SelfServeException(Constants.HTTP_GET_ERROR_MSG + ex.getMessage());
        }
    }

    public static ResponseEntity<String> sendPOST(String url, Object body, Map<String, String> headerMap, String entityKey) {
        return sendPOST(url, body, headerMap, entityKey, false);
    }

    public static ResponseEntity<String> sendPOST(String url, Object body, Map<String, String> headerMap, String entityKey, Boolean isQueryParamEncoded) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headerMap.forEach(headers::set);
            if (!isQueryParamEncoded && url.contains("?")) {

                String[] urls = url.split("\\?");
                String encodedParams = encodeQuery(urls[1]);
                url = urls[0] + "?" + encodedParams;
            }

            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add(entityKey, body);

            HttpEntity request = new HttpEntity<>(map, headers);
            RestTemplate restTemplate = new RestTemplate();
            logger.info("HttpUtils:: sendPOST:: requestUrl:: " + url);
            return restTemplate.postForEntity(url, request, String.class);
        }
        catch (Exception ex) {
            throw new SelfServeException("Got exception while making POST/PUT call :" + ex.getMessage(), ex);
        }
    }

    public static HttpResponse<String> makePostOrPutCall(String url, HttpMethod httpMethod, Map<String, String> headers, String body) {
        return makePostOrPutCall(url, httpMethod, headers, body, false);
    }

    public static HttpResponse<String> makePostOrPutCall(String url, HttpMethod httpMethod, Map<String, String> headers, String body, Boolean isQueryParamEncoded ) {
        try {
            if (!isQueryParamEncoded && url.contains("?")) {
                String[] urls = url.split("\\?");
                String encodedParams = encodeQuery(urls[1]);
                url = urls[0] + "?" + encodedParams;
            }
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));

            setHeadersFromHashMap(requestBuilder, headers);

            switch (httpMethod) {
                case PUT:
                    requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body));
                    break;
                case POST:
                default:
                    requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
                    break;
            }
            HttpRequest request = requestBuilder.build();
            logger.info("HttpUtils:: makePostOrPutCall:: requestUrl:: " + url);
            return HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new SelfServeException("Got exception while making POST/PUT call :" + ex.getMessage(), ex);
        }
    }

    private static void setHeadersFromHashMap(HttpRequest.Builder requestBuilder, Map<String, String> headers) {
        headers.forEach(requestBuilder::header);
    }

    private static void setHeadersFromJsonNode(HttpRequest.Builder requestBuilder, JsonNode headers) {
        Iterator<String> fieldNames;
        if (headers != null) {
            fieldNames = headers.fieldNames();
            if (fieldNames != null) {
                while (fieldNames.hasNext()) {
                    String value = "";
                    String fieldName = fieldNames.next();
                    JsonNode fieldValue = headers.get(fieldName);
                    if (fieldValue.isTextual()) {
                        value = fieldValue.asText();
                    }
                    requestBuilder.header(fieldName, value);
                }
            }
        }
    }

    private static URI getUriWithQueryParameters(String url, JsonNode queryParameters) throws URISyntaxException {
        Iterator<String> fieldNames;
        URIBuilder uriBuilder = new URIBuilder(url);
        if (queryParameters != null) {
            fieldNames = queryParameters.fieldNames();
            if (fieldNames != null) {
                while (fieldNames.hasNext()) {
                    String value = "";
                    String fieldName = fieldNames.next();
                    JsonNode fieldValue = queryParameters.get(fieldName);
                    uriBuilder.addParameter(fieldName, fieldValue.toString());
                }
            }
        }
        return uriBuilder.build();
    }

    private static String encodeQuery(String query) {
        String[] params = query.split("&");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            stringBuilder.append(params[i].split("=")[0]);
            stringBuilder.append("=");
            if (params[i].split("=").length > 1)
                stringBuilder.append(URLEncoder.encode(params[i].split("=")[1], StandardCharsets.UTF_8));
            if (i + 1 != params.length) {
                stringBuilder.append("&");
            }
        }
        return stringBuilder.toString();
    }
    
    public static String getSessionCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        if (cookies != null) {
            Optional<String> optionalAccessToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> Constants.SESSION_COOKIE_NAME.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findAny();
            if(optionalAccessToken.isPresent()){
                accessToken = optionalAccessToken.get();
            }
            else if(request.getHeader("access_token")!=null){
                accessToken = request.getHeader("access_token");
            }
        }
        else if(request.getHeader("access_token")!=null){
            accessToken = request.getHeader("access_token");
        }

        return accessToken;
    }

}
