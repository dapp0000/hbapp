package com.uart.hbapp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;

import com.clj.fastble.data.BleDevice;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uart.hbapp.comm.Observer;
import com.uart.hbapp.comm.ObserverManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements Observer {
    public static final String KEY_DATA = "key_data";

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
        initBottomNavigation();
        initData();

        ObserverManager.getInstance().addObserver(this);
    }

    public void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new DeviceFragment());
        mFragments.add(new RecordFragment());
        mFragments.add(new UserInfoFragment());
        // 初始化展示MessageFragment
        setFragmentPosition(0);
    }

    public void initBottomNavigation() {
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
                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });
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
        ObserverManager.getInstance().deleteObserver(this);
    }

    @Override
    public void disConnected(BleDevice bleDevice) {

    }

//    private long firstPressedTime;
//    @Override
//    public void onBackPressed() {
//        if (System.currentTimeMillis() - firstPressedTime < 2000) {
//            super.onBackPressed();
//            System.exit(0);
//        } else {
//            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
//            firstPressedTime = System.currentTimeMillis();
//        }
//    }

}
