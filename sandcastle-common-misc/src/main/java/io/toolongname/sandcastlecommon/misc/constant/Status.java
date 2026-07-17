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

        // 等待发布状态
        public static final int WATTING_PUBLISH = 0b0000_0000_0000_0000_0000_0001;

        // 已过期
        public static final int EXPIRED = 0b0100_0000_0000_0000_0000_0000;

        // 删除状态
        public static final int DELETED = 0b1000_0000_0000_0000_0000_0000;
    }
}
