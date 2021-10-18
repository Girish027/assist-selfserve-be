package com.tfsc.ilabs.selfservice.common.utils;

import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Sushil.Kumar
 */
public class JwtUtil {

    private JwtUtil(){}

    //TODO Vault Integration required for RSA key
    public static Algorithm loadRSAAlgorithmToSign(String algorithm, String keyPath) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException{
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        Path path = Paths.get(keyPath);
        byte[] bytes = Files.readAllBytes(path);
        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(bytes);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(encodedKeySpec);
        return Algorithm.RSA256(null, privateKey);
    }

    //TODO Vault Integration required for RSA key
    public static Algorithm loadRSAAlgorithmToVerify(String algorithm, String keyPath) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException{
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        Path path = Paths.get(keyPath);
        byte[] bytes = Files.readAllBytes(path);
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(bytes);
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(encodedKeySpec);
        return Algorithm.RSA256(publicKey, null);
    }

}
