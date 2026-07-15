package io.toolongname.sandcastle.services;

import io.toolongname.sandcastle.entity.bo.user.UserBO;
import io.toolongname.sandcastlecommon.misc.constant.Status;

public interface UserService {

    /**
     * 用户注册
     *
     * @param username          用户名
     * @param email             邮箱
     * @param plaintextPassword 密码
     */
    void register(String username, String email, String plaintextPassword);


    /**
     * 邮箱登录
     *
     * @param email             邮箱
     * @param plaintextPassword 密码
     */
    UserBO loginByEmail(String email, String plaintextPassword);

    /**
     *  通过 uuid 查询用户
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
     * 判断用户状态是否表示已删除
     *
     * @param status 用户状态值
     * @return 如果状态表示用户已被删除则返回true，否则返回false
     */
    default boolean isUserDeleted(int status) {
        return (status & Status.User.DELETED) == Status.User.DELETED;
    }
}
