package com.uart.hbapp.utils.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class EcgView  extends View {
    //画笔
    protected Paint mPaint;

    protected Paint mPath;

    //背景颜色
    protected int mBackgroundColor =Color.parseColor("#071B34");
    //自身的大小
    protected int mWidth, mHeight;

    Context context;

    public EcgView(Context context) {
        super(context);
        this.context = context;
    }

    public EcgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public EcgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EcgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initBackground(canvas);
    }

    private void initBackground(Canvas canvas) {
        mPaint = new Paint();
        mPath = new Paint();

        //canvas.drawColor(mBackgroundColor);

        mPath.setColor(Color.parseColor("#91D4F5"));
        mPath.setStrokeWidth(3.0F);
        final float scale = context.getResources().getDisplayMetrics().density;

        if (data.size() > 0) {
            //    画搏动折线
            for (int k = 1; k < data.size(); k++) {
                if (data.get(k) == xx) {
                    float b = (k) * 5 * scale;
                    float x = 0;
                    for (int i = 1; i < xlist.size(); i++) {
                        HashMap<Integer, Integer> map1 = xlist.get(i - 1);
                        int x1 = map1.get(0);
                        int y1 = map1.get(1);
                        HashMap<Integer, Integer> map2 = xlist.get(i);
                        int x2 = map2.get(0);
                        int y2 = map2.get(1);
                        if (i == 1) {
                            x = x1 - b;
                        }
                        canvas.drawLine(x1 - x, y1, x2 - x, y2, mPath);
                    }
                } else {
                    canvas.drawLine((k - 1) * 3 * scale, data.get(k - 1) * scale, k * 3 * scale, data.get(k) * scale, mPath);
                }
            }
        }

        if (data.size() > 120) {
            data.remove(0);//移除左边图形  让图形移动起来
            data.remove(1);
            data.remove(2);
            data.remove(3);
            data.remove(4);
            data.remove(5);
            data.remove(6);
            data.remove(7);
        }
    }

    ArrayList<Integer> data = new ArrayList<>();

    public void setData(int i) {
        data.add(getMobileHeight(context) + normalized(i));
        postInvalidate();
    }

    int xx;

    public int getMobileHeight(Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        xx = (int) (mHeight / scale / 4);
        return xx;
    }

    ArrayList<HashMap<Integer, Integer>> xlist = new ArrayList<>();

    private int minValue = 0;
    public void setMinValue(int _minValue){
        minValue = _minValue;
    }

    private int maxValue = 1000;
    public void setMaxValue(int _maxValue){
        maxValue = _maxValue;
    }



    private int  normalized(int value){
        float rate = ((float)value - minValue) / (maxValue - minValue);
        int result = Math.abs(Math.round(rate*100));
        return result;
    }
}
