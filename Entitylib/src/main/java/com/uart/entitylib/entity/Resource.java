package com.uart.entitylib.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 资源
 */
@Entity
public class Resource {
    @Id(autoincrement = true)
    private Long id;
    private String name;//资源名称
    private String extension;//文件扩展名
    private Integer type;//资源类型（音乐1、指导语2）
    private Integer status;//资源状态（未下载0，已下载1）
    private Integer duration;//资源时长（秒）
    private String durationStr;//资源时长（5'55''）
    private String speaker;//播音员
    private String urlPath;//资源地址
    private String localFilePath;//本地文件路径

    @Transient
    public boolean isChecked;//选中状态

    @Generated(hash = 1815484929)
    public Resource(Long id, String name, String extension, Integer type,
            Integer status, Integer duration, String durationStr, String speaker,
            String urlPath, String localFilePath) {
        this.id = id;
        this.name = name;
        this.extension = extension;
        this.type = type;
        this.status = status;
        this.duration = duration;
        this.durationStr = durationStr;
        this.speaker = speaker;
        this.urlPath = urlPath;
        this.localFilePath = localFilePath;
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

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSpeaker() {
        return this.speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getUrlPath() {
        return this.urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getLocalFilePath() {
        return this.localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getDurationStr() {
        return this.durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }




}
