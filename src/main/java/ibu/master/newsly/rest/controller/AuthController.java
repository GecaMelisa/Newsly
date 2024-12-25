package ibu.master.newsly.rest.controller;

import ibu.master.newsly.core.service.AuthService;
import ibu.master.newsly.rest.dto.UserLoginDTO;
import ibu.master.newsly.rest.dto.UserRegistrationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Validated @RequestBody UserRegistrationDTO userDTO) {
        authService.registerUser(userDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Validated @RequestBody UserLoginDTO loginDTO) {
        try {
            String token = authService.loginUser(loginDTO);
            return ResponseEntity.ok("Bearer " + token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during login: " + e.getMessage());
        }
    }
}
