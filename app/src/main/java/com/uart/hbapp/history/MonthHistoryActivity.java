package com.uart.hbapp.history;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.bean.RecordBean;
import com.uart.hbapp.bean.RecordMonthBean;
import com.uart.hbapp.bean.RecordWeekBean;
import com.uart.hbapp.utils.CommandUtils;
import com.uart.hbapp.utils.URLUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthHistoryActivity extends AppCompatActivity {


    @BindView(R.id.btn_month)
    TextView btnMonth;
    @BindView(R.id.btn_share_friend)
    ImageView btnShareFriend;
    @BindView(R.id.btn_share_send)
    ImageView btnShareSend;
    @BindView(R.id.layout_week)
    LinearLayout layoutWeek;
    @BindView(R.id.txt_month_start)
    TextView txtMonthStart;
    @BindView(R.id.txt_month_end)
    TextView txtMonthEnd;
    @BindView(R.id.pb_this_count)
    ProgressBar pbThisCount;
    @BindView(R.id.txt_this_count)
    TextView txtThisCount;
    @BindView(R.id.pb_last_count)
    ProgressBar pbLastCount;
    @BindView(R.id.txt_last_count)
    TextView txtLastCount;
    @BindView(R.id.pb_this_vigor)
    ProgressBar pbThisVigor;
    @BindView(R.id.txt_this_vigor)
    TextView txtThisVigor;
    @BindView(R.id.pb_last_vigor)
    ProgressBar pbLastVigor;
    @BindView(R.id.txt_last_vigor)
    TextView txtLastVigor;
    @BindView(R.id.pb_this_use)
    ProgressBar pbThisUse;
    @BindView(R.id.txt_this_use)
    TextView txtThisUse;
    @BindView(R.id.pb_last_use)
    ProgressBar pbLastUse;
    @BindView(R.id.txt_last_use)
    TextView txtLastUse;
    @BindView(R.id.pb_this_sleep)
    ProgressBar pbThisSleep;
    @BindView(R.id.txt_this_sleep)
    TextView txtThisSleep;
    @BindView(R.id.pb_last_sleep)
    ProgressBar pbLastSleep;
    @BindView(R.id.txt_last_sleep)
    TextView txtLastSleep;
    @BindView(R.id.pb_this_relax)
    ProgressBar pbThisRelax;
    @BindView(R.id.txt_this_relax)
    TextView txtThisRelax;
    @BindView(R.id.pb_last_relax)
    ProgressBar pbLastRelax;
    @BindView(R.id.txt_last_relax)
    TextView txtLastRelax;
    @BindView(R.id.line_chart_use)
    LineChart lineChartUse;
    @BindView(R.id.line_chart_sleep)
    LineChart lineChartSleep;
    @BindView(R.id.line_chart_relax)
    LineChart lineChartRelax;
    @BindView(R.id.txt_suggest)
    TextView txtSuggest;


    private int monthNum;
    private RecordMonthBean monthBean;
    private List<String> xValuesLabel;
    private  Calendar calendar;
    private int dayCount = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_history);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        //月报初始化
        RecordBean bean = (RecordBean) getIntent().getSerializableExtra("record");
        if (bean != null) {
            int num = bean.monthNum;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int todayweekNum = calendar.get(Calendar.WEEK_OF_YEAR);
            int todaymonthNum = calendar.get(Calendar.MONTH);
            monthNum = todaymonthNum - num;

            calendar.setTimeInMillis(bean.startTime);
            calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
            String firstDate = getDateString(calendar);
            long first = calendar.getTimeInMillis();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            String lastDate = getDateString(calendar);
            dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            txtMonthStart.setText(firstDate);
            txtMonthEnd.setText(lastDate);

            long oneDayLong = 24*60*60*1000;
            xValuesLabel = new ArrayList<>();
            for (int i=0;i<dayCount;i++){
                long x = first + i*oneDayLong;
                calendar.setTimeInMillis(x);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                xValuesLabel.add(month+"/"+day);
            }


            initData();
        }

    }

    private List<Float> getYValues(){
        List<Float> yValues = new ArrayList<>();
        for (int i=0;i<dayCount;i++){
            yValues.add(0f);
        }
        return yValues;
    }


    private void initData() {
        String token = HbApplication.getInstance().loginUser.getToken();
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("num", monthNum);
        JSONObject jsonObject = new JSONObject(params);
        String base_url = URLUtil.url + URLUtil.monthSleepLogQuery;
        OkGo.<String>post(base_url)
                .tag(this)
                .cacheKey("cachePostKey")
                .headers("Content-Type", "application/json")
                .upJson(jsonObject.toString())
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body());
                            int error = jsonObject.getInteger("error");
                            if (error == 0) {
                                initLastMonthData();
                                JSONObject dataJSON = jsonObject.getJSONObject("data");
                                if (dataJSON != null) {
                                    monthBean = dataJSON.toJavaObject(RecordMonthBean.class);
                                    if (monthBean != null) {
                                        int count = monthBean.list.size();
                                        txtThisCount.setText(count+"次");
                                        txtThisVigor.setText(monthBean.thisVigor+"%");
                                        txtThisUse.setText(monthBean.thisTime+"分钟");
                                        txtThisSleep.setText(monthBean.thisSleepTime+"分钟");
                                        txtThisRelax.setText(monthBean.thisRelax+"分钟");


                                        List<Float> useList = getMonthValues(monthBean.list,0);
                                        CommandUtils.drawHistoryLog(lineChartUse,useList,xValuesLabel);

                                        List<Float> sleepList = getMonthValues(monthBean.list,1);
                                        CommandUtils.drawHistoryLog(lineChartSleep,sleepList,xValuesLabel);

                                        List<Float> relaxList = getMonthValues(monthBean.list,2);
                                        CommandUtils.drawHistoryLog(lineChartRelax,relaxList,xValuesLabel);



                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtils.e(response.body());
                    }
                });
    }


    private String getSuggestion(RecordMonthBean lastMonth){
        //  您本周休息次数【times_per_week】，每次平均使用时间【service_time】精力回升效果【energy_recovery】；
        //  平均有效睡眠时间为【effective_sleep_time】，其占比【proportion_of_effective_sleep_time】%，每次使用的平均放松时间为【relax_time】。
        //  【suggestion1】【suggestion2】【suggestion3】【suggestion4】【suggestion5】
//你本周休息次数比上周有所减少，使用效果有小
//幅度提升；有效睡眠时间占比较少，建议你适当调
//整休息环境，有利于提升休息质量！

        int times_per_week = monthBean.list.size();
        long service_time = monthBean.thisTime;
        int energy_recovery = monthBean.thisVigor;
        String energy_recovery_str = "";
        if (energy_recovery>25)
            energy_recovery_str = "显著";
        else
            energy_recovery_str = "良好";


        long effective_sleep_time = monthBean.thisSleepTime;
        float effective = effective_sleep_time/service_time;
        int proportion_of_effective_sleep_time =Math.round(effective*100);
        String proportion_of_effective_sleep_time_str ="";
        if(proportion_of_effective_sleep_time<10)
            proportion_of_effective_sleep_time_str = "较少";
        else if(proportion_of_effective_sleep_time<20)
            proportion_of_effective_sleep_time_str = "正常";
        else
            proportion_of_effective_sleep_time_str = "理想";
        long relax_time = monthBean.thisRelax;


        String suggestion1 = String.format("    您本月休息次数%s，每次平均使用时间%s,精力回升效果%s；平均有效睡眠时间为%s，其占比%s，每次使用的平均放松时间为%s。"
                ,times_per_week+"次"
                ,service_time+"分钟"
                ,energy_recovery_str
                ,effective_sleep_time+"分钟"
                ,proportion_of_effective_sleep_time_str
                ,relax_time+"分钟");


        String tip1 = monthBean.list.size()>=lastMonth.list.size()?"增多":"减少";

        String tip2 = "";
        int offsetVigor =Math.abs(monthBean.thisVigor - lastMonth.thisVigor);
        if(offsetVigor>60)
            tip2 = "大幅度";
        else if(offsetVigor>30)
            tip2= "中幅度";
        else
            tip2 = "小幅度";

        String tip3 = monthBean.thisVigor >= lastMonth.thisVigor?"提升":"下降";


        String tip4 = "";
        if(energy_recovery<15){
            tip4 = "有效睡眠时间占比较少，建议你适当调整休息环境，有利于提升休息质量！";
        }
        else {
            tip4 = "";
        }


        String suggestion2 = String.format("    你本月休息次数比上周有所%s，使用效果有%s%s；%s"
                ,tip1,tip2,tip3,tip4);

        return suggestion1 + "\n" + suggestion2+ "\n";
    }


    private List<Float> getMonthValues(List<RecordBean> datas,int type){
        List<Float> list = getYValues();

        int length = datas.size();
        //时间顺序
        for (int i=length-1;i>=0;i--){
            RecordBean item = datas.get(i);
            float yValue = 0;
            switch (type){
                case 0://使用时间
                    yValue = item.relax_1+item.relax_2+item.relax_3+item.relax_4+item.relax_5;
                    break;
                case 1://有效睡眠
                    yValue = item.sober_3+item.sober_4+item.sober_5;
                    break;
                case 2://放松时长
                    yValue = item.relax_2+item.relax_3+item.relax_4+item.relax_5;
                    break;
            }


            if(calendar==null)
                calendar = Calendar.getInstance();

            calendar.setTimeInMillis(item.startTime);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            float current = list.get(day-1)+yValue;
            list.set(day-1,current);
        }

        return list;
    }



    private void initLastMonthData() {
        String token = HbApplication.getInstance().loginUser.getToken();
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("num", monthNum + 1);
        JSONObject jsonObject = new JSONObject(params);
        String base_url = URLUtil.url + URLUtil.monthSleepLogQuery;
        OkGo.<String>post(base_url)
                .tag(this)
                .cacheKey("cachePostKey")
                .headers("Content-Type", "application/json")
                .upJson(jsonObject.toString())
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body());
                            int error = jsonObject.getInteger("error");
                            if (error == 0) {
                                JSONObject dataJSON = jsonObject.getJSONObject("data");
                                if (dataJSON != null) {
                                    RecordMonthBean lastBean = dataJSON.toJavaObject(RecordMonthBean.class);
                                    if (lastBean != null) {
                                        int count = lastBean.list.size();
                                        txtLastCount.setText(count + "次");
                                        txtLastVigor.setText(lastBean.thisVigor + "%");
                                        txtLastUse.setText(lastBean.thisTime + "分钟");
                                        txtLastSleep.setText(lastBean.thisSleepTime + "分钟");
                                        txtLastRelax.setText(lastBean.thisRelax + "分钟");

                                        setProgress(pbThisCount, pbLastCount, monthBean.list.size(), count);
                                        setProgress(pbThisVigor, pbLastVigor, monthBean.thisVigor, lastBean.thisRelax);
                                        setProgress(pbThisUse, pbLastUse, monthBean.thisTime, lastBean.thisTime);
                                        setProgress(pbThisSleep, pbLastSleep, monthBean.thisSleepTime, lastBean.thisSleepTime);
                                        setProgress(pbThisRelax, pbLastRelax, monthBean.thisRelax, lastBean.thisRelax);

                                        txtSuggest.setText(getSuggestion(lastBean));
                                    }


                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtils.e(response.body());
                    }
                });
    }


    private void setProgress(ProgressBar thisPb, ProgressBar lastPb, long thisValue, long lastValue) {
        if (thisValue >= lastValue) {
            thisPb.setMax(100);
            thisPb.setProgress(100);

            float rate = lastValue / (float) thisValue;
            int last = Math.round(rate * 100);
            lastPb.setMax(100);
            lastPb.setProgress(last);
        } else {
            lastPb.setMax(100);
            lastPb.setProgress(100);

            float rate = thisValue / (float) lastValue;
            int value = Math.round(rate * 100);
            thisPb.setMax(100);
            thisPb.setProgress(value);
        }

    }


    private String getDateString(Calendar c) {
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        return year+"年"+month+"月"+day+"日";
        return c.getTime().toLocaleString().split(" ")[0];
    }
}
