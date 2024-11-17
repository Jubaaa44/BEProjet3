package com.projet3.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtil {
	
	@Value("${jwt.key}")
    private String secret_key;

    private final long EXPIRATION_TIME = 86400000; // 1 jour en millisecondes

    // Générer un JWT
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    // Extraire l'email (nom d'utilisateur) du token
    public String getEmailFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire une revendication spécifique
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret_key)
                .parseClaimsJws(token)
                .getBody();
    }

    // Vérifier si le token a expiré
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Valider le token en comparant l'email et la date d'expiration
    public boolean validateToken(String token, String email) {
        final String emailFromToken = getEmailFromToken(token);
        return (email.equals(emailFromToken) && !isTokenExpired(token));
    }
}
