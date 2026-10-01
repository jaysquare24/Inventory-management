package com.inventory.ManagementSystem.security;

import com.inventory.ManagementSystem.model.Staff;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // Generate a Base64 encoded secret key and place it here
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final SecureRandom random = new SecureRandom();


    public String generateOTP(){
        int otp = random.nextInt(900000)+100000;
        return String.valueOf(otp);
    }


    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateResetPasswordToken(String email){

        return Jwts.builder()
                .subject(email)
                .claim("purpose", "PASSWORD_RESET")
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + (5 * 60 * 1000)
                        )
                )
                .signWith(getSigningKey())
                .compact();

    }

    public String generateToken(Staff user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("purpose", "AUTH")
                .claim("tokenVersion", user.getTokenVersion())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60 * 24 // 24 hours
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    public String extractPurpose(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("purpose", String.class);
    }


    public boolean isResetPasswordToken(String token) {

        return "PASSWORD_RESET".equals(
                extractPurpose(token)
        );
    }

    public boolean isAuthToken(String token) {

        return "AUTH".equals(
                extractPurpose(token)
        );
    }


    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Integer extractTokenVersion(String token) {

        Claims claims = extractAllClaims(token);

        return claims.get("tokenVersion", Integer.class);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {

        String username = extractUsername(token);

        Integer tokenVersion = extractTokenVersion(token);

        Integer currentVersion = ((UserDataDetails) userDetails).getTokenVersion();

        return username.equals(userDetails.getUsername())
                && tokenVersion.equals(currentVersion)
                && !isTokenExpired(token);
    }




}
