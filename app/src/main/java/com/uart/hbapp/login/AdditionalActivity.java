package com.uart.hbapp.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;
import com.uart.hbapp.utils.view.scaleruler.ScaleRulerView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class AdditionalActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener {

    ScaleRulerView mHeightWheelView, mWeightWheelView;
    TextView mHeightValue, mWeightValue;

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
        getSupportActionBar().hide();
        mHeightWheelView = findViewById(R.id.scaleWheelView_height);
        mWeightWheelView = findViewById(R.id.scaleWheelView_weight);
        mHeightValue = findViewById(R.id.tv_user_height_value);
        mWeightValue = findViewById(R.id.tv_user_weight_value);

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
        this.year=year;
        this.monthOfYear=month;
        this.dayOfMonth=day;
//        Toast.makeText(AdditionalActivity.this,"您选择的日期是："+year+"年"+(month+1)+"月"+day+"日!",Toast
//                .LENGTH_SHORT).show();
    }

    private void init() {
        mHeightValue.setText((int) mHeight + "");
        mWeightValue.setText(mWeight + "");


        mHeightWheelView.initViewParam(mHeight, mMaxHeight, mMinHeight);
        mHeightWheelView.setValueChangeListener(new ScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mHeightValue.setText((int) value + "");
                mHeight = value;
            }
        });

        mWeightWheelView.initViewParam(mWeight, mMaxWeight, mMinWeight);
        mWeightWheelView.setValueChangeListener(new ScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mWeightValue.setText(value + "");
                mWeight = value;
            }
        });

    }

    public void confirmInfo(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "选择身高： " + mHeight + " 体重： " + mWeight+" 生日是："+year+"-"+monthOfYear+"-"+dayOfMonth, Toast.LENGTH_LONG).show();
    }
}
