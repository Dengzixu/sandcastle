package io.toolongname.sandcastle.services.impl;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.UUIDUtil;
import io.toolongname.sandcastle.entity.bo.user.ActiveCodeBO;
import io.toolongname.sandcastle.entity.bo.user.UserBO;
import io.toolongname.sandcastle.entity.dataobject.user.ActiveCodeDO;
import io.toolongname.sandcastle.entity.dataobject.user.UserDO;
import io.toolongname.sandcastle.mapper.ActiveCodeMapper;
import io.toolongname.sandcastle.mapper.UserMapper;
import io.toolongname.sandcastle.services.UserService;
import io.toolongname.sandcastle.utils.password.Password;
import io.toolongname.sandcastlecommon.misc.constant.Status;
import io.toolongname.sandcastlecommon.misc.exception.user.ActiveCodeInvalidException;
import io.toolongname.sandcastlecommon.misc.exception.user.EmailDuplicateException;
import io.toolongname.sandcastlecommon.misc.exception.user.UserNotExistException;
import io.toolongname.sandcastlecommon.misc.exception.user.UsernameDuplicateException;
import org.apache.commons.codec.binary.Hex;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Random random = new Random(System.currentTimeMillis());

    private final UserMapper userMapper;
    private final ActiveCodeMapper activeCodeMapper;
    private final SqlSessionFactory sqlSessionFactory;


    public UserServiceImpl(UserMapper userMapper, ActiveCodeMapper activeCodeMapper, SqlSessionFactory sqlSessionFactory) {
        this.userMapper = userMapper;
        this.activeCodeMapper = activeCodeMapper;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void register(String username, String email, String plaintextPassword, String activeCode) {
        // 判断用户名是否已被注册
        userMapper.queryByUsername(username).ifPresent(_ -> {
            throw new UsernameDuplicateException();
        });

        // 判断邮箱是否已被使用
        userMapper.queryByEmail(email).ifPresent(_ -> {
            throw new EmailDuplicateException();
        });

        // 生成 UUID
        UUID userUuid = Generators.timeBasedEpochRandomGenerator().generate();

        // 判断 激活码是否存在
        ActiveCodeDO activeCodeDO = activeCodeMapper
                .queryByCode(activeCode)
                .filter(code -> !this.isActiveCodeUsed(code.getStatus()))
                .orElseThrow(ActiveCodeInvalidException::new);

        // 标记激活码被使用
        activeCodeMapper.modifyById(activeCodeDO.getId(), UUIDUtil.asByteArray(userUuid), Status.ActiveCode.USED);

        // 加密密码
        Password password = Password.fromPlaintext(plaintextPassword, Password.randomSalt());

        userMapper.add(UUIDUtil.asByteArray(userUuid), Status.User.DEFAULT, username, email, password.asByteArray());
    }

    @Override
    public UserBO loginByEmail(String email, String plaintextPassword) {
        UserDO userDO = userMapper
                .queryByEmail(email)
                .filter(u -> !this.isUserDeleted(u.getStatus()))
                .orElseThrow(UserNotExistException::new);

        Password dbPassword = Password.fromCombinedByteArray(userDO.getPassword());

        final byte[] salt = dbPassword.salt();

        if (!dbPassword.equals(Password.fromPlaintext(plaintextPassword, salt))) {
            throw new UserNotExistException();
        }

        return UserBO.fromUserDO(userDO);
    }

    @Override
    public UserBO getByUuid(String uuidString) {
        UUID uuid = UUIDUtil.uuid(uuidString);

        return userMapper.queryByUUID(UUIDUtil.asByteArray(uuid))
                .filter(u -> !this.isUserDeleted(u.getStatus()))
                .map(UserBO::fromUserDO)
                .orElseThrow(UserNotExistException::new);
    }

    @Override
    public UserBO getByEmail(String email) {
        return userMapper.queryByEmail(email)
                .filter(u -> !this.isUserDeleted(u.getStatus()))
                .map(UserBO::fromUserDO)
                .orElseThrow(UserNotExistException::new);
    }

    @Override
    public String generateActiveCode() {
        UUID nilUuid = UUIDUtil.nilUUID();

        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);

        String code = Hex.encodeHexString(randomBytes).toUpperCase();
        activeCodeMapper.add(UUIDUtil.asByteArray(nilUuid), 0, code);

        return code;
    }

    @Override
    public List<String> batchGenerateActiveCode(int batchSize) {
        UUID nilUuid = UUIDUtil.nilUUID();

        List<String> codeList = new ArrayList<>();

        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            ActiveCodeMapper activeCodeMapper = sqlSession.getMapper(ActiveCodeMapper.class);

            for (int i = 0; i < batchSize; i++) {
                byte[] randomBytes = new byte[16];
                random.nextBytes(randomBytes);
                String code = Hex.encodeHexString(randomBytes).toUpperCase();

                activeCodeMapper.add(UUIDUtil.asByteArray(nilUuid), 0, code);
                codeList.add(code);
            }
            sqlSession.commit();
        }

        return codeList;
    }

    @Override
    public List<ActiveCodeBO> listAvailableActiveCode() {
        return activeCodeMapper
                .listByStatus(Status.ActiveCode.DEFAULT)
                .stream()
                .map(ActiveCodeBO::fromActiveDO)
                .toList();
    }

    @Override
    public UserBO getByUsername(String username) {
        return userMapper.queryByUsername(username)
                .filter(u -> !this.isUserDeleted(u.getStatus()))
                .map(UserBO::fromUserDO)
                .orElseThrow(UserNotExistException::new);
    }
}
