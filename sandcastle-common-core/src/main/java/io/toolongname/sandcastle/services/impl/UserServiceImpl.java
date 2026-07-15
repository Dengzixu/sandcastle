package io.toolongname.sandcastle.services.impl;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.UUIDUtil;
import io.toolongname.sandcastle.entity.bo.user.UserBO;
import io.toolongname.sandcastle.entity.dataobject.user.UserDO;
import io.toolongname.sandcastle.mapper.UserMapper;
import io.toolongname.sandcastle.services.UserService;
import io.toolongname.sandcastle.utils.Password;
import io.toolongname.sandcastlecommon.misc.constant.Constant;
import io.toolongname.sandcastlecommon.misc.constant.Status;
import io.toolongname.sandcastlecommon.misc.exception.user.EmailDuplicateException;
import io.toolongname.sandcastlecommon.misc.exception.user.UserNotExistException;
import io.toolongname.sandcastlecommon.misc.exception.user.UsernameDuplicateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void register(String username, String email, String plaintextPassword) {
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

        // 生成 slat
        byte[] salt = Password.getSalt(Constant.SALT_LENGTH_BYTE);
        byte[] encryptedPasswordBytes = new byte[Constant.SHA_512_LENGTH_BYTE];

        // 加密密码
        try {
            encryptedPasswordBytes = new Password(Constant.SHA_512_LENGTH_BIT).hashed(plaintextPassword, salt);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("密码加密错误", e);
        }

        // 合并密码与 Salt
        ByteBuffer passwordBuffer = ByteBuffer.allocate(Constant.SHA_512_LENGTH_BYTE + Constant.SALT_LENGTH_BYTE);
        passwordBuffer.put(encryptedPasswordBytes);
        passwordBuffer.put(salt);
        passwordBuffer.flip();

        userMapper.add(UUIDUtil.asByteArray(userUuid), Status.User.DEFAULT, username, email, passwordBuffer.array());
    }

    @Override
    public UserBO loginByEmail(String email, String plaintextPassword) {
        UserDO userDO = userMapper
                .queryByEmail(email)
                .filter(u -> !this.isUserDeleted(u.getStatus()))
                .orElseThrow(UserNotExistException::new);

        byte[] pwd = new byte[Constant.SHA_512_LENGTH_BYTE];
        byte[] salt = new byte[Constant.SALT_LENGTH_BYTE];

        // 分离密码
        ByteBuffer passwordBuffer = ByteBuffer.wrap(userDO.getPassword());
        passwordBuffer.get(pwd);
        passwordBuffer.get(salt);


        // 加密密码
        byte[] encryptedPasswordBytes = new byte[Constant.SHA_512_LENGTH_BYTE];
        try {
            encryptedPasswordBytes = new Password(Constant.SHA_512_LENGTH_BIT).hashed(plaintextPassword, salt);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("密码加密错误", e);
        }

        // 判断密码是否正确
        if (!Arrays.equals(encryptedPasswordBytes, pwd)) {
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
    public UserBO getByUsername(String username) {
        return userMapper.queryByUsername(username)
                .filter(u -> !this.isUserDeleted(u.getStatus()))
                .map(UserBO::fromUserDO)
                .orElseThrow(UserNotExistException::new);
    }
}
