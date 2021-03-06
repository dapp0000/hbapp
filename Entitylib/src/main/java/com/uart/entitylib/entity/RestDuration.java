package com.uart.entitylib.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 休息时段
 */
@Entity
public class RestDuration {
    @Id(autoincrement = true)
    private Long id;
    private String name;//时段名称
    private Integer minute;//时段时长（分钟）
    @Generated(hash = 225238474)
    public RestDuration(Long id, String name, Integer minute) {
        this.id = id;
        this.name = name;
        this.minute = minute;
    }
    @Generated(hash = 296076510)
    public RestDuration() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getMinute() {
        return this.minute;
    }
    public void setMinute(Integer minute) {
        this.minute = minute;
    }


}
