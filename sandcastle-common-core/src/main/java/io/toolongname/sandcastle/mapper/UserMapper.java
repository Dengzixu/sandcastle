package io.toolongname.sandcastle.mapper;

import io.toolongname.sandcastle.entity.dataobject.user.UserDO;
import io.toolongname.sandcastle.mapper.provider.UserMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface UserMapper {
    @InsertProvider(type = UserMapperProvider.class, method = "addSQLBuilder")
    void add(byte[] uuid, int status, String username, String email, byte[] password);

    @SelectProvider(type = UserMapperProvider.class, method = "queryByUUIDSQLBuilder")
    Optional<UserDO> queryByUUID(byte[] uuid);

    @SelectProvider(type = UserMapperProvider.class, method = "queryByEmailSQLBuilder")
    Optional<UserDO> queryByEmail(String email);

    @SelectProvider(type = UserMapperProvider.class, method = "queryByUsernameSQLBuilder")
    Optional<UserDO> queryByUsername(String username);
}
