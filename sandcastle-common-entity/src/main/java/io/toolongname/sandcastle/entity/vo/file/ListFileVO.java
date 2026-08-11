package io.toolongname.sandcastle.entity.vo.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ListFileVO(@JsonProperty(value = "file_list") List<FileVO> fileVOList) {
}
