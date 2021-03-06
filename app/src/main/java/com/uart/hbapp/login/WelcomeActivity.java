package com.uart.hbapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.entitylib.dao.UserInfoDao;
import com.uart.entitylib.entity.UserInfo;
import com.uart.hbapp.AppConstants;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;
import com.uart.hbapp.utils.DialogUtils;
import com.uart.hbapp.utils.URLUtil;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends Activity {
    TextView welcome;
    ImageView img_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        try {
            //getSupportActionBar().hide();
            getActionBar().hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        welcome=findViewById(R.id.welcome);
        img_welcome=findViewById(R.id.img_welcome);
        getRandomHome();
        if (new Date().getTime()/1000<1582948800){
            CountHandler.postDelayed(CountRunnable, 1000);
        }

    }

    private void getRandomHome() {
        String base_url = URLUtil.url + URLUtil.randomHome;
        OkGo.<String>get(base_url)
                .tag(this)
//                .cacheKey("cachePostKey")
//                .cacheMode(CacheMode.DEFAULT)
//                .headers("token", SpUtils.get(DynameicFaceApplication.myContext, "token", "") + "")
//                .headers("Content-Type","application/json")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e("eee", "AddFaceT:" + response.body());
                            JSONObject jsonObject = new JSONObject(response.body());
                            int error = jsonObject.getInt("error");
                            if (error == 0) {
                                JSONObject data=jsonObject.getJSONObject("data");
                                JSONObject pageHome=data.getJSONObject("pageHome");
                                if (pageHome!=null){
                                    String homePageLang=pageHome.getString("homePageLang");
                                    String imageUrl=pageHome.getString("imageUrl");
                                    welcome.setText(homePageLang);
                                    Glide.with(WelcomeActivity.this).load(imageUrl).into(img_welcome);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            DialogUtils.closeProgressDialog();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        DialogUtils.closeProgressDialog();
                        ToastUtils.showShort("服务器异常");
                    }
                });
    }

    private Message message = new Message();
    private int recLen = 2;
    private Handler CountHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    CountHandler.removeCallbacks(CountRunnable);
                    recLen = 0;
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);

        }
    };
    private Runnable CountRunnable = new Runnable() {
        @Override
        public void run() {
            recLen--;
            if (recLen == 0) {
                message.what = 1;
                CountHandler.sendMessage(message);
//                if (SPUtils.getInstance().getBoolean(AppConstants.ACTIVATED_KEY)) {
//                    if (SPUtils.getInstance().getString("token") != null && !SPUtils.getInstance().getString("token").isEmpty()) {
//                        startActivity(new Intent(WelcomeActivity.this, ScanActivity.class));
//                        finish();
//                    } else {
//                        startActivity(new Intent(WelcomeActivity.this, LoginUserPwdActivity.class));
//                        finish();
//                    }
//                } else {
//                    SPUtils.getInstance().put(AppConstants.ACTIVATED_KEY, true);
//                    startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
//                    finish();
//                }

                UserInfo userInfo = HbApplication.getInstance().loginUser;
                boolean firstUse=SPUtils.getInstance().getBoolean("firstUse",false);
                if (firstUse==true) {
                    if (!TextUtils.isEmpty(SPUtils.getInstance().getString("username"))) {
                        autoLogin(SPUtils.getInstance().getString("username"),SPUtils.getInstance().getString("userpwd"));
                        if(userInfo.getSign()!=0){
                            startActivity(new Intent(WelcomeActivity.this, ScanActivity.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(WelcomeActivity.this, AdditionalActivity.class));
                            finish();
                        }

                    } else {
                        startActivity(new Intent(WelcomeActivity.this, LoginUserPwdActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
                    finish();
                }
            }

            CountHandler.postDelayed(this, 1000);
        }
    };



    //自动登录
    private void autoLogin(String uname, String pwd) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userName", uname);
        params.put("passWord", pwd);

        JSONObject jsonObject = new JSONObject(params);
        String base_url =URLUtil.url + URLUtil.login;
        OkGo.<String>post(base_url)
                .tag(this)
                .cacheKey("cachePostKey")
                .headers("Content-Type","application/json")
                .upJson(jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e("eee", "AddFaceT:" + response.body());
                            JSONObject jsonObject = new JSONObject(response.body());
                            int error = jsonObject.getInt("error");
                            if (error == 0) {
                                //查找本地用户
                                UserInfo user = HbApplication.getDaoInstance().getUserInfoDao().queryBuilder().where(UserInfoDao.Properties.UserName.eq(uname)).unique();
                                if(user == null){
                                    user = new UserInfo();
                                    user.setActivated(false);//未激活
                                    user.setToken("");//未登录
                                    user.setSign(1);//未完善信息
                                }

                                HbApplication.getInstance().loginUser = user;
                                JSONObject data = jsonObject.getJSONObject("data");
                                if(data!=null){
                                    String token = data.getString("token");
                                    int sign = data.getInt("sign");
                                    user.setSign(sign);
                                    user.setUserName(uname);
                                    user.setPassword(pwd);
                                    user.setLastlogin(System.currentTimeMillis());
                                    user.setToken(token);
                                    user.setActivated(true);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showShort("服务器异常");
                    }
                });

    }



}
