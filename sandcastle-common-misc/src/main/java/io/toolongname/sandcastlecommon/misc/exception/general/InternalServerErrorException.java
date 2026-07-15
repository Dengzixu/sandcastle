package io.toolongname.sandcastlecommon.misc.exception.general;

import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class InternalServerErrorException extends BusinessException {
    public InternalServerErrorException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR, 500);
    }
}
