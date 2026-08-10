package io.toolongname.sandcastle.entity.bo.user;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.toolongname.sandcastle.entity.dataobject.user.ActiveCodeDO;

import java.util.UUID;

public record ActiveCodeBO(long id, UUID userUuid, int status, String code, String createTime, String modifyTime) {


    public static ActiveCodeBO fromActiveDO(ActiveCodeDO activeCodeDO) {
        return new ActiveCodeBO(activeCodeDO.getId(),
                UUIDUtil.uuid(activeCodeDO.getUserUuid()),
                activeCodeDO.getStatus(),
                activeCodeDO.getCode(),
                activeCodeDO.getCreateTime(),
                activeCodeDO.getModifyTime());
    }
}
