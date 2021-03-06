package com.uart.hbapp.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.entitylib.dao.UserInfoDao;
import com.uart.entitylib.entity.UserInfo;
import com.uart.hbapp.AppConstants;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;
import com.uart.hbapp.utils.DialogUtils;
import com.uart.hbapp.utils.URLUtil;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginUserPwdActivity extends Activity {
    @BindView(R.id.welcome)
    TextView welcome;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.phoneLogin)
    TextView phoneLogin;
    @BindView(R.id.regist)
    TextView regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginuserpwd);
        ButterKnife.bind(this);
        try {
            //getSupportActionBar().hide();
            getActionBar().hide();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        username.setText(HbApplication.getInstance().loginUser.getUserName());
        checkPermissions();
    }


    private void login(String uname, String pwd) {
        DialogUtils.showProgressDialog(this, "请稍等...");
        HashMap<String, Object> params = new HashMap<>();
        params.put("userName", uname);
        params.put("passWord", pwd);

        JSONObject jsonObject = new JSONObject(params);
        String base_url =URLUtil.url + URLUtil.login;
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
                                //查找本地用户
                                UserInfo user = HbApplication.getDaoInstance().getUserInfoDao().queryBuilder().where(UserInfoDao.Properties.UserName.eq(uname)).unique();
                                if(user == null){
                                    user = new UserInfo();
                                    user.setActivated(false);//未激活
                                    user.setToken("");//未登录
                                    user.setSign(1);//未完善信息
                                }

                                HbApplication.getInstance().loginUser = user;
                                JSONObject data = jsonObject.getJSONObject("data");
                                if(data!=null){
                                    String token = data.getString("token");
                                    int sign = data.getInt("sign");
                                    user.setSign(sign);
                                    user.setUserName(uname);
                                    SPUtils.getInstance().put("username",uname);
                                    SPUtils.getInstance().put("userpwd",pwd);
                                    user.setPassword(pwd);
                                    user.setLastlogin(System.currentTimeMillis());
                                    user.setToken(token);
                                    user.setActivated(true);
                                }

                                if (user.getSign()!=0) {
                                    startActivity(new Intent(LoginUserPwdActivity.this, ScanActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(LoginUserPwdActivity.this, AdditionalActivity.class));
                                    finish();
                                }

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
                finish();
                break;
            case R.id.regist:
                startActivity(new Intent(LoginUserPwdActivity.this, RegistActivity.class));
//                finish();
                break;
        }
    }

    @SuppressLint("WrongConstant")
    private void checkPermissions() {
        String[] requestPermissions = new String[]{
                PermissionConstants.LOCATION,
                PermissionConstants.STORAGE
        };

        if(!PermissionUtils.isGranted(requestPermissions)){
            PermissionUtils.permission(requestPermissions).rationale(new PermissionUtils.OnRationaleListener() {
                @Override
                public void rationale(ShouldRequest shouldRequest) {
                    ToastUtils.showShort("拒绝权限可能无法使用app");
                }
            }).callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    //ToastUtils.showShort("权限申请成功");
                }
                @Override
                public void onDenied() {
                    ToastUtils.showShort("权限申请失败，请前往系统设置页面手动设置");
                }

            }).request();
        }

    }
}
