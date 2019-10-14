package com.uart.hbapp.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uart.hbapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.welcome)
    TextView welcome;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.phoneNum)
    EditText phoneNum;
    @BindView(R.id.checkCode)
    EditText checkCode;
    @BindView(R.id.sendCode)
    Button sendCode;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.userPwdLogin)
    Button userPwdLogin;

    MyCountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();


        timer= new MyCountDownTimer(5000, 1000);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //手机号登录接口
                startActivity(new Intent(LoginActivity.this, AdditionalActivity.class));
            }
        });
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送验证码接口
                timer.start();
            }
        });
        userPwdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //倒计时效果
    private class MyCountDownTimer extends CountDownTimer {

        //millisInFuture：总时间  countDownInterval：每隔多少时间刷新一次
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long lm) {
            //不允许再次点击
            sendCode.setClickable(false);
            sendCode.setText(lm / 1000 + "秒");
        }

        //计时结束
        @Override
        public void onFinish() {
            sendCode.setClickable(true);
            sendCode.setText("重新获取");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
