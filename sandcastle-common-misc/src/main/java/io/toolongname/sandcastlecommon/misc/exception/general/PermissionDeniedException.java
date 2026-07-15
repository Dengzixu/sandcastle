package io.toolongname.sandcastlecommon.misc.exception.general;

import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class PermissionDeniedException extends BusinessException {
    public PermissionDeniedException() {
        super(ErrorCode.PERMISSION_DENIED, 403);
    }
}
