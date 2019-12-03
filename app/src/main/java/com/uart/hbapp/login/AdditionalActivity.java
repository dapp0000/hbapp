package com.uart.hbapp.login;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.entitylib.entity.UserInfo;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;
import com.uart.hbapp.utils.CommandUtils;
import com.uart.hbapp.utils.DatePickerUtil;
import com.uart.hbapp.utils.DialogUtils;
import com.uart.hbapp.utils.TimeUtil;
import com.uart.hbapp.utils.URLUtil;
import com.uart.hbapp.utils.view.scaleruler.ScaleRulerView;


import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uart.hbapp.utils.DatePickerUtil.hideDay;

public class AdditionalActivity extends Activity implements DatePicker.OnDateChangedListener {
    @BindView(R.id.welcome_1)
    TextView welcome1;
    @BindView(R.id.welcome_2)
    TextView welcome2;
    @BindView(R.id.layout_welcome)
    LinearLayout layoutWelcome;

    @BindView(R.id.scaleWheelView_height)
    ScaleRulerView scaleWheelViewHeight;

    @BindView(R.id.tv_user_height_value)
    TextView tvUserHeightValue;
    @BindView(R.id.scaleWheelView_weight)
    ScaleRulerView scaleWheelViewWeight;

    @BindView(R.id.tv_user_weight_value)
    TextView tvUserWeightValue;
    @BindView(R.id.layout_content)
    LinearLayout layoutContent;

    @BindView(R.id.sexMan)
    ImageView sexMan;
    @BindView(R.id.sexWoman)
    ImageView sexWoman;

    private float mHeight = 170;
    private float mMaxHeight = 220;
    private float mMinHeight = 100;


    private float mWeight = 60.0f;
    private float mMaxWeight = 200;
    private float mMinWeight = 25;

    private int year, monthOfYear, dayOfMonth;
    private String sex="男";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional);
        ButterKnife.bind(this);
        try {
            //getSupportActionBar().hide();
            getActionBar().hide();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Intent intent = getIntent();
        init();

        DatePicker datePicker = (DatePicker) findViewById(R.id.datepicker);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, monthOfYear, dayOfMonth, AdditionalActivity.this);
        DatePickerUtil.setDatePickerDividerColor(this, datePicker);
        hideDay(datePicker);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.monthOfYear = month;
        this.dayOfMonth = day;

    }

    private void init() {
        tvUserHeightValue.setText((int) mHeight + "");
        tvUserWeightValue.setText(mWeight + "");


        scaleWheelViewHeight.initViewParam(mHeight, mMaxHeight, mMinHeight);
        scaleWheelViewHeight.setValueChangeListener(new ScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tvUserHeightValue.setText((int) value + "");
                mHeight = value;
            }
        });

        scaleWheelViewWeight.initViewParam(mWeight, mMaxWeight, mMinWeight);
        scaleWheelViewWeight.setValueChangeListener(new ScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tvUserWeightValue.setText(value + "");
                mWeight = value;
            }
        });

        sexMan.setEnabled(false);
        sexMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexMan.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.head_man_select));
                sexWoman.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.head_woman_normal));
                sexMan.setEnabled(false);
                sexWoman.setEnabled(true);
                sex="男";
            }
        });
        sexWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexMan.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.head_man_normal));
                sexWoman.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.head_woman_select));
                sexWoman.setEnabled(false);
                sexMan.setEnabled(true);
                sex="女";
            }
        });
    }

    public void confirmInfo(View view) {
        updateMessage();
        //Toast.makeText(this, "性别："+sex+" 身高： " + mHeight + " 体重： " + mWeight + " 生日是：" + year + "-" + monthOfYear  , Toast.LENGTH_LONG).show();
   }

    private void updateMessage() {
        DialogUtils.showProgressDialog(this, "请稍等...");
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", HbApplication.getInstance().loginUser.getToken());
        params.put("userName", HbApplication.getInstance().loginUser.getUserName());
        params.put("gender",CommandUtils.getSexVaue(sex) );
        try {
            params.put("age", TimeUtil.getAge(year+"-"+monthOfYear+"-"+dayOfMonth));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("height", mHeight);
        params.put("weight", mWeight);

        JSONObject jsonObject = new JSONObject(params);
        String base_url = URLUtil.url + URLUtil.userinfoUpdate;
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
                                UserInfo user = HbApplication.getInstance().loginUser;
                                user.setSex(CommandUtils.getSexVaue(sex));
                                user.setHeight((int)mHeight);
                                user.setWeight((int)mWeight);
                                user.setBirthday(year+"-"+monthOfYear+"-"+dayOfMonth);

                                if (user.getSign()==0) {
                                    ToastUtils.showShort("修改成功");
                                    finish();
                                } else {
                                    user.setSign(0);//已完善
                                    Intent intent = new Intent(AdditionalActivity.this, ScanActivity.class);
                                    startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记录登录用户信息
        HbApplication.getDaoInstance().getUserInfoDao().insertOrReplace(HbApplication.getInstance().loginUser);
    }



}
