package com.uart.hbapp.bean;

public class SleepDataInfo {
    /**
     * 0-30：精神紧张
     * 31-60：轻度放松
     * 61-80：中度放松
     * 81-100：深度放松
     * 当深度放松持续10s时间：持续放松状态
     */
    private int relax;

    /**
     * 清醒状态：其余
     *
     * 导眠状态：当Delta或Theta大于LowAlpha或HighAlpha时
     *
     * 浅睡状态：当LowAlpha的值大于Delta，Theta，HighAlpha，LowBeta，HighBeta，LowGamma，MiddleGamma时，
     *
     * 当浅睡状态持续10min时间：深度睡眠状态
     *
     * 当浅睡状态持续30min时间：持续深睡状态
     */
    private int quality;//0-清醒，1-导眠 ，2-浅睡，3-深度睡眠，4-持续深度睡眠


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


    public int getRelaxType(){
        if(0<=relax&&relax<=30)
            return 0;
        else if(31<=relax&&relax<=60)
            return 1;
        else if(61<=relax&&relax<=80)
            return 2;
        else
            return 3;
    }

    public String getRelaxTypeName(){
        String name="";
        switch (getRelaxType()){
            case 0:
                name="精神紧张";
                break;
            case 1:
                name="轻度放松";
                break;
            case 2:
                name="中度放松";
                break;
            case 3:
                name="深度放松";
                break;
        }
        return name;
    }

    public String getQualityTypeName(){
        String name="";
        switch (getQuality()){
            case 0:
                name="清醒";
                break;
            case 1:
                name="导眠";
                break;
            case 2:
                name="浅睡";
                break;
            case 3:
                name="深度睡眠";
                break;
        }
        return name;
    }

}
