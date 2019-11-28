package com.uart.entitylib.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 使用记录
 */
@Entity
public class UsageRecord {
    @Id(autoincrement = true)
    private Long id;
    private Long userId;//用户id
    private String userCode;//用户编号
    private String userName;//用户名称
    private String deviceSN;//设备sn
    private String deviceMac;//设备Mac
    private String deviceName;//设备名称
    private Long musicId;//选择音乐
    private Long speakId;//选择指导语
    private Long restDurationId;//选择时长
    private Long startTime;//开始时间
    private Long endTime;//结束时间
    private int vigor;//精力回升
    private String originalDataFile;//原始数据文件名
    private int synchState;//同步状态（生成原始txt文件0，压缩zip文件1，上传成功2，清理文件完成3）
    @Generated(hash = 1450755214)
    public UsageRecord(Long id, Long userId, String userCode, String userName,
            String deviceSN, String deviceMac, String deviceName, Long musicId,
            Long speakId, Long restDurationId, Long startTime, Long endTime,
            int vigor, String originalDataFile, int synchState) {
        this.id = id;
        this.userId = userId;
        this.userCode = userCode;
        this.userName = userName;
        this.deviceSN = deviceSN;
        this.deviceMac = deviceMac;
        this.deviceName = deviceName;
        this.musicId = musicId;
        this.speakId = speakId;
        this.restDurationId = restDurationId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.vigor = vigor;
        this.originalDataFile = originalDataFile;
        this.synchState = synchState;
    }
    @Generated(hash = 954559608)
    public UsageRecord() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUserCode() {
        return this.userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getDeviceSN() {
        return this.deviceSN;
    }
    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }
    public String getDeviceMac() {
        return this.deviceMac;
    }
    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }
    public String getDeviceName() {
        return this.deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
    public int getVigor() {
        return this.vigor;
    }
    public void setVigor(int vigor) {
        this.vigor = vigor;
    }
    public String getOriginalDataFile() {
        return this.originalDataFile;
    }
    public void setOriginalDataFile(String originalDataFile) {
        this.originalDataFile = originalDataFile;
    }
    public int getSynchState() {
        return this.synchState;
    }
    public void setSynchState(int synchState) {
        this.synchState = synchState;
    }
    public Long getMusicId() {
        return this.musicId;
    }
    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }
    public Long getSpeakId() {
        return this.speakId;
    }
    public void setSpeakId(Long speakId) {
        this.speakId = speakId;
    }
    public Long getRestDurationId() {
        return this.restDurationId;
    }
    public void setRestDurationId(Long restDurationId) {
        this.restDurationId = restDurationId;
    }

}
