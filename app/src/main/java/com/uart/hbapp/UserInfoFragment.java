package com.uart.hbapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.uart.hbapp.utils.CommandUtils;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel mViewModel;
    private ActionBar actionBar;
    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_info_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null)
            actionBar.setTitle(R.string.title_notifications);
        // TODO: Use the ViewModel
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(actionBar!=null&&!hidden)
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
        switch (id){
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


    private void send(byte[] hexCmd){
        BleDevice bleDevice = ((MainActivity)getActivity()).getBleDevice();
        BluetoothGattCharacteristic characteristic= ((MainActivity)getActivity()).getCharacteristic();
        if(bleDevice==null || characteristic==null)
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

                                ToastUtils.showShort("onWriteSuccess: "+result);
                            }
                        });
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //addText(txt, exception.toString());
                                ToastUtils.showShort("onWriteFailure:"+exception.toString());
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
