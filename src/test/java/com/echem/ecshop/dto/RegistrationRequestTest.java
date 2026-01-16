package com.echem.ecshop.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationRequestTest {

    @Test
    void testRecordCreation() {
        RegistrationRequest request = new RegistrationRequest(
                "testuser",
                "password123",
                "test@example.com"
        );

        assertEquals("testuser", request.username());
        assertEquals("password123", request.password());
        assertEquals("test@example.com", request.email());
    }

    @Test
    void testRecordEquality() {
        RegistrationRequest request1 = new RegistrationRequest(
                "testuser",
                "password123",
                "test@example.com"
        );

        RegistrationRequest request2 = new RegistrationRequest(
                "testuser",
                "password123",
                "test@example.com"
        );

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testRecordInequality() {
        RegistrationRequest request1 = new RegistrationRequest(
                "testuser",
                "password123",
                "test@example.com"
        );

        RegistrationRequest request2 = new RegistrationRequest(
                "otheruser",
                "password123",
                "test@example.com"
        );

        assertNotEquals(request1, request2);
    }

    @Test
    void testToString() {
        RegistrationRequest request = new RegistrationRequest(
                "testuser",
                "password123",
                "test@example.com"
        );

        String toString = request.toString();

        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("test@example.com"));
    }
}
