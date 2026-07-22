package io.toolongname.sandcastle.entity.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VerifyVO(@JsonProperty(value = "user") UserVO userVO) {
}
