package io.toolongname.sandcastle.entity.dataobject.user;

import java.util.Arrays;

public class ActiveCodeDO {
    private long id;
    private byte[] userUuid;
    private int status;
    private String code;

    private String createTime;
    private String modifyTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(byte[] userUuid) {
        this.userUuid = userUuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        return "ActiveCodeDO{" +
                "id=" + id +
                ", userId=" + Arrays.toString(userUuid) +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", createTime='" + createTime + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                '}';
    }
}
