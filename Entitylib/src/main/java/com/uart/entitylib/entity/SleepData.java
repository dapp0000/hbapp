package com.uart.entitylib.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 睡眠数据
 */
@Entity
public class SleepData {
    @Id(autoincrement = true)
    private Long id;
    private Long usageRecordId;//使用记录id
    private int type;//数据类型（完全清醒0，导眠入睡1，浅度睡眠2，深度睡眠3，持续睡眠4）
    private Long startTime;//开始时间
    private Long endTime;//结束时间
    @Generated(hash = 378385672)
    public SleepData(Long id, Long usageRecordId, int type, Long startTime,
            Long endTime) {
        this.id = id;
        this.usageRecordId = usageRecordId;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    @Generated(hash = 1639116881)
    public SleepData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUsageRecordId() {
        return this.usageRecordId;
    }
    public void setUsageRecordId(Long usageRecordId) {
        this.usageRecordId = usageRecordId;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Long getStartTime() {
        return this.startTime;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
    public Long getEndTime() {
        return this.endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

}
