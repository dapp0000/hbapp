package com.uart.hbapp.bean;

public class RecordBean {
    public RecordBean(){}
    public RecordBean(boolean week,boolean month){
        this.isweek = week;
        this.ismonth = month;
    }
    public long starttime;
    public int restspan;
    public int battery;
    public boolean isweek;
    public boolean ismonth;
}
