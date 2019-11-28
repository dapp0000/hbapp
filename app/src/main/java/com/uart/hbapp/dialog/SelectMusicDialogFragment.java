package com.uart.hbapp.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.uart.entitylib.entity.Resource;
import com.uart.entitylib.entity.RestDuration;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.adapter.CommonAdapter;
import com.uart.hbapp.adapter.CommonViewHolder;
import com.uart.hbapp.utils.DownLoadFileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectMusicDialogFragment extends DialogFragment {
    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;
    @BindView(R.id.spinnerMusic)
    Spinner spinnerMusic;
    @BindView(R.id.spinnerStyle)
    Spinner spinnerStyle;
    @BindView(R.id.spinnerSpan)
    Spinner spinnerSpan;

    private static final String ARG_KEY = "key";
    public static SelectMusicDialogFragment newInstance(String key) {
        final SelectMusicDialogFragment fragment = new SelectMusicDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final View v = inflater.inflate(R.layout.dialog_select_music, null);
        ButterKnife.bind(this, v);

        spinnerInit();

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        win.setAttributes(params);
    }

    @OnClick({R.id.layout_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_title:
                getDialog().dismiss();
                break;
        }
    }


    private void spinnerInit() {
        List<RestDuration> restDurationList = HbApplication.getDaoInstance().getRestDurationDao().loadAll();
        CommonAdapter<RestDuration> spanAdapter = new CommonAdapter<RestDuration>(getActivity(), restDurationList,R.layout.adapter_spinner_dropdown_item) {
            @Override
            protected void convertView(CommonViewHolder holder, RestDuration restDuration) {
                holder.setText(R.id.text_name,restDuration.getName());
            }
        };
        spinnerSpan.setAdapter(spanAdapter);

        List<Resource> musicList = new ArrayList<Resource>();
        List<Resource> speakList = new ArrayList<Resource>();
        List<Resource> resourceList = HbApplication.getDaoInstance().getResourceDao().loadAll();
        for (Resource res : resourceList){
            if(res.getType()==0)
                musicList.add(res);
            else
                speakList.add(res);
        }

        CommonAdapter<Resource> musicAdapter = new CommonAdapter<Resource>(getActivity(),musicList,R.layout.adapter_spinner_dropdown_item) {
            @Override
            protected void convertView(CommonViewHolder holder, Resource resource) {
                holder.setText(R.id.text_name,resource.getName());

            }
        };
        spinnerMusic.setAdapter(musicAdapter);

        CommonAdapter<Resource> speakAdapter = new CommonAdapter<Resource>(getActivity(),speakList,R.layout.adapter_spinner_dropdown_item) {
            @Override
            protected void convertView(CommonViewHolder holder, Resource resource) {
                holder.setText(R.id.text_name,resource.getName());
            }
        };
        spinnerStyle.setAdapter(speakAdapter);
    }
}
