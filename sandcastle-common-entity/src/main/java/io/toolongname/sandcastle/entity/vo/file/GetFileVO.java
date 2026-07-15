package io.toolongname.sandcastle.entity.vo.file;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetFileVO(@JsonProperty(value = "file") FileVO fileVO,
                        @JsonProperty(value = "object") ObjectVO objectVO) {
}
