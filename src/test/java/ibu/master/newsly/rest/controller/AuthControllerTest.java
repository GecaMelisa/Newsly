package ibu.master.newsly.rest.controller;

import ibu.master.newsly.core.service.AuthService;
import ibu.master.newsly.rest.dto.UserLoginDTO;
import ibu.master.newsly.rest.dto.UserRegistrationDTO;
import ibu.master.newsly.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setPassword("password123");
        registrationDTO.setName("Test User");

        User createdUser = new User("Test User", "test@example.com", "encodedPassword");

        // Mock behavior using thenReturn instead of doNothing
        when(authService.registerUser(any(UserRegistrationDTO.class))).thenReturn(createdUser);

        // Act
        ResponseEntity<Object> response = authController.registerUser(registrationDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(authService, times(1)).registerUser(any(UserRegistrationDTO.class));
    }

    @Test
    void testLoginUser_Success() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password123");

        String fakeToken = "fake-jwt-token";
        when(authService.loginUser(any(UserLoginDTO.class))).thenReturn(fakeToken);

        // Act
        ResponseEntity<Object> response = authController.loginUser(loginDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bearer " + fakeToken, response.getBody());
        verify(authService, times(1)).loginUser(any(UserLoginDTO.class));
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("wrong@example.com");
        loginDTO.setPassword("wrongpassword");

        when(authService.loginUser(any(UserLoginDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid email or password"));

        // Act
        ResponseEntity<Object> response = authController.loginUser(loginDTO);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password", response.getBody());
        verify(authService, times(1)).loginUser(any(UserLoginDTO.class));
    }

    @Test
    void testLoginUser_InternalServerError() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("error@example.com");
        loginDTO.setPassword("password123");

        when(authService.loginUser(any(UserLoginDTO.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<Object> response = authController.loginUser(loginDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("An error occurred during login"));
    }
}
