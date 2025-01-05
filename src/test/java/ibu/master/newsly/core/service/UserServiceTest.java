package ibu.master.newsly.core.service;

import ibu.master.newsly.core.model.User;
import ibu.master.newsly.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Initialize mocks and service
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);

        // Initialize test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
    }

    @Test
    void testCreateUser() {
        // Mock behavior
        when(userRepository.save(testUser)).thenReturn(testUser);

        // When
        User createdUser = userService.createUser(testUser);

        // Then
        assertNotNull(createdUser, "Created user should not be null");
        assertEquals(testUser.getId(), createdUser.getId(), "User ID should match");
        assertEquals(testUser.getName(), createdUser.getName(), "User name should match");
    }

    @Test
    void testGetUserById_UserExists() {
        // Mock behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> foundUser = userService.getUserById(1L);

        // Then
        assertTrue(foundUser.isPresent(), "User should be found");
        assertEquals(testUser.getId(), foundUser.get().getId(), "User ID should match");
    }

    @Test
    void testGetUserById_UserDoesNotExist() {
        // Mock behavior
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userService.getUserById(1L);

        // Then
        assertFalse(foundUser.isPresent(), "User should not be found");
    }

    @Test
    void testGetUserByEmail_UserExists() {
        // Mock behavior
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        // When
        Optional<User> foundUser = userService.getUserByEmail("test@example.com");

        // Then
        assertTrue(foundUser.isPresent(), "User should be found");
        assertEquals(testUser.getEmail(), foundUser.get().getEmail(), "User email should match");
    }

    @Test
    void testGetUserByEmail_UserDoesNotExist() {
        // Mock behavior
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        // When
        Optional<User> foundUser = userService.getUserByEmail("nonexistent@example.com");

        // Then
        assertFalse(foundUser.isPresent(), "User should not be found");
    }

    @Test
    void testUpdateUser_UserExists() {
        // Given
        User updatedDetails = new User();
        updatedDetails.setName("Updated Name");
        updatedDetails.setEmail("updated@example.com");
        updatedDetails.setPassword("newpassword");

        // Mock behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        // When
        User updatedUser = userService.updateUser(1L, updatedDetails);

        // Then
        assertNotNull(updatedUser, "Updated user should not be null");
        assertEquals(updatedDetails.getName(), updatedUser.getName(), "User name should be updated");
        assertEquals(updatedDetails.getEmail(), updatedUser.getEmail(), "User email should be updated");
    }

    @Test
    void testUpdateUser_UserDoesNotExist() {
        // Mock behavior
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, testUser);
        }, "Should throw exception when user is not found");
    }

    @Test
    void testDeleteUser() {
        // Mock behavior
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).deleteById(1L);
    }
}
