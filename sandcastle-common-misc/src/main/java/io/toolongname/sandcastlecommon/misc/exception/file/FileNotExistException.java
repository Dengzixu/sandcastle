package io.toolongname.sandcastlecommon.misc.exception.file;

import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class FileNotExistException extends BusinessException {
    public FileNotExistException() {
        super(ErrorCode.FILE_NOT_EXIST, 404);
    }
}
