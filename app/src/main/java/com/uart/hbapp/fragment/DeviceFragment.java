package com.uart.hbapp.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
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

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.entitylib.entity.RelaxData;
import com.uart.entitylib.entity.Resource;
import com.uart.entitylib.entity.RestDuration;
import com.uart.entitylib.entity.SleepData;
import com.uart.entitylib.entity.UsageRecord;
import com.uart.hbapp.AppConstants;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.MainActivity;
import com.uart.hbapp.R;
import com.uart.hbapp.bean.SleepDataInfo;
import com.uart.hbapp.dialog.EditDeviceDialogFragment;
import com.uart.hbapp.dialog.SelectMusicDialogFragment;
import com.uart.hbapp.utils.ByteUtils;
import com.uart.hbapp.utils.CommandUtils;
import com.uart.hbapp.utils.OriginalDataUtil;
import com.uart.hbapp.utils.URLUtil;
import com.uart.hbapp.utils.view.LineChart.LineChartManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
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
    @BindView(R.id.txt_record_music_name)
    TextView txtRecordMusicName;
    @BindView(R.id.txt_record_duration)
    TextView txtRecordDuration;
    @BindView(R.id.txt_record_speak_name)
    TextView txtRecordSpeakName;
    @BindView(R.id.iv_battery)
    ImageView ivBattery;
    @BindView(R.id.iv_signal)
    ImageView ivSignal;
    @BindView(R.id.iv_relax_type)
    ImageView ivRelaxType;
    @BindView(R.id.txt_relax_type)
    TextView txtRelaxType;
    @BindView(R.id.iv_sleep_type)
    ImageView ivSleepType;
    @BindView(R.id.txt_sleep_type)
    TextView txtSleepType;

    private static final String TAG = DeviceFragment.class.getSimpleName();
    public static final int PROPERTY_READ = 1;
    public static final int PROPERTY_WRITE = 2;
    public static final int PROPERTY_WRITE_NO_RESPONSE = 3;
    public static final int PROPERTY_NOTIFY = 4;
    public static final int PROPERTY_INDICATE = 5;
    ObjectAnimator animationR;
    DeviceViewModel mViewModel;
    ActionBar actionBar;
    LineChartManager lineChartManager;
    BleDevice bleDevice;
    BluetoothGattCharacteristic heartRateCharacteristic;
    ArrayList<Float> xValues;

    File cacheDir;
    String dataFileName;
    String zipFileName;
    long startTime;
    long endTime;
    boolean isResting;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device, container, false);
        ButterKnife.bind(this, v);

        initView();
        initData();
        initChart();
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
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (actionBar != null && !hidden) {
            actionBar.setTitle(R.string.title_home);
            setMusicAbout();
        }
        else{
           if(fragmentDevice!=null)
               fragmentDevice.dismiss();
           if(fragmentMusic!=null)
               fragmentMusic.dismiss();
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

    private void initView() {
        cacheDir = Objects.requireNonNull(getActivity()).getCacheDir();
        lineChartManager = new LineChartManager(lineChartSignal);
        timerOriginal.schedule(task, 100, 15000);//15s

        /* 设置旋转动画*/
//        animationR = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animationR.setDuration(3000);
//        animationR.setRepeatCount(RotateAnimation.INFINITE);
//        animationR.setInterpolator(new LinearInterpolator());

        animationR = ObjectAnimator.ofFloat(btnMusic, "rotation", 0f, 360.0f);
        animationR.setDuration(3000);
        animationR.setInterpolator(new LinearInterpolator());//不停顿
        animationR.setRepeatCount(-1);//设置动画重复次数
        animationR.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
        animationR.start();
    }

    private void initData() {
        bleDevice = ((MainActivity) getActivity()).getBleDevice();
        if (bleDevice == null) {
            lineChartSignal.setNoDataText("设备未连接");
            //设置设备名称
            btnEditDevice.setText(CommandUtils.getUIDeviceName(null));
            //设置电量
            ivBattery.setImageBitmap(CommandUtils.getUIDeviceBattery(getContext(),null));
            //设置信号
            ivSignal.setImageBitmap(CommandUtils.getUIDeviceSignal(getContext(),null));

            btnPlay.setEnabled(false);
            return;
        } else {
            lineChartSignal.setNoDataText("设备准备就绪");
            btnPlay.setEnabled(true);
        }

        send(CommandUtils.bt_dispair_cmd());
        send(CommandUtils.bt_enable_cmd());

        UsageRecord usageRecord = HbApplication.getInstance().usageRecord;
        usageRecord.setDeviceName(bleDevice.getName());
        usageRecord.setDeviceSN(bleDevice.getKey());
        usageRecord.setDeviceMac(bleDevice.getMac());
        usageRecord.setDeviceBattery(100);
        usageRecord.setDeviceSignal(Math.abs(100 - Math.abs(bleDevice.getRssi())));

        //设置设备名称
        btnEditDevice.setText(CommandUtils.getUIDeviceName(HbApplication.getInstance().usageRecord.getDeviceName()));
        //设置电量
        ivBattery.setImageBitmap(CommandUtils.getUIDeviceBattery(getContext(),HbApplication.getInstance().usageRecord.getDeviceBattery()));
        //设置信号
        ivSignal.setImageBitmap(CommandUtils.getUIDeviceSignal(getContext(),HbApplication.getInstance().usageRecord.getDeviceSignal()));

        setMusicAbout();

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
                case AppConstants.man_service_uuid://脑电波服务
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
                case AppConstants.device_info_service_uuid://设备信息服务
                    configDIS(service);
                    break;
                default:
                    break;
            }
        }
    }

    private void setMusicAbout() {
        if(HbApplication.getInstance().selectMusic!=null)
            txtRecordMusicName.setText(HbApplication.getInstance().selectMusic.getName());
        if(HbApplication.getInstance().selectSpeak!=null)
            txtRecordSpeakName.setText(HbApplication.getInstance().selectSpeak.getName());
        if(HbApplication.getInstance().selectDuration!=null)
            txtRecordDuration.setText(HbApplication.getInstance().selectDuration.getMinute() + "min");
    }








    private void configDIS(BluetoothGattService service) {
        List<BluetoothGattCharacteristic> characteristics_DIS = service.getCharacteristics();
        for (BluetoothGattCharacteristic characteristic : characteristics_DIS) {
            String uuid = characteristic.getUuid().toString();
            LogUtils.i("DISService:" + uuid);
            switch (uuid) {
                case AppConstants.device_info_serial_number://Serial Number String (d@后对应音频蓝牙地址)
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

                    private String hexString1080 = "";
                    private String hexString2002 = "";

                    @Override
                    public void onCharacteristicChanged(byte[] datas) {
                        //aa aa 10 80 07ff07ff064a04b9038b02adf8000691
                        //aa aa 20 02 64831802621616ffd9022d3e043fd60302980235490147181055ea0400050065
                        if (datas == null || datas.length <= 4)//非法长度字符
                            return;

                        if (isData1080(datas)) {
                            hexString1080 = ByteUtils.bytesToHex(datas);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //小包数据不上传
//                                    if (isResting) {
//                                        originalList.add(hexString1080);
//                                    }
                                    addLineData(datas);
                                    hexString2002 = "";
                                }
                            });
                        } else if (isData2002((datas))) {
                            hexString2002 = ByteUtils.bytesToHex(datas);
                        } else if (isData2002_end(datas)) {
                            if (hexString2002.length() == 40) {
                                hexString2002 += ByteUtils.bytesToHex(datas);
                                final SleepDataInfo dataInfo = OriginalDataUtil.parseSleepData(hexString2002);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //大包数据上传
                                        if (isResting) {
                                            originalList.add(hexString2002);
                                        }

                                        hexString2002 = "";

                                        //处理放松度
                                        handleRelaxData(dataInfo);
                                        //处理睡眠质量
                                        handleSleepData(dataInfo);
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void handleRelaxData(SleepDataInfo dataInfo) {
        RelaxData data = new RelaxData();
        data.setEndTime(System.currentTimeMillis());
        data.setUsageRecordId(HbApplication.getInstance().usageRecord.getId());
        data.setRelax(dataInfo.getRelax());
        data.setType(dataInfo.getRelaxType());
        data.setTypeName(dataInfo.getRelaxTypeName());
        relaxDataList.add(data);

        txtRelaxType.setText(data.getTypeName());
        switch (data.getType()){
            case 0:
                ivRelaxType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.relax_0));
                break;
            case 1:
                ivRelaxType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.relax_1));
                break;
            case 2:
                ivRelaxType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.relax_2));
                break;
            case 3:
                ivRelaxType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.relax_3));
                break;
            case 4:
                ivRelaxType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.relax_4));
                break;
        }
    }

    private void handleSleepData(SleepDataInfo dataInfo) {
        SleepData data = new SleepData();
        data.setEndTime(System.currentTimeMillis());
        data.setUsageRecordId(HbApplication.getInstance().usageRecord.getId());
        data.setType(dataInfo.getQuality());
        data.setTypeName(dataInfo.getQualityTypeName());
        int relaxSize = relaxDataList.size();
        if (relaxSize >= 200) {
            int startSum = 0;
            int endSum = 0;
            for (int i = 0; i < 100; i++) {
                RelaxData startItem = relaxDataList.get(i);
                RelaxData endItem = relaxDataList.get(relaxSize - 1 - i);
                startSum += startItem.getRelax();
                endSum += endItem.getRelax();
            }
            int vigor = Math.abs(startSum - endSum) / 100;
            data.setVigor(vigor);
        }
        sleepDataList.add(data);

        txtSleepType.setText(data.getTypeName());
        switch (data.getType()){
            case 0:
                ivSleepType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.sleep_0));
                break;
            case 1:
                ivSleepType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.sleep_1));
                break;
            case 2:
                ivSleepType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.sleep_2));
                break;
            case 3:
                ivSleepType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.sleep_3));
                break;
            case 4:
                ivSleepType.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.sleep_4));
                break;
        }
    }

    private boolean isData1080(byte[] datas) {
        //aaaa1080fe7cfb560009042502fbfcfbfb1b036a
        return datas.length == 20
                && (datas[0] & 0xFF) == 170
                && (datas[1] & 0xFF) == 170
                && (datas[2] & 0xFF) == 16
                && (datas[3] & 0xFF) == 128;
    }

    private boolean isData2002(byte[] datas) {
        //aaaa200264831807072a04843f0136d403312901
        return datas.length == 20
                && (datas[0] & 0xFF) == 170
                && (datas[1] & 0xFF) == 170
                && (datas[2] & 0xFF) == 32
                && (datas[3] & 0xFF) == 2;
    }

    private boolean isData2002_end(byte[] datas) {
        //d36605d58c00f4b203494a040005004e
        return datas.length == 16;
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
        //aaaa108007ff07ff064a04b9038b02adf8000691
        int maxValue = 100;
        int size = datas.length;

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


    private MediaPlayer mediaPlayer_music;

    private void playMusic() {
        try {
            if(!existResource(HbApplication.getInstance().selectMusic)){
                mediaPlayer_music = MediaPlayer.create(getActivity(), R.raw.right1);
            }
            else{
                mediaPlayer_music = MediaPlayer.create(getActivity(),Uri.parse(HbApplication.getInstance().selectMusic.getLocalFilePath()));
            }

            mediaPlayer_music.setLooping(true);
            mediaPlayer_music.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stopMusic(){
        if(mediaPlayer_music!=null)
            mediaPlayer_music.stop();
    }

    private MediaPlayer mediaPlayer_speak;
    private void playSpeak(){
        try {
            if(existResource(HbApplication.getInstance().selectSpeak)){
                mediaPlayer_speak = MediaPlayer.create(getActivity(),Uri.parse(HbApplication.getInstance().selectSpeak.getLocalFilePath()));
                mediaPlayer_speak.setLooping(true);
                mediaPlayer_speak.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stopSpeak(){
        if(mediaPlayer_speak!=null)
            mediaPlayer_speak.stop();
    }

    private boolean existResource(Resource resource){
        return resource!=null && !TextUtils.isEmpty(resource.getLocalFilePath())&& FileUtils.isFileExists(resource.getLocalFilePath());
    }


    private void startRest() {
        isResting = true;
        initUsageRecord();

        btnMenu.setVisibility(View.GONE);
        btnMenuStop.setVisibility(View.VISIBLE);
        layoutControl.setVisibility(View.GONE);
        layoutPlaying.setVisibility(View.VISIBLE);

        //播放音乐
        playMusic();
        playSpeak();

        //设置进度条最大长度为音频时长
        //seekMusic.setMax(mediaPlayer.getDuration());
        int minute = HbApplication.getInstance().selectDuration.getMinute();
        seekMusic.setMax(minute * 60);

        //线程开始运行
        startTimerTask();

        seekMusic.setEnabled(false);

        animationR.resume();
    }

    private void stopRest() {
        if (bleDevice != null) {
            //停止休息上传睡眠数据(包括记录和文件）
            updateNet();
        }

        //停止音乐
        stopMusic();
        stopSpeak();

        isResting = false;
        endTime = System.currentTimeMillis();
        originalList.clear();
        btnMenu.setVisibility(View.VISIBLE);
        btnMenuStop.setVisibility(View.GONE);
        layoutControl.setVisibility(View.VISIBLE);
        layoutPlaying.setVisibility(View.GONE);

        endTimerTask();

        animationR.pause();

        //记录使用记录
        HbApplication.getInstance().usageRecord.setEndTime(endTime);
        HbApplication.getDaoInstance().getUsageRecordDao().insertOrReplace(HbApplication.getInstance().usageRecord);
    }

    private void updateNet() {
        updateSleepInfo(bleDevice.getName(), bleDevice.getMac(), HbApplication.getInstance().loginUser.getUserName(), startTime, endTime);
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
                LogUtils.i("数据文件准备上传");
                FileUtils.delete(srcFile);//删除原始文件
                String base_url = URLUtil.url + URLUtil.fileData;
                OkGo.<String>post(base_url)
                        .tag(this)
                        .params("originalDataFile", zipFile)
                        .isMultipart(true)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                LogUtils.i("上传成功" + response.body());
                                FileUtils.delete(zipFile);
                            }

                            @Override
                            public void onError(Response<String> response) {
                                LogUtils.i("上传失败" + response.body());
                            }
                        });

            } else {
                LogUtils.i("数据文件丢失");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Todo:上传失败后补传
     */
    private void updateFail() {

    }

    private void initUsageRecord() {
        //记录使用记录
        HbApplication.getDaoInstance().getUsageRecordDao().insertOrReplace(HbApplication.getInstance().usageRecord);
        UsageRecord newRecord = new UsageRecord();
        newRecord.setUserId(HbApplication.getInstance().usageRecord.getUserId());
        newRecord.setUserCode(HbApplication.getInstance().usageRecord.getUserCode());
        newRecord.setUserName(HbApplication.getInstance().usageRecord.getUserName());
        newRecord.setMusicId(HbApplication.getInstance().usageRecord.getMusicId());
        newRecord.setSpeakId(HbApplication.getInstance().usageRecord.getSpeakId());
        newRecord.setRestDurationId(HbApplication.getInstance().usageRecord.getRestDurationId());
        newRecord.setDeviceSN(HbApplication.getInstance().usageRecord.getDeviceSN());
        newRecord.setDeviceMac(HbApplication.getInstance().usageRecord.getDeviceMac());
        newRecord.setDeviceName(HbApplication.getInstance().usageRecord.getDeviceName());
        newRecord.setDeviceBattery(HbApplication.getInstance().usageRecord.getDeviceBattery());
        newRecord.setDeviceSignal(HbApplication.getInstance().usageRecord.getDeviceSignal());
        HbApplication.getInstance().usageRecord = newRecord;

        startTime = System.currentTimeMillis();
        String fileName = HbApplication.getInstance().loginUser.getUserName() + "_" + System.currentTimeMillis();
        dataFileName = fileName + ".txt";
        zipFileName = fileName + ".zip";
        originalList.clear();

        relaxDataList.clear();
        sleepDataList.clear();

        HbApplication.getInstance().usageRecord.setStartTime(startTime);
    }

    private List<RelaxData> relaxDataList = new ArrayList<>();
    private List<SleepData> sleepDataList = new ArrayList<>();
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    EditDeviceDialogFragment fragmentDevice;
    SelectMusicDialogFragment fragmentMusic;
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
                fragmentDevice = EditDeviceDialogFragment.newInstance("");
                fragmentDevice.show(getFragmentManager(), "");
                break;
            case R.id.btn_menu:
                fragmentMusic = SelectMusicDialogFragment.newInstance("");
                fragmentMusic.show(getFragmentManager(), "");
                fragmentMusic.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        setMusicAbout();
                    }
                });
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
    public void onResume() {
        super.onResume();
        if (isResting) {
            animationR.resume();
        } else {
            animationR.pause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private int currentSecond;
    private Timer timerMusic = new Timer();
    private TimerTask timerTask = null;

    private void startTimerTask() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        currentSecond++;
                        if (currentSecond < seekMusic.getMax()) {
                            //设置进度条当前位置为音频播放位置
                            seekMusic.setProgress(currentSecond);
                        } else {
                            //休息完成
                            stopRest();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        currentSecond = -1;
        timerMusic.schedule(timerTask, 0, 1000);
    }

    private void endTimerTask() {
        if(timerTask!=null){
            timerTask.cancel();
            timerTask = null;
        }
    }


    private void send(byte[] hexCmd) {
        BleDevice bleDevice = ((MainActivity) getActivity()).getBleDevice();
        BluetoothGattCharacteristic characteristic = ((MainActivity) getActivity()).getCharacteristic();
        if (bleDevice == null || characteristic == null)
            return;

        BleManager.getInstance().write(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                hexCmd,
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String result = "current: " + current
                                        + " total: " + total
                                        + " justWrite: " + HexUtil.formatHexString(justWrite, true);

                                ToastUtils.showShort("onWriteSuccess: " + result);
                            }
                        });
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //addText(txt, exception.toString());
                                ToastUtils.showShort("onWriteFailure:" + exception.toString());
                            }
                        });
                    }
                });
    }


}
