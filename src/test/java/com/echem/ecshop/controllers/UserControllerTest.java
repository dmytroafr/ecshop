package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.order.OrderService;
import com.echem.ecshop.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testProfileUser() throws Exception {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("username", "testuser");
        userMap.put("email", "test@example.com");

        when(userService.getUserDetailsMap("testuser")).thenReturn(userMap);

        mockMvc.perform(get("/users/testuser")
                        .principal(() -> "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("username"));

        verify(userService).getUserDetailsMap("testuser");
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserDTO user1 = UserDTO.builder()
                .id(1L)
                .username("user1")
                .email("user1@test.com")
                .build();

        UserDTO user2 = UserDTO.builder()
                .id(2L)
                .username("user2")
                .email("user2@test.com")
                .build();

        when(userService.getUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/users"))
                .andExpect(model().attributeExists("users"));

        verify(userService).getUsers();
    }
}
