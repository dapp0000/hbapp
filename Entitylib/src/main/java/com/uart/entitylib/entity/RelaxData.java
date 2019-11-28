package com.uart.entitylib.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 放松数据
 */
@Entity
public class RelaxData {
    @Id(autoincrement = true)
    private Long id;
    private Long usageRecordId;//使用记录id
    private int type;//数据类型（精神紧张0，轻度放松1，中度放松2，深度放松3，持续放松4）
    private Long startTime;//开始时间
    private Long endTime;//结束时间
    @Generated(hash = 1490733607)
    public RelaxData(Long id, Long usageRecordId, int type, Long startTime,
            Long endTime) {
        this.id = id;
        this.usageRecordId = usageRecordId;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    @Generated(hash = 1794330521)
    public RelaxData() {
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
