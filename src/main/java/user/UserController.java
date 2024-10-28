package user;

import user.UserModel;
import service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
 // Route pour obtenir un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) {
        UserModel user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user); // Renvoie l'utilisateur si trouvé
        } else {
            return ResponseEntity.notFound().build(); // Renvoie 404 si non trouvé
        }
    }

    
}
