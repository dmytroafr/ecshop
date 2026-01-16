package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.Role;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.bucket.BucketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BucketControllerTest {

    @Mock
    private BucketService bucketService;

    @InjectMocks
    private BucketController bucketController;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private UserDTO testUserDTO;
    private BucketDTO testBucketDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bucketController).build();
        session = new MockHttpSession();

        testUserDTO = UserDTO.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(Role.ROLE_CLIENT)
                .build();

        session.setAttribute("user", testUserDTO);

        testBucketDTO = new BucketDTO();
        testBucketDTO.setId(1L);
        testBucketDTO.setProductList(new ArrayList<>());
    }

    @Test
    void testGetBucket() throws Exception {
        when(bucketService.getBucketDtoByUserId(1L)).thenReturn(testBucketDTO);

        mockMvc.perform(get("/buckets").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("bucket"))
                .andExpect(model().attributeExists("bucket"));

        verify(bucketService).getBucketDtoByUserId(1L);
    }

    @Test
    void testGetBucket_WithoutSession() throws Exception {
        MockHttpSession emptySession = new MockHttpSession();

        mockMvc.perform(get("/buckets").session(emptySession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testAddToBucket() throws Exception {
        Principal principal = () -> "testuser";
        doNothing().when(bucketService).addBucketDetails(1L, 1L);

        mockMvc.perform(get("/buckets/1/add")
                        .session(session)
                        .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(bucketService).addBucketDetails(1L, 1L);
    }

    @Test
    void testDeleteFromBucket() throws Exception {
        doNothing().when(bucketService).deleteProductFromBucket(1L, 1L);

        mockMvc.perform(delete("/buckets/1/delete").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buckets"));

        verify(bucketService).deleteProductFromBucket(1L, 1L);
    }

    @Test
    void testIncreaseProductAmount() throws Exception {
        Principal principal = () -> "testuser";
        doNothing().when(bucketService).increaseProductAmount(1L, 1L);

        mockMvc.perform(post("/buckets/1/increase")
                        .session(session)
                        .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buckets"));

        verify(bucketService).increaseProductAmount(1L, 1L);
    }

    @Test
    void testDecreaseProductAmount() throws Exception {
        Principal principal = () -> "testuser";
        doNothing().when(bucketService).decreaseProductAmount(1L, 1L);

        mockMvc.perform(post("/buckets/1/decrease")
                        .session(session)
                        .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buckets"));

        verify(bucketService).decreaseProductAmount(1L, 1L);
    }
}
