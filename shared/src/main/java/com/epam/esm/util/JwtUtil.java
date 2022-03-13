package com.epam.esm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${jwt.util.secret}")
    private String secretKey;

    @Value("${jwt.util.exp}")
    private long expirationMillis;

    @Value("${jwt.util.iss}")
    private String issuer;

    public String createJwt(String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + expirationMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(SIGNATURE_ALGORITHM, signingKey);

        return builder.compact();
    }

    public Claims decodeJwt(String jwt) {
        byte[] key = DatatypeConverter.parseBase64Binary(secretKey);
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
