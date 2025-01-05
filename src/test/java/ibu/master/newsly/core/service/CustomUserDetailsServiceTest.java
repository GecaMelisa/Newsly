package ibu.master.newsly.core.service;

import ibu.master.newsly.core.model.User;
import ibu.master.newsly.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        String email = "johndoe@example.com";
        User mockUser = new User("John Doe", email, "encodedPassword123");

        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("encodedPassword123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String email = "unknown@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(email));
    }

    @Test
    void testLoadUserByUsername_CaseInsensitive() {
        // Arrange
        String email = "JOHNDOE@EXAMPLE.COM";
        User mockUser = new User("John Doe", email.toLowerCase(), "encodedPassword123");

        when(userRepository.findByEmail(email.toLowerCase())).thenReturn(mockUser);

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email.toLowerCase());

        // Assert
        assertNotNull(userDetails);
        assertEquals(email.toLowerCase(), userDetails.getUsername());
    }
}
