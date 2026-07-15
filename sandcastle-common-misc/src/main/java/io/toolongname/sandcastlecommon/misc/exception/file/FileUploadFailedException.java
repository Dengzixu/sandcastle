package io.toolongname.sandcastlecommon.misc.exception.file;


import io.toolongname.sandcastlecommon.misc.enums.ErrorCode;
import io.toolongname.sandcastlecommon.misc.exception.BusinessException;

public class FileUploadFailedException extends BusinessException {
    public FileUploadFailedException() {
        super(ErrorCode.FILE_UPLOAD_FAILED, 500);
    }
}
