package io.toolongname.sandcastle.entity.bo.file;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.toolongname.sandcastle.entity.dataobject.file.FileDO;

import java.util.UUID;

public record FileBO(long id,
                     UUID uuid,
                     UUID userUuid,
                     UUID objectUuid,
                     int status,
                     int flag,
                     String title,
                     String contentType,
                     String type,
                     byte[] password,
                     long expireTimestamp,
                     String createTime,
                     String modifyTime) {

    public static FileBO fromFileDo(FileDO fileDO) {
        return new FileBO(fileDO.getId(),
                UUIDUtil.uuid(fileDO.getUuid()),
                UUIDUtil.uuid(fileDO.getUserUuid()),
                UUIDUtil.uuid(fileDO.getObjectUuid()),
                fileDO.getStatus(),
                fileDO.getFlag(),
                fileDO.getTitle(),
                fileDO.getContentType(),
                fileDO.getType(),
                fileDO.getPassword(),
                fileDO.getExpireTimestamp(),
                fileDO.getCreateTime(),
                fileDO.getModifyTime());
    }
}
