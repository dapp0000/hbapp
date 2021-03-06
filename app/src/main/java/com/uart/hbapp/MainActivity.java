package com.uart.hbapp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uart.entitylib.entity.UsageRecord;
import com.uart.hbapp.comm.Observer;
import com.uart.hbapp.comm.ObserverManager;
import com.uart.hbapp.fragment.DeviceFragment;
import com.uart.hbapp.fragment.RecordFragment;
import com.uart.hbapp.fragment.SettingFragment;
import com.uart.hbapp.fragment.UserInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Observer {
    public static final String KEY_DATA = "key_data";
    @BindView(R.id.ll_frameLayout)
    FrameLayout llFrameLayout;
    @BindView(R.id.nav_view)
    BottomNavigationView navView;
    @BindView(R.id.container)
    ConstraintLayout container;


    private BleDevice bleDevice;
    private BluetoothGattService bluetoothGattService;
    private BluetoothGattCharacteristic characteristic;

    private int charaProp;
    private int lastIndex;
    List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBottomNavigation();
        SPUtils.getInstance().put("firstUse",true);
        try {
            getSupportActionBar().hide();
            getActionBar().hide();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        bleDevice = getIntent().getParcelableExtra(KEY_DATA);
        if (bleDevice == null)
            ToastUtils.showShort("未连接蓝牙设备");

        ObserverManager.getInstance().addObserver(this);
    }



    public void initBottomNavigation() {
        mFragments = new ArrayList<>();
        mFragments.add(new DeviceFragment());
        mFragments.add(new RecordFragment());
        mFragments.add(new UserInfoFragment());
        mFragments.add(new SettingFragment());

        BottomNavigationView mBottomNavigationView = findViewById(R.id.nav_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        setFragmentPosition(0);
                        break;
                    case R.id.navigation_dashboard:
                        setFragmentPosition(1);
                        break;
                    case R.id.navigation_notifications:
                        setFragmentPosition(2);
                        break;
                    case R.id.navigation_settings:
                        setFragmentPosition(3);
                        break;
                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });

        if("record".equals(getIntent().getAction())){
            //setFragmentPosition(1);
            mBottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        }
        else if("resource".equals(getIntent().getAction())){
            mBottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
        }
        else{
            // 初始化展示MessageFragment
            //setFragmentPosition(0);
            mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.ll_frameLayout, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try{
            ObserverManager.getInstance().deleteObserver(this);
            if (bleDevice != null && BleManager.getInstance().isConnected(bleDevice)) {
                BleManager.getInstance().disconnect(bleDevice);
            }

            //记录登录用户信息
            HbApplication.getDaoInstance().getUserInfoDao().insertOrReplace(HbApplication.getInstance().loginUser);
            //记录使用记录
            HbApplication.getDaoInstance().getUsageRecordDao().insertOrReplace(HbApplication.getInstance().usageRecord);


            HbApplication.getInstance().usageRecord = new UsageRecord();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void disConnected(BleDevice bleDevice) {
        ToastUtils.showShort("蓝牙设备断开了");
    }




    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public BluetoothGattService getBluetoothGattService() {
        return bluetoothGattService;
    }

    public void setBluetoothGattService(BluetoothGattService bluetoothGattService) {
        this.bluetoothGattService = bluetoothGattService;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    public int getCharaProp() {
        return charaProp;
    }

    public void setCharaProp(int charaProp) {
        this.charaProp = charaProp;
    }

    private long firstPressedTime;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed();
            finish();
            System.exit(0);
        } else {
            ToastUtils.showShort("再按一次退出");
            firstPressedTime = System.currentTimeMillis();
        }
    }
}
