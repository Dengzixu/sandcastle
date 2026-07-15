package io.toolongname.sandcastlecommon.misc.exception.user;


import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class UserNotExistException extends BusinessException {
    public UserNotExistException() {
        super(ErrorCode.USER_NOT_EXIST, 404);
    }
}
