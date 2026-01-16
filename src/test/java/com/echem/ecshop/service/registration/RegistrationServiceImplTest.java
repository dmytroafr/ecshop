package com.echem.ecshop.service.registration;

import com.echem.ecshop.dto.RegistrationRequest;
import com.echem.ecshop.service.email.EmailSender;
import com.echem.ecshop.service.token.ConfirmationTokenService;
import com.echem.ecshop.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private ConfirmationTokenService tokenService;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private RegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = new RegistrationRequest(
                "testuser",
                "password123",
                "test@example.com"
        );
    }

    @Test
    void testRegister_Success() {
        String expectedToken = "test-token-123";
        when(userService.signUpUser(registrationRequest)).thenReturn(expectedToken);

        String result = registrationService.register(registrationRequest);

        assertEquals(expectedToken, result);
        verify(userService).signUpUser(registrationRequest);
    }

    @Test
    void testRegister_ReturnsToken() {
        when(userService.signUpUser(any(RegistrationRequest.class))).thenReturn("generated-token");

        String token = registrationService.register(registrationRequest);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testRegister_CallsUserService() {
        when(userService.signUpUser(registrationRequest)).thenReturn("token");

        registrationService.register(registrationRequest);

        verify(userService, times(1)).signUpUser(registrationRequest);
    }

    @Test
    void testRegister_WithDifferentUser() {
        RegistrationRequest anotherRequest = new RegistrationRequest(
                "anotheruser",
                "pass456",
                "another@example.com"
        );

        when(userService.signUpUser(anotherRequest)).thenReturn("another-token");

        String result = registrationService.register(anotherRequest);

        assertEquals("another-token", result);
        verify(userService).signUpUser(anotherRequest);
    }
}
