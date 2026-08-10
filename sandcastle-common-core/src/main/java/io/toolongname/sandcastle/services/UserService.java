package io.toolongname.sandcastle.services;

import io.toolongname.sandcastle.entity.bo.user.ActiveCodeBO;
import io.toolongname.sandcastle.entity.bo.user.UserBO;
import io.toolongname.sandcastlecommon.misc.constant.Status;

import java.util.List;

public interface UserService {

    /**
     * 用户注册
     *
     * @param username          用户名
     * @param email             邮箱
     * @param plaintextPassword 密码
     * @param activeCode        激活码
     */
    void register(String username, String email, String plaintextPassword, String activeCode);

    /**
     * 邮箱登录
     *
     * @param email             邮箱
     * @param plaintextPassword 密码
     */
    UserBO loginByEmail(String email, String plaintextPassword);

    /**
     * 通过 uuid 查询用户
     *
     * @param uuidString 用户 uuid
     * @return User
     */
    UserBO getByUuid(String uuidString);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     */
    UserBO getByUsername(String username);

    /**
     * 通过邮件查询用户
     *
     * @param email 邮箱
     */
    UserBO getByEmail(String email);

    /**
     * 生成一个用户激活码。
     *
     * @return 用户激活码字符串；
     */
    String generateActiveCode();

    /**
     * 批量生成用户激活码。
     *
     * @param batchSize 激活码生成数量；
     * @return 生成的激活码列表，每个元素为对应的激活码字符串；
     */
    List<String> batchGenerateActiveCode(int batchSize);

    /**
     * 列出所有可用的激活码。
     *
     * @return 可用的激活码列表，每个元素为 {@link ActiveCodeBO} 对象；
     */
    List<ActiveCodeBO> listAvailableActiveCode();

    /**
     * 判断用户状态是否表示已删除
     *
     * @param status 用户状态值
     * @return 如果状态表示用户已被删除则返回true，否则返回false
     */
    default boolean isUserDeleted(int status) {
        return (status & Status.User.DELETED) == Status.User.DELETED;
    }

    default boolean isActiveCodeUsed(int status) {
        return (status & Status.ActiveCode.USED) == Status.ActiveCode.USED;
    }
}
