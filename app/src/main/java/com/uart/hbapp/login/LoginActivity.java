package com.uart.hbapp.login;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();


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
            }
        });
        userPwdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
