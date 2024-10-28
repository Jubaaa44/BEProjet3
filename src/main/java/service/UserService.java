package service;

import user.*;
import user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
 // Méthode pour obtenir un utilisateur par son ID
    public UserModel getUserById(Long id) {
        Optional<UserModel> userOptional = userRepository.findById(id);
        return userOptional.orElse(null); // Renvoie l'utilisateur si trouvé, sinon null
    }
}
