package com.uart.hbapp.login;

import android.app.Activity;
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

public class RegistActivity extends Activity {

    @BindView(R.id.welcome)
    TextView welcome;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.regist)
    TextView regist;
    @BindView(R.id.regist_cancel)
    TextView registCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        try {
            //getSupportActionBar().hide();
            getActionBar().hide();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void regist(String uname, String pwd) {
        DialogUtils.showProgressDialog(this, "请稍等...");
        HashMap<String, Object> params = new HashMap<>();
        params.put("userName", uname);
        params.put("nickName", "");
        params.put("passWord", pwd);
        params.put("gender", 1);
        params.put("age", 1);
        params.put("height", 1);
        params.put("weight", 1);

        JSONObject jsonObject = new JSONObject(params);
        String base_url =URLUtil.url + URLUtil.regist;
        OkGo.<String>post(base_url)
                .tag(this)
                .cacheKey("cachePostKey")
                .headers("Content-Type","application/json")
                .upJson(jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e("eee", "AddFaceT:" + response.body());
                            JSONObject jsonObject = new JSONObject(response.body());
                            int error = jsonObject.getInt("error");
                            if (error == 0) {
                                ToastUtils.showShort("注册成功");
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

    @OnClick({R.id.regist, R.id.regist_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.regist:
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    ToastUtils.showShort("用户名和密码不能为空");
                } else {
                    regist(username.getText().toString(), password.getText().toString());
                }
                break;
            case R.id.regist_cancel:
                startActivity(new Intent(RegistActivity.this, LoginUserPwdActivity.class));
                finish();
                break;
        }
    }
}
