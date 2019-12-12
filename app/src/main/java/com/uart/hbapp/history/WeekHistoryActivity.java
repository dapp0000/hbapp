package com.uart.hbapp.history;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uart.hbapp.R;
import com.uart.hbapp.utils.BItmapUtil;
import com.uart.hbapp.utils.ShareUtil;

import butterknife.ButterKnife;

public class WeekHistoryActivity extends AppCompatActivity {

    private IWXAPI wxApi;
    private static final String APP_ID = "wx94f346ad6a97fff7";

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
