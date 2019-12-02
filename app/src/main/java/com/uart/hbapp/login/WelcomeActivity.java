package com.uart.hbapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.blankj.utilcode.util.SPUtils;
import com.uart.hbapp.AppConstants;
import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;

import androidx.appcompat.app.AppCompatActivity;

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
