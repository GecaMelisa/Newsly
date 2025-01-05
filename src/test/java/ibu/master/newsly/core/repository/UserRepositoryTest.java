package ibu.master.newsly.core.repository;

import ibu.master.newsly.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByEmail_UserExists() {
        // Given
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        // Mock the behavior of the repository
        when(userRepository.findByEmail(email)).thenReturn(user);

        // When
        User foundUser = userRepository.findByEmail(email);

        // Then
        assertNotNull(foundUser, "User should not be null");
        assertEquals(email, foundUser.getEmail(), "Email should match");
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {
        // Given
        String email = "nonexistent@example.com";

        // Mock the behavior of the repository
        when(userRepository.findByEmail(email)).thenReturn(null);

        // When
        User foundUser = userRepository.findByEmail(email);

        // Then
        assertNull(foundUser, "User should be null");
    }
}
