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

import com.blankj.utilcode.util.SPUtils;
import com.clj.fastble.data.BleDevice;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.MainActivity;
import com.uart.hbapp.R;
import com.uart.hbapp.dialog.EditDeviceDialogFragment;
import com.uart.hbapp.login.AdditionalActivity;
import com.uart.hbapp.login.LoginUserPwdActivity;
import com.uart.hbapp.utils.CommandUtils;

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


        nickName.setText(SPUtils.getInstance().getString("username"));
        //设置设备名称
        btnEditDevice.setText(CommandUtils.getUIDeviceName(HbApplication.getInstance().usageRecord.getDeviceName()));
        //设置电量
        ivBattery.setImageBitmap(CommandUtils.getUIDeviceBattery(getContext(),HbApplication.getInstance().usageRecord.getDeviceBattery()));
        //设置信号
        ivSignal.setImageBitmap(CommandUtils.getUIDeviceSignal(getContext(),HbApplication.getInstance().usageRecord.getDeviceSignal()));

        return v;
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
