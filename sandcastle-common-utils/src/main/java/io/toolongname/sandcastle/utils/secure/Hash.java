package io.toolongname.sandcastle.utils.secure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Hash {
    private final MessageDigest messageDigest;

    protected Hash(String algorithm) {
        try {
            this.messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(byte[] input) {
        messageDigest.update(input);
    }

    public void update(byte[] input, int offset, int len) {
        messageDigest.update(input, offset, len);
    }

    public void update(ByteBuffer input) {
        messageDigest.update(input);
    }

    public byte[] digest() {
        return messageDigest.digest();
    }

    public byte[] digest(byte[] input) {
        return messageDigest.digest(input);
    }

    public byte[] digest(InputStream stream) throws IOException {
        DigestInputStream dis = new DigestInputStream(stream, messageDigest);
        dis.readAllBytes();

        return messageDigest.digest();
    }

    public byte[] digest(OutputStream stream) throws IOException {
        DigestOutputStream dos = new DigestOutputStream(stream, messageDigest);
        dos.close();

        return messageDigest.digest();
    }
}
