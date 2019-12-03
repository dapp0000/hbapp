package com.uart.hbapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;



public class WelcomeActivity extends Activity {

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

        CountHandler.postDelayed(CountRunnable, 1000);
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
                if (HbApplication.getInstance().loginUser.getActivated()) {
                    if (!TextUtils.isEmpty(HbApplication.getInstance().loginUser.getToken())) {
                        if(HbApplication.getInstance().loginUser.getSign()==0){
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
}
