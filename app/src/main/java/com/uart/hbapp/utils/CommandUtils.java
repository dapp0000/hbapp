package com.uart.hbapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.uart.hbapp.R;
import com.uart.hbapp.bean.RecordBean;
import com.uart.hbapp.utils.view.LineChart.LineChartManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandUtils {
    private final static String SPLITCHAR = "-";
    private final static String BT_DISABLE ="AA-AA-01-50-00-AE";//关机后，需2s再开机
    private final static String BT_ENABLE ="AA-AA-01-50-01-AE";//4秒后开启广播配对
    private final static String BT_DISPAIR ="AA-AA-01-50-02-AE";//5秒后清除配对信息

    public static byte[] bt_disable_cmd(){
        return ByteUtils.hexToByteArray(BT_DISABLE.split(SPLITCHAR));
    }

    public static byte[] bt_enable_cmd(){
        return ByteUtils.hexToByteArray(BT_ENABLE.split(SPLITCHAR));
    }

    public static byte[] bt_dispair_cmd(){
        return ByteUtils.hexToByteArray(BT_DISPAIR.split(SPLITCHAR));
    }

    public static int getSexVaue(String sex){
        int value = 2;
        switch (sex) {
            case "女":
                value = 0;
                break;
            case "男":
                value = 1;
                break;
        }
        return value;
    }


    public static String getUIDeviceName(String name){
        if(TextUtils.isEmpty(name))
        {
            return "我的设备";
        }

       return "我的设备:"+name;
    }

    public static Bitmap getUIDeviceBattery(Context context, Integer battery) {
        if(battery==null)
        {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.p0);
        }

        //设置信号
        if (battery > 80) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.p4);
        } else if (battery > 50) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.p3);
        } else if (battery > 20) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.p2);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.p1);
        }
    }

    public static Bitmap getUIDeviceSignal(Context context,Integer signal) {
        if(signal==null)
        {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.s00);
        }

        //设置信号
        if (signal > 50) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.s05);
        } else if (signal > 30) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.s04);
        } else if (signal > 10) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.s03);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.s02);
        }
    }


    public static void drawHistoryLog(LineChart chart, List<Float> yValues,final List<String> xValuesLabel){
        LineChartManager lineChartManager = new LineChartManager(chart);
        XAxis xAxis = chart.getXAxis();
        YAxis yAxisLeft = chart.getAxisLeft();
        YAxis yAxisRight = chart.getAxisRight();
        xAxis.setTextColor(Color.WHITE);
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisRight.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            SimpleDateFormat mFormat = new SimpleDateFormat("M/d");
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)value;
                if(xValuesLabel.size()>7){
                    if(index%10==0 || index==xValuesLabel.size()-1)
                        return xValuesLabel.get(index);
                    else
                        return "";
                }else {
                    return xValuesLabel.get(index);
                }


                //String dateStr = mFormat.format(new Date((long) value*1000));
                //return dateStr;
            }
        });

        float max = 10;
        List<Float> xValues = new ArrayList<>();
        int index = 0;
        for (Float y : yValues){
            xValues.add((float)index);
            index++;
            if(y>max)
                max = y+100;
        }

        lineChartManager.showLineChart(xValues,yValues,"",Color.GREEN);
        lineChartManager.setYAxis(max,0,3);

        LineData lineData = chart.getLineData();
        lineData.setValueTextColor(Color.GREEN);
        //折线图例 标签 设置
        Legend legend = chart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setForm(Legend.LegendForm.EMPTY);
        Description description = chart.getDescription();
        description.setTextColor(Color.WHITE);
        description.setText("");
        chart.setDrawBorders(false);
        chart.setDrawMarkers(false);

    }
}
