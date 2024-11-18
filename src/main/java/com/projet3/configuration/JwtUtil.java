package com.projet3.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

/**
 * Service utilitaire pour gérer la génération, l'extraction et la validation des tokens JWT.
 * Utilise la bibliothèque JJWT pour créer et manipuler des tokens.
 */
@Service
public class JwtUtil {

    // Clé secrète utilisée pour signer le token JWT, injectée depuis les propriétés de l'application
    @Value("${jwt.key}")
    private String secret_key;

    // Durée de validité du token (1 jour en millisecondes)
    private final long EXPIRATION_TIME = 86400000;

    /**
     * Génère un token JWT en utilisant l'email de l'utilisateur.
     * @param email l'email de l'utilisateur pour lequel le token est généré
     * @return un token JWT signé
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Définit l'email comme sujet du token
                .setIssuedAt(new Date()) // Date de création du token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Date d'expiration
                .signWith(SignatureAlgorithm.HS256, secret_key) // Signature avec algorithme HS256 et clé secrète
                .compact(); // Compacte et génère le token
    }

    /**
     * Extrait l'email (sujet) du token JWT.
     * @param token le token JWT
     * @return l'email de l'utilisateur
     */
    public String getEmailFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait une revendication spécifique du token.
     * @param token le token JWT
     * @param claimsResolver fonction pour résoudre la revendication
     * @param <T> type de la revendication
     * @return la valeur de la revendication extraite
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrait toutes les revendications du token JWT.
     * @param token le token JWT
     * @return les revendications présentes dans le token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret_key) // Utilise la clé secrète pour vérifier le token
                .parseClaimsJws(token) // Parse le token pour obtenir le JWS (JSON Web Signature)
                .getBody(); // Retourne le corps des revendications
    }

    /**
     * Vérifie si le token a expiré.
     * @param token le token JWT
     * @return true si le token est expiré, sinon false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrait la date d'expiration du token JWT.
     * @param token le token JWT
     * @return la date d'expiration
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Valide le token en vérifiant l'email et l'expiration.
     * @param token le token JWT
     * @param email l'email à valider
     * @return true si le token est valide, sinon false
     */
    public boolean validateToken(String token, String email) {
        final String emailFromToken = getEmailFromToken(token);
        return (email.equals(emailFromToken) && !isTokenExpired(token)); // Vérifie que l'email correspond et que le token n'est pas expiré
    }
}
