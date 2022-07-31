package com.example.praca.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * @author Szymon Królik
 */
@Service
public class JwtTokenService {

    private final static int tokenExpirationTime = 30 * 60 * 1000;

    private static String secret;

    @Value("${jwt.secret}")
    public void setSecret(String secret){
        JwtTokenService.secret = secret;
    }

    public static String generateJwtoken(String email, Claims claims) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static void verifyJwtoken(String token) throws JwtException {
        Jwts.parser()
                .setSigningKey(secret)
                .parse(token.substring(7));
    }

    public static Claims getClaimsFromJwtoken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.substring(7))
                .getBody();
    }

    public static String getJwtokenFromHeader() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
    }
}