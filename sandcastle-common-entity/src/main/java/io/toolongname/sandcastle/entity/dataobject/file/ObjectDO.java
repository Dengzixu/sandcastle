package io.toolongname.sandcastle.entity.dataobject.file;

import java.util.Arrays;

public class ObjectDO {
    private long id;
    private byte[] uuid;
    private int status;
    private byte[] sha512;
    private long size;
    private String path;
    private byte[] encryptKey;

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

    public byte[] getSha512() {
        return sha512;
    }

    public void setSha512(byte[] sha512) {
        this.sha512 = sha512;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(byte[] encryptKey) {
        this.encryptKey = encryptKey;
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
        return "ObjectDO{" +
                "id=" + id +
                ", uuid=" + Arrays.toString(uuid) +
                ", status=" + status +
                ", sha512=" + Arrays.toString(sha512) +
                ", size=" + size +
                ", path='" + path + '\'' +
                ", encryptKey=" + Arrays.toString(encryptKey) +
                ", createTime='" + createTime + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                '}';
    }
}
