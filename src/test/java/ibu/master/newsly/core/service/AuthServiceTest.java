package ibu.master.newsly.core.service;

import ibu.master.newsly.core.model.User;
import ibu.master.newsly.core.repository.UserRepository;
import ibu.master.newsly.rest.configuration.JwtTokenProvider;
import ibu.master.newsly.rest.dto.UserLoginDTO;
import ibu.master.newsly.rest.dto.UserRegistrationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPassword("password123");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User registeredUser = authService.registerUser(userDTO);

        // Assert
        assertNotNull(registeredUser);
        assertEquals("John Doe", registeredUser.getName());
        assertEquals("john@example.com", registeredUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setEmail("existing@example.com");
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(new User());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password123");

        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");
        mockUser.setId(1L);

        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(mockUser);
        when(passwordEncoder.matches(loginDTO.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(mockUser.getId(), mockUser.getEmail())).thenReturn("validToken");

        // Act
        String token = authService.loginUser(loginDTO);

        // Assert
        assertNotNull(token);
        assertEquals("validToken", token);
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("invalid@example.com");
        loginDTO.setPassword("wrongPassword");

        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.loginUser(loginDTO));
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Arrange
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        // Act
        UserDetails userDetails = authService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername("notfound@example.com"));
    }
}
