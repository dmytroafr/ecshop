package com.echem.ecshop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmailValidatorTest {

    private final EmailValidator emailValidator = new EmailValidator();

    @ParameterizedTest
//    @DisplayName(value = "regex")
    @ValueSource(strings = {"testuser@example.com", "Test.User@example.com", "john.doe123@example.com",
            "JoHn.DoE123@example.com", "alice+bob@example.com", "ALICE+BOB@example.com", "test_user@example.com",
            "Test_User@example.com", "123testuser@example.com", "123TestUser@example.com", "tEstUser123@example.com",
            "test_user-123@example.com", "test.user+123@example.com", "Test.User-123@example.com",
            "Test.User-123@example.com", "testuser123@example.com", "TestUser123@example.com", "user.test@example.com",
            "user_test@example.com", "user123@example.com", "USER123@example.com"})
    void shouldPassAllPossibleEmails(String email){
            Assertions.assertTrue(emailValidator.test(email));
    }
}
