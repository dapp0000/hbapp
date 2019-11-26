package com.uart.hbapp.fragment;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.MainActivity;
import com.uart.hbapp.R;
import com.uart.hbapp.dialog.EditDeviceDialogFragment;
import com.uart.hbapp.dialog.SelectMusicDialogFragment;
import com.uart.hbapp.utils.ByteUtils;
import com.uart.hbapp.utils.OriginalDataUtil;
import com.uart.hbapp.utils.URLUtil;
import com.uart.hbapp.utils.view.LineChart.LineChartManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceFragment extends Fragment {
    @BindView(R.id.btn_edit_device)
    TextView btnEditDevice;
    @BindView(R.id.layout_device)
    LinearLayout layoutDevice;
    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;
    @BindView(R.id.line_chart_signal)
    LineChart lineChartSignal;
    @BindView(R.id.btn_play)
    ImageView btnPlay;
    @BindView(R.id.layout_control)
    FrameLayout layoutControl;
    @BindView(R.id.seek_music)
    SeekBar seekMusic;
    @BindView(R.id.layout_playing)
    FrameLayout layoutPlaying;
    @BindView(R.id.btn_music)
    ImageView btnMusic;
    @BindView(R.id.btn_menu)
    ImageView btnMenu;
    @BindView(R.id.btn_menu_stop)
    ImageView btnMenuStop;

    private static final String TAG = DeviceFragment.class.getSimpleName();
    public static final int PROPERTY_READ = 1;
    public static final int PROPERTY_WRITE = 2;
    public static final int PROPERTY_WRITE_NO_RESPONSE = 3;
    public static final int PROPERTY_NOTIFY = 4;
    public static final int PROPERTY_INDICATE = 5;
    RotateAnimation animationR;
    DeviceViewModel mViewModel;
    ActionBar actionBar;
    LineChartManager lineChartManager;
    BleDevice bleDevice;
    BluetoothGattCharacteristic heartRateCharacteristic;
    ArrayList<Float> xValues;
    MediaPlayer mediaPlayer;
    String musicPath;
    File cacheDir;
    String dataFileName;
    String zipFileName;
    long startTime;
    long endTime;
    boolean isResting;
    int restMinute = 10;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device, container, false);
        ButterKnife.bind(this, v);

        cacheDir = Objects.requireNonNull(getActivity()).getCacheDir();
        lineChartManager = new LineChartManager(lineChartSignal);
        timerOriginal.schedule(task, 100, 15000);//15s

        initData();
        initChart();

        /* 设置旋转动画*/
        animationR = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationR.setDuration(3000);
        animationR.setRepeatCount(RotateAnimation.INFINITE);
        animationR.setInterpolator(new LinearInterpolator());
        btnMusic.setAnimation(animationR);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        setHasOptionsMenu(true);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.title_home);
        // TODO: Use the ViewModel

        animationR.startNow();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (actionBar != null && !hidden) {
            actionBar.setTitle(R.string.title_home);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.btn_close_device:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        bleDevice = ((MainActivity) getActivity()).getBleDevice();
        if (bleDevice == null) {
            lineChartSignal.setNoDataText("设备未连接");
            btnPlay.setEnabled(false);
            return;
        } else {
            lineChartSignal.setNoDataText("设备准备就绪");
            btnPlay.setEnabled(true);
        }

//
//        txtDeviceName.setText(bleDevice.getName());
//
//        //设置信号
//        int signal = Math.abs(bleDevice.getRssi());
//        if (signal > 80) {
//            tvSignal.setText("很差");
//            viewSignal.setBackground(getResources().getDrawable(R.drawable.ic_signal_cellular_1_bar_black_24dp));
//        } else if (signal > 70) {
//            tvSignal.setText("差");
//            viewSignal.setBackground(getResources().getDrawable(R.drawable.ic_signal_cellular_2_bar_black_24dp));
//        } else if (signal > 60) {
//            tvSignal.setText("正常");
//            viewSignal.setBackground(getResources().getDrawable(R.drawable.ic_signal_cellular_3_bar_black_24dp));
//        } else {
//            tvSignal.setText("很好");
//            viewSignal.setBackground(getResources().getDrawable(R.drawable.ic_signal_cellular_4_bar_black_24dp));
//        }


        BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                LogUtils.i(TAG, "onRssiFailure" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                LogUtils.i(TAG, "onRssiSuccess: " + rssi);

            }
        });


        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);

        List<BluetoothGattService> services = gatt.getServices();
        for (BluetoothGattService service : services) {
            String uuid = service.getUuid().toString();
            LogUtils.i("BluetoothGattService:" + uuid);
            switch (uuid) {
                case HbApplication.man_service_uuid://脑电波服务
                    ((MainActivity) getActivity()).setBluetoothGattService(service);
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    heartRateCharacteristic = characteristics.get(0);
                    int charaProp = heartRateCharacteristic.getProperties();
                    if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                        ((MainActivity) getActivity()).setCharacteristic(characteristics.get(1));
                        ((MainActivity) getActivity()).setCharaProp(DeviceFragment.PROPERTY_WRITE);
                    }

                    openNotify(bleDevice, heartRateCharacteristic);
                    break;
                case HbApplication.device_info_service_uuid://设备信息服务
                    configDIS(service);
                    break;
                default:
                    break;
            }
        }
    }

    private void configDIS(BluetoothGattService service) {
        List<BluetoothGattCharacteristic> characteristics_DIS = service.getCharacteristics();
        for (BluetoothGattCharacteristic characteristic : characteristics_DIS) {
            String uuid = characteristic.getUuid().toString();
            LogUtils.i("DISService:" + uuid);
            switch (uuid) {
                case HbApplication.device_info_serial_number://Serial Number String (d@后对应音频蓝牙地址)
                    BleManager.getInstance().read(
                            bleDevice,
                            characteristic.getService().getUuid().toString(),
                            characteristic.getUuid().toString(),
                            new BleReadCallback() {
                                @Override
                                public void onReadSuccess(final byte[] data) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //addText(txt, HexUtil.formatHexString(data, true));
                                            try {
                                                String srt2 = new String(data, "UTF-8");
                                                ToastUtils.showShort("音频蓝牙地址：" + srt2);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onReadFailure(final BleException exception) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //addText(txt, exception.toString());
                                        }
                                    });
                                }
                            });


                    break;
                case "":
                    break;
                default:
                    break;
            }
        }
    }

    private void openNotify(BleDevice bleDevice, BluetoothGattCharacteristic characteristic) {
        boolean connected = BleManager.getInstance().isConnected(bleDevice);
        if (!connected) {
            return;
        }

        BleManager.getInstance().notify(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                new BleNotifyCallback() {

                    @Override
                    public void onNotifySuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //addText(txt, "notify success");
                                //ToastUtils.showShort("notify success");
                            }
                        });
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //addText(txt, exception.toString());
                                ToastUtils.showShort(exception.toString());
                            }
                        });
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //addText(txt, HexUtil.formatHexString(characteristic.getValue(), true));
                                addLineData(characteristic.getValue());
                            }
                        });
                    }
                });
    }

    private void closeNotify(BleDevice bleDevice, BluetoothGattCharacteristic characteristic) {
        boolean connected = BleManager.getInstance().isConnected(bleDevice);
        if (!connected) {
            return;
        }

        BleManager.getInstance().stopNotify(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString());
    }

    private void runOnUiThread(Runnable runnable) {
        if (isAdded() && getActivity() != null)
            getActivity().runOnUiThread(runnable);
    }

    private void addLineData(byte[] datas) {
        showChart(datas);
    }

    private void initChart() {
        XAxis xAxis = lineChartSignal.getXAxis();
        YAxis yAxisLeft = lineChartSignal.getAxisLeft();
        YAxis yAxisRight = lineChartSignal.getAxisRight();
        xAxis.setTextColor(Color.WHITE);
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisRight.setTextColor(Color.WHITE);

        //设置x轴的数据
        xValues = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<Float> yValues = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            yValues.add(0f);
        }

        lineChartManager.showLineChart(xValues, yValues, "", Color.GREEN);
        lineChartManager.setYAxis(100, 0, 0);
        lineChartManager.setYAxis(10, 0, 0);

        LineData lineData = lineChartSignal.getLineData();
        lineData.setValueTextColor(Color.GREEN);
        Legend legend = lineChartSignal.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setForm(Legend.LegendForm.EMPTY);
        Description description = lineChartSignal.getDescription();
        description.setTextColor(Color.WHITE);
        description.setText("");

        lineChartSignal.setDrawBorders(false);
        lineChartSignal.setDrawMarkers(false);
    }

    private void showChart(byte[] datas) {
        boolean isData = true;
        int maxValue = 100;
        int size = datas.length;
        if (size != 20)
            isData = false;
        if ((datas[0] & 0xFF) != 170)
            isData = false;
        if ((datas[1] & 0xFF) != 170)
            isData = false;
        if ((datas[2] & 0xFF) != 16)
            isData = false;
        if ((datas[3] & 0xFF) != 128)
            isData = false;

        String hexString = ByteUtils.bytesToHex(datas);
        if (!isData) {
            LogUtils.e(hexString);
            //LogUtils.e(ByteUtils.bytesToUTF8(datas));
            return;
        } else {
            if (isResting) {
                originalList.add(hexString);
            }
        }

        //设置y轴的数据()
        List<Float> yValue = new ArrayList<>();
        for (int j = 4; j < size - 1; j = j + 2) {
            int hign = datas[j] & 0xFF;
            int low = datas[j + 1] & 0xFF;
            float point = (hign * 256 + low) / 1000f;
            if (point > maxValue)
                maxValue = (int) point + 100;


            yValue.add(point);
        }

        lineChartManager.showLineChart(xValues, yValue, "", Color.GREEN);
        lineChartManager.setYAxis(maxValue, 0, 0);
        LineData lineData = lineChartSignal.getLineData();
        lineData.setValueTextColor(Color.GREEN);

    }


    private void mediaPlayer(String filePath) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startRest() {
        mediaPlayer(musicPath);

        isResting = true;
        startTime = System.currentTimeMillis();
        String fileName = HbApplication.loginUser + "_" + System.currentTimeMillis();
        dataFileName = fileName + ".txt";
        zipFileName = fileName + ".zip";
        originalList.clear();

        btnMenu.setVisibility(View.GONE);
        btnMenuStop.setVisibility(View.VISIBLE);
        layoutControl.setVisibility(View.GONE);
        layoutPlaying.setVisibility(View.VISIBLE);
        //播放音乐
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.right1);
        //设置进度条最大长度为音频时长
        seekMusic.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        //线程开始运行
        new MusicThread().start();
        //设置进度条快进效果
