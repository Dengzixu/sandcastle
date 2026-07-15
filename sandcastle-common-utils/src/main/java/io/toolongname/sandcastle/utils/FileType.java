package io.toolongname.sandcastle.utils;

import java.util.HashMap;
import java.util.Map;

public class FileType {
    private static final Map<String, FileType.Type> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put("image/apng", Type.IMAGE);
        TYPE_MAP.put("image/avif", Type.IMAGE);
        TYPE_MAP.put("image/bmp", Type.IMAGE);
        TYPE_MAP.put("image/gif", Type.IMAGE);
        TYPE_MAP.put("image/heic", Type.IMAGE);
        TYPE_MAP.put("image/heif", Type.IMAGE);
        TYPE_MAP.put("image/jpeg", Type.IMAGE);
        TYPE_MAP.put("image/png", Type.IMAGE);
        TYPE_MAP.put("image/svg+xml", Type.IMAGE);
        TYPE_MAP.put("image/webp", Type.IMAGE);
    }

    public static FileType.Type getType(String contentType) {
        return TYPE_MAP.getOrDefault(contentType, Type.OTHER);
    }


    public enum Type {
        APPLICATION,
        AUDIO,
        EXAMPLE,
        FONT,
        HAPTICS,
        IMAGE,
        MESSAGE,
        MODEL,
        MULTIPART,
        TEXT,
        VIDEO,
        OTHER;
    }
}
