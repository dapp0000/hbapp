package com.uart.hbapp.utils;

public class CommandUtils {
    private final static String SPLITCHAR = "-";
    private final static String BT_DISABLE ="AA-AA-01-50-00-AE";//关机后，需2s再开机
    private final static String BT_ENABLE ="AA-AA-01-50-01-AE";//4秒后开启广播配对
    private final static String BT_DISPAIR ="AA-AA-01-50-02-AE";//5秒后清除配对信息

    public static byte[] bt_disable_cmd(){
        return ByteUtils.hexToByteArray(BT_DISABLE.split(SPLITCHAR));
    }

    public static byte[] bt_enable_cmd(){
        return ByteUtils.hexToByteArray(BT_ENABLE.split(SPLITCHAR));
    }

    public static byte[] bt_dispair_cmd(){
        return ByteUtils.hexToByteArray(BT_DISPAIR.split(SPLITCHAR));
    }

    public static int getSexVaue(String sex){
        int value = 2;
        switch (sex) {
            case "女":
                value = 0;
                break;
            case "男":
                value = 1;
                break;
        }
        return value;
    }
}
