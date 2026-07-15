package io.toolongname.sandcastlecommon.misc.constant;

import java.util.HashMap;

public class Constant {
    public static final HashMap<String, String> MEDIA_TYPES = new HashMap<>();

    static {
        MEDIA_TYPES.put("image/apng", "apng");
        MEDIA_TYPES.put("image/avif", "avif");
        MEDIA_TYPES.put("image/bmp", "bmp");
        MEDIA_TYPES.put("image/gif", "gif");
        MEDIA_TYPES.put("image/heic", "heic");
        MEDIA_TYPES.put("image/heif", "heif");
        MEDIA_TYPES.put("image/jpeg", "jpeg");
        MEDIA_TYPES.put("image/png", "png");
        MEDIA_TYPES.put("image/svg+xml", "svg");
        MEDIA_TYPES.put("image/webp", "webp");
    }

    /**
     * 默认盐值<b>字节</b>长度
     */
    public static final int SALT_LENGTH_BYTE = 16;

    /**
     * 默认盐值<b>比特</b>长度
     */
    public static final int SALT_LENGTH_BIT = 16 * 8;

    /**
     * SHA-512 <b>字节</b>长度
     */
    public static final int SHA_512_LENGTH_BYTE = 64;

    /**
     * SHA-512 <b>比特</b>长度
     */
    public static final int SHA_512_LENGTH_BIT = 64 * 8;

    /**
     * AES-256-GCM Key 长度
     * 256 Bit
     */
    public static final int AES_256_GCM_KEY_LENGTH = 256;

    /**
     * AES-256-GCM IV 长度
     * 96 Bit = 12 Byte
     */
    public static final int AES_256_GCM_IV_LENGTH = 12;

    /**
     * GCM TAG 长度
     * 128 Bit
     */
    public static final int AES_256_GCM_TAG_LENGTH = 128;

    /**
     * AES 块大小
     * 128 Bit = 16 Byte
     */
    public static final int AES_BLOCK_SIZE = 16;
}
