package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .id(1L)
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .phone("+380501234567")
                .role(Role.ROLE_CLIENT)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetMap_AllFieldsPresent() {
        Map<String, String> map = userDTO.getMap();

        assertNotNull(map);
        assertEquals("John", map.get("firstName"));
        assertEquals("Doe", map.get("lastName"));
        assertEquals("+380501234567", map.get("phone"));
        assertEquals("test@example.com", map.get("email"));
        assertEquals("ROLE_CLIENT", map.get("role"));
        assertEquals("true", map.get("accountNonExpired"));
        assertEquals("true", map.get("accountNonLocked"));
        assertEquals("true", map.get("credentialsNonExpired"));
        assertEquals("true", map.get("enabled"));
        assertNotNull(map.get("created"));
    }

    @Test
    void testGetMap_WithNullFields() {
        userDTO.setFirstName(null);
        userDTO.setLastName(null);
        userDTO.setPhone(null);

        Map<String, String> map = userDTO.getMap();

        assertNotNull(map);
        assertEquals("", map.get("firstName"));
        assertEquals("", map.get("lastName"));
        assertEquals("", map.get("phone"));
        assertEquals("test@example.com", map.get("email"));
    }

    @Test
    void testBuilder() {
        UserDTO dto = UserDTO.builder()
                .id(2L)
                .username("newuser")
                .email("new@example.com")
                .build();

        assertEquals(2L, dto.getId());
        assertEquals("newuser", dto.getUsername());
        assertEquals("new@example.com", dto.getEmail());
    }

    @Test
    void testNoArgsConstructor() {
        UserDTO dto = new UserDTO();

        assertNull(dto.getId());
        assertNull(dto.getUsername());
        assertNull(dto.getEmail());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        UserDTO dto = new UserDTO(
                3L,
                "user3",
                "Jane",
                "Smith",
                "jane@example.com",
                "+380509876543",
                Role.ROLE_ADMIN,
                true,
                true,
                true,
                true,
                now
        );

        assertEquals(3L, dto.getId());
        assertEquals("user3", dto.getUsername());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("jane@example.com", dto.getEmail());
        assertEquals("+380509876543", dto.getPhone());
        assertEquals(Role.ROLE_ADMIN, dto.getRole());
        assertEquals(now, dto.getCreated());
    }

    @Test
    void testGetMap_AdminRole() {
        userDTO.setRole(Role.ROLE_ADMIN);

        Map<String, String> map = userDTO.getMap();

        assertEquals("ROLE_ADMIN", map.get("role"));
    }

    @Test
    void testGetMap_DisabledAccount() {
        userDTO.setEnabled(false);
        userDTO.setAccountNonLocked(false);

        Map<String, String> map = userDTO.getMap();

        assertEquals("false", map.get("enabled"));
        assertEquals("false", map.get("accountNonLocked"));
    }
}
