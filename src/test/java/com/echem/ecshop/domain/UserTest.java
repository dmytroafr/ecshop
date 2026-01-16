package com.echem.ecshop.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setRole(Role.ROLE_CLIENT);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENT")));
    }

    @Test
    void testGetAuthorities_AdminRole() {
        user.setRole(Role.ROLE_ADMIN);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());

        user.setAccountNonExpired(false);
        assertFalse(user.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());

        user.setAccountNonLocked(false);
        assertFalse(user.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());

        user.setCredentialsNonExpired(false);
        assertFalse(user.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(user.isEnabled());

        user.setEnabled(false);
        assertFalse(user.isEnabled());
    }

    @Test
    void testConstructorWithBasicFields() {
        User newUser = new User("john", "pass123", "john@test.com", Role.ROLE_CLIENT);

        assertEquals("john", newUser.getUsername());
        assertEquals("pass123", newUser.getPassword());
        assertEquals("john@test.com", newUser.getEmail());
        assertEquals(Role.ROLE_CLIENT, newUser.getRole());
    }

    @Test
    void testUserDetailsInterface() {
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void testDefaultEnabledValue() {
        User newUser = new User();
        newUser.setRole(Role.ROLE_CLIENT);

        assertFalse(newUser.isEnabled()); // Default is false
    }

    @Test
    void testDefaultAccountStates() {
        User newUser = new User();

        assertTrue(newUser.isAccountNonExpired()); // Default is true
        assertTrue(newUser.isAccountNonLocked()); // Default is true
        assertTrue(newUser.isCredentialsNonExpired()); // Default is true
    }
}
