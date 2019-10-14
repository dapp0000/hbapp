package com.uart.hbapp;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
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
import com.uart.hbapp.utils.ByteUtils;
import com.uart.hbapp.utils.DownLoadFileUtils;
import com.uart.hbapp.utils.view.LineChart.LineChartManager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;

public class DeviceFragment extends Fragment {
    public static final int PROPERTY_READ = 1;
    public static final int PROPERTY_WRITE = 2;
    public static final int PROPERTY_WRITE_NO_RESPONSE = 3;
    public static final int PROPERTY_NOTIFY = 4;
    public static final int PROPERTY_INDICATE = 5;
    private static final String TAG = DeviceFragment.class.getSimpleName();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String musicPath;
    @BindView(R.id.txt_device_name)
    TextView txtDeviceName;
    @BindView(R.id.line_chart_signal)
    LineChart lineChartSignal;
//    @BindView(R.id.spinner_music)
    Spinner spinnerMusic;
    @BindView(R.id.spinner_text)
    Spinner spinnerText;
    @BindView(R.id.spinner_span)
    Spinner spinnerSpan;
    @BindView(R.id.btn_start_rest)
    Button btnStartRest;
    @BindView(R.id.layout_ready)
    LinearLayout layoutReady;
    @BindView(R.id.btn_stop_rest)
    Button btnStopRest;
    @BindView(R.id.layout_rest)
    LinearLayout layoutRest;


    private DeviceViewModel mViewModel;
    private ActionBar actionBar;

    public static DeviceFragment newInstance() {
        return new DeviceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.device_fragment, container, false);
        initView(v);
        showData();

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
        if (actionBar != null && !hidden)
            actionBar.setTitle(R.string.title_home);
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

