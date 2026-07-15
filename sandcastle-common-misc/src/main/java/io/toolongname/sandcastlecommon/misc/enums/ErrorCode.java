package io.toolongname.sandcastlecommon.misc.enums;

public enum ErrorCode {
    NO_ERROR(0, "无错误"),

    USER_NOT_EXIST(-1000_0001, "用户不存在或密码错误"),
    USERNAME_DUPLICATE(-1000_0002, "用户名已被使用。"),
    EMAIL_DUPLICATE(-1000_0003, "邮箱已被使用。"),
    TOKEN_INVALID(-1000_1000, "身份验证失败, Token 无效或已过期"),

    FILE_NOT_EXIST(-1001_0001, "文件不存在"),
    FILE_UPLOAD_FAILED(-1001_0002, "文件上传失败，内部错误"),
    FILE_TYPE_NOT_ALLOWED(-1001_0003, "文件类型不允许使用"),

    DATA_VALIDATION_FAILED(-1800_0001, "请求数据错误"),

    UNAUTHORIZED(-1900_0401, "未授权，请检查 Token"),
    PERMISSION_DENIED(-1900_0403, "权限不足"),
    INTERNAL_SERVER_ERROR(-1900_0500, "内部错误"),

    UNDEFINED(-1999_1999, "未定义错误"),
    UNKNOWN(-1999_9999, "未知错误。");


    private final int code;
    private final String message;


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
