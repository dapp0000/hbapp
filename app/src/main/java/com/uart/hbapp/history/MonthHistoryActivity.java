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
import com.uart.hbapp.utils.URLUtil;

import java.util.Calendar;
import java.util.HashMap;

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
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            String lastDate = getDateString(calendar);

            txtMonthStart.setText(firstDate);
            txtMonthEnd.setText(lastDate);

            initData();
        }

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
