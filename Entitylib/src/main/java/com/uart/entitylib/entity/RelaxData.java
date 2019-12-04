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
    private String typeName;//数据类型（精神紧张0，轻度放松1，中度放松2，深度放松3，持续放松4）
    private Integer type;//数据类型（精神紧张0，轻度放松1，中度放松2，深度放松3，持续放松4）
    private Integer relax;//放松度，0-30：精神紧张，31-60：轻度放松，61-80：中度放松，81-100：深度放松，当深度放松持续10s时间：持续放松状态
    private Long startTime;//开始时间
    private Long endTime;//结束时间
    @Generated(hash = 751874197)
    public RelaxData(Long id, Long usageRecordId, String typeName, Integer type, Integer relax,
            Long startTime, Long endTime) {
        this.id = id;
        this.usageRecordId = usageRecordId;
        this.typeName = typeName;
        this.type = type;
        this.relax = relax;
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
    public Integer getType() {
        return this.type;
    }
    public void setType(Integer type) {
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
    public Integer getRelax() {
        return this.relax;
    }
    public void setRelax(Integer relax) {
        this.relax = relax;
    }
    public String getTypeName() {
        return this.typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


}
