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
    private Integer sex;//性别（女0，男1，未知2）
    private Integer height;//身高（厘米）
    private Integer weight;//体重（kg)
    private String birthday;//生日（1982-02-05）
    private Integer sign;//是否完善信息(0--信息全,1--需要补足信息)
    private Boolean activated;//是否激活
    private Long lastlogin;//最后登录时间
    @Generated(hash = 1350998147)
    public UserInfo(Long id, String userCode, String userName, String mobile,
            String password, String token, Integer sex, Integer height,
            Integer weight, String birthday, Integer sign, Boolean activated,
            Long lastlogin) {
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
        this.sign = sign;
        this.activated = activated;
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
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Integer getSex() {
        return this.sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public Integer getHeight() {
        return this.height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }
    public Integer getWeight() {
        return this.weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public Integer getSign() {
        return this.sign;
    }
    public void setSign(Integer sign) {
        this.sign = sign;
    }
    public Boolean getActivated() {
        return this.activated;
    }
    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
    public Long getLastlogin() {
        return this.lastlogin;
    }
    public void setLastlogin(Long lastlogin) {
        this.lastlogin = lastlogin;
    }


}
