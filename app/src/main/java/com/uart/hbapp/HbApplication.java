package com.uart.hbapp;

import android.app.Application;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.uart.entitylib.DbManage;
import com.uart.entitylib.dao.DaoSession;
import com.uart.entitylib.dao.UsageRecordDao;
import com.uart.entitylib.dao.UserInfoDao;
import com.uart.entitylib.entity.Resource;
import com.uart.entitylib.entity.RestDuration;
import com.uart.entitylib.entity.UsageRecord;
import com.uart.entitylib.entity.UserInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import okhttp3.OkHttpClient;

public class HbApplication extends Application {
    private static HbApplication mInstance;
    public static HbApplication getInstance() {
        return mInstance;
    }
    private static DaoSession daoSession;
    public static DaoSession getDaoInstance() {
        return daoSession;
    }

    /**
     * 当前登录用户
     */
    public UserInfo loginUser;
    /**
     * 当前设备使用记录
     */
    public UsageRecord usageRecord;

    public Resource selectMusic;
    public Resource selectSpeak;
    public RestDuration selectDuration;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        daoSession = DbManage.setupDatabase(this,"hbapp");
        initOkGo();

        List<UserInfo> userInfos = daoSession.getUserInfoDao().queryBuilder().orderDesc(UserInfoDao.Properties.Lastlogin).limit(1).list();
        if(userInfos.size()>0)
            loginUser = userInfos.get(0);
        else
        {
            loginUser = new UserInfo();
            loginUser.setActivated(false);//未激活
            loginUser.setToken("");//未登录
            loginUser.setSign(1);//未完善信息
        }

        List<UsageRecord> usageRecords = daoSession.getUsageRecordDao().queryBuilder().orderDesc(UsageRecordDao.Properties.Id).limit(1).list();
        if(usageRecords.size()>0)
            usageRecord = usageRecords.get(0);
        else
            usageRecord = new UsageRecord();
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


}
