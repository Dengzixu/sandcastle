package io.toolongname.sandcastle;

import io.toolongname.sandcastle.utils.password.Password;
import org.junit.jupiter.api.Test;

import java.util.Base64;


public class PasswordTest {
    private static final String PASSWORD = "password";

    @Test
    void testRandomSalt() {
        final byte[] salt = Password.randomSalt();
        System.out.println("Salt: " + Base64.getEncoder().encodeToString(salt));
    }


    @Test
    void test1() {
        byte[] testSalt = Base64.getDecoder().decode("LuR/ap/gPrp+g0Pvy5jgUQ==");

        Password password1 = Password.fromPlaintext("plaintext", testSalt);

        System.out.println("password1: " + password1.asString());

        String base64Password = "nQnsvU9rjN++0Q6XemJszZ/0BYL1LYsTiBKLzHvX81BjiXz9z2/u2Ar+w7nzB243U/us96W9VT3uBHcV/OeOuC7kf2qf4D66foND78uY4FE=";

        Password password2 = Password.fromCombinedByteArray(Base64.getDecoder().decode(base64Password));

        System.out.println(password1.equals(password2));

    }
}
