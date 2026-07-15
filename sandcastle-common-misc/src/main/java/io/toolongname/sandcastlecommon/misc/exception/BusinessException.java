package io.toolongname.sandcastlecommon.misc.exception;


import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;

public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int httpStatus;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public BusinessException(ErrorCode errorCode, int httpStatus) {
        super(errorCode.message(), null, false, false);

        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public BusinessException() {
        this(ErrorCode.UNKNOWN, 500);
    }

}
