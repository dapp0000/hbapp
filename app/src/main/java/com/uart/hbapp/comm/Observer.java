package com.uart.hbapp.comm;


import com.clj.fastble.data.BleDevice;

public interface Observer {

    void disConnected(BleDevice bleDevice);
}
