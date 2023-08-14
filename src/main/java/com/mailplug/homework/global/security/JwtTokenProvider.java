package com.mailplug.homework.global.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenExpireLength;

    @Value("${jwt.issuer}")
    private String issuer;

    public String createAccessToken(final Long memberId) {

        final Date date = new Date();
        final Date validity = new Date(date.getTime() + this.accessTokenExpireLength);

        return Jwts.builder()
                .setIssuer(this.issuer)
                .setSubject(this.issuer + "/members/" + memberId)
                .claim("memberId", memberId)
                .setIssuedAt(date)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public Long decodeAccessToken(final String accessToken) {

        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(accessToken)
                .getBody()
                .get("memberId", Long.class);
    }
}
