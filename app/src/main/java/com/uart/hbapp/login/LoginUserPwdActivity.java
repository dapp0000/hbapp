package com.uart.hbapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.utils.DialogUtils;
import com.uart.hbapp.utils.URLUtil;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginUserPwdActivity extends AppCompatActivity {
    @BindView(R.id.welcome)
    TextView welcome;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.phoneLogin)
    Button phoneLogin;
    @BindView(R.id.regist)
    Button regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginuserpwd);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        HbApplication.getApp().addActivity(this);
    }


    private void login(String uname, String pwd) {
        DialogUtils.showProgressDialog(this, "请稍等...");
        HashMap<String, Object> params = new HashMap<>();
        params.put("userName", uname);
        params.put("passWord", pwd);

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
                                ToastUtils.showShort(jsonObject.getString("message"));
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
                        ToastUtils.showShort("服务器异常");
                    }
                });

    }


    @OnClick({R.id.login, R.id.phoneLogin, R.id.regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    ToastUtils.showShort("用户名和密码不能为空");
                } else {
                    login(username.getText().toString(), password.getText().toString());
                }
                break;
            case R.id.phoneLogin:
                startActivity(new Intent(LoginUserPwdActivity.this, LoginActivity.class));
                break;
            case R.id.regist:
                startActivity(new Intent(LoginUserPwdActivity.this, RegistActivity.class));
                break;
        }
    }
}
