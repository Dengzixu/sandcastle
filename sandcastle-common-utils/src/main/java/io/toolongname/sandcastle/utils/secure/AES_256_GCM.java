package io.toolongname.sandcastle.utils.secure;


import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class AES_256_GCM extends Crypto {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES_256/GCM/NoPadding";
    private static final int TAG_LENGTH = 128;

    private final Cipher cipher;
    private final SecretKeySpec secretKey;
    private final AlgorithmParameterSpec spec;

    public AES_256_GCM(byte[] key, byte[] iv) {
        this.secretKey = new SecretKeySpec(key, ALGORITHM);
        this.spec = new GCMParameterSpec(TAG_LENGTH, iv);

        try {
            this.cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }


    public final void init(int opmode) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, this.spec);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public final int update(ByteBuffer input, ByteBuffer output) throws ShortBufferException {
        return cipher.update(input, output);
    }

    public final byte[] update(byte[] input, int inputOffset, int inputLen) {
        return cipher.update(input, inputOffset, inputLen);
    }

    public final int doFinal(ByteBuffer input, ByteBuffer output) throws ShortBufferException, IllegalBlockSizeException {
        try {
            return cipher.doFinal(input, output);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public final byte[] doFinal() throws IllegalBlockSizeException, ShortBufferException {
        try {
            return cipher.doFinal();
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public final byte[] encrypt(byte[] input) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, this.spec);
            return cipher.doFinal(input);
        } catch (IllegalBlockSizeException | InvalidAlgorithmParameterException | BadPaddingException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public final CipherInputStream encrypt(InputStream input) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, this.spec);
            return new CipherInputStream(input, cipher);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public final OutputStream encrypt(OutputStream output) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, this.spec);
            return new CipherOutputStream(output, cipher);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public void encrypt(ByteBuffer input, ByteBuffer output) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, this.spec);

            cipher.doFinal(input, output);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | ShortBufferException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
