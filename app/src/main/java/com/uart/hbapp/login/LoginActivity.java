package com.uart.hbapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.uart.hbapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username,phoneNum,checkCode;
    Button login,userPwdLogin,sendCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        username=findViewById(R.id.username);
        phoneNum=findViewById(R.id.phoneNum);
        checkCode=findViewById(R.id.checkCode);
        login=findViewById(R.id.login);
        userPwdLogin=findViewById(R.id.userPwdLogin);
        sendCode=findViewById(R.id.sendCode);

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
