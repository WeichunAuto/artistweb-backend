package com.bobby.artistweb.service;

import com.bobby.artistweb.config.SecurityConfig;
import com.bobby.artistweb.model.JwtObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private String secretKey;

    public JwtService()  {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKeyBeforeEncode = keyGen.generateKey();
            System.out.println(secretKeyBeforeEncode.toString());

            // base64 encode
            this.secretKey =  Base64.getEncoder().encodeToString(secretKeyBeforeEncode.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public JwtObject generateToken(String name) {
        Map<String, Object> claims = new HashMap<>();

        long tokenExpiration = System.currentTimeMillis() + SecurityConfig.TOKEN_EXPIRE_TIME_Millis;

        String token =  Jwts.builder()
                .setClaims(claims)
                .setSubject(name)
                .setIssuedAt(new Date(System.currentTimeMillis())) // jwt token publish date
                .setExpiration(new Date(tokenExpiration)) // jwt token expired date
                .signWith(this.getKey(), SignatureAlgorithm.HS256)
                .compact();

        JwtObject jwtObject = new JwtObject(token, tokenExpiration);
        return jwtObject;
    }


    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String appname = this.extractAppName(jwtToken);

        return (appname.equals(userDetails.getUsername()) && !this.isTokenExpired(jwtToken));
    }

    private boolean isTokenExpired(String jwtToken) {
        return this.extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public String extractAppName(String jwtToken) {
        return this.extractClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(jwtToken).getBody();
    }
}
