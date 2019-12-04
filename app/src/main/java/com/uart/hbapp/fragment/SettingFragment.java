package com.uart.hbapp.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.dialog.EditDeviceDialogFragment;
import com.uart.hbapp.login.AdditionalActivity;
import com.uart.hbapp.login.LoginUserPwdActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingFragment extends Fragment {

    SettingViewModel mViewModel;
    ActionBar actionBar;
    @BindView(R.id.headImg)
    ImageView headImg;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.layout_device)
    LinearLayout layoutDevice;
    @BindView(R.id.exit)
    TextView exit;
    @BindView(R.id.btnEditDevice)
    TextView btnEditDevice;
    @BindView(R.id.iv_battery)
    ImageView ivBattery;
    @BindView(R.id.iv_signal)
    ImageView ivSignal;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, v);


        nickName.setText(HbApplication.getInstance().loginUser.getUserName());
        //设置设备名称
        setDeviceName(HbApplication.getInstance().usageRecord.getDeviceName());
        //设置电量
        setDeviceBattery(HbApplication.getInstance().usageRecord.getDeviceBattery());
        //设置信号
        setDeviceSignal(HbApplication.getInstance().usageRecord.getDeviceSignal());

        return v;
    }

    private void setDeviceName(String name){
        if(TextUtils.isEmpty(name))
            return;

        btnEditDevice.setText("我的设备:"+name);
    }

    private void setDeviceBattery(Integer battery) {
        if(battery==null)
            return;

        //设置信号
        if (battery > 80) {
            ivBattery.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.p4));
        } else if (battery > 70) {
            ivBattery.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.p3));
        } else if (battery > 60) {
            ivBattery.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.p2));
        } else {
            ivBattery.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.p1));
        }
    }

    private void setDeviceSignal(Integer signal) {
        if(signal==null)
            return;

        //设置信号
        if (signal > 50) {
            ivSignal.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.s05));
        } else if (signal > 30) {
            ivSignal.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.s03));
        } else if (signal > 10) {
            ivSignal.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.s02));
        } else {
            ivSignal.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.s00));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        // TODO: Use the ViewModel
        setHasOptionsMenu(true);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.title_settings);
    }

    @OnClick({R.id.headImg, R.id.nickName, R.id.exit,R.id.btnEditDevice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.headImg:
                startActivity(new Intent(getActivity(), AdditionalActivity.class));
                break;
            case R.id.nickName:
                startActivity(new Intent(getActivity(), AdditionalActivity.class));
                break;
            case R.id.exit:
                startActivity(new Intent(getActivity(), LoginUserPwdActivity.class));
                getActivity().finish();
                break;
            case R.id.btnEditDevice:
                EditDeviceDialogFragment fragmentDevice = EditDeviceDialogFragment.newInstance("");
                fragmentDevice.show(getFragmentManager(), "");
                break;
        }
    }
}
