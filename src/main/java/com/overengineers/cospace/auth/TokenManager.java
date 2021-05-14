package com.overengineers.cospace.auth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class TokenManager {

    private static final int expirationDay = 7;
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public static String generateToken(Authentication user){

        return Jwts.builder()
                .setSubject(user.getName())
                .claim("authorities", getAuthorities(user))
                .setIssuer("www.cospace.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(calculateExpirationDate(expirationDay))
                .signWith(key)
                .compact();
    }

    public static List<String> getAuthorities(Authentication user) {
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private static Date calculateExpirationDate(Integer expirationDay){
        Instant expirationTime = LocalDate.now()
                .plusDays(expirationDay)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return Date.from(expirationTime);
    }

    public static boolean tokenValidate(String token){
        if (getUsernameFromToken(token) != null && !isExpired(token)){
            return true;
        }
        return false;
    }

    public static String getUsernameFromToken(String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    private static boolean isExpired(String token){
        Claims claims = getClaims(token);
        return claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }

    private static Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }


}
