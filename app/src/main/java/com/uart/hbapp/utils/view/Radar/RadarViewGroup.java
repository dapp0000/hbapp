package com.uart.hbapp.utils.view.Radar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.clj.fastble.data.BleDevice;
import com.uart.hbapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by admin on 2019/8/9.
 */

public class RadarViewGroup  extends ViewGroup implements RadarView.IScanningListener {
    private int mWidth, mHeight;//viewgroup的宽高
    private List<BleDevice> mDatas=new ArrayList<>();//数据源
    private int minItemPosition;//最小距离的item所在数据源中的位置
    private CircleView minShowChild;//最小距离的item
    private CircleView currentShowChild;//当前展示的item
    private IRadarClickListener iRadarClickListener;//雷达图中点击监听CircleView小圆点回调接口

    public void setiRadarClickListener(IRadarClickListener iRadarClickListener) {
        this.iRadarClickListener = iRadarClickListener;
    }

    public RadarViewGroup(Context context) {
        this(context, null);
    }

    public RadarViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mWidth = mHeight = Math.min(mWidth, mHeight);
        //测量每个children
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getId() == R.id.id_scan_circle) {
                //为雷达扫描图设置需要的属性
                ((RadarView) child).setScanningListener(this);
                //考虑到数据没有添加前扫描图在扫描，但是不会开始为CircleView布局
                if (mDatas != null && mDatas.size() > 0) {
                    ((RadarView) child).setMaxScanItemCount(mDatas.size());
                    //((RadarView) child).startScan();
                }
                continue;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        LogUtils.e("childCount"+childCount);
        //首先放置雷达扫描图
        View view = findViewById(R.id.id_scan_circle);
        if (view != null) {
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }

        //放置雷达图中需要展示的item圆点
        for (int i = 0; i < childCount; i++) {
            final int j = i;
            final View child = getChildAt(i);
            LogUtils.e("i"+i);
            if (child instanceof CircleView) {
                CircleView item  = (CircleView)child;
                float angle = item.getAngle();
                //设置CircleView小圆点的坐标信息
                //坐标 = 旋转角度 * 半径 * 根据远近距离的不同计算得到的应该占的半径比例
                float x =(float) Math.cos(Math.toRadians(angle))* item.getProportion() * mWidth / 2;
                float y =(float) Math.sin(Math.toRadians(angle))* item.getProportion() * mWidth / 2;
                item.setDisX(x);
                item.setDisY(y);
                //放置Circle小圆点
                item.layout(
                        (int) item.getDisX() + mWidth / 2,
                        (int) item.getDisY() + mHeight /2,
                        (int) item.getDisX() + child.getMeasuredWidth() + mWidth / 2,
                        (int) item.getDisY() + child.getMeasuredHeight() + mHeight / 2);

                //设置点击事件
                item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetAnim(currentShowChild);
                        currentShowChild = item;
                        //因为雷达图是childAt(0),所以这里需要作-1才是正确的Circle
                        startAnim(currentShowChild, j - 1);
                        if (iRadarClickListener != null) {
                            iRadarClickListener.onRadarItemClick(j - 1);
                        }
                    }
                });

            }

        }

    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;

    }



    private Random random = new Random();
    public void setDatas(BleDevice device) {
        removeIfExist(device);

        this.mDatas.add(device);
        //根据数据源信息动态添加CircleView
        CircleView circleView = new CircleView(getContext());
        circleView.setTag(device);
        String deviceName = device.getName();
        if(deviceName.length()>8)
            deviceName = deviceName.substring(0,7)+"...";
        circleView.setText("    "+deviceName);
        circleView.setTextSize(12);
        circleView.setTextColor(getResources().getColor(R.color.white));
        circleView.setAngle(random.nextInt(360));
        //根据远近距离的不同计算得到的应该占的半径比例 0.312-0.832
        float proportion = Math.abs(device.getRssi())/150.0f;
        if(proportion<0.312)
            proportion = 0.312f;
        if(proportion>0.832)
            proportion =  0.832f;
        circleView.setProportion(proportion);
        addView(circleView);
    }


    public void clearDatas(){
        mDatas.clear();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof CircleView) {
                removeView(child);
            }
        }
    }

    public void removeIfExist(BleDevice bleDevice){
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if(child!=null){
                Object tagObj = getChildAt(i).getTag();
                if(tagObj instanceof BleDevice){
                    BleDevice tag = (BleDevice)tagObj;
                    if (tag.getName().equals(bleDevice.getName()) && tag.getMac().equals(bleDevice.getMac())) {
                        removeViewAt(i);
                        mDatas.remove(tag);
                    }
                }
            }
        }
    }


    /**
     * 雷达图没有扫描完毕时回调
     *
     * @param position
     * @param scanAngle
     */
    @Override
    public void onScanning(int position, float scanAngle) {
//        if (scanAngle == 0) {
//            scanAngleList.put(position, 1f);
//        } else {
//            scanAngleList.put(position, scanAngle);
//        }
//        requestLayout();
    }

    /**
     * 雷达图扫描完毕时回调
     */
    @Override
    public void onScanSuccess() {
        Log.e("tag","完成回调");
        resetAnim(currentShowChild);
        currentShowChild = minShowChild;
        startAnim(currentShowChild, minItemPosition);
    }

    /**
     * 恢复CircleView小圆点原大小
     *
     * @param object
     */
    private void resetAnim(CircleView object) {
        if (object != null) {
            object.clearPortaitIcon();
            ObjectAnimator.ofFloat(object, "scaleX", 1f).setDuration(300).start();
            ObjectAnimator.ofFloat(object, "scaleY", 1f).setDuration(300).start();
        }

    }

    /**
     * 放大CircleView小圆点大小
     *
     * @param object
     * @param position
     */
    private void startAnim(CircleView object, int position) {
        if (object != null) {
//            object.setPortraitIcon(mDatas.get(position).getDevicesId());
            ObjectAnimator.ofFloat(object, "scaleX", 2f).setDuration(300).start();
            ObjectAnimator.ofFloat(object, "scaleY", 2f).setDuration(300).start();
        }
    }

    /**
     * 雷达图中点击监听CircleView小圆点回调接口
     */
    public interface IRadarClickListener {
        void onRadarItemClick(int position);
    }



    public void setCurrentShowItem(BleDevice bleDevice) {
        resetAnim(currentShowChild);
        currentShowChild = null;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if(child!=null){
                Object tagObj = getChildAt(i).getTag();
                if(tagObj instanceof BleDevice){
                    BleDevice tag = (BleDevice)tagObj;
                    if (tag.getName().equals(bleDevice.getName()) && tag.getMac().equals(bleDevice.getMac())) {
                        currentShowChild = (CircleView)getChildAt(i);
                        startAnim(currentShowChild, i);
                    }
                }
            }
        }

        if(currentShowChild ==null){
            currentShowChild = (CircleView)getChildAt(2);
            startAnim(currentShowChild, 1);
        }

    }

}
