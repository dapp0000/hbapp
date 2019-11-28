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

        setData();

        //adapter = new MusicAdapter(getActivity(), musicList);
        //listMusic.setAdapter(adapter);

        List<Resource> resourceList = HbApplication.getDaoInstance().getResourceDao().loadAll();
        for (Resource res : resourceList){
            if(res.getType()==0)
                musicList.add(res);
            else
                speakList.add(res);
        }

        CommonAdapter<Resource> musicAdapter = new CommonAdapter<Resource>(getActivity(),musicList,R.layout.adapter_music) {
            @Override
            protected void convertView(CommonViewHolder holder, Resource resource) {
                holder.setText(R.id.musicName,resource.getName())
                        .setText(R.id.musicTime,resource.getDuration()+"")
                        .setText(R.id.musicState,"未下载");

            }
        };
        listMusic.setAdapter(musicAdapter);

        CommonAdapter<Resource> speakAdapter = new CommonAdapter<Resource>(getActivity(),speakList,R.layout.adapter_speak) {
            @Override
            protected void convertView(CommonViewHolder holder, Resource resource) {
                holder.setText(R.id.musicName,resource.getName())
                        .setText(R.id.musicSpeaker,resource.getSpeaker())
                        .setText(R.id.musicState,"未下载");
            }
        };
        listSpeak.setAdapter(speakAdapter);
    }

    private void setData() {
        //todo:musicListQuery接口需要添加音乐类型和播音员字段
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", HbApplication.getInstance().loginUser.getToken());
        params.put("pageNo", 1);
        params.put("pageRows", 100);
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
                                setLocalData();
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

     //初始数据
      List<RestDuration> restDurationList = HbApplication.getDaoInstance().getRestDurationDao().loadAll();
      if(restDurationList==null||restDurationList.size()==0){
          RestDuration r10 = new RestDuration();
          r10.setName("10分钟体验");
          r10.setMinute(10);

          RestDuration r20 = new RestDuration();
          r20.setName("20分钟小憩");
          r20.setMinute(20);

          RestDuration r30 = new RestDuration();
          r30.setName("30分钟精力恢复");
          r30.setMinute(30);

          RestDuration r60 = new RestDuration();
          r60.setName("1小时小睡");
          r60.setMinute(60);

          RestDuration r120 = new RestDuration();
          r120.setName("2小时原地复活");
          r120.setMinute(120);

          HbApplication.getDaoInstance().getRestDurationDao().insertOrReplace(r10);
          HbApplication.getDaoInstance().getRestDurationDao().insertOrReplace(r20);
          HbApplication.getDaoInstance().getRestDurationDao().insertOrReplace(r30);
          HbApplication.getDaoInstance().getRestDurationDao().insertOrReplace(r60);
          HbApplication.getDaoInstance().getRestDurationDao().insertOrReplace(r120);
      }

      List<Resource> resourceList = HbApplication.getDaoInstance().getResourceDao().loadAll();
      if(resourceList==null||resourceList.size()==0){
          Resource res1 = new Resource();
          res1.setType(0);
          res1.setName("高山流水");
          res1.setDuration(200);
          res1.setStatus(1);
          res1.setUrlPath("");
          res1.setLocalFilePath("");

          Resource res2 = new Resource();
          res2.setType(0);
          res2.setName("四脚朝天");
          res2.setDuration(200);
          res2.setStatus(1);
          res2.setUrlPath("");
          res2.setLocalFilePath("");

          Resource res3 = new Resource();
          res3.setType(1);
          res3.setName("温柔语音");
          res3.setSpeaker("林志玲");
          res3.setDuration(200);
          res3.setStatus(1);
          res3.setUrlPath("");
          res3.setLocalFilePath("");

          Resource res4 = new Resource();
          res4.setType(1);
          res4.setName("相声风格");
          res4.setSpeaker("郭德纲");
          res4.setDuration(200);
          res4.setStatus(1);
          res4.setUrlPath("");
          res4.setLocalFilePath("");

          HbApplication.getDaoInstance().getResourceDao().insertOrReplace(res1);
          HbApplication.getDaoInstance().getResourceDao().insertOrReplace(res2);
          HbApplication.getDaoInstance().getResourceDao().insertOrReplace(res3);
          HbApplication.getDaoInstance().getResourceDao().insertOrReplace(res4);
      }
    }

    private void setLocalData(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<String> musicNames = DownLoadFileUtils.getFileName(DownLoadFileUtils.customLocalStoragePath("HbMusic"));
                for (int i=0;i<musicNames.size();i++){
                    //todo:如果有本地则已下载，如没有本地显示未下载
//                    MusicBean bean=new MusicBean();
//                    bean.musicName=musicNames.get(i);
//                    bean.musicTime= getTimeFromMusic(musicNames.get(i));
//                    Log.e("shichang",getTimeFromMusic(musicNames.get(i)));
//                    bean.musicState="未下载";
//                    bean.musicCheck=false;
//                    musicList.add(bean);
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


//    {
//        "error": 0,
//            "message": "successful",
//            "data": {
//        "pager": {
//            "f": {},
//            "pageFlag": "pageFlag",
//                    "resultList": [
//            {
//                "musicId": 1,
//                    "musicName": "left1",
//                    "musicUrl": "D:/data/muisc/left1.mp3"
//            },
//            {
//                "musicId": 6,
//                    "musicName": "right2",
//                    "musicUrl": "D:/data/muisc/right2.mp3"
//            }
//            ],
//            "pageNo": 1,
//                    "pageRows": 100,
//                    "totalCount": 2,
//                    "totalPages": 1,
//                    "first": 1,
//                    "last": 2,
//                    "hasNext": false,
//                    "nextPage": 1,
//                    "hasPre": false,
//                    "prePage": 1,
//                    "beginCount": 0
//        }
//    }
//    }
}
