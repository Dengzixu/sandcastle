package io.toolongname.sandcastle.services.impl;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.UUIDUtil;
import io.toolongname.sandcastle.component.StorageComponent;
import io.toolongname.sandcastle.entity.bo.file.FileBO;
import io.toolongname.sandcastle.entity.bo.file.ObjectBO;
import io.toolongname.sandcastle.entity.dataobject.file.FileDO;
import io.toolongname.sandcastle.entity.dataobject.file.ObjectDO;
import io.toolongname.sandcastle.mapper.FileMapper;
import io.toolongname.sandcastle.mapper.ObjectMapper;
import io.toolongname.sandcastle.services.FileService;
import io.toolongname.sandcastle.utils.FileType;
import io.toolongname.sandcastle.utils.secure.AES_256_GCM;
import io.toolongname.sandcastle.utils.secure.SHA_512;
import io.toolongname.sandcastlecommon.misc.constant.Constant;
import io.toolongname.sandcastlecommon.misc.constant.Flag;
import io.toolongname.sandcastlecommon.misc.constant.Status;
import io.toolongname.sandcastlecommon.misc.constant.TimeZone;
import io.toolongname.sandcastlecommon.misc.exception.file.FileNotExistException;
import io.toolongname.sandcastlecommon.misc.exception.file.FileUploadFailedException;
import io.toolongname.sandcastlecommon.misc.exception.general.PermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.ShortBufferException;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    private static final int BUFFER_SIZE = 1024 * 1024 * 5;

    private final FileMapper fileMapper;
    private final ObjectMapper objectMapper;

    private final StorageComponent storageComponent;

    public FileServiceImpl(FileMapper fileMapper,
                           ObjectMapper objectMapper,
                           StorageComponent storageComponent) {
        this.fileMapper = fileMapper;
        this.objectMapper = objectMapper;
        this.storageComponent = storageComponent;
    }

    @Override
    public UUID upload(@org.jspecify.annotations.NonNull InputStream inputStream,
                       String fileName,
                       String contentType,
                       long size,
                       UUID userUuid) {
        // 处理文件
        UUID objectUuid = this.saveObject(inputStream, size);

        // 生成文件 UUID
        UUID fileUuid = Generators.timeBasedEpochRandomGenerator().generate();

        // 获取文件类型
        FileType.Type fileType = FileType.getType(contentType);

        fileMapper.add(UUIDUtil.asByteArray(fileUuid),
                UUIDUtil.asByteArray(userUuid),
                UUIDUtil.asByteArray(objectUuid),
                Status.File.WATTING_PUBLISH,
                Flag.File.DEFAULT,
                fileName,
                contentType,
                fileType.toString(),
                new byte[80],
                0);

        return fileUuid;
    }

    @Override
    public UUID saveObject(@org.jspecify.annotations.NonNull InputStream inputStream, long size) {
        LOGGER.info("开始上传文件");

        // 进行加密文件
        byte[] key, iv, sha512Bytes;

        // 生成 Key
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(Constant.AES_256_GCM_KEY_LENGTH);
            key = keyGenerator.generateKey().getEncoded();

        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("文件上传失败，密钥生成错误", e);
            throw new FileUploadFailedException();
        }

        // 生成 IV
        iv = new byte[Constant.AES_256_GCM_IV_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        // 创建临时文件与目录
        File tempFile;
        try {
            File tempDir = new File("./temp/");
            if (!tempDir.exists()) {
                var _ = tempDir.mkdirs();
            }
            tempFile = File.createTempFile("temp-file-", ".enc", tempDir);
            tempFile.deleteOnExit();
        } catch (IOException e) {
            LOGGER.error("文件上传失败，无法写入临时文件", e);
            throw new FileUploadFailedException();
        }

        try (inputStream; FileOutputStream fos = new FileOutputStream(tempFile, false)) {
            // 创建缓冲区
            byte[] buffer = new byte[BUFFER_SIZE];

            // 计算 SHA-512 并直接加密文件
            SHA_512 sha512 = new SHA_512();
            AES_256_GCM aes256Gcm = new AES_256_GCM(key, iv);
            aes256Gcm.init(Cipher.ENCRYPT_MODE);

            // 写出 iv
            fos.write(iv);

            int n;
            while ((n = inputStream.read(buffer)) != -1) {
                sha512.update(buffer, 0, n);
                byte[] result = aes256Gcm.update(buffer, 0, n);
                if (result != null) {
                    fos.write(result);
                }
            }

            sha512Bytes = sha512.digest();
            fos.write(aes256Gcm.doFinal());
        } catch (IOException | IllegalBlockSizeException | ShortBufferException e) {
            LOGGER.error("文件上传失败，加密错误", e);
            throw new RuntimeException(e);
        }

        // 判断是否存在
        Optional<ObjectDO> optionalObjectDO = objectMapper.queryBySha512(sha512Bytes);
        if (optionalObjectDO.isPresent()) {
            UUID existingObjectUuid = UUIDUtil.uuid(optionalObjectDO.get().getUuid());
            LOGGER.info("对象已存在，跳过上传，对象 UUID: {}", existingObjectUuid);
            return existingObjectUuid;
        }

        // 生成 Object UUID
        UUID objectUuid = Generators.timeBasedEpochRandomGenerator().generate();
        // 生成 path
        String objectPath = this.getPath(objectUuid.toString());
        // 上传 Object
        storageComponent.save(tempFile, objectPath);
        // 保存 Object
        objectMapper.add(UUIDUtil.asByteArray(objectUuid), 0, sha512Bytes, size, objectPath, key);

        return objectUuid;
    }

    @Override
    public UUID publish(UUID fileUuid,
                        @org.jspecify.annotations.Nullable Long flag,
                        @org.jspecify.annotations.Nullable String password,
                        int validityPeriod,
                        UUID userUuid) {
        FileDO fileDO = fileMapper.queryByUuid(UUIDUtil.asByteArray(fileUuid))
                .filter(e -> !this.isDeleted(e.getStatus()))
                .orElseThrow(FileNotExistException::new);

        // 验证权限
        if (!userUuid.equals(UUIDUtil.uuid(fileDO.getUserUuid()))) {
            throw new PermissionDeniedException();
        }

        // 计算过期时间
        long expireTimestamp = ZonedDateTime.now(ZoneId.of(TimeZone.ASIA_SHANGHAI))
                .plusDays(validityPeriod)
                .toEpochSecond();

        if ((fileDO.getStatus() & Status.File.WATTING_PUBLISH) == Status.File.WATTING_PUBLISH) {
            Integer newStatus = fileDO.getStatus() ^ Status.File.WATTING_PUBLISH;
            fileMapper.modifyByUuid(UUIDUtil.asByteArray(fileUuid), newStatus, flag, new byte[80], expireTimestamp);
        } else {
            LOGGER.warn("文件: [{}] 已发布。", fileUuid);
        }

        return fileUuid;
    }

    @Override
    public FileBO readByUUID(UUID fileUuid) {
        FileDO fileDO = fileMapper.queryByUuid(UUIDUtil.asByteArray(fileUuid))
                .filter(f -> !(this.isDeleted(f.getStatus())))
                .filter(f -> !(this.isExpired(f.getStatus())))
                .orElseThrow(FileNotExistException::new);

        // 二次判断文件是否过期
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(TimeZone.ASIA_SHANGHAI));
        if (fileDO.getExpireTimestamp() <= now.toEpochSecond()) {
            LOGGER.warn("文件: [{}] 已过期，但尚未标记。过期时间: {}",
                    UUIDUtil.uuid(fileDO.getUuid()),
                    Instant.ofEpochSecond(fileDO.getExpireTimestamp())
                            .atZone(ZoneId.of(TimeZone.ASIA_SHANGHAI))
                            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            this.markExpire(fileUuid);
            throw new FileNotExistException();
        }

        return FileBO.fromFileDo(fileDO);
    }

    @Override
    public void markExpire(UUID fileUuid) {
        FileDO fileDO = fileMapper.queryByUuid(UUIDUtil.asByteArray(fileUuid))
                .filter(f -> !(this.isDeleted(f.getStatus())))
                .orElseThrow(FileNotExistException::new);

        int status = fileDO.getStatus();

        if (this.isExpired(status)) {
            return;
        }
        status = status | Status.File.EXPIRED;

        fileMapper.modifyByUuid(UUIDUtil.asByteArray(fileUuid), status, null, null, 0L);
    }

    @Override
    public FileBO getById(long id) {
        FileBO fileBO = FileBO
                .fromFileDo(fileMapper.queryById(id)
                        .filter(fileDO -> !this.isDeleted(fileDO.getStatus()))
                        .orElseThrow(FileNotExistException::new));

        return fileBO;
    }

    @Override
    public FileBO getByUuid(UUID fileUuid) {
        FileBO fileBO = FileBO
                .fromFileDo(fileMapper.queryByUuid(UUIDUtil.asByteArray(fileUuid))
                        .filter(fileDO -> !this.isDeleted(fileDO.getStatus()))
                        .orElseThrow(FileNotExistException::new));

        return fileBO;
    }

    @Override
    public ObjectBO getObjectByUuid(UUID objectUuid) {
        ObjectDO objectDO = objectMapper.queryByUuid(UUIDUtil.asByteArray(objectUuid)).orElseThrow(FileNotExistException::new);

        String objectUrl = storageComponent.getPresignUrl(objectDO.getPath());

        ObjectBO objectBO = new ObjectBO(objectDO.getId(),
                UUIDUtil.uuid(objectDO.getUuid()),
                objectDO.getStatus(),
                objectDO.getSha512(),
                objectDO.getSize(),
                objectDO.getPath(),
                objectUrl,
                objectDO.getEncryptKey(),
                objectDO.getCreateTime(),
                objectDO.getModifyTime());
        return objectBO;
    }

    private String getPath(String uuidString) {
        String prefix = uuidString.substring(0, 2);

        return "file/" + prefix + "/" + uuidString;
    }
}
