package com.gru.ifsp.AgendamentoBanca.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gru.ifsp.AgendamentoBanca.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.gru.ifsp.AgendamentoBanca.util.Constants.PREFIX_AUTHORIZATION;
import static com.gru.ifsp.AgendamentoBanca.util.Constants.SECRET;

@Component
public class JwtUtil {

    private static final int minutes = 60 * 1000;
    private static final Long ACCESS_TOKEN_VALIDITY = 30L * minutes;
    private static final Long REFRESH_TOKEN_VALIDITY = 60L * minutes;

    public static final Algorithm signatureAlgorithm = Algorithm.HMAC256(SECRET.getBytes());

    public static DecodedJWT getDecodedJWTFromToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(JwtUtil.signatureAlgorithm).build();
        return verifier.verify(token);
    }

    public static String generateAccessToken(Usuario user, String url) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .withIssuer(url)
                .sign(signatureAlgorithm);
    }

    public static String generateRefreshToken(Usuario user, String url) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .withIssuer(url)
                .sign(signatureAlgorithm);
    }


    public static DecodedJWT checkAndGetDecodedJWT(String authorizationHeader) throws JWTVerificationException {
        String accessToken = authorizationHeader.substring(PREFIX_AUTHORIZATION.length());
        return getDecodedJWTFromToken(accessToken);
    }

}