        return super.onOptionsItemSelected(item);
    }


    TextView txt_device_name;
    LineChart line_chart_signal;
    Button btn_start_rest, btn_stop_rest;
    LineChartManager lineChartManager;
    LinearLayout layout_ready, layout_rest;

    private void initView(View v) {
        layout_ready = v.findViewById(R.id.layout_ready);
        layout_rest = v.findViewById(R.id.layout_rest);
        layout_ready.setVisibility(View.VISIBLE);
        layout_rest.setVisibility(View.GONE);

        txt_device_name = v.findViewById(R.id.txt_device_name);
        line_chart_signal = v.findViewById(R.id.line_chart_signal);
        lineChartManager = new LineChartManager(line_chart_signal);
        btn_start_rest = v.findViewById(R.id.btn_start_rest);
        btn_start_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_ready.setVisibility(View.GONE);
                layout_rest.setVisibility(View.VISIBLE);
                initChart();
                openNotify(bleDevice, heartRateCharacteristic);
                initMediaPlayer();
            }
        });
        btn_stop_rest = v.findViewById(R.id.btn_stop_rest);
        btn_stop_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_ready.setVisibility(View.VISIBLE);
                layout_rest.setVisibility(View.GONE);
                closeNotify(bleDevice, heartRateCharacteristic);
                mediaPlayer.reset();
            }
        });
        spinnerMusic=v.findViewById(R.id.spinner_music);
        spinnerInit();
    }


    BleDevice bleDevice;
    List<BluetoothGattService> services;
    List<BluetoothGattCharacteristic> characteristics;
    BluetoothGattCharacteristic heartRateCharacteristic;

    private void showData() {

        bleDevice = ((MainActivity) getActivity()).getBleDevice();
        if (bleDevice == null) {
            line_chart_signal.setNoDataText("设备未连接");
            btn_start_rest.setEnabled(false);
            return;
        } else {
            line_chart_signal.setNoDataText("设备准备就绪");
            btn_start_rest.setEnabled(true);
        }

        txt_device_name.setText(bleDevice.getName());

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
        services = gatt.getServices();
        for (BluetoothGattService service : services) {
            String uuid = service.getUuid().toString();
            LogUtils.i("BluetoothGattService:" + uuid);
            switch (uuid) {
                case "6e400001-b5a3-f393-e0a9-e50e24dcca9e"://脑电波服务
                    ((MainActivity) getActivity()).setBluetoothGattService(service);
                    characteristics = service.getCharacteristics();
                    heartRateCharacteristic = characteristics.get(0);
                    int charaProp = heartRateCharacteristic.getProperties();
                    if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                        ((MainActivity) getActivity()).setCharacteristic(characteristics.get(1));
                        ((MainActivity) getActivity()).setCharaProp(DeviceFragment.PROPERTY_WRITE);
                    }
                    break;
                case "0000180a-0000-1000-8000-00805f9b34fb"://设备信息服务
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
                case "00002a25-0000-1000-8000-00805f9b34fb"://Serial Number String (d@后对应音频蓝牙地址)
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
        BleManager.getInstance().stopNotify(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString());
    }


    private void runOnUiThread(Runnable runnable) {
        if (isAdded() && getActivity() != null)
            getActivity().runOnUiThread(runnable);
    }

    private void addText(TextView textView, String content) {
        textView.append(content);
        textView.append("\n");
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());
        }
    }

    private void addLineData(byte[] datas) {
        showChart(datas);
    }

    ArrayList<Float> xValues;
    List<String> names;
    List<Integer> colours;

    private void initChart() {
        XAxis xAxis = line_chart_signal.getXAxis();
        YAxis yAxisLeft = line_chart_signal.getAxisLeft();
        YAxis yAxisRight = line_chart_signal.getAxisRight();
        xAxis.setTextColor(Color.WHITE);
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisRight.setTextColor(Color.WHITE);

        //设置x轴的数据
        xValues = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            List<Float> yValue = new ArrayList<>();
            for (int j = 0; j <= 7; j++) {
                yValue.add((float) (Math.random() * 80));
            }
            yValues.add(yValue);
        }
        //颜色集合
        colours = new ArrayList<>();
        colours.add(Color.GREEN);
        //线的名字集合
        names = new ArrayList<>();
        names.add("信号");

        lineChartManager.showLineChart(xValues, yValues, names, colours);
        lineChartManager.setYAxis(100, 0, 6);
        lineChartManager.setDescription("脑电波");

        LineData lineData = line_chart_signal.getLineData();
        lineData.setValueTextColor(Color.GREEN);
        Legend legend = line_chart_signal.getLegend();
        legend.setTextColor(Color.WHITE);
        Description description = line_chart_signal.getDescription();
        description.setTextColor(Color.WHITE);
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

        if (!isData) {
            LogUtils.e(ByteUtils.bytesToHex(datas));
            //LogUtils.e(ByteUtils.bytesToUTF8(datas));
            return;
        }

        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            List<Float> yValue = new ArrayList<>();

            for (int j = 4; j < size - 1; j = j + 2) {
                int hign = datas[j] & 0xFF;
                int low = datas[j + 1] & 0xFF;
                float point = (hign * 256 + low) / 1000f;
                if (point > maxValue)
                    maxValue = (int) point + 100;


                yValue.add(point);
            }

            yValues.add(yValue);
        }

        lineChartManager.showLineChart(xValues, yValues, names, colours);
        lineChartManager.setYAxis(maxValue, 0, 6);
        LineData lineData = line_chart_signal.getLineData();
        lineData.setValueTextColor(Color.GREEN);
    }

    /**
     * 加载数据列，监听选择
     */
    private void spinnerInit() {
        Vector<String> musicNames = DownLoadFileUtils.getFileName(DownLoadFileUtils.customLocalStoragePath("HbMusic"));
        //原始string数组
        final String[] spi = new String[musicNames.size()];
        final String[] spinnerItems = musicNames.toArray(spi);

        //简单的string数组适配器：样式res，数组
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerItems);
        //下拉的样式res
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinnerMusic.setAdapter(spinnerAdapter);
        //选择监听
        spinnerMusic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //parent就是父控件spinner
            //view就是spinner内填充的textview,id=@android:id/text1
            //position是值所在数组的位置
            //id是值所在行的位置，一般来说与positin一致
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                ((TextView) view).setGravity(Gravity.CENTER);
                musicPath = Environment.getExternalStorageDirectory() + "/HbMusic/" + spinnerItems[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void initMediaPlayer() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
