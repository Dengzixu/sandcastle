package io.toolongname.sandcastle.mapper;

import io.toolongname.sandcastle.entity.bo.file.FileBO;
import io.toolongname.sandcastle.entity.dataobject.file.FileDO;
import io.toolongname.sandcastle.mapper.provider.FileMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Mapper
@Repository
public interface FileMapper {

    @InsertProvider(type = FileMapperProvider.class, method = "addSqlBuilder")
    void add(byte[] uuid, byte[] userUuid, byte[] objectUuid, int status, long flag, String title,
             String contentType, String type, byte[] password, long expireTimestamp);

    @SelectProvider(type = FileMapperProvider.class, method = "queryByIdSqlBuilder")
    Optional<FileDO> queryById(long id);

    @SelectProvider(type = FileMapperProvider.class, method = "queryByUuidSqlBuilder")
    Optional<FileDO> queryByUuid(byte[] fileUuid);

    @UpdateProvider(type = FileMapperProvider.class, method = "modifyByUuidSqlBuilder")
    void modifyByUuid(byte[] fileUuid, Integer status, Long flag, byte[] password, Long expireTimestamp);
}
