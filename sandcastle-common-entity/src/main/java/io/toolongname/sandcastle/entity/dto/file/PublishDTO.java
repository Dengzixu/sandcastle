package io.toolongname.sandcastle.entity.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.Nullable;

public record PublishDTO(@JsonProperty(value = "file_uuid")
                         @NotBlank(message = "文件 UUID 不能为空")
                         String fileUuid,
                         @Nullable
                         Long flag,
                         @Nullable
                         String password,
                         @JsonProperty(value = "validity_period")
                         @Min(value = 1, message = "最短天数不能小于 1 天")
                         @Max(value = 7, message = "最长天数不能大于 7 天")
                         int validityPeriod) {
}
