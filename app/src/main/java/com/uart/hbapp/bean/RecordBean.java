package com.uart.hbapp.bean;

public class RecordBean {
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




//              "dataId": 24,
//              "vigor": 0,
//              "sleepTime": 1,
//              "sober_1": 0,
//              "sober_2": 1,
//              "sober_3": 0,
//              "sober_4": 0,
//              "sober_5": 0,
//              "relax_1": 1,
//              "relax_2": 0,
//              "relax_3": 0,
//              "relax_4": 0,
//              "relax_5": 0,
//              "logsId": 27,
//              "startTime": 1575625542050,
//              "endTime": 0

}
