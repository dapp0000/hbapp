package com.uart.hbapp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.uart.hbapp.adapter.MusicAdapter;
import com.uart.hbapp.bean.MusicBean;
import com.uart.hbapp.utils.CommandUtils;
import com.uart.hbapp.utils.DownLoadFileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class UserInfoFragment extends Fragment {

//    @BindView(R.id.list_music)
    ListView listMusic;
    @BindView(R.id.list_music2)
    ListView listText;


    private UserInfoViewModel mViewModel;
    private ActionBar actionBar;

    private MusicAdapter adapter;
    final List<MusicBean> musicList = new ArrayList<MusicBean>();
    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.user_info_fragment, container, false);
        listMusic=v.findViewById(R.id.list_music);

        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.title_notifications);
        // TODO: Use the ViewModel
        setData();
        adapter = new MusicAdapter(getActivity(), musicList);
        listMusic.setAdapter(adapter);
    }
    private void setData() {
        List<String> musicNames = DownLoadFileUtils.getFileName(DownLoadFileUtils.customLocalStoragePath("HbMusic"));
        for (int i=0;i<musicNames.size();i++){
            MusicBean bean=new MusicBean();
            bean.musicName=musicNames.get(i);
            bean.musicTime= getTimeFromMusic(musicNames.get(i));
            Log.e("shichang",getTimeFromMusic(musicNames.get(i)));
            bean.musicState="未下载";
            bean.musicCheck=false;
            musicList.add(bean);
        }
    }

    private String getTimeFromMusic(String name){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(DownLoadFileUtils.customLocalStoragePath("HbMusic")+name);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int time=mediaPlayer.getDuration();
        if (time / 1000 % 60 < 10) {
            String tt = time / 1000 / 60 + ":0" + time / 1000 % 60;
            return tt;
        } else {
            String tt = time / 1000 / 60 + ":" + time / 1000 % 60;
            return tt;
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (actionBar != null && !hidden)
            actionBar.setTitle(R.string.title_notifications);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
        inflater.inflate(R.menu.user_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.bt_disable:
                send(CommandUtils.bt_disable_cmd());
                break;
            case R.id.bt_enable:
                send(CommandUtils.bt_enable_cmd());
                break;
            case R.id.bt_dispair:
                send(CommandUtils.bt_dispair_cmd());
                break;
        }

        return super.onOptionsItemSelected(item);
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


    private void runOnUiThread(Runnable runnable) {
        if (isAdded() && getActivity() != null)
            getActivity().runOnUiThread(runnable);
    }


}
