package com.uart.hbapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.uart.hbapp.utils.view.scaleruler.DrawUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

public class HbApplication extends Application {
    public static final String man_service_uuid = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    public static final String device_info_service_uuid = "0000180a-0000-1000-8000-00805f9b34fb";
    public static final String device_info_serial_number="00002a25-0000-1000-8000-00805f9b34fb";

    public List<Activity> mList = new LinkedList<Activity>();
    public static Context mContext;
    public static HbApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initOkGo();
    }
    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //全局的读取超时时间
        builder.readTimeout(15000, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(15000, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(5000, TimeUnit.MILLISECONDS);
        OkGo.getInstance().init(this).setRetryCount(1).setOkHttpClient(builder.build());
    }


    public static HbApplication getApp() {
        if (mInstance != null && mInstance instanceof HbApplication) {
            return (HbApplication) mInstance;
        } else {
            mInstance = new HbApplication();
            mInstance.onCreate();
            return (HbApplication) mInstance;
        }
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
