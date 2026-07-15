package io.toolongname.sandcastlecommon.misc.exception.user;


import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class UsernameDuplicateException extends BusinessException {
    public UsernameDuplicateException() {
        super(ErrorCode.USERNAME_DUPLICATE, 409);
    }
}
