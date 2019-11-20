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

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;
import com.uart.hbapp.utils.DatePickerUtil;
import com.uart.hbapp.utils.view.scaleruler.ScaleRulerView;


import java.lang.reflect.Field;
import java.util.Calendar;

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
    private String tag;
    private String sex="男";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional);
        ButterKnife.bind(this);
        getActionBar().hide();
        Intent intent = getIntent();
        tag = intent.getStringExtra("AdditionalActivity");
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
        if (tag.equals("changeMessage")) {

        } else {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
            finish();
        }
        Toast.makeText(this, "性别："+sex+" 身高： " + mHeight + " 体重： " + mWeight + " 生日是：" + year + "-" + monthOfYear  , Toast.LENGTH_LONG).show();
    }

    private long firstPressedTime;

    @Override
    public void onBackPressed() {

        if (tag.equals("changeMessage")) {
            finish();
        } else {
            if (System.currentTimeMillis() - firstPressedTime < 2000) {
                super.onBackPressed();
                finish();
                HbApplication.getApp().exit();
            } else {
                ToastUtils.showShort("再按一次退出");
                firstPressedTime = System.currentTimeMillis();
            }
        }

    }
}
