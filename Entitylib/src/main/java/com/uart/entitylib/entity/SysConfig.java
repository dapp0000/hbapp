package com.uart.entitylib.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 系统配置表
 */
@Entity
public class SysConfig {
    @Id(autoincrement = true)
    private Long id;
    private String configKey;//配置key
    private String configValue;//配置value
    private int version;//当前版本号
    private long createTime;//创建时间
    private String creator;//创建者
    private long editTime;//编辑时间
    private String editor;//编辑者
    @Generated(hash = 327036165)
    public SysConfig(Long id, String configKey, String configValue, int version,
            long createTime, String creator, long editTime, String editor) {
        this.id = id;
        this.configKey = configKey;
        this.configValue = configValue;
        this.version = version;
        this.createTime = createTime;
        this.creator = creator;
        this.editTime = editTime;
        this.editor = editor;
    }
    @Generated(hash = 1454359576)
    public SysConfig() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getConfigKey() {
        return this.configKey;
    }
    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }
    public String getConfigValue() {
        return this.configValue;
    }
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
    public int getVersion() {
        return this.version;
    }
    public void setVersion(int version) {
        this.version = version;
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
    public long getEditTime() {
        return this.editTime;
    }
    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }
    public String getEditor() {
        return this.editor;
    }
    public void setEditor(String editor) {
        this.editor = editor;
    }

}
