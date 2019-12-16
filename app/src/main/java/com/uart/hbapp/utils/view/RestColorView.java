package com.uart.hbapp.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.uart.hbapp.R;

public class RestColorView extends LinearLayout {

    private int rest_mode,number_1,number_2,number_3,number_4,number_5;
    private Context context;
    private LinearLayout layout_root;
    private TextView txt_Title,txt_title_status1,txt_title_status2,txt_title_status3,txt_title_status4,txt_title_status5;
    private TextView txt_title_status1_value,txt_title_status2_value,txt_title_status3_value,txt_title_status4_value,txt_title_status5_value;
    private LinearLayout ll_mode0_1,ll_mode0_2,ll_mode0_3,ll_mode0_4,ll_mode0_5,ll_mode1_2,ll_mode1_3,ll_mode1_4,ll_mode1_5;
    private TextView txt_mode0_1,txt_mode0_2,txt_mode1_1;
    private TextView myText1,myText2,myText3,myText4,myText5;
    private LinearLayout p1,p2,p3,p4,p5;
    public RestColorView(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public RestColorView(Context context,@Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
        initAttrs(context, attrs);
    }

    public RestColorView(Context context,@Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_rest_color, this, true);
        layout_root = findViewById(R.id.layout_root);
        txt_Title = findViewById(R.id.txt_Title);
        txt_title_status1 = findViewById(R.id.txt_title_status1);
        txt_title_status2 = findViewById(R.id.txt_title_status2);
        txt_title_status3 = findViewById(R.id.txt_title_status3);
        txt_title_status4 = findViewById(R.id.txt_title_status4);
        txt_title_status5 = findViewById(R.id.txt_title_status5);

        txt_title_status1_value = findViewById(R.id.txt_title_status1_value);
        txt_title_status2_value = findViewById(R.id.txt_title_status2_value);
        txt_title_status3_value = findViewById(R.id.txt_title_status3_value);
        txt_title_status4_value = findViewById(R.id.txt_title_status4_value);
        txt_title_status5_value = findViewById(R.id.txt_title_status5_value);

        ll_mode0_1 = findViewById(R.id.ll_mode0_1);
        ll_mode0_2 = findViewById(R.id.ll_mode0_2);
        ll_mode0_3 = findViewById(R.id.ll_mode0_3);
        ll_mode0_4 = findViewById(R.id.ll_mode0_4);
        ll_mode0_5 = findViewById(R.id.ll_mode0_5);
        ll_mode1_2 = findViewById(R.id.ll_mode1_2);
        ll_mode1_3 = findViewById(R.id.ll_mode1_3);
        ll_mode1_4 = findViewById(R.id.ll_mode1_4);
        ll_mode1_5 = findViewById(R.id.ll_mode1_5);

        txt_mode0_1 = findViewById(R.id.txt_mode0_1);
        txt_mode0_2 = findViewById(R.id.txt_mode0_2);
        txt_mode1_1 = findViewById(R.id.txt_mode1_1);

        myText1 = findViewById(R.id.myTextView1);
        myText2 = findViewById(R.id.myTextView2);
        myText3 = findViewById(R.id.myTextView3);
        myText4 = findViewById(R.id.myTextView4);
        myText5 = findViewById(R.id.myTextView5);

        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        p5 = findViewById(R.id.p5);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RestColorView);
        String title = mTypedArray.getString(R.styleable.RestColorView_title_text);
        if (!TextUtils.isEmpty(title)) {
            txt_Title.setText(title);
            txt_Title.setTextColor(mTypedArray.getColor(R.styleable.RestColorView_title_text_clolor, Color.WHITE));
        }

        rest_mode = mTypedArray.getInteger(R.styleable.RestColorView_rest_mode,0);
        number_1 = mTypedArray.getInteger(R.styleable.RestColorView_number_1,0);
        number_2 = mTypedArray.getInteger(R.styleable.RestColorView_number_2,0);
        number_3 = mTypedArray.getInteger(R.styleable.RestColorView_number_3,0);
        number_4 = mTypedArray.getInteger(R.styleable.RestColorView_number_4,0);
        number_5 = mTypedArray.getInteger(R.styleable.RestColorView_number_5,0);

