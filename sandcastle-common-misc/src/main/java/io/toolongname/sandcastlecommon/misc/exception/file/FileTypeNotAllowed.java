package io.toolongname.sandcastlecommon.misc.exception.file;

import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class FileTypeNotAllowed extends BusinessException {
    public FileTypeNotAllowed() {
        super(ErrorCode.FILE_TYPE_NOT_ALLOWED, 400);
    }
}
