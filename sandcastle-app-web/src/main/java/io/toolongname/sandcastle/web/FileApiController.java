package io.toolongname.sandcastle.web;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.toolongname.sandcastle.entity.bo.file.FileBO;
import io.toolongname.sandcastle.entity.bo.file.ObjectBO;
import io.toolongname.sandcastle.entity.dto.file.PublishDTO;
import io.toolongname.sandcastle.entity.vo.file.*;
import io.toolongname.sandcastle.model.ResponseData;
import io.toolongname.sandcastle.services.FileService;
import io.toolongname.sandcastlecommon.misc.annotation.JwtDecode;
import io.toolongname.sandcastlecommon.misc.exception.user.TokenInvalidException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/file/api")
@CrossOrigin(origins = "*")
public class FileApiController {
    private final FileService fileService;

    public FileApiController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/v1/upload")
    public ResponseEntity<ResponseData> upload(@RequestParam("file") MultipartFile multipartFile,
                                               @JwtDecode Optional<String> userUuidOptional) {
        UUID userUuid = UUIDUtil.uuid(userUuidOptional.orElseThrow(TokenInvalidException::new));

        UUID fileUuid;
        try {
            fileUuid = fileService.upload(multipartFile.getInputStream(),
                    multipartFile.getOriginalFilename(),
                    multipartFile.getContentType(),
                    multipartFile.getSize(),
                    userUuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(ResponseData.SUCCEEDED_WITH_DATA(new UploadVO(fileUuid.toString())));
    }

    @PostMapping("/v1/publish")
    public ResponseEntity<ResponseData> publish(@RequestBody @Validated PublishDTO publishDTO,
                                                @JwtDecode Optional<String> userUuidOptional) {
        UUID userUuid = UUIDUtil.uuid(userUuidOptional.orElseThrow(TokenInvalidException::new));
        UUID fileUuid = UUIDUtil.uuid(publishDTO.fileUuid());

        fileService.publish(fileUuid,
                publishDTO.flag(),
                publishDTO.password(),
                publishDTO.validityPeriod(),
                userUuid);

        return ResponseEntity.ok(ResponseData.SUCCEEDED_WITH_DATA(new PublishVO(fileUuid.toString())));
    }


    @GetMapping("/v1/id/{id}")
    public ResponseEntity<ResponseData> getById(@PathVariable long id,
                                                @JwtDecode Optional<String> userUuidOptional) {
        FileBO fileBO = fileService.readById(id);
        ObjectBO objectBO = fileService.getObjectByUuid(fileBO.objectUuid());

        FileVO fileVO = FileVO.fromFileBO(fileBO);
        ObjectVO objectVO = ObjectVO.fromObjectBO(objectBO);

        return ResponseEntity.ok(ResponseData.SUCCEEDED_WITH_DATA(new GetFileVO(fileVO, objectVO)));
    }

    @GetMapping("/v1/uuid/{uuid}")
    public ResponseEntity<ResponseData> getByUuid(@PathVariable(name = "uuid") String fileUuidStr,
                                                  @JwtDecode Optional<String> userUuidOptional) {

        FileBO fileBO = fileService.readByUuid(UUIDUtil.uuid(fileUuidStr));
        ObjectBO objectBO = fileService.getObjectByUuid(fileBO.objectUuid());

        FileVO fileVO = FileVO.fromFileBO(fileBO);
        ObjectVO objectVO = ObjectVO.fromObjectBO(objectBO);

        return ResponseEntity.ok(ResponseData.SUCCEEDED_WITH_DATA(new GetFileVO(fileVO, objectVO)));
    }

}
