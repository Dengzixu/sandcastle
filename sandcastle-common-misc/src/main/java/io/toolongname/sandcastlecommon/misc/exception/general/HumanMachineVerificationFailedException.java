package io.toolongname.sandcastlecommon.misc.exception.general;

import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class HumanMachineVerificationFailedException extends BusinessException {
    public HumanMachineVerificationFailedException() {
        super(ErrorCode.HUMAN_MACHINE_VERIFICATION_FAILED, 403);
    }
}
