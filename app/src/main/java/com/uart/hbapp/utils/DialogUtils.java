package com.uart.hbapp.utils;

/**
 * 加载进度条
 * @author F.K
 *
 */

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uart.hbapp.R;


/**
 * 对话框工具类
 *
 * @author djy
 * @param imageUrl
 * @return
 */
public class DialogUtils {
    public static Dialog dialog;
//	public static ProgressDialog dialog;

    /**
     * 显示加载的dialog
     *
     * @param context
     */
    public static void showProgressDialog(Context context, String message) {
        dialog = CreateLoadingDialog(context, message);
//		dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
//		dialog.setMessage(message);
        if (dialog.isShowing()) {
        } else {
            dialog.show();
        }
    }

    /**
     * 把dialog关闭
     */
    public static void closeProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            Log.e("dialog--1", "关闭dialog");
        }else {
            Log.e("dialog", "已经关闭dialog");
        }
    }

    /**
     * 得到自定义的progressDialog
     *
     */
    private static Dialog CreateLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);

        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.load_animation);
        // 使用ImageView显示旋转动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

//		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;

    }
}
