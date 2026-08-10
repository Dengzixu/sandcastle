package io.toolongname.sandcastlecommon.misc.exception.user;

import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class ActiveCodeInvalidException extends BusinessException {
    public ActiveCodeInvalidException() {
        super(ErrorCode.ACTIVE_CODE_INVALID, 400);
    }
}
