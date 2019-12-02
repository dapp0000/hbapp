package com.uart.hbapp.search;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.uart.hbapp.AppConstants;
import com.uart.hbapp.MainActivity;
import com.uart.hbapp.R;
import com.uart.hbapp.adapter.DeviceAdapter;
import com.uart.hbapp.comm.ObserverManager;
import com.uart.hbapp.utils.view.Radar.RadarView;
import com.uart.hbapp.utils.view.Radar.RadarViewGroup;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ScanActivity extends AppCompatActivity {
    @BindView(R.id.id_scan_circle)
    RadarView idScanCircle;
    @BindView(R.id.radar)
    RadarViewGroup radar;
    @BindView(R.id.list_device)
    ListView listDevice;
    @BindView(R.id.btnscan)
    Button btnscan;
    @BindView(R.id.btnskip)
    Button btnskip;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 100;
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final long SCAN_DURATION = 5000;

    private DeviceAdapter mDeviceAdapter;
    private Handler mHandler = new Handler();

    boolean mIsScanning = false;
    boolean isAutoConnect = false;
    String str_uuid = null;
    String str_name = null;
    String mac = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        radar.setiRadarClickListener(new RadarViewGroup.IRadarClickListener() {
            @Override
            public void onRadarItemClick(int position) {
                idScanCircle.stopScan();
                BleManager.getInstance().cancelScan();
                connect( mDeviceAdapter.getItem(position));
            }
        });

        mDeviceAdapter = new DeviceAdapter(this);
        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                }
            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().disconnect(bleDevice);
                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
//                    Intent intent = new Intent(MainActivity.this, OperationActivity.class);
//                    intent.putExtra(OperationActivity.KEY_DATA, bleDevice);
//                    startActivity(intent);
                }
            }
        });

        listDevice.setAdapter(mDeviceAdapter);

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);


    }

    public void updateRadar(BleDevice mDatas) {
        radar.setDatas(mDatas);
    }

    public void clearRadar(){
        radar.clearDatas();
    }

    private void setScanRule() {
        String[] uuids;
        if (TextUtils.isEmpty(str_uuid)) {
            uuids = null;
        } else {
            uuids = str_uuid.split(",");
        }
        UUID[] serviceUuids = null;
        if (uuids != null && uuids.length > 0) {
            serviceUuids = new UUID[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                String name = uuids[i];
                String[] components = name.split("-");
                if (components.length != 5) {
                    serviceUuids[i] = null;
                } else {
                    serviceUuids[i] = UUID.fromString(uuids[i]);
                }
            }
        }

        String[] names;
        if (TextUtils.isEmpty(str_name)) {
            names = null;
        } else {
            names = str_name.split(",");
        }


        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(isAutoConnect)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();

        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    private void connect(final BleDevice bleDevice) {

        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                ToastUtils.showShort("开始连接蓝牙设备");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                ToastUtils.showShort(getString(R.string.connect_fail));
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                //mDeviceAdapter.addDevice(bleDevice);
                //mDeviceAdapter.notifyDataSetChanged();
                ToastUtils.showShort(getString(R.string.connect_success));
                BluetoothGattService manService = gatt.getService(UUID.fromString(AppConstants.man_service_uuid));
                if(manService==null){
                    BleManager.getInstance().disconnect(bleDevice);
                    ToastUtils.showShort("设备未能识别");
                    return;
                }

                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.KEY_DATA, bleDevice);
                startActivity(intent);
                finish();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (isActiveDisConnected) {
                    ToastUtils.showShort(getString(R.string.active_disconnected));
                } else {
                    ToastUtils.showShort(getString(R.string.disconnected));
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }

            }
        });
    }

    private void readRssi(BleDevice bleDevice) {
        BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                Log.i(TAG, "onRssiFailure" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                Log.i(TAG, "onRssiSuccess: " + rssi);
            }
        });
    }

    private void setMtu(BleDevice bleDevice, int mtu) {
        BleManager.getInstance().setMtu(bleDevice, mtu, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                Log.i(TAG, "onsetMTUFailure" + exception.toString());
            }

            @Override
            public void onMtuChanged(int mtu) {
                Log.i(TAG, "onMtuChanged: " + mtu);
            }
        });
    }

    public void scanDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, getString(R.string.please_open_blue), Toast.LENGTH_LONG).show();
            return;
        }

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();

                clearRadar();
                idScanCircle.startScan();
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                try {
                    if (bleDevice != null && !TextUtils.isEmpty(bleDevice.getName())) {
                        mDeviceAdapter.addDevice(bleDevice);
                        mDeviceAdapter.notifyDataSetChanged();

                        updateRadar(bleDevice);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                idScanCircle.stopScan();
                BleDevice maxDevice = mDeviceAdapter.getItem(0);
                int count = mDeviceAdapter.getCount();
                for (int i=0;i<count;i++){
                    BleDevice device = mDeviceAdapter.getItem(i);
                    if(device.getRssi()>maxDevice.getRssi())
                        maxDevice = device;
                }

                radar.setCurrentShowItem(maxDevice);

            }
        });
    }

    public void stopScanDevice() {
        BleManager.getInstance().cancelScan();

    }

    public void skip() {
        Intent intent = new Intent(ScanActivity.this, MainActivity.class);
        intent.setAction("skip");
        startActivity(intent);
        mHandler.postDelayed(() -> {
            if (mIsScanning) {
                stopScanDevice();
            }
        }, SCAN_DURATION);
        finish();
    }

    @OnClick({R.id.btnscan, R.id.btnskip})
    public void onCliecked(View v){
        switch (v.getId()){
            case R.id.btnscan:
                scanDevice();
                break;
            case R.id.btnskip:
                skip();
                break;
        }
    }

//    private long firstPressedTime;
//
//    @Override
//    public void onBackPressed() {
//        if (System.currentTimeMillis() - firstPressedTime < 2000) {
//            super.onBackPressed();
//            finish();
//            System.exit(0);
//        } else {
//            ToastUtils.showShort("再按一次退出");
//            firstPressedTime = System.currentTimeMillis();
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        idScanCircle.stopScan();
        if(BleManager.getInstance().getScanSate() == BleScanState.STATE_SCANNING)
            BleManager.getInstance().cancelScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBluetoothPermission();
    }

    /*
    校验蓝牙权限
   */
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notifyTitle)
                    .setMessage(R.string.gpsNotifyMsg)
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .setPositiveButton(R.string.setting,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                }
                            })

                    .setCancelable(false)
                    .show();
        } else {
            setScanRule();
            scanDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ENABLE_BT){
            if (checkGPSIsOpen()) {
                setScanRule();
                scanDevice();
            }else{
                ToastUtils.showShort("蓝牙权限未开启,请设置");
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
