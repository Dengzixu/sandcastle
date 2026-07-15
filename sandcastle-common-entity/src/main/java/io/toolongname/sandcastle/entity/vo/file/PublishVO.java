package io.toolongname.sandcastle.entity.vo.file;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PublishVO(@JsonProperty(value = "file_uuid") String fileUuid) {
}
