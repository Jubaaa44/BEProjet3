package com.projet3;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.projet3.controller.UserController;
import com.projet3.controller.MessageController;
import com.projet3.controller.RentalController;
import com.projet3.controller.AuthController;

@SpringBootTest
class Projet3OcApplicationTests {
    
    @Autowired
    private UserController userController;

    @Autowired
    private MessageController messageController;

    @Autowired
    private RentalController rentalController;

    @Autowired
    private AuthController authController;

    @Test
    void contextLoads() throws Exception {
        assertThat(userController).isNotNull();
        assertThat(messageController).isNotNull();
        assertThat(rentalController).isNotNull();
        assertThat(authController).isNotNull();
    }
}
