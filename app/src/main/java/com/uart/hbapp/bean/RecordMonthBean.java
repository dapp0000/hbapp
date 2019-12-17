package com.uart.hbapp.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecordMonthBean implements Serializable {
    public long thisTime;
    public long thisSleepTime;
    public long thisRelax;
    public int thisVigor;
    public int thisNum;

    public List<RecordBean> list=new ArrayList<>();


}
