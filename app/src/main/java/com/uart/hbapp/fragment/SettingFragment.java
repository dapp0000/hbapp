package com.uart.hbapp.fragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.login.AdditionalActivity;
import com.uart.hbapp.login.LoginUserPwdActivity;

public class SettingFragment extends Fragment {

    SettingViewModel mViewModel;
    ActionBar actionBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_setting, container, false);
        v.findViewById(R.id.headImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AdditionalActivity.class);
                intent.putExtra("AdditionalActivity","changeMessage");
                startActivity(intent);
            }
        });
        v.findViewById(R.id.nickName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AdditionalActivity.class);
                intent.putExtra("AdditionalActivity","changeMessage");
                startActivity(intent);
            }
        });
        v.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginUserPwdActivity.class));
                getActivity().finish();
            }
        });
        TextView textView=v.findViewById(R.id.nickName);
        textView.setText(HbApplication.getInstance().loginUser.getUserName());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        // TODO: Use the ViewModel
        setHasOptionsMenu(true);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null)
            actionBar.setTitle(R.string.title_settings);
    }

}
