package com.uart.hbapp.utils;

public class URLUtil {
    public static String url="http://47.93.32.7:8080";//服务器地址
    public static String login = "/sleep/userinfo/login";//登录
    public static String regist = "/sleep/userinfo/registered";//注册
    public static String getUser = "/sleep/userinfo/getUser";//获取user
    public static String getToken = "/sleep/userinfo/getToken";//获取token
    public static String addSleepInfo = "/sleep/SleepLog/addSleepInfo";//上传记录
    public static String fileData = "/sleep/SleepLog/fileData";//上传原始文件

    public static String historyQuery = "/sleep/SleepLog/query";//历史记录查询
    public static String daySleepLogQuery = "/sleep/SleepLog/queryDay";//历史日报查询
    public static String weekSleepLogQuery = "/sleep/SleepLog/weekSleepLog";//历史周报查询
    public static String monthSleepLogQuery = "/sleep/SleepLog/monthSleepLog";//历史月报查询

    public static String musicListQuery = "/sleep/music/query";//获取音乐列表
    public static String userinfoUpdate = "/sleep/userinfo/update";//更新用户信息
    public static String randomHome = "/sleep/pageHome/randomHome";//更新用户信息
    public static String musicFileDownload = "/sleep/music/fileDownLoad";//音乐文件下载


}
