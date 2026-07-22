package io.toolongname.sandcastle.entity.bo.file;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.toolongname.sandcastle.entity.dataobject.file.ObjectDO;

import java.util.UUID;

public record ObjectBO(long id, UUID uuid, int status, byte[] sha512, long size, String path, String objectUrl,
                       byte[] encryptKey,
                       String createTime, String modifyTime) {
    public static ObjectBO formObjectDO(ObjectDO objectDO) {
        return new ObjectBO(objectDO.getId(),
                UUIDUtil.uuid(objectDO.getUuid()),
                objectDO.getStatus(),
                objectDO.getSha512(),
                objectDO.getSize(),
                objectDO.getPath(),
                "",
                objectDO.getEncryptKey(),
                objectDO.getCreateTime(),
                objectDO.getModifyTime());
    }
}
