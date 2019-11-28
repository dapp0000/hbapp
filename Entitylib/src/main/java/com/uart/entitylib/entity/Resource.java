package com.uart.entitylib.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 资源
 */
@Entity
public class Resource {
    @Id(autoincrement = true)
    private Long id;
    private String name;//资源名称
    private int type;//资源类型（音乐0、指导语1）
    private int duration;//资源时长（秒）
    private int status;//资源状态（未下载0，已下载1）
    @Generated(hash = 1318121985)
    public Resource(Long id, String name, int type, int duration, int status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.status = status;
    }
    @Generated(hash = 632359988)
    public Resource() {
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
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getDuration() {
        return this.duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }


}
