package com.uart.hbapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonViewHolder {

    //所有控件的集合
    private SparseArray<View> mViews;
    //记录位置 可能会用到
    private int mPosition;
    //复用的View
    private View mConvertView;
    /**
     * 构造函数
     *
     * @param context  上下文对象
     * @param parent   父类容器
     * @param layoutId 布局的ID
     * @param position item的位置
     */
    public CommonViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<>();
        //构造方法中就指定布局
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        //设置Tag
        mConvertView.setTag(this);
    }

    /**
     * 得到一个ViewHolder
     *
     * @param context     上下文对象
     * @param convertView 复用的View
     * @param parent      父类容器
     * @param layoutId    布局的ID
     * @param position    item的位置
     * @return
     */
    public static CommonViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        //如果为空  直接新建一个ViewHolder
        if (convertView == null) {
            return new CommonViewHolder(context, parent, layoutId, position);
        } else {
            //否则返回一个已经存在的ViewHolder
            CommonViewHolder viewHolder = (CommonViewHolder) convertView.getTag();
            //记得更新条目位置
            viewHolder.mPosition = position;
            return viewHolder;
        }
    }

    /**
     * @return 返回复用的View
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过ViewId获取控件
     *
     * @param viewId View的Id
     * @param <T>    View的子类
     * @return 返回View
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为文本设置text
     *
     * @param viewId view的Id
     * @param text   文本
     * @return 返回ViewHolder
     */
    public CommonViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 为Image设置bitmap
     *
     * @param viewId view的Id
     * @param bmp   bitmap
     * @return 返回ViewHolder
     */
    public CommonViewHolder setBitmap(int viewId, Bitmap bmp) {
        ImageView tv = getView(viewId);
        tv.setImageBitmap(bmp);
        return this;
    }

    public CommonViewHolder setImageResource(int viewId, int resId) {
        ImageView tv = getView(viewId);
        tv.setImageResource(resId);
        return this;
    }


    public CommonViewHolder setTag(int viewId, Object tag) {
        View v = getView(viewId);
        v.setTag(tag);
        return this;
    }

    public CommonViewHolder setVisibility(int viewId,int visibility){
        View v = getView(viewId);
        v.setVisibility(visibility);
        return this;
    }


}
