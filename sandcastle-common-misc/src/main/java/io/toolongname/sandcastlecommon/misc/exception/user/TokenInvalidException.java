package io.toolongname.sandcastlecommon.misc.exception.user;


import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class TokenInvalidException extends BusinessException {
    public TokenInvalidException() {
        super(ErrorCode.TOKEN_INVALID, 401);
    }
}
