package com.uart.hbapp.bean;


import java.io.Serializable;

public class RecordBean implements Serializable {
    public RecordBean(){}
    public RecordBean(boolean week,boolean month){
        this.isweek = week;
        this.ismonth = month;
    }

    public boolean isweek;
    public boolean ismonth;
    public long startTime;
    public long endTime;
    public int dataId;
    public int logsId;
    public int vigor;
    public int sleepTime;
    public int sober_1;
    public int sober_2;
    public int sober_3;
    public int sober_4;
    public int sober_5;
    public int relax_1;
    public int relax_2;
    public int relax_3;
    public int relax_4;
    public int relax_5;

    public String startTimeString;
    public String startDateString;
    public int weekNum;
    public int monthNum;

}
