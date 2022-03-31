package com.gru.ifsp.AgendamentoBanca.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.gru.ifsp.AgendamentoBanca.util.Constants.SECRET;

@Component
public class JwtUtil {

    private static final int minutes = 60 * 1000;
    private static final int TOKEN_VALIDITY = 10 * minutes;
    private static final int REFRESH_TOKEN_VALIDITY = 30 * minutes;

    public static final Algorithm signatureAlgorithm = Algorithm.HMAC256(SECRET.getBytes());


    public static String generateSessionToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .sign(signatureAlgorithm);
    }

    public static String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .sign(signatureAlgorithm);
    }
}
