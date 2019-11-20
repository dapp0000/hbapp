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

import com.uart.hbapp.R;
import com.uart.hbapp.login.AdditionalActivity;

public class SettingFragment extends Fragment {

    SettingViewModel mViewModel;
    ActionBar actionBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.setting_fragment, container, false);
        v.findViewById(R.id.changeMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AdditionalActivity.class);
                intent.putExtra("AdditionalActivity","changeMessage");
                startActivity(intent);
            }
        });
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
