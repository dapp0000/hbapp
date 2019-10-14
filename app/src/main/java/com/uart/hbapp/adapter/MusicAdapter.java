package com.uart.hbapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.uart.hbapp.R;
import com.uart.hbapp.bean.MusicBean;
import com.uart.hbapp.utils.DownLoadFileUtils;

import java.util.List;
import java.util.Vector;

/**
 * Created by admin on 2017/5/2.
 */

public class MusicAdapter extends BaseAdapter {
    private List<MusicBean> listData;
    private Context context;

    public MusicAdapter(Context context, List<MusicBean> listData) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.listData.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView = null;

        if (convertView == null) {

            holdView = new HoldView();

            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_music, null);

            holdView.musicName = (TextView) convertView.findViewById(R.id.musicName);
            holdView.musicTime = (TextView) convertView.findViewById(R.id.musicTime);
            holdView.musicState = (TextView) convertView.findViewById(R.id.musicState);
            holdView.musicCheck = (CheckBox) convertView.findViewById(R.id.musicCheck);

            convertView.setTag(holdView);

        } else {
            holdView = (HoldView) convertView.getTag();

        }

        final MusicBean sj = this.listData.get(position);
        Vector<String> musicNames = DownLoadFileUtils.getFileName(DownLoadFileUtils.customLocalStoragePath("HbMusic"));
        holdView.musicState.setText(sj.getMusicState());
        holdView.musicCheck.setChecked(sj.isMusicCheck());
        for (int i = 0; i < musicNames.size(); i++) {
            if (sj.getMusicName().equals(musicNames.get(i))) {
                holdView.musicState.setText("已下载");
            }
        }

        holdView.musicName.setText(sj.getMusicName());
        holdView.musicTime.setText(sj.getMusicTime());
        holdView.musicCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sj.isMusicCheck()){
                    sj.setMusicCheck(false);
                }else {
                    sj.setMusicCheck(true);
                }
            }
        });

        return convertView;
    }

    class HoldView {
        TextView musicName, musicTime, musicState;
        CheckBox musicCheck;
    }
}
