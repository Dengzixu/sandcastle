package io.toolongname.sandcastle.entity.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginVO(@JsonProperty(value = "user") UserVO userVO,
                      String token) {
}
