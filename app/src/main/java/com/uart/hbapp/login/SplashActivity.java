package com.uart.hbapp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.uart.hbapp.R;
import com.uart.hbapp.login.adapter.SplashAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {

    @BindView(R.id.vp_guide)
    ViewPager vpGuide;
    @BindView(R.id.ll_guide_points)
    LinearLayout llGuidePoints;
    @BindView(R.id.startBtn)
    Button startBtn;


    //向导界面的图片
    private int[] mPics = new int[]{R.mipmap.img1, R.mipmap.img2, R.mipmap.img3};

    private int currentItem;
    private SplashAdapter adapter;
    private List<ImageView> guids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        try {
            //getSupportActionBar().hide();
            getActionBar().hide();
        }catch (Exception ex){
            ex.printStackTrace();
        }


        initData();
        initEvent();
    }




    private void initData() {
        // viewpaper adapter适配器
        guids = new ArrayList<ImageView>();

        //创建viewpager的适配器
        for (int i = 0; i < mPics.length; i++) {
            ImageView iv_temp = new ImageView(getApplicationContext());
            iv_temp.setBackgroundResource(mPics[i]);

            //添加界面的数据
            guids.add(iv_temp);

            //灰色的点在LinearLayout中绘制：
            //获取点
            View v_point = new View(getApplicationContext());
            v_point.setBackgroundResource(R.drawable.point_smiple);//灰点背景色
            //设置灰色点的显示大小
            int dip = 10;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            //设置点与点的距离,第一个点除外
            if (i != 0)
                params.leftMargin = 47;
            v_point.setLayoutParams(params);

            llGuidePoints.addView(v_point);
        }

        // 创建viewpager的适配器
        adapter = new SplashAdapter(getApplicationContext(), guids);
        // 设置适配器
        vpGuide.setAdapter(adapter);
    }

    private void initEvent() {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginUserPwdActivity.class);
                startActivity(intent);
                //这部分代码是切换Activity时的动画，看起来就不会很生硬
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        });
        //滑动事件监听滑动距离，点更随滑动。
        vpGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //当前viewpager显示的页码
                //如果viewpager滑动到第三页码（最后一页），显示进入的button
                if (position == guids.size() - 1) {
                    startBtn.setVisibility(View.VISIBLE);//设置按钮的显示
                } else {
                    //隐藏该按钮
                    startBtn.setVisibility(View.GONE);
                }
                currentItem = position;
            }

            /**
             *页面滑动调用，拿到滑动距离设置视图的滑动状态
             * @param position 当前页面位置
             * @param positionOffset 移动的比例值
             * @param positionOffsetPixels 便宜的像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //给页面设置触摸事件
        vpGuide.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float endX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        //获取屏幕的宽度
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;
                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        if (currentItem == (guids.size() - 1) && startX - endX >= (width / 4)) {
                            //进入主页
                            Intent intent = new Intent(SplashActivity.this, LoginUserPwdActivity.class);
                            startActivity(intent);
                            //这部分代码是切换Activity时的动画，看起来就不会很生硬
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            finish();
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

}
