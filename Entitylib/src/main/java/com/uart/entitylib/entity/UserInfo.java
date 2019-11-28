package com.uart.entitylib.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户信息
 */
@Entity
public class UserInfo {
    @Id(autoincrement = true)
    private Long id;
    private String userCode;//用户编号
    private String userName;//用户名称
    private String mobile;//手机号
    private String password;//密码
    private String token;//访问令牌
    private int sex;//性别（女0，男1，未知2）
    private int height;//身高（厘米）
    private int weight;//体重（kg)
    private String birthday;//生日（1982-02-05）
    private Long lastlogin;//最后登录时间
    @Generated(hash = 2104237385)
    public UserInfo(Long id, String userCode, String userName, String mobile,
            String password, String token, int sex, int height, int weight,
            String birthday, Long lastlogin) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
        this.mobile = mobile;
        this.password = password;
        this.token = token;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.birthday = birthday;
        this.lastlogin = lastlogin;
    }
    @Generated(hash = 1279772520)
    public UserInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWeight() {
        return this.weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public Long getLastlogin() {
        return this.lastlogin;
    }
    public void setLastlogin(Long lastlogin) {
        this.lastlogin = lastlogin;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
