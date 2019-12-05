package com.uart.hbapp.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
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

import com.blankj.utilcode.util.ToastUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.entitylib.entity.Resource;
import com.uart.entitylib.entity.RestDuration;
import com.uart.entitylib.entity.UserInfo;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.MainActivity;
import com.uart.hbapp.R;
import com.uart.hbapp.adapter.CommonAdapter;
import com.uart.hbapp.adapter.CommonViewHolder;
import com.uart.hbapp.adapter.MusicAdapter;
import com.uart.hbapp.bean.MusicBean;
import com.uart.hbapp.login.AdditionalActivity;
import com.uart.hbapp.login.LoginUserPwdActivity;
import com.uart.hbapp.utils.CommandUtils;
import com.uart.hbapp.utils.DialogUtils;
import com.uart.hbapp.utils.DownLoadFileUtils;
import com.uart.hbapp.utils.URLUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoFragment extends Fragment {

    @BindView(R.id.list_music)
    ListView listMusic;
    @BindView(R.id.list_music2)
    ListView listSpeak;


    private UserInfoViewModel mViewModel;
    private ActionBar actionBar;

    //private MusicAdapter adapter;
    //final List<MusicBean> musicList = new ArrayList<MusicBean>();

    List<Resource> musicList = new ArrayList<Resource>();
    List<Resource> speakList = new ArrayList<Resource>();
    CommonAdapter<Resource> musicAdapter,speakAdapter;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, v);

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

        List<Resource> resourceList = HbApplication.getDaoInstance().getResourceDao().loadAll();
        for (Resource res : resourceList){
            if(res.getType()==0)
                musicList.add(res);
            else
                speakList.add(res);
        }

        musicAdapter = new CommonAdapter<Resource>(getActivity(),musicList,R.layout.adapter_music) {
            @Override
            protected void convertView(CommonViewHolder holder, Resource resource) {
                holder.setText(R.id.musicName,resource.getName())
                        .setText(R.id.musicTime,resource.getDuration()+"")
                        .setText(R.id.musicState,"未下载");

            }
        };

        listMusic.setAdapter(musicAdapter);

        speakAdapter = new CommonAdapter<Resource>(getActivity(),speakList,R.layout.adapter_speak) {
            @Override
            protected void convertView(CommonViewHolder holder, Resource resource) {
                holder.setText(R.id.musicName,resource.getName())
                        .setText(R.id.musicSpeaker,resource.getSpeaker())
                        .setText(R.id.musicState,"未下载");
            }
        };

        listSpeak.setAdapter(speakAdapter);

        setData(1);
        setData(2);
    }

    private void setData(int resourceType) {
        //todo:musicListQuery接口需要添加音乐类型和播音员字段
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", HbApplication.getInstance().loginUser.getToken());
        params.put("pageNo", 1);
        params.put("pageRows", 100);
        params.put("type", resourceType);
        JSONObject jsonObject = new JSONObject(params);
        String base_url = URLUtil.url + URLUtil.musicListQuery;
        OkGo.<String>post(base_url)
                .tag(this)
                .cacheKey("cachePostKey")
                .headers("Content-Type","application/json")
                .upJson(jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int error = jsonObject.getInt("error");
                            if (error == 0) {
                                List<Resource> remoteList = new ArrayList<>();
                                JSONObject dataJSON = jsonObject.getJSONObject("data");
                                if(dataJSON!=null){
                                    JSONObject pagerJSON = dataJSON.getJSONObject("pager");
                                    if(pagerJSON!=null){
                                        int totalCount = pagerJSON.getInt("totalCount");
                                        if(totalCount>0){
                                           JSONArray resultList = pagerJSON.getJSONArray("resultList");
                                           if(resultList!=null){
                                              for (int i=0;i<totalCount;i++){
                                                 JSONObject item = resultList.getJSONObject(i);
                                                 if(item!=null){
                                                     Resource itemData = new Resource();
                                                     itemData.setName(item.getString("musicName"));
                                                     itemData.setType(item.getInt("type"));
                                                     itemData.setUrlPath(item.getString("musicUrl"));
                                                     remoteList.add(itemData);
                                                 }
                                              }
                                           }
                                        }
                                    }
                                }
                                setLocalData(remoteList,resourceType);
                            } else {
                                ToastUtils.showShort(jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showShort("服务器异常");
                    }
                });

    }

    private void setLocalData(List<Resource> remoteList,int resourceType){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(resourceType==1){
                    musicList.clear();
                    List<String> musicNames = DownLoadFileUtils.getFileName(DownLoadFileUtils.customLocalStoragePath("HbMusic"));
                    for (Resource music : remoteList){
                        if(musicNames.contains(music.getName())){
                            music.setStatus(1);
                            music.setLocalFilePath(music.getName());
                            music.setDuration(getDurationFromFile(music.getName()));
                        }
                        else{
                            music.setStatus(0);
                            music.setDuration(0);
                        }

                        musicList.add(music);
                        HbApplication.getDaoInstance().getResourceDao().insertOrReplace(music);
                    }
                    musicAdapter.notifyDataSetChanged();

                }
                else if(resourceType==2){
                    speakList.clear();
                    List<String> speakNames = DownLoadFileUtils.getFileName(DownLoadFileUtils.customLocalStoragePath("HbSpeak"));
                    for (Resource speak : remoteList){
                        if(speakNames.contains(speak.getName())){
                            speak.setStatus(1);
                            speak.setLocalFilePath(speak.getName());
                            speak.setDuration(getDurationFromFile(speak.getName()));
                        }
                        else{
                            speak.setStatus(0);
                            speak.setDuration(0);
                        }

                        speakList.add(speak);
                        HbApplication.getDaoInstance().getResourceDao().insertOrReplace(speak);
                    }

                    speakAdapter.notifyDataSetChanged();
                }
            }
        });
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

    private int getDurationFromFile(String name){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(DownLoadFileUtils.customLocalStoragePath("HbMusic")+name);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int time=mediaPlayer.getDuration();

        return time/1000;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (actionBar != null && !hidden) {
            actionBar.setTitle(R.string.title_notifications);
            setData(1);
            setData(2);
        }
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
