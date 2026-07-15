package io.toolongname.sandcastlecommon.misc.constant;

public interface Status {
    class User {
        // 默认状态
        public static final int DEFAULT = 0b0000_0000_0000_0000_0000_0000;

        // 删除状态
        public static final int DELETED = 0b1000_0000_0000_0000_0000_0000;
    }

    class File {
        // 默认状态
        public static final int DEFAULT = 0b0000_0000_0000_0000_0000_0000;

        public static final int WATTING_PUBLISH = 0b0000_0000_0000_0000_0000_0001;

        // 删除状态
        public static final int DELETED = 0b1000_0000_0000_0000_0000_0000;
    }
}
