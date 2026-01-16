package com.echem.ecshop.service.user;

import com.echem.ecshop.dao.UserRepository;
import com.echem.ecshop.domain.ConfirmationToken;
import com.echem.ecshop.domain.Role;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.RegistrationRequest;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.bucket.BucketService;
import com.echem.ecshop.service.token.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private BucketService bucketService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Role.ROLE_CLIENT);
        testUser.setEnabled(true);
        testUser.setAccountNonExpired(true);
        testUser.setAccountNonLocked(true);
        testUser.setCredentialsNonExpired(true);
    }

    @Test
    void testGetUserDTOByUserName_Success() {
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDTO result = userService.getUserDTOByUserName("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findUserByUsername("testuser");
    }

    @Test
    void testGetUserDTOByUserName_UserNotFound() {
        when(userRepository.findUserByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUserDTOByUserName("nonexistent");
        });
    }

    @Test
    void testGetUserDTOByEmail_Success() {
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDTO result = userService.getUserDTOByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findUserByEmail("test@example.com");
    }

    @Test
    void testGetUserDTOByEmail_UserNotFound() {
        when(userRepository.findUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUserDTOByEmail("nonexistent@example.com");
        });
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails result = userService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertTrue(result.isEnabled());
        verify(userRepository).findUserByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findUserByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void testGetUserByUsername_Success() {
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(testUser));

        User result = userService.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findUserByUsername("testuser");
    }

    @Test
    void testSignUpUser_Success() {
        RegistrationRequest request = new RegistrationRequest(
                "newuser",
                "password123",
                "newuser@example.com"
        );

        when(userRepository.findUserByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String token = userService.signUpUser(request);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("newuser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("newuser@example.com", savedUser.getEmail());

        verify(confirmationTokenService).saveConfirmationToken(any(ConfirmationToken.class));
    }

    @Test
    void testSignUpUser_EmailAlreadyExists() {
        RegistrationRequest request = new RegistrationRequest(
                "newuser",
                "password123",
                "existing@example.com"
        );

        when(userRepository.findUserByEmail("existing@example.com")).thenReturn(Optional.of(testUser));

        assertThrows(IllegalStateException.class, () -> {
            userService.signUpUser(request);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testEnableUser_Success() {
        testUser.setEnabled(false);
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.enableUser("testuser");

        assertTrue(testUser.isEnabled());
        verify(bucketService).createBucket(testUser);
        verify(userRepository, atLeast(1)).save(testUser);
    }

    @Test
    void testGetUserDetailsMap() {
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(testUser));

        Map<String, String> result = userService.getUserDetailsMap("testuser");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("test@example.com", result.get("email"));
    }

    @Test
    void testGetUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@test.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@test.com");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDTO> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }
}
