package com.uart.hbapp.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.Toast;

import com.uart.hbapp.MainActivity;
import com.uart.hbapp.R;
import com.uart.hbapp.bean.ExtendedBluetoothDevice;
import com.uart.hbapp.login.WelcomeActivity;
import com.uart.hbapp.utils.LogUtil;
import com.uart.hbapp.utils.view.Radar.RadarViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import no.nordicsemi.android.ble.BleManagerCallbacks;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public class ScanActivity extends BleProfileActivity implements RadarViewGroup.IRadarClickListener {

    public static final UUID UART_SERVICE_UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");
    private RadarViewGroup radarViewGroup;
    private List<ExtendedBluetoothDevice> radarList = new ArrayList<>();
    private final static int REQUEST_PERMISSION_REQ_CODE = 34; // any 8-bit number
    private boolean mIsScanning = false;
    private final static long SCAN_DURATION = 5000;
    private final Handler mHandler = new Handler();
    private final ArrayList<ExtendedBluetoothDevice> mListBondedValues = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private ParcelUuid filterUuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        radarViewGroup = findViewById(R.id.radar);
        radarViewGroup.setiRadarClickListener(this);
        final BluetoothManager manager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            mBluetoothAdapter = manager.getAdapter();
        }

        scanDevice(null);
    }

    @Override
    protected LoggableBleManager<? extends BleManagerCallbacks> initializeManager() {
        return null;
    }

    @Override
    protected void setDefaultUI() {

    }

    @Override
    protected int getDefaultDeviceName() {
        return 0;
    }

    @Override
    protected int getAboutTextId() {
        return 0;
    }

    @Override
    protected UUID getFilterUUID() {
        return null;
    }

    public void scanDevice(View view){
        if(mIsScanning) {
            return;
        }

        // Since Android 6.0 we need to obtain either Manifest.permission.ACCESS_COARSE_LOCATION or Manifest.permission.ACCESS_FINE_LOCATION to be able to scan for
        // Bluetooth LE devices. This is related to beacons as proximity devices.
        // On API older than Marshmallow the following code does nothing.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // When user pressed Deny and still wants to use this functionality, show the rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(ScanActivity.this, "Since Android 6.0 Marshmallow system requires granting access to device\\'s location in order to scan for Bluetooth Smart devices. Bluetooth beacons may be used to determine the phone\\'s and user\\'s location.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);
            }
            return;
        }


        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        final ScanSettings settings = new ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(1000).setUseHardwareBatchingIfSupported(false).build();

        final List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setServiceUuid(filterUuid).build());
        scanner.startScan(filters, settings, scanCallback);


        mIsScanning = true;
    }


    public void stopScanDevice(View view){
        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        scanner.stopScan(scanCallback);
        mIsScanning = false;
    }





    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, final ScanResult result) {
            // do nothing
        }

        @Override
        public void onBatchScanResults(final List<ScanResult> results) {
            update(results);
        }

        @Override
        public void onScanFailed(final int errorCode) {
            // should never be called
        }
    };

    public void update(final List<ScanResult> results) {
        for (final ScanResult result : results) {
            ExtendedBluetoothDevice device = findDevice(result);
            if (device == null) {
                device =  new ExtendedBluetoothDevice(result);
                if(device.name!=null){
                    radarList.add(device);
                }
            } else {
                device.name = result.getScanRecord() != null ? result.getScanRecord().getDeviceName() : null;
                device.rssi = result.getRssi();
            }
        }
        LogUtil.getInstance().e("results"+results.size()+"+++radarList="+radarList.size());
        if (radarList.size()>0){
            stopScanDevice(null);
            radarViewGroup.setDatas(radarList);
        }
        radarViewGroup.postInvalidate();
    }

    private ExtendedBluetoothDevice findDevice(final ScanResult result) {
        for (final ExtendedBluetoothDevice device : mListBondedValues)
            if (device.matches(result))
                return device;
        for (final ExtendedBluetoothDevice device : radarList)
            if (device.matches(result))
                return device;
        return null;
    }



    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We have been granted the Manifest.permission.ACCESS_COARSE_LOCATION permission. Now we may proceed with scanning.
                    scanDevice(null);
                } else {
                    Toast.makeText(this, R.string.no_required_permission, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onRadarItemClick(int position) {
        stopScanDevice(null);
        ExtendedBluetoothDevice device = radarList.get(position);
        Toast.makeText(ScanActivity.this, "连接 " + device.device.getName() + "...", Toast.LENGTH_SHORT).show();
        mBleManager.connect(device.device)
                .useAutoConnect(true)
                .retry(3, 100)
                .enqueue();


        startActivity(new Intent(ScanActivity.this, MainActivity.class));
    }



    public void skip(View view){
        startActivity(new Intent(ScanActivity.this, MainActivity.class));
        mHandler.postDelayed(() -> {
            if (mIsScanning) {
                stopScanDevice(null);
            }
        }, SCAN_DURATION);
    }


    private long firstPressedTime;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed();
            System.exit(0);
        } else {
            Toast.makeText(ScanActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            firstPressedTime = System.currentTimeMillis();
        }
    }


    @Override
    public boolean shouldEnableBatteryLevelNotifications(@NonNull BluetoothDevice device) {
        return false;
    }

    @Override
    public void onBatteryValueReceived(@NonNull BluetoothDevice device, int value) {

    }


}
