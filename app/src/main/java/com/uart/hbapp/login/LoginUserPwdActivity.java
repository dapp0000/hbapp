package com.uart.hbapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.hbapp.R;
import com.uart.hbapp.utils.DialogUtils;
import com.uart.hbapp.utils.ToastUtils;
import com.uart.hbapp.utils.URLUtil;

import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class LoginUserPwdActivity extends AppCompatActivity {

    EditText username, password;
    Button login, phoneLogin, regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginuserpwd);
        getSupportActionBar().hide();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        regist = findViewById(R.id.regist);
        phoneLogin = findViewById(R.id.phoneLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                    ToastUtils.showShort(LoginUserPwdActivity.this, "用户名和密码不能为空");
                }else {
                    login(username.getText().toString(),password.getText().toString());
                }

            }
        });
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUserPwdActivity.this, RegistActivity.class));
            }
        });
        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUserPwdActivity.this, LoginActivity.class));
            }
        });
    }

    public void loginByPhone(View view) {

        Intent intent = new Intent(this, AdditionalActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(String uname,String pwd) {
        DialogUtils.showProgressDialog(this,"请稍等...");
        HashMap<String, Object> params = new HashMap<>();
        params.put("userName", uname);
        params.put("nickName", "");
        params.put("passWord", pwd);
        params.put("gender","");
        params.put("age", "");
        params.put("height", "");
        params.put("weight", "");

        JSONObject jsonObject = new JSONObject(params);
        String base_url = URLUtil.login;
        OkGo.<String>post(base_url)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
//                .headers("token", SpUtils.get(DynameicFaceApplication.myContext, "token", "") + "")
                .upJson(jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        try {
                            Log.e("eee", "AddFaceT:" + response.body());
                            JSONObject jsonObject = new JSONObject(response.body());
                            int error = jsonObject.getInt("error");
                            if (error == 0) {
                                Intent intent = new Intent(LoginUserPwdActivity.this, AdditionalActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.showShort(LoginUserPwdActivity.this, jsonObject.getString("message"));
                            }
                            DialogUtils.closeProgressDialog();

                        } catch (Exception e) {
                            e.printStackTrace();
                            DialogUtils.closeProgressDialog();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        DialogUtils.closeProgressDialog();
                        ToastUtils.showShort(LoginUserPwdActivity.this, "服务器异常");
                    }
                });

    }
}
