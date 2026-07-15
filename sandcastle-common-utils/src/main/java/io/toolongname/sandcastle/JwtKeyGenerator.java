package io.toolongname.sandcastle;

import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;

public class JwtKeyGenerator {

    static void main() {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(System.currentTimeMillis());

        byte[] key = new byte[64];
        secureRandom.nextBytes(key);

        System.out.println("Jwt Key: " + Base64.encodeBase64URLSafeString(key));
    }
}
