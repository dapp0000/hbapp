package com.uart.entitylib.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 系统配置版本表
 */
@Entity
public class SysConfigVersion {
    @Id(autoincrement = true)
    private Long id;
    private int version;//当前版本号
    private int previousVersion;//前版本号
    private String remark;//备注
    private long createTime;//创建时间
    private String creator;//创建者
    @Generated(hash = 872664058)
    public SysConfigVersion(Long id, int version, int previousVersion,
            String remark, long createTime, String creator) {
        this.id = id;
        this.version = version;
        this.previousVersion = previousVersion;
        this.remark = remark;
        this.createTime = createTime;
        this.creator = creator;
    }
    @Generated(hash = 1303394644)
    public SysConfigVersion() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getVersion() {
        return this.version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public int getPreviousVersion() {
        return this.previousVersion;
    }
    public void setPreviousVersion(int previousVersion) {
        this.previousVersion = previousVersion;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public String getCreator() {
        return this.creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
}
