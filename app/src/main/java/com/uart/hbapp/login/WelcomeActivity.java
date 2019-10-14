package com.uart.hbapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.blankj.utilcode.util.SPUtils;
import com.uart.hbapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
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
                if (SPUtils.getInstance().getBoolean("first")){
                    startActivity(new Intent(WelcomeActivity.this, LoginUserPwdActivity.class));
                    finish();
                }else {
                    SPUtils.getInstance().put("first",true);
                    startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
                    finish();
                }

            }
            CountHandler.postDelayed(this, 1000);
        }
    };
}
