package com.projet3.configuration;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.projet3.entity.UserEntity;
import com.projet3.repository.UserRepository;

/**
 * Service personnalisé pour charger les détails de l'utilisateur lors de l'authentification.
 * Implémente l'interface `UserDetailsService` de Spring Security pour permettre de rechercher un utilisateur
 * dans la base de données à partir de son email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Dépendance injectée pour accéder au référentiel des utilisateurs
    @Autowired
    private UserRepository UserRepository;

    /**
     * Charge les détails d'un utilisateur à partir de son email.
     * @param email l'email de l'utilisateur à rechercher
     * @return un objet `UserDetails` contenant l'email, le mot de passe et une liste de rôles vides
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé dans la base de données
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Recherche de l'utilisateur dans la base de données par email
        UserEntity user = UserRepository.findByEmail(email);
        
        // Vérifie si l'utilisateur est trouvé, sinon lève une exception
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur introuvable avec l'email : " + email);
        }

        // Retourne un objet User de Spring Security avec l'email et le mot de passe de l'utilisateur
        // La liste des autorisations (authorities) est vide pour le moment
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
