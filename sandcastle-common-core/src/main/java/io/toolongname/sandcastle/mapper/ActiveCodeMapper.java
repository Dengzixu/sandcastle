package io.toolongname.sandcastle.mapper;

import io.toolongname.sandcastle.entity.dataobject.user.ActiveCodeDO;
import io.toolongname.sandcastle.mapper.provider.ActiveCodeMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface ActiveCodeMapper {
    @InsertProvider(type = ActiveCodeMapperProvider.class, method = "addSql")
    void add(byte[] userUuidByte, long status, String code);

    @SelectProvider(type = ActiveCodeMapperProvider.class, method = "getByCodeSql")
    Optional<ActiveCodeDO> queryByCode(String code);

    @SelectProvider(type = ActiveCodeMapperProvider.class, method = "listByStatusSql")
    List<ActiveCodeDO> listByStatus(int status);

    @UpdateProvider(type = ActiveCodeMapperProvider.class, method = "modifyByIdSql")
    void modifyById(long id, byte[] userUuid, int status);
}
