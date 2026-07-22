package io.toolongname.sandcastle.entity.dataobject.file;

import java.util.Arrays;

public class FileDO {
    private long id;
    private byte[] uuid;
    private byte[] userUuid;
    private byte[] objectUuid;
    private int status;
    private int flag;
    private String title;
    private String contentType;
    private String type;
    private byte[] password;
    private long expireTimestamp;

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

    public byte[] getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(byte[] userUuid) {
        this.userUuid = userUuid;
    }

    public byte[] getObjectUuid() {
        return objectUuid;
    }

    public void setObjectUuid(byte[] objectUuid) {
        this.objectUuid = objectUuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public long getExpireTimestamp() {
        return expireTimestamp;
    }

    public void setExpireTimestamp(long expireTimestamp) {
        this.expireTimestamp = expireTimestamp;
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
        return "FileDO{" +
                "id=" + id +
                ", uuid=" + Arrays.toString(uuid) +
                ", userUuid=" + Arrays.toString(userUuid) +
                ", objectUuid=" + Arrays.toString(objectUuid) +
                ", status=" + status +
                ", flag=" + flag +
                ", title='" + title + '\'' +
                ", contentType='" + contentType + '\'' +
                ", type='" + type + '\'' +
                ", password=" + Arrays.toString(password) +
                ", expireTimestamp=" + expireTimestamp +
                ", createTime='" + createTime + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                '}';
    }
}
