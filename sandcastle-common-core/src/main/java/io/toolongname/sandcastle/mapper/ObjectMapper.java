package io.toolongname.sandcastle.mapper;

import io.toolongname.sandcastle.entity.dataobject.file.ObjectDO;
import io.toolongname.sandcastle.mapper.provider.ObjectMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface ObjectMapper {
    @InsertProvider(type = ObjectMapperProvider.class, method = "addSqlBuilder")
    void add(byte[] uuid, int status, byte[] sha512, long size, String path, byte[] encryptKey);


    @SelectProvider(type = ObjectMapperProvider.class, method = "queryByUuidSqlBuilder")
    Optional<ObjectDO> queryByUuid(byte[] uuid);

    @SelectProvider(type = ObjectMapperProvider.class, method = "queryBySha512SqlBuilder")
    Optional<ObjectDO> queryBySha512(byte[] sha512);
}