//        seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            //值改变
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            }
//
//            //值改变前
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            //值改变后
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                mediaPlayer.seekTo(seekMusic.getProgress());
//            }
//        });

        seekMusic.setEnabled(false);

        animationR.startNow();
    }

    private void stopRest() {
        isResting = false;
        endTime = System.currentTimeMillis();
        originalList.clear();

        if (bleDevice != null) {
            mediaPlayer.reset();
            updateNet();
        }

        btnMenu.setVisibility(View.VISIBLE);
        btnMenuStop.setVisibility(View.GONE);
        layoutControl.setVisibility(View.VISIBLE);
        layoutPlaying.setVisibility(View.GONE);

        animationR.cancel();
    }

    private void updateNet() {
        updateSleepInfo(bleDevice.getName(), bleDevice.getMac(), HbApplication.loginUser, startTime, endTime);
    }

    private void updateSleepInfo(String deviceName, String deviceMac, String userName, long startTime, long endTime) {
//        {
//            "deviceName":"xv-90",
//                "deviceMac":"MVC-DD",
//                "userName":"wyqljwwy",
//                "startTime":1571626067000,
//                "endTime":1571626067000
//        }


        HashMap<String, Object> params = new HashMap<>();
        params.put("deviceName", deviceName);
        params.put("deviceMac", deviceMac);
        params.put("userName", userName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        JSONObject jsonObject = new JSONObject(params);
        String base_url = URLUtil.url + URLUtil.addSleepInfo;
        OkGo.<String>post(base_url)
                .tag(this)
//                .cacheKey("cachePostKey")
//                .cacheMode(CacheMode.DEFAULT)
//                .headers("token", SpUtils.get(DynameicFaceApplication.myContext, "token", "") + "")
                .headers("Content-Type", "application/json")
                .upJson(jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.e("eee", "AddFaceT:" + response.body());
                            JSONObject jsonObject = new JSONObject(response.body());
                            int error = jsonObject.getInt("error");
                            if (error == 0) {
                                ToastUtils.showShort("休息记录已经上传成功");
                                updateZipFile();
                            } else {
                                ToastUtils.showShort(jsonObject.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showShort(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showShort("服务器异常");
                    }
                });

    }

    private void updateZipFile() {
        File srcFile = new File(cacheDir, dataFileName);
        File zipFile = new File(cacheDir, zipFileName);

        try {
            boolean result = ZipUtils.zipFile(srcFile, zipFile);
            if (result) {
                ToastUtils.showShort("数据文件准备上传");
                FileUtils.delete(srcFile);//删除原始文件
                String base_url = URLUtil.url + URLUtil.fileData;
                OkGo.<String>post(base_url)
                        .tag(this)
                        .params("originalDataFile", zipFile)
                        .isMultipart(true)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                ToastUtils.showShort("上传成功" + response.body());
                                FileUtils.delete(zipFile);
                            }

                            @Override
                            public void onError(Response<String> response) {
                                ToastUtils.showShort("上传失败" + response.body());
                            }
                        });

            } else {
                ToastUtils.showShort("数据文件丢失");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFail() {

    }

    private List<String> originalList = new ArrayList<>();
    private Timer timerOriginal = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            try {
                //1分钟写入原始数据
                if (isResting) {
                    Object[] datas = originalList.toArray();
                    originalList.clear();
                    OriginalDataUtil.writeListIntoCache(cacheDir, dataFileName, datas);
                } else {
                    updateFail();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @OnClick({R.id.btn_play, R.id.btn_menu_stop, R.id.btn_edit_device, R.id.btn_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                startRest();
                break;
            case R.id.btn_menu_stop:
                stopRest();
                break;
            case R.id.btn_edit_device:
                EditDeviceDialogFragment fragmentDevice = EditDeviceDialogFragment.newInstance("");
                fragmentDevice.show(getFragmentManager(), "");
                break;
            case R.id.btn_menu:
                SelectMusicDialogFragment fragmentMusic = SelectMusicDialogFragment.newInstance("");
                fragmentMusic.show(getFragmentManager(), "");
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeNotify(bleDevice, heartRateCharacteristic);
        stopRest();
        timerOriginal.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        animationR.startNow();
    }

    @Override
    public void onPause() {
        super.onPause();
        animationR.cancel();
    }


    class MusicThread extends Thread {
        @Override
        public void run() {
            super.run();
            //判断当前播放位置是否小于总时长
            while (seekMusic.getProgress() <= seekMusic.getMax()) {
                //设置进度条当前位置为音频播放位置
                seekMusic.setProgress(mediaPlayer.getCurrentPosition());
            }
        }
    }

}
