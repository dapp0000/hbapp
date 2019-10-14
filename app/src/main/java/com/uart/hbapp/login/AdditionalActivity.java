package com.uart.hbapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;
import com.uart.hbapp.utils.view.scaleruler.ScaleRulerView;
import com.uart.hbapp.utils.view.scaleruler.SlantedTextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdditionalActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener {
    @BindView(R.id.welcome_1)
    TextView welcome1;
    @BindView(R.id.welcome_2)
    TextView welcome2;
    @BindView(R.id.layout_welcome)
    LinearLayout layoutWelcome;
    @BindView(R.id.radioMan)
    RadioButton radioMan;
    @BindView(R.id.radioWoman)
    RadioButton radioWoman;
    @BindView(R.id.scaleWheelView_height)
    ScaleRulerView scaleWheelViewHeight;
    @BindView(R.id.slant_one)
    SlantedTextView slantOne;
    @BindView(R.id.tv_user_height_value)
    TextView tvUserHeightValue;
    @BindView(R.id.scaleWheelView_weight)
    ScaleRulerView scaleWheelViewWeight;
    @BindView(R.id.slant_two)
    SlantedTextView slantTwo;
    @BindView(R.id.tv_user_weight_value)
    TextView tvUserWeightValue;
    @BindView(R.id.datepicker)
    DatePicker datepicker;
    @BindView(R.id.layout_content)
    LinearLayout layoutContent;

    private float mHeight = 170;
    private float mMaxHeight = 220;
    private float mMinHeight = 100;


    private float mWeight = 60.0f;
    private float mMaxWeight = 200;
    private float mMinWeight = 25;

    int year, monthOfYear, dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        init();

        DatePicker datePicker = (DatePicker) findViewById(R.id.datepicker);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, monthOfYear, dayOfMonth, AdditionalActivity.this);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.monthOfYear = month;
        this.dayOfMonth = day;
//        Toast.makeText(AdditionalActivity.this,"您选择的日期是："+year+"年"+(month+1)+"月"+day+"日!",Toast
//                .LENGTH_SHORT).show();
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

    }

    public void confirmInfo(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "选择身高： " + mHeight + " 体重： " + mWeight + " 生日是：" + year + "-" + monthOfYear + "-" + dayOfMonth, Toast.LENGTH_LONG).show();
    }
}
