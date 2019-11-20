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

import com.uart.hbapp.R;
import com.uart.hbapp.utils.DownLoadFileUtils;

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
//        Vector<String> musicNames = DownLoadFileUtils.getFileName(DownLoadFileUtils.customLocalStoragePath("HbMusic"));
//        //原始string数组
//        final String[] spi = new String[musicNames.size()];
//        final String[] spinnerItems = musicNames.toArray(spi);
//
//        ArrayAdapter<String> musicAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, spinnerItems);
//        musicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerMusic.setAdapter(musicAdapter);
//
//        String[] spinnerStrList = new String[]{"你好我好大家好", "晚上好我的兄弟"};
//        ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, spinnerStrList);
//        spinnerText.setAdapter(wordAdapter);
//
//
//        //选择监听
//        spinnerMusic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            //parent就是父控件spinner
//            //view就是spinner内填充的textview,id=@android:id/text1
//            //position是值所在数组的位置
//            //id是值所在行的位置，一般来说与positin一致
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
//                //((TextView) view).setGravity(Gravity.CENTER);
//                //musicPath = Environment.getExternalStorageDirectory() + "/HbMusic/" + spinnerItems[pos];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Another interface callback
//            }
//        });
//
//        String[] spinnerSpanList = new String[]{"自然醒", "10分钟", "20分钟", "30分钟", "1小时", "2小时"};
//        ArrayAdapter<String> spanAdapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, spinnerSpanList);
//        //下拉的样式res
//        spanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //绑定 Adapter到控件
//        spinnerSpan.setAdapter(spanAdapter);
    }
}
