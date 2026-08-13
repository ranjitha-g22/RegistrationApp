package com.registrationjwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.key}")
    private String key;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiry}")
    private long expiry;

    private Algorithm algorithm;
    @PostConstruct
    public void postConstruct() throws UnsupportedEncodingException{
        algorithm=Algorithm.HMAC256(key);
    }
    public String generateToken(String userName){
        return JWT.create()
                .withClaim("userName",userName)
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis()+expiry))
                .sign(algorithm);
    }
    public String getUserName(String token){
        DecodedJWT decodedToken=JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
        return decodedToken.getClaim("userName").asString();
    }
}
