package com.overengineers.cospace.Auth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class TokenManager {

    // validityTime as ms -> (24: hour - 60: min -  60: sec - 1000: ms)
    private static final int validityTime = 24 * 60 * 60 * 1000;
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public String generateToken(String username){

        return Jwts.builder()
                .setSubject(username)
                .setIssuer("www.cospace.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityTime))
                .signWith(key)
                .compact();
    }

    public boolean tokenValidate(String token){
        if (getUsernameFromToken(token) != null && !isExpired(token)){
            return true;
        }
        return false;
    }

    public String getUsernameFromToken(String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    private boolean isExpired(String token){
        Claims claims = getClaims(token);
        return claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }


}
