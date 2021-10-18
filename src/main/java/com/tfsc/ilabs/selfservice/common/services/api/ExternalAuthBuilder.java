package com.tfsc.ilabs.selfservice.common.services.api;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tfsc.ilabs.selfservice.common.models.Environment;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 *
 * @author Sushil.Kumar
 */
public interface ExternalAuthBuilder {
    String generateSignedToken(Environment env);
    DecodedJWT verify(String token,Environment env) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, JWTVerificationException;
}
