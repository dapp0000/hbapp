package com.uart.hbapp.history;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uart.hbapp.R;
import com.uart.hbapp.bean.RecordBean;
import com.uart.hbapp.utils.BItmapUtil;
import com.uart.hbapp.utils.ShareUtil;
import com.uart.hbapp.utils.view.RestColorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayHistoryActivity extends AppCompatActivity {

    @BindView(R.id.txt_start_date)
    TextView txtStartDate;
    @BindView(R.id.txt_start_time)
    TextView txtStartTime;
    @BindView(R.id.txt_sleepTime)
    TextView txtSleepTime;
    @BindView(R.id.btn_share_friend)
    ImageView btnShareFriend;
    @BindView(R.id.btn_share_send)
    ImageView btnShareSend;
    @BindView(R.id.layout_week)
    LinearLayout layoutWeek;
    @BindView(R.id.txt_vigor)
    TextView txtVigor;
    @BindView(R.id.rest_0)
    RestColorView rest0;
    @BindView(R.id.rest_1)
    RestColorView rest1;
    private IWXAPI wxApi;
    private static final String APP_ID = "wx94f346ad6a97fff7";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_history);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        wxApi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        wxApi.registerApp(APP_ID);
        RecordBean bean = (RecordBean) getIntent().getSerializableExtra("record");
        if (bean != null) {
            txtStartDate.setText(bean.startDateString);
            txtStartTime.setText(bean.startTimeString);
            txtSleepTime.setText(String.format("休息%d分钟", bean.sleepTime));
            txtVigor.setText(bean.vigor + "%");
            rest0.setNumber(bean.sober_1,bean.sober_2,bean.sober_3,bean.sober_4,bean.sober_5);
            rest1.setNumber(bean.relax_1,bean.relax_2,bean.relax_3,bean.relax_4,bean.relax_5);
        }
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
    }
    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag) {
        Bitmap bmp = ShareUtil.screenShotWholeScreen(DayHistoryActivity.this);
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
