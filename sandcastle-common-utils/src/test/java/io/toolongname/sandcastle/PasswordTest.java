package io.toolongname.sandcastle;

import io.toolongname.sandcastle.utils.Password;
import org.junit.jupiter.api.Test;


public class PasswordTest {
    private static final String PASSWORD = "password";

    @Test
    void testHashed() {
        Password password = new Password();
        final byte[] salt = password.getSalt(16);

        password.hashed(PASSWORD, salt);
    }
}
