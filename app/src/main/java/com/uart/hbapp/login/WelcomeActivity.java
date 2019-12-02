package com.uart.hbapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.entitylib.entity.UserInfo;
import com.uart.hbapp.AppConstants;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;
import com.uart.hbapp.utils.DialogUtils;
import com.uart.hbapp.utils.URLUtil;

import org.json.JSONObject;

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
        CountHandler.postDelayed(CountRunnable, 1000);
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
                if (SPUtils.getInstance().getBoolean(AppConstants.ACTIVATED_KEY)) {
                    if (SPUtils.getInstance().getString("token") != null && !SPUtils.getInstance().getString("token").isEmpty()) {
                        startActivity(new Intent(WelcomeActivity.this, ScanActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(WelcomeActivity.this, LoginUserPwdActivity.class));
                        finish();
                    }
                } else {
                    SPUtils.getInstance().put(AppConstants.ACTIVATED_KEY, true);
                    startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
                    finish();
                }
            }

            CountHandler.postDelayed(this, 1000);
        }
    };
}
