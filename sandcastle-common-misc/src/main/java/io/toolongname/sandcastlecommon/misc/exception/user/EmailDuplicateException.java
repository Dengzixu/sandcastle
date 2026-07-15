package io.toolongname.sandcastlecommon.misc.exception.user;


import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class EmailDuplicateException extends BusinessException {
    public EmailDuplicateException() {
        super(ErrorCode.EMAIL_DUPLICATE, 409);
    }
}
