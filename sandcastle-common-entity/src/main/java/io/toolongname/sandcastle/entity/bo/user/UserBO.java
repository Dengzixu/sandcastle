package io.toolongname.sandcastle.entity.bo.user;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.toolongname.sandcastle.entity.dataobject.user.UserDO;

import java.util.UUID;

public record UserBO(long id,
                     UUID uuid,
                     int status,
                     String username,
                     String email,
                     String createTime,
                     String modifyTime) {


    public static UserBO fromUserDO(UserDO userDO) {
        return new UserBO(userDO.getId(), UUIDUtil.uuid(userDO.getUuid()),
                userDO.getStatus(), userDO.getUsername(),
                userDO.getEmail(), userDO.getCreateTime(),
                userDO.getModifyTime());
    }
}
