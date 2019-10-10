package com.uart.hbapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.uart.hbapp.MainActivity;
import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);
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
                startActivity(new Intent(WelcomeActivity.this, ScanActivity.class));
                finish();
            }
            CountHandler.postDelayed(this, 1000);
        }
    };
}
