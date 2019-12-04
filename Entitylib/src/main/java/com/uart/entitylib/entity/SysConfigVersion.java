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
    private Integer version;//当前版本号
    private Integer previousVersion;//前版本号
    private String remark;//备注
    private Long createTime;//创建时间
    private String creator;//创建者
    @Generated(hash = 38779960)
    public SysConfigVersion(Long id, Integer version, Integer previousVersion,
            String remark, Long createTime, String creator) {
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
    public Integer getVersion() {
        return this.version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
    public Integer getPreviousVersion() {
        return this.previousVersion;
    }
    public void setPreviousVersion(Integer previousVersion) {
        this.previousVersion = previousVersion;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public String getCreator() {
        return this.creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }


}
