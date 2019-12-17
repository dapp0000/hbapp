package com.uart.hbapp.history;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
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
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.bean.RecordBean;
import com.uart.hbapp.bean.RecordWeekBean;
import com.uart.hbapp.utils.BItmapUtil;
import com.uart.hbapp.utils.CommandUtils;
import com.uart.hbapp.utils.ShareUtil;
import com.uart.hbapp.utils.URLUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeekHistoryActivity extends AppCompatActivity {

    @BindView(R.id.btn_week)
    TextView btnWeek;
    @BindView(R.id.btn_share_friend)
    ImageView btnShareFriend;
    @BindView(R.id.btn_share_send)
    ImageView btnShareSend;
    @BindView(R.id.layout_week)
    LinearLayout layoutWeek;
    @BindView(R.id.txt_week_start)
    TextView txtWeekStart;
    @BindView(R.id.txt_week_end)
    TextView txtWeekEnd;
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

    private IWXAPI wxApi;
    private static final String APP_ID = "wx94f346ad6a97fff7";
    private int weekNum;
    private RecordWeekBean weekBean;
    private List<String> xValuesLabel;
    private  Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_history);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        wxApi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        wxApi.registerApp(APP_ID);

        findViewById(R.id.btn_share_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wechatShare(SendMessageToWX.Req.WXSceneTimeline);
            }
        });
        findViewById(R.id.btn_share_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wechatShare(SendMessageToWX.Req.WXSceneSession);
            }
        });

        //周报初始化
        Calendar calendar = Calendar.getInstance();
        RecordBean bean = (RecordBean) getIntent().getSerializableExtra("record");
        if (bean != null) {
            int num = bean.weekNum;

            calendar.setTimeInMillis(System.currentTimeMillis());
            int todayweekNum = calendar.get(Calendar.WEEK_OF_YEAR);
            int todaymonthNum = calendar.get(Calendar.MONTH);
            weekNum = todayweekNum - num;

            calendar.setTimeInMillis(bean.startTime);

            long oneDayLong = 24*60*60*1000;
            int week= calendar.get(Calendar.DAY_OF_WEEK)-1;
            //int firstDayOfWeek = calendar.getFirstDayOfWeek();
            long first = bean.startTime - (week)*oneDayLong;
            long last = bean.startTime + (7-week-1)*oneDayLong;

            calendar.setTimeInMillis(first);
            String firstDate = getDateString(calendar);
            calendar.setTimeInMillis(last);
            String lastDate =  getDateString(calendar);
            txtWeekStart.setText(firstDate);
            txtWeekEnd.setText(lastDate);

            xValuesLabel = new ArrayList<>();
            for (int i=0;i<7;i++){
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
        for (int i=0;i<7;i++){
            yValues.add(0f);
        }
        return yValues;
    }

    private String getDateString(Calendar c){
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        return year+"年"+month+"月"+day+"日";
        return c.getTime().toLocaleString().split(" ")[0];
    }

    private void initData() {
        String token = HbApplication.getInstance().loginUser.getToken();
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("num", weekNum);
        JSONObject jsonObject = new JSONObject(params);
        String base_url = URLUtil.url + URLUtil.weekSleepLogQuery;
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
                                initLastWeekData();
                                JSONObject dataJSON = jsonObject.getJSONObject("data");
                                if (dataJSON != null) {
                                    weekBean = dataJSON.toJavaObject(RecordWeekBean.class);
                                    if(weekBean!=null){
                                        int count = weekBean.list.size();
                                        txtThisCount.setText(count+"次");
                                        txtThisVigor.setText(weekBean.thisVigor+"%");
                                        txtThisUse.setText(weekBean.thisTime+"分钟");
                                        txtThisSleep.setText(weekBean.thisSleepTime+"分钟");
                                        txtThisRelax.setText(weekBean.thisRelax+"分钟");

                                        List<Float> useList = getWeekValues(weekBean.list,0);
                                        CommandUtils.drawHistoryLog(lineChartUse,useList,xValuesLabel);

                                        List<Float> sleepList = getWeekValues(weekBean.list,1);
                                        CommandUtils.drawHistoryLog(lineChartSleep,sleepList,xValuesLabel);

                                        List<Float> relaxList = getWeekValues(weekBean.list,2);
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


    private List<Float> getWeekValues(List<RecordBean> datas,int type){
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
                calendar=Calendar.getInstance();

            calendar.setTimeInMillis(item.startTime);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            float current = list.get(week)+yValue;
            list.set(week,current);
        }

        return list;
    }

    private String getSuggestion(RecordWeekBean lastWeek){
      //  您本周休息次数【times_per_week】，每次平均使用时间【service_time】精力回升效果【energy_recovery】；
        //  平均有效睡眠时间为【effective_sleep_time】，其占比【proportion_of_effective_sleep_time】%，每次使用的平均放松时间为【relax_time】。
        //  【suggestion1】【suggestion2】【suggestion3】【suggestion4】【suggestion5】
//你本周休息次数比上周有所减少，使用效果有小
//幅度提升；有效睡眠时间占比较少，建议你适当调
//整休息环境，有利于提升休息质量！

        int times_per_week = weekBean.list.size();
        long service_time = weekBean.thisTime;
        int energy_recovery = weekBean.thisVigor;
        String energy_recovery_str = "";
        if (energy_recovery>25)
            energy_recovery_str = "显著";
        else
            energy_recovery_str = "良好";


        long effective_sleep_time = weekBean.thisSleepTime;
        float effective = effective_sleep_time/service_time;
        int proportion_of_effective_sleep_time =Math.round(effective*100);
        String proportion_of_effective_sleep_time_str ="";
        if(proportion_of_effective_sleep_time<10)
            proportion_of_effective_sleep_time_str = "较少";
        else if(proportion_of_effective_sleep_time<20)
            proportion_of_effective_sleep_time_str = "正常";
        else
            proportion_of_effective_sleep_time_str = "理想";
        long relax_time = weekBean.thisRelax;


        String suggestion1 = String.format("    您本周休息次数%s，每次平均使用时间%s,精力回升效果%s；平均有效睡眠时间为%s，其占比%s，每次使用的平均放松时间为%s。"
        ,times_per_week+"次"
        ,service_time+"分钟"
        ,energy_recovery_str
        ,effective_sleep_time+"分钟"
        ,proportion_of_effective_sleep_time_str
        ,relax_time+"分钟");


        String tip1 = weekBean.list.size()>=lastWeek.list.size()?"增多":"减少";

        String tip2 = "";
        int offsetVigor =Math.abs(weekBean.thisVigor - lastWeek.thisVigor);
        if(offsetVigor>60)
            tip2 = "大幅度";
        else if(offsetVigor>30)
            tip2= "中幅度";
        else
            tip2 = "小幅度";

        String tip3 = weekBean.thisVigor >= lastWeek.thisVigor?"提升":"下降";


        String tip4 = "";
        if(energy_recovery<15){
            tip4 = "有效睡眠时间占比较少，建议你适当调整休息环境，有利于提升休息质量！";
        }
        else {
            tip4 = "";
        }


        String suggestion2 = String.format("    你本周休息次数比上周有所%s，使用效果有%s%s；%s"
        ,tip1,tip2,tip3,tip4);

        return suggestion1 + "\n" + suggestion2+ "\n";
    }

    private void initLastWeekData() {
        String token = HbApplication.getInstance().loginUser.getToken();
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("num", weekNum + 1);
        JSONObject jsonObject = new JSONObject(params);
        String base_url = URLUtil.url + URLUtil.weekSleepLogQuery;
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
                                    RecordWeekBean lastWeekBean = dataJSON.toJavaObject(RecordWeekBean.class);
                                    if(lastWeekBean!=null){
                                        int count = lastWeekBean.list.size();
                                        txtLastCount.setText(count+"次");
                                        txtLastVigor.setText(lastWeekBean.thisVigor+"%");
                                        txtLastUse.setText(lastWeekBean.thisTime+"分钟");
                                        txtLastSleep.setText(lastWeekBean.thisSleepTime+"分钟");
                                        txtLastRelax.setText(lastWeekBean.thisRelax+"分钟");

                                        setProgress(pbThisCount,pbLastCount,weekBean.list.size(),count);
                                        setProgress(pbThisVigor,pbLastVigor,weekBean.thisVigor,lastWeekBean.thisRelax);
                                        setProgress(pbThisUse,pbLastUse,weekBean.thisTime,lastWeekBean.thisTime);
                                        setProgress(pbThisSleep,pbLastSleep,weekBean.thisSleepTime,lastWeekBean.thisSleepTime);
                                        setProgress(pbThisRelax,pbLastRelax,weekBean.thisRelax,lastWeekBean.thisRelax);

                                        txtSuggest.setText(getSuggestion(lastWeekBean));

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


    private void setProgress(ProgressBar thisPb,ProgressBar lastPb,long thisValue,long lastValue){
        if(thisValue>=lastValue){
            thisPb.setMax(100);
            thisPb.setProgress(100);

            float rate = lastValue/(float)thisValue;
            int last = Math.round(rate*100);
            lastPb.setMax(100);
            lastPb.setProgress(last);
        }
        else{
            lastPb.setMax(100);
            lastPb.setProgress(100);

            float rate = thisValue/(float)lastValue;
            int value = Math.round(rate*100);
            thisPb.setMax(100);
            thisPb.setProgress(value);
        }

    }


    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag) {
        Bitmap bmp = ShareUtil.screenShotWholeScreen(WeekHistoryActivity.this);
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

//设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 1, 1, true);
        bmp.recycle();
        msg.thumbData = BItmapUtil.Bimap2Bytes(thumbBmp);

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = flag;
//        req.userOpenId = APP_ID;
//调用api接口，发送数据到微信
        wxApi.sendReq(req);

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


}
