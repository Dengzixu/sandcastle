package io.toolongname.sandcastle.entity.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(@NotBlank(message = "用户名不能为空")
                          String username,
                          @NotBlank(message = "电子邮件不能为空")
                          @Email(message = "电子邮件格式不正确")
                          String email,
                          @NotBlank(message = "密码不能为空")
                          @Size(message = "密码不能小于 8 位", min = 8)
                          String password) {
}
