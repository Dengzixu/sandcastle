package io.toolongname.sandcastle.entity.dataobject.user;

import java.util.Arrays;

public class UserDO {
    private long id;
    private byte[] uuid;
    private int status;
    private String username;
    private String email;
    private byte[] password;

    private String createTime;
    private String modifyTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getUuid() {
        return uuid;
    }

    public void setUuid(byte[] uuid) {
        this.uuid = uuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "id=" + id +
                ", uuid=" + Arrays.toString(uuid) +
                ", status=" + status +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password=" + Arrays.toString(password) +
                ", createTime='" + createTime + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                '}';
    }
}
