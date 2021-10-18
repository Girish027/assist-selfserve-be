package com.tfsc.ilabs.selfservice.common.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.UserAccessTokenInfo;
import com.tfsc.ilabs.selfservice.common.services.api.ExternalAuthBuilder;
import com.tfsc.ilabs.selfservice.common.services.api.ExternalAuthConfigService;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import com.tfsc.ilabs.selfservice.common.utils.JwtUtil;
import com.tfsc.ilabs.selfservice.security.service.SecurityContextUtil;
import com.tfsc.ilabs.selfservice.security.service.SessionValidatorWithOKTA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.HashMap;
import java.util.Map;

/**
 * Assist JWT token builder
 * @author Sushil.Kumar
 */
@Component("assistExternalAuthTokenBuilder")
@Slf4j
public class AssistExternalAuthTokenBuilder implements ExternalAuthBuilder {
  
    private static final String DEFAULT_KEY_ID = "key123000237";
    public static final String SERVICE_ASSIST = "assist";
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private ExternalAuthConfigService externalAuthConfigService;

    /**
     * Generates Signed token using defined Algorithm for the Assist Service
     * @return
     */
    @Override
    public String generateSignedToken(Environment env) {
        String token = null;
        String accessToken = SecurityContextUtil.getAuthenticationToken().getAccessToken();
        UserAccessTokenInfo userAccessTokenInfo = SessionValidatorWithOKTA.getTokenMap().get(accessToken);

        Map<String, UserAccessTokenInfo> externalApiTokenMap = userAccessTokenInfo.getExternalApiTokenMap();
        try {
            if(externalApiTokenMap != null && isTokenValid(externalApiTokenMap.get(SERVICE_ASSIST))){
                token = externalApiTokenMap.get(SERVICE_ASSIST).getToken();
            }else {
                String issuerName = externalAuthConfigService.getValue(Constants.DB_CONFIG_ISS, SERVICE_ASSIST);
                Integer expiryTimeInSec = Integer.parseInt(externalAuthConfigService.getValue(Constants.DB_CONFIG_EXP, SERVICE_ASSIST));
                String algorithm = externalAuthConfigService.getValue(Constants.DB_CONFIG_ALG, SERVICE_ASSIST);
                String privateKeyPathInDB = Constants.DB_CONFIG_PRIVATE_KEY_TEST;
                if(env.equals(Environment.LIVE)){
                    privateKeyPathInDB = Constants.DB_CONFIG_PRIVATE_KEY_LIVE;
                }
                String privateKeyPath = externalAuthConfigService.getValue(privateKeyPathInDB, SERVICE_ASSIST);
                long currentInMillis = System.currentTimeMillis();
                long exp = currentInMillis + expiryTimeInSec * Constants.NUM_THOUSAND;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                token = JWT.create()
                        .withIssuer(issuerName)
                        .withIssuedAt(simpleDateFormat.parse(simpleDateFormat.format(new Date(currentInMillis))))
                        .withExpiresAt(simpleDateFormat.parse(simpleDateFormat.format(new Date(exp))))
                        .withSubject(SecurityContextUtil.getCurrentUserName())
                        .withClaim(Constants.USER_ID, SecurityContextUtil.getCurrentUserId())
                        .withClaim(Constants.EMAIL_ID, userAccessTokenInfo.getSub())
                        .withKeyId(DEFAULT_KEY_ID)  //TODO will change during Vault integration
                        .sign(JwtUtil.loadRSAAlgorithmToSign(algorithm, privateKeyPath));
                if(userAccessTokenInfo.getExternalApiTokenMap() == null){
                    userAccessTokenInfo.setExternalApiTokenMap(new HashMap<>());
                }
                UserAccessTokenInfo externalApiToken = new UserAccessTokenInfo();
                externalApiToken.setToken(token);
                externalApiToken.setExp(exp);
                externalApiToken.setCreatedAtMillis(currentInMillis);
                externalApiToken.setIat(currentInMillis);
                userAccessTokenInfo.getExternalApiTokenMap().put(SERVICE_ASSIST, externalApiToken);
                log.info("New Token generated for Service: {}, issuer: {}, user: {}, emailId: {}",
                        SERVICE_ASSIST, issuerName, SecurityContextUtil.getCurrentUserId(), userAccessTokenInfo.getSub() );
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("Invalid Algorithm, Expected RSA, User {} ",SecurityContextUtil.getCurrentUserId(), e);
        } catch (IOException e) {
            log.error("Error while reading private key file, User {}", SecurityContextUtil.getCurrentUserId(), e);
        } catch (Exception e) {
            log.error("Error while creating Token, User {}", SecurityContextUtil.getCurrentUserId(), e);
        }
        return token;
    }

    /**
     * Verify if token is valid and not expired
     * @param token
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws JWTVerificationException
     */
    @Override
    public DecodedJWT verify(String token,Environment env) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, JWTVerificationException {
        String issuerName = externalAuthConfigService.getValue(Constants.DB_CONFIG_ISS, SERVICE_ASSIST);
        String algorithm = externalAuthConfigService.getValue(Constants.DB_CONFIG_ALG, SERVICE_ASSIST);
        String publicKeyPathInDB = Constants.DB_CONFIG_PUBLIC_KEY_TEST;
        if(env.equals(Environment.LIVE)){
            publicKeyPathInDB = Constants.DB_CONFIG_PUBLIC_KEY_LIVE;
        }
        String publicKeyPath = externalAuthConfigService.getValue(publicKeyPathInDB, SERVICE_ASSIST);
        JWTVerifier jwtVerifier = JWT.require(JwtUtil.loadRSAAlgorithmToVerify(algorithm, publicKeyPath))
                .withIssuer(issuerName)
                .build();
        return jwtVerifier.verify(token);
    }

    /**
     * Validate token Expiry Time
     * @param externalApiToken
     * @return
     */
    private boolean isTokenValid(UserAccessTokenInfo externalApiToken){
        try{
            if(externalApiToken != null && System.currentTimeMillis() < externalApiToken.getExp()) {
                return true;
            }
        }catch (Exception e){
            log.info("Error validating token, User {}", SecurityContextUtil.getCurrentUserId(), e.getMessage());
        }
        return false;
    }
}
