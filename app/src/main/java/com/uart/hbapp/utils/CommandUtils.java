package com.uart.hbapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.uart.hbapp.R;

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


    public static String getUIDeviceName(String name){
        if(TextUtils.isEmpty(name))
        {
            return "我的设备";
        }

       return "我的设备:"+name;
    }

    public static Bitmap getUIDeviceBattery(Context context, Integer battery) {
        if(battery==null)
        {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.p0);
        }

        //设置信号
        if (battery > 80) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.p4);
        } else if (battery > 50) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.p3);
        } else if (battery > 20) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.p2);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.p1);
        }
    }

    public static Bitmap getUIDeviceSignal(Context context,Integer signal) {
        if(signal==null)
        {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.s00);
        }

        //设置信号
        if (signal > 50) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.s05);
        } else if (signal > 30) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.s04);
        } else if (signal > 10) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.s03);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.s02);
        }
    }


}
