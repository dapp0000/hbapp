package com.uart.hbapp.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.uart.hbapp.R;

public class RestColorView extends LinearLayout {

    private int rest_mode;
    private Context context;
    private LinearLayout layout_root;
    private TextView txt_Title,txt_title_status1,txt_title_status2,txt_title_status3,txt_title_status4,txt_title_status5;
    private LinearLayout ll_mode0_1,ll_mode0_2,ll_mode0_3,ll_mode0_4,ll_mode0_5,ll_mode1_2,ll_mode1_3,ll_mode1_4,ll_mode1_5;
    private TextView txt_mode0_1,txt_mode0_2,txt_mode1_1;
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
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RestColorView);
        String title = mTypedArray.getString(R.styleable.RestColorView_title_text);
        if (!TextUtils.isEmpty(title)) {
            txt_Title.setText(title);
            txt_Title.setTextColor(mTypedArray.getColor(R.styleable.RestColorView_title_text_clolor, Color.WHITE));
        }

        rest_mode = mTypedArray.getInteger(R.styleable.RestColorView_rest_mode,0);
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
        }

        mTypedArray.recycle();
    }





}
