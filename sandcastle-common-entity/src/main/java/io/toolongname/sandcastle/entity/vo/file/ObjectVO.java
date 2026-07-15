package io.toolongname.sandcastle.entity.vo.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.toolongname.sandcastle.entity.bo.file.ObjectBO;

import java.util.Base64;

public record ObjectVO(@JsonIgnore long id,
                       String uuid,
                       int status,
                       long size,
                       @JsonProperty(value = "object_url") String objectUrl,
                       @JsonProperty(value = "encrypt_key") String encryptKey,
                       @JsonIgnore @JsonProperty(value = "create_time") String createTime,
                       @JsonIgnore @JsonProperty(value = "modify_time") String modifyTime) {

    public static ObjectVO fromObjectBO(ObjectBO objectBO) {
        return new ObjectVO(objectBO.id(),
                objectBO.uuid().toString(),
                objectBO.status(),
                objectBO.size(),
                objectBO.objectUrl(),
                Base64.getEncoder().encodeToString(objectBO.encryptKey()),
                objectBO.createTime(),
                objectBO.modifyTime());
    }
}
