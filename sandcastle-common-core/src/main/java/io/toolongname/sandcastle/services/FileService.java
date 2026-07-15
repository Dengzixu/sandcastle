package io.toolongname.sandcastle.services;

import io.toolongname.sandcastle.entity.bo.file.FileBO;
import io.toolongname.sandcastle.entity.bo.file.ObjectBO;
import io.toolongname.sandcastlecommon.misc.constant.Constant;
import io.toolongname.sandcastlecommon.misc.constant.Status;

import java.io.InputStream;
import java.util.UUID;

public interface FileService {
    /**
     * 上传文件
     *
     * @param inputStream 文件 InputStream
     * @param fileName    文件名
     * @param contentType Content-Type
     * @param size        文件大小
     * @param userUuid    上传用户的 UUID
     * @return 文件的 UUID
     */
    UUID upload(@org.jspecify.annotations.NonNull InputStream inputStream,
                String fileName, String contentType, long size, UUID userUuid);

    /**
     * 保存对象
     *
     * @param inputStream 文件 InputStream
     * @param size        文件大小
     * @return 对象 UUID
     */
    UUID saveObject(@org.jspecify.annotations.NonNull InputStream inputStream, long size);

    /**
     * 发布文件
     *
     * @param fileUuid 文件 UUID
     * @param flag     标签标记
     * @param password 密码（如果为空则不设置密码）
     * @param userUuid 用户 UUID
     * @return 文件 UUID
     */
    UUID publish(UUID fileUuid,
                 @org.jspecify.annotations.Nullable Long flag,
                 @org.jspecify.annotations.Nullable String password,
                 int validityPeriod,
                 UUID userUuid);

    /**
     * 读取文件
     *
     * @param fileUuid 文件 UUID
     * @return FileBO
     */
    FileBO readByUUID(UUID fileUuid);

    FileBO getById(long id);

    /**
     * 根据UUID获取文件实体。
     *
     * @param fileUuid 要检索的文件的UUID
     * @return 包含文件信息的FileBO对象
     */
    FileBO getByUuid(UUID fileUuid);

    ObjectBO getObjectByUuid(UUID objectUuid);

    default boolean allowImageType(String contentType) {
        return Constant.MEDIA_TYPES.containsKey(contentType);
    }

    default boolean isDeleted(int status) {
        return (status & Status.File.DELETED) == Status.File.DELETED;
    }
}
