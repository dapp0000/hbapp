package com.uart.hbapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    TextView sendCode;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.userPwdLogin)
    TextView userPwdLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        HbApplication.getApp().addActivity(this);
    }

    @OnClick({R.id.sendCode, R.id.login, R.id.userPwdLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sendCode:
                //发送验证码接口
                timer.start();
                break;
            case R.id.login:
                String userName = username.getText().toString();
                if(StringUtils.isEmpty(userName)){
                    ToastUtils.showShort("用户名不能为空");
                    return;
                }

                HbApplication.loginUser = userName;

                //手机号登录接口
                Intent intent = new Intent(LoginActivity.this, AdditionalActivity.class);
                intent.putExtra("AdditionalActivity", "login");
                startActivity(intent);
                finish();
                break;
            case R.id.userPwdLogin:
                startActivity(new Intent(LoginActivity.this, LoginUserPwdActivity.class));
                //finish();
                break;
        }
    }

    private CountDownTimer timer = new CountDownTimer(60000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //不允许再次点击
            sendCode.setClickable(false);
            sendCode.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            sendCode.setClickable(true);
            sendCode.setText("重新获取");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
