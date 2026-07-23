package io.toolongname.sandcastle.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Deprecated
public class Password {
    // 迭代次数
    private final int iterations;
    // 密钥长度
    private final int keyLength;
    // 算法
    private final String algorithm;

    public Password(int iterations, int keyLength) throws NoSuchAlgorithmException {
        this.iterations = iterations;
        this.keyLength = keyLength;

        switch (keyLength) {
            case 256 -> this.algorithm = "PBKDF2WithHmacSHA256";
            case 512 -> this.algorithm = "PBKDF2WithHmacSHA512";
            default -> throw new NoSuchAlgorithmException();
        }
    }

    public Password(int keyLength) throws NoSuchAlgorithmException {
        this(300_000, keyLength);
    }

    // 生成的密钥长度 512 bits
    private static final int KEY_LENGTH = 512;
    // 算法
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    // 迭代次数
    private static final int ITERATIONS = 300_000;

    public byte[] hashed(char[] password, byte[] salt) {
        try {
            KeySpec keySpec = new PBEKeySpec(password, salt, this.iterations, this.keyLength);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(this.algorithm);

            return secretKeyFactory.generateSecret(keySpec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] hashed(String password, byte[] salt) {
        return this.hashed(password.toCharArray(), salt);
    }

    public static byte[] getSalt(int size) {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(System.currentTimeMillis());

        byte[] salt = new byte[size];
        secureRandom.nextBytes(salt);

        return salt;
    }
}
