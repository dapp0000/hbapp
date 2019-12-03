package com.uart.hbapp.bean;

public class SleepDataInfo {
    private int relax;

    /*
     * 	0-30：精神紧张
        31-60：轻度放松
        61-80：中度放松
        81-100：深度放松

     */
    private int quality;//0-清醒，1-导眠 ，2-浅睡


    public int getRelax() {
        return relax;
    }

    public void setRelax(int relax) {
        this.relax = relax;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

}
