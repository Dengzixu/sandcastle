package io.toolongname.sandcastle.entity.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.toolongname.sandcastle.entity.bo.user.UserBO;

public record UserVO(long id,
                     String uuid,
                     int status,
                     String username,
                     String email,
                     @JsonProperty("create_time")
                     String createTime,
                     @JsonProperty("modify_time")
                     String modifyTime) {

    public static UserVO fromUserBO(UserBO userBO) {
        return new UserVO(userBO.id(),
                userBO.uuid().toString(),
                userBO.status(),
                userBO.username(),
                userBO.email(),
                userBO.createTime(),
                userBO.modifyTime());
    }
}
