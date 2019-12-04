package com.uart.hbapp.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditDeviceDialogFragment extends DialogFragment {
    @BindView(R.id.layout_editname)
    LinearLayout layoutEditname;
    @BindView(R.id.layout_disconnect)
    LinearLayout layoutDisconnect;
    @BindView(R.id.tab_edit_device)
    TextView tabEditDevice;
    @BindView(R.id.tab_disconnect)
    TextView tabDisconnect;

    private static final String ARG_KEY = "key";

    public static EditDeviceDialogFragment newInstance(String key) {
        final EditDeviceDialogFragment fragment = new EditDeviceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final View v = inflater.inflate(R.layout.dialog_edit_device, null);
        ButterKnife.bind(this, v);
        selectTab(0);

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.TOP;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        win.setAttributes(params);
    }

    private void selectTab(int position) {
        if (position == 0) {
            layoutEditname.setVisibility(View.VISIBLE);
            layoutDisconnect.setVisibility(View.GONE);
            tabEditDevice.setBackground(getResources().getDrawable(R.drawable.shape_button_left));
            tabDisconnect.setBackground(null);
        } else if (position == 1) {
            layoutEditname.setVisibility(View.GONE);
            layoutDisconnect.setVisibility(View.VISIBLE);
            tabEditDevice.setBackground(null);
            tabDisconnect.setBackground(getResources().getDrawable(R.drawable.shape_button_right));
        }
    }

    @OnClick({R.id.tab_edit_device, R.id.tab_disconnect,R.id.btn_edit_device_ok,R.id.btn_disconnect_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab_edit_device:
                selectTab(0);
                break;
            case R.id.tab_disconnect:
                selectTab(1);
                break;
            case R.id.btn_edit_device_ok:
                getDialog().dismiss();
                break;
            case R.id.btn_disconnect_ok:
                startActivity(new Intent(getActivity(), ScanActivity.class));
                getActivity().finish();
                getDialog().dismiss();
                break;
        }
    }
}
