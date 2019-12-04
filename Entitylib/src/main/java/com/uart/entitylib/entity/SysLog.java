package com.uart.entitylib.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 系统日志
 */
@Entity
public class SysLog {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String logType;//日志类型
    private Long logTime;//日期
    private String content;//日志内容
    @Generated(hash = 234037210)
    public SysLog(Long id, @NotNull String logType, Long logTime, String content) {
        this.id = id;
        this.logType = logType;
        this.logTime = logTime;
        this.content = content;
    }
    @Generated(hash = 1793142996)
    public SysLog() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLogType() {
        return this.logType;
    }
    public void setLogType(String logType) {
        this.logType = logType;
    }
    public Long getLogTime() {
        return this.logTime;
    }
    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }



}
