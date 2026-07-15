package io.toolongname.sandcastle.entity.vo.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.toolongname.sandcastle.entity.bo.file.FileBO;

public record FileVO(long id,
                     @JsonProperty(value = "file_uuid")
                     String uuid,
                     @JsonProperty(value = "user_uuid")
                     String userUuid,
                     @JsonProperty(value = "object_uuid")
                     String objectUuid,
                     int status,
                     long flag,
                     String title,
                     @JsonProperty(value = "content_type") String contentType,
                     String type,
                     @JsonProperty(value = "create_time") String createTime,
                     @JsonIgnore @JsonProperty(value = "modify_time") String modifyTime) {

    public static FileVO fromFileBO(FileBO fileBO) {
        return new FileVO(fileBO.id(),
                fileBO.uuid().toString(),
                fileBO.userUuid().toString(),
                fileBO.objectUuid().toString(),
                fileBO.status(),
                fileBO.flag(),
                fileBO.title(),
                fileBO.contentType(),
                fileBO.type(),
                fileBO.createTime(),
                fileBO.modifyTime());
    }
}