        setNumber(number_1,number_2,number_3,number_4,number_5);

        mTypedArray.recycle();
    }


    public void setNumber(int n1,int n2,int n3,int n4,int n5){
        number_1=n1;
        number_2=n2;
        number_3=n3;
        number_4=n4;
        number_5=n5;

        if(rest_mode==0){
            ll_mode0_1.setVisibility(VISIBLE);
            ll_mode0_2.setVisibility(VISIBLE);
            ll_mode0_3.setVisibility(VISIBLE);
            ll_mode0_4.setVisibility(VISIBLE);
            ll_mode0_5.setVisibility(VISIBLE);
            ll_mode1_2.setVisibility(GONE);
            ll_mode1_3.setVisibility(GONE);
            ll_mode1_4.setVisibility(GONE);
            ll_mode1_5.setVisibility(GONE);
            txt_mode0_1.setVisibility(VISIBLE);
            txt_mode0_2.setVisibility(VISIBLE);
            txt_mode1_1.setVisibility(GONE);

            txt_title_status1.setText("完全清醒");
            txt_title_status2.setText("导眠入睡");
            txt_title_status3.setText("浅度睡眠");
            txt_title_status4.setText("深度睡眠");
            txt_title_status5.setText("持续睡眠");

            int sum12 = number_1+number_2;
            txt_mode0_1.setText(String.format("导眠和清醒：%d分钟",sum12));
            int sum345 = number_3+number_4+number_5;
            txt_mode0_2.setText(String.format("有效睡眠：%d分钟",sum345));
        }
        else{
            ll_mode0_1.setVisibility(GONE);
            ll_mode0_2.setVisibility(GONE);
            ll_mode0_3.setVisibility(GONE);
            ll_mode0_4.setVisibility(GONE);
            ll_mode0_5.setVisibility(GONE);
            ll_mode1_2.setVisibility(VISIBLE);
            ll_mode1_3.setVisibility(VISIBLE);
            ll_mode1_4.setVisibility(VISIBLE);
            ll_mode1_5.setVisibility(VISIBLE);
            txt_mode0_1.setVisibility(GONE);
            txt_mode0_2.setVisibility(GONE);
            txt_mode1_1.setVisibility(VISIBLE);

            txt_title_status1.setText("精神紧张");
            txt_title_status2.setText("轻度放松");
            txt_title_status3.setText("中度放松");
            txt_title_status4.setText("深度放松");
            txt_title_status5.setText("持续放松");

            int sum2345 = number_2+number_3+number_4+number_5;
            txt_mode1_1.setText(String.format("放松状态：%d分钟",sum2345));
        }

        txt_title_status1_value.setText(number_1+"分钟");
        txt_title_status2_value.setText(number_2+"分钟");
        txt_title_status3_value.setText(number_3+"分钟");
        txt_title_status4_value.setText(number_4+"分钟");
        txt_title_status5_value.setText(number_5+"分钟");

        myText1.setText(number_1+"");
        myText2.setText(number_2+"");
        myText3.setText(number_3+"");
        myText4.setText(number_4+"");
        myText5.setText(number_5+"");

        int sum = number_1+number_2+number_3+number_4+number_5;
        if(sum>0){
            p1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,getPercentWidth(sum,number_1)));
            p2.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,getPercentWidth(sum,number_2)));
            p3.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,getPercentWidth(sum,number_3)));
            p4.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,getPercentWidth(sum,number_4)));
            p5.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,getPercentWidth(sum,number_5)));
        }

    }


    private float getPercentWidth(int sum, int number){
        float percent = number/(float)sum;
        if(percent<0.03f)
            percent = 0.03f;

        return percent;
    }


}
