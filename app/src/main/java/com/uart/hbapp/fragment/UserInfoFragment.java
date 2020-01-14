package com.uart.hbapp.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
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
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.MainActivity;
import com.uart.hbapp.R;
import com.uart.hbapp.adapter.CommonAdapter;
import com.uart.hbapp.adapter.CommonViewHolder;
import com.uart.hbapp.utils.CommandUtils;
import com.uart.hbapp.utils.DownLoadFileUtils;
import com.uart.hbapp.utils.DownloadCallback;
import com.uart.hbapp.utils.URLUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoFragment extends Fragment {

    @BindView(R.id.list_music)
    ListView listMusic;
    @BindView(R.id.list_music2)
    ListView listSpeak;


    private UserInfoViewModel mViewModel;
    private ActionBar actionBar;
    private String hbMusicDir;
    private String hbSpeakDir;
    private String hbBaseServerUrl;

    private List<Resource> musicList = new ArrayList<Resource>();
    private List<Resource> speakList = new ArrayList<Resource>();
    private CommonAdapter<Resource> musicAdapter, speakAdapter;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, v);

        hbMusicDir = DownLoadFileUtils.customLocalStoragePath("HbMusic");
        hbSpeakDir = DownLoadFileUtils.customLocalStoragePath("HbSpeak");
        hbBaseServerUrl = URLUtil.url;

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
        for (Resource res : resourceList) {
            if (res.getType() == 1)
                musicList.add(res);
            else if (res.getType() == 2)
                speakList.add(res);
        }

        musicAdapter = new CommonAdapter<Resource>(getActivity(), musicList, R.layout.adapter_music) {
            @Override
            protected void convertView(CommonViewHolder holder, Resource resource) {
                holder.setText(R.id.musicName, resource.getName())
                        .setText(R.id.musicTime, resource.getDurationStr())
                        .setText(R.id.musicState, resource.getStatus()==1?"已下载":"未下载");

                CheckBox cb = holder.getView(R.id.musicCheck);
                cb.setChecked(resource.isChecked);
                cb.setTag(resource);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Resource entity = (Resource) cb.getTag();
                        entity.isChecked = isChecked;
                    }
                });
            }
        };

        listMusic.setAdapter(musicAdapter);

        speakAdapter = new CommonAdapter<Resource>(getActivity(), speakList, R.layout.adapter_speak) {
            @Override
            protected void convertView(CommonViewHolder holder, Resource resource) {
                holder.setText(R.id.musicName, resource.getName())
                        .setText(R.id.musicSpeaker, resource.getSpeaker())
                        .setText(R.id.musicState, resource.getStatus()==1?"已下载":"未下载");

                CheckBox cb = holder.getView(R.id.musicCheck);
                cb.setChecked(resource.isChecked);
                cb.setTag(resource);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Resource entity = (Resource) cb.getTag();
                        entity.isChecked = isChecked;
                    }
                });
            }
        };

        listSpeak.setAdapter(speakAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        HbApplication.getDaoInstance().getResourceDao().deleteAll();

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
                .headers("Content-Type", "application/json")
                .upJson(jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int error = jsonObject.getInt("error");
                            if (error == 0) {
                                List<Resource> remoteList = new ArrayList<>();
                                JSONObject dataJSON = jsonObject.getJSONObject("data");
                                if (dataJSON != null) {
                                    JSONObject pagerJSON = dataJSON.getJSONObject("pager");
                                    if (pagerJSON != null) {
                                        int totalCount = pagerJSON.getInt("totalCount");
                                        if (totalCount > 0) {
                                            JSONArray resultList = pagerJSON.getJSONArray("resultList");
                                            if (resultList != null) {
                                                for (int i = 0; i < totalCount; i++) {
                                                    JSONObject item = resultList.getJSONObject(i);
                                                    if (item != null) {
                                                        Resource itemData = new Resource();
                                                        itemData.setName(item.getString("musicName"));
                                                        itemData.setType(item.getInt("type"));
                                                        itemData.setUrlPath(item.getString("musicUrl"));
                                                        itemData.setSpeaker(item.getString("voice"));
                                                        itemData.setExtension(DownLoadFileUtils.getExtensionName(itemData.getUrlPath()));
                                                        remoteList.add(itemData);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                setLocalData(remoteList, resourceType);
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

    private void setLocalData(List<Resource> remoteList, int resourceType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (resourceType == 1) {
                    List<String> musicNames = DownLoadFileUtils.getFileName(hbMusicDir);
                    for (Resource music : remoteList) {
                        if (musicNames.contains(music.getName()+music.getExtension())) {
                            music.setStatus(1);
                            music.setDuration(getDurationFromFile(music.getName()));
                            music.setDurationStr(getTimeFromMusic(music.getDuration()));
                        } else {
                            music.setStatus(0);
                            music.setDuration(0);
                            music.setDurationStr("0'00''");
                        }

                        insertOrUpdate(musicList, music);
                    }

                    musicAdapter.notifyDataSetChanged();
                } else if (resourceType == 2) {
                    List<String> speakNames = DownLoadFileUtils.getFileName(hbSpeakDir);
                    for (Resource speak : remoteList) {
                        if (speakNames.contains(speak.getName()+speak.getExtension())) {
                            speak.setStatus(1);
                            speak.setDuration(getDurationFromFile(speak.getName()));
                        } else {
                            speak.setStatus(0);
                            speak.setDuration(0);
                        }

                        insertOrUpdate(speakList, speak);
                    }

                    speakAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void insertOrUpdate(List<Resource> localList, Resource target) {
        boolean isExist = false;
        for (Resource resource : localList) {
            if (resource.getName().equals(target.getName())) {
                isExist = true;
                resource.setUrlPath(target.getUrlPath());
            }
        }

        if (isExist) {
            HbApplication.getDaoInstance().getResourceDao().update(target);
        } else {
            localList.add(target);
            HbApplication.getDaoInstance().getResourceDao().insertOrReplace(target);
        }
    }

    private String getTimeFromMusic(int musicDuration) {
        if (musicDuration % 60 < 10) {
            return musicDuration / 60 + "'0" + musicDuration % 60 + "''";
        } else {
            return musicDuration / 60 + "'" + musicDuration % 60 + "''";
        }
    }

    private int getDurationFromFile(String name) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        int time = 0;
        try {
            mediaPlayer.setDataSource(hbMusicDir + name);
            mediaPlayer.prepare();
            time = mediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return time / 1000;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (actionBar != null && !hidden) {
            actionBar.setTitle(R.string.title_notifications);
            setData(1);
            setData(2);
        } else {
            try {
                if (HbApplication.getInstance().selectMusic == null)
                    HbApplication.getInstance().selectMusic = musicList.get(0);
                if (HbApplication.getInstance().selectSpeak == null)
                    HbApplication.getInstance().selectSpeak = speakList.get(0);
            }catch (Exception e){
                ToastUtils.showShort("暂无音乐");
            }


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
                //send(CommandUtils.bt_disable_cmd());
                break;
            case R.id.bt_enable:
                //send(CommandUtils.bt_enable_cmd());
                break;
            case R.id.bt_dispair:
                //send(CommandUtils.bt_dispair_cmd());
                break;
        }

        return super.onOptionsItemSelected(item);
    }




    private void runOnUiThread(Runnable runnable) {
        if (isAdded() && getActivity() != null)
            getActivity().runOnUiThread(runnable);
    }


    @OnClick({R.id.btn_check_all_music, R.id.btn_download_music, R.id.btn_delete_music, R.id.btn_check_all_speak, R.id.btn_download_speak, R.id.btn_delete_speak})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_check_all_music:
                for (Resource resource:musicList)
                    resource.isChecked=true;

                musicAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_download_music:
                startDownloadMusic(getCheckResource(musicList));
                break;
            case R.id.btn_delete_music:
                startDeleteMusic(getCheckResource(musicList));
                break;
            case R.id.btn_check_all_speak:
                for (Resource resource:speakList)
                    resource.isChecked=true;

                speakAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_download_speak:
                startDownloadSpeak(getCheckResource(speakList));
                break;
            case R.id.btn_delete_speak:
                startDeleteSpeak(getCheckResource(speakList));
                break;
        }
    }

    private List<Resource> getCheckResource(List<Resource> resources){
        List<Resource> resourceList = new ArrayList<>();
        for (Resource resource : resources){
            if(resource.isChecked)
                resourceList.add(resource);
        }
        return  resourceList;
    }


    private Handler handler = new Handler();
    private void startDownloadMusic(List<Resource> musics){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(musics.size()>0){
                    Resource item = musics.get(0);
                    //http://127.0.0.1:8888/sleep/music/fileDownLoad?musicName=%E5%B7%A6%E5%A3%B0%E9%81%93&musicUrl=D:/data/muisc/%E5%B7%A6%E5%A3%B0%E9%81%93.mp3
                    String fileUrl = hbBaseServerUrl +URLUtil.musicFileDownload;
                    DownLoadFileUtils.httpDownloadFile(getContext(), fileUrl, hbMusicDir, item.getName(), item.getUrlPath(), new DownloadCallback() {
                       @Override
                       public void onSuccess(Response<File> response) {
                           item.setLocalFilePath(hbMusicDir+"/"+item.getName()+item.getExtension());
                           item.setStatus(1);
                           item.isChecked=false;
                           saveResource(item);
                       }
                       @Override
                       public void onFinish() {
                           super.onFinish();
                           musics.remove(item);
                           startDownloadMusic(musics);
                       }
                   });
                }
                else{
                    //处理完成
                    musicAdapter.notifyDataSetChanged();
                    ToastUtils.showShort("所有资源下载完成");
                }
            }
        },200);
    }

    private void startDeleteMusic(List<Resource> musics){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(musics.size()>0){
                    Resource item = musics.get(0);
                    if(!TextUtils.isEmpty(item.getLocalFilePath())){
                        if(FileUtils.delete(item.getLocalFilePath())){
                            item.setStatus(0);
                            item.isChecked=false;
                            saveResource(item);
                        }
                    }
                    musics.remove(item);
                    startDeleteMusic(musics);
                }
                else{
                    //处理完成
                    musicAdapter.notifyDataSetChanged();
                    ToastUtils.showShort("删除完成");
                }
            }
        },200);
    }
    private void startDownloadSpeak(List<Resource> speaks){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(speaks.size()>0){
                    Resource item = speaks.get(0);
                    //http://127.0.0.1:8888/sleep/music/fileDownLoad?musicName=%E5%B7%A6%E5%A3%B0%E9%81%93&musicUrl=D:/data/muisc/%E5%B7%A6%E5%A3%B0%E9%81%93.mp3
                    String fileUrl = hbBaseServerUrl +URLUtil.musicFileDownload;
                    DownLoadFileUtils.httpDownloadFile(getContext(), fileUrl, hbSpeakDir, item.getName(), item.getUrlPath(), new DownloadCallback() {
                        @Override
                        public void onSuccess(Response<File> response) {
                            item.setLocalFilePath(hbSpeakDir+"/"+item.getName()+item.getExtension());
                            item.setStatus(1);
                            item.isChecked=false;
                            saveResource(item);
                        }
                        @Override
                        public void onFinish() {
                            super.onFinish();
                            speaks.remove(item);
                            startDownloadSpeak(speaks);
                        }
                    });
                }
                else{
                    //处理完成
                    speakAdapter.notifyDataSetChanged();
                    ToastUtils.showShort("所有资源下载完成");
                }
            }
        },200);
    }
    private void startDeleteSpeak(List<Resource> speaks){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(speaks.size()>0){
                    Resource item = speaks.get(0);
                    if(!TextUtils.isEmpty(item.getLocalFilePath())){
                        if(FileUtils.delete(item.getLocalFilePath())){
                            item.setStatus(0);
                            item.isChecked=false;
                            saveResource(item);
                        }
                    }
                    speaks.remove(item);
                    startDeleteSpeak(speaks);
                }
                else{
                    //处理完成
                    speakAdapter.notifyDataSetChanged();
                    ToastUtils.showShort("删除完成");
                }
            }
        },200);
    }

    private void saveResource(Resource item){
        if(item.getId()!=null){
            HbApplication.getDaoInstance().getResourceDao().update(item);
        }
        else{
            HbApplication.getDaoInstance().getResourceDao().insertOrReplace(item);
        }
    }
}
