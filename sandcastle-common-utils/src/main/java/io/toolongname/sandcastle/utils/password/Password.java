package io.toolongname.sandcastle.utils.password;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;


/**
 * 一种安全的密码管理类，采用 PBKDF2 配合 HMAC-SHA512 进行密码哈希。
 * 密码与其对应的盐值（salt）以组合字节数组形式一同存储。
 * 该类支持使用常量时间相等性检查进行安全比对，
 * 并提供序列化和反序列化带盐值的哈希密码的方法。
 */
public final class Password {
    private static final int SHA_512_BYTE_LENGTH = 64;
    private static final int SHA_512_BIT_LENGTH = 512;
    private static final int SALT_BYTE_LENGTH = 16;
    private static final int SHA_512_WITH_SALT_BYTE_LENGTH = SHA_512_BYTE_LENGTH + SALT_BYTE_LENGTH;

    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final int ITERATIONS = 300_000;

    private static final SecretKeyFactory SECRET_KEY_FACTORY;

    static {
        try {
            SECRET_KEY_FACTORY = SecretKeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("系统不支持算法: " + ALGORITHM, e);
        }
    }

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // Hash 后的密码
    private final byte[] hashedByteArray;
    // Salt
    private final byte[] saltByteArray;

    private Password(byte[] combinedByteArray) {
        if (combinedByteArray.length != SHA_512_WITH_SALT_BYTE_LENGTH) {
            throw new IllegalArgumentException(
                    "无效的混合密码长度: 混合密码长度应为：" + SHA_512_WITH_SALT_BYTE_LENGTH + ", 实际长度为：" + combinedByteArray.length);
        }

        this.hashedByteArray = Arrays.copyOfRange(
                combinedByteArray, 0, SHA_512_BYTE_LENGTH);

        this.saltByteArray = Arrays.copyOfRange(
                combinedByteArray, SHA_512_BYTE_LENGTH, SHA_512_WITH_SALT_BYTE_LENGTH);
    }

    private Password(char[] password, byte[] salt) {
        this.saltByteArray = salt.clone();

        try {
            KeySpec keySpec = new PBEKeySpec(password, this.saltByteArray, ITERATIONS, SHA_512_BIT_LENGTH);
            this.hashedByteArray = SECRET_KEY_FACTORY.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } finally {
            Arrays.fill(password, '\0');
        }
    }


    /**
     * 根据明文密码和盐值生成一个 Password 实例。
     *
     * @param plaintextPassword 待哈希的明文密码字符串
     * @param saltByteArray     盐值字节数组，其长度必须等于 SALT_BYTE_LENGTH
     * @return Password 实例
     */
    public static Password fromPlaintext(String plaintextPassword, byte[] saltByteArray) {
        if (saltByteArray.length != SALT_BYTE_LENGTH) {
            throw new IllegalArgumentException(
                    "无效的 Salt 长度: Salt 长度应为：" + SALT_BYTE_LENGTH + ", 实际长度为：" + saltByteArray.length);
        }

        return new Password(plaintextPassword.toCharArray(), saltByteArray);
    }


    /**
     * 根据包含哈希密码和盐值的组合字节数组生成一个 Password 实例。
     *
     * @param combinedPasswordByteArray 组合字节数组，必须按顺序包含哈希后的密码字节后接盐值字节
     * @return Password 实例
     */
    public static Password fromCombinedByteArray(byte[] combinedPasswordByteArray) {
        return new Password(combinedPasswordByteArray);
    }

    /**
     * 生成一个密码学安全的随机盐值。
     *
     * @return 包含随机生成盐值的字节数组，其长度等于 {@link #SALT_BYTE_LENGTH}
     */
    public static byte[] randomSalt() {
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }

    /**
     * 返回用于密码哈希的盐值字节数组的拷贝。
     *
     * @return 盐值字节数组。
     */
    public byte[] salt() {
        return this.saltByteArray.clone();
    }

    /**
     * 返回一个包含哈希密码字节后接盐值字节的字节数组。
     *
     * @return 新构建的字节数组，按顺序组合了已哈希的密码与盐值
     */
    public byte[] asByteArray() {
        byte[] combined = new byte[hashedByteArray.length + saltByteArray.length];
        System.arraycopy(hashedByteArray, 0, combined, 0, hashedByteArray.length);
        System.arraycopy(saltByteArray, 0, combined, hashedByteArray.length, saltByteArray.length);

        return combined;
    }

    /**
     * 返回该密码实例的 Base64 编码字符串表示。
     * 该编码包含哈希后的密码字节后接盐值字节，并按顺序组合为一个字节数组后进行 Base64 编码。
     *
     * @return 密码的 Base64 字符串
     */
    public String asString() {
        return Base64.getEncoder().encodeToString(this.asByteArray());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.asByteArray());
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Password password)) return false;

        byte[] thisBytes = this.asByteArray();
        byte[] thatBytes = password.asByteArray();

        if (thisBytes.length != thatBytes.length) {
            return false;
        }

        int diff = 0;
        for (int i = 0; i < thisBytes.length; i++) {
            diff |= thisBytes[i] ^ thatBytes[i];
        }
        return diff == 0;
    }


    public static int findIterations(int targetMs) {
        char[] password = "test".toCharArray();
        byte[] salt = new byte[16];
        new java.security.SecureRandom().nextBytes(salt);

        int iterations = 10_000;
        while (true) {
            long start = System.nanoTime();
            try {
                PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, 512);
                SECRET_KEY_FACTORY.generateSecret(spec); // 执行一次
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;

            if (elapsedMs >= targetMs) {
                return iterations; // 返回刚好 ≥ targetMs 的迭代次数
            }
            iterations *= 2;
        }
    }

}
