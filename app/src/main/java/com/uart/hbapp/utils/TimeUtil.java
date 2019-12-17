package com.uart.hbapp.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat format_date =  new SimpleDateFormat("yyyy年MM月dd日");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat format_time =  new SimpleDateFormat("HH:mm");
    public static String getTimeString(long timestamp){
        return format_time.format(timestamp);
    }
    public static String getDateString(long timestamp){
        return format_date.format(timestamp);
    }


    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat dateFormatter =  new SimpleDateFormat("yyyy年MM月dd日");
    public static int getDayNumInYear(String dateString){
        int num = 0;
        try {
            Date date = dateFormatter.parse(dateString);
            dateFormatter.applyPattern("D");
            num = Integer.parseInt(dateFormatter.format(date));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return num;
    }

    public static int getDayNumInMonth(String dateString){
        int num = 0;
        try {
            Date date = dateFormatter.parse(dateString);
            dateFormatter.applyPattern("d");
            num = Integer.parseInt(dateFormatter.format(date));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return num;
    }

    public static int getWeekNumInYear(String dateString){
        int num = 0;
        try {
            Date date = dateFormatter.parse(dateString);
            dateFormatter.applyPattern("W");
            num = Integer.parseInt(dateFormatter.format(date));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return num;
    }

    public static int getWeekNumInMonth(String dateString){
        int num = 0;
        try {
            Date date = dateFormatter.parse(dateString);
            dateFormatter.applyPattern("w");
            num = Integer.parseInt(dateFormatter.format(date));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return num;
    }

    public static int getDayNumInWeek(String dateString){
        int num = 0;
        try {
            Date date = dateFormatter.parse(dateString);
            dateFormatter.applyPattern("E");
            num = Integer.parseInt(dateFormatter.format(date));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return num;
    }


    public static int getAge(String birthday) throws Exception {
        Date birthDay=parse(birthday);
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一

            }
        }
        return age;
    }
    private  static Date parse(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(strDate);
    }
}
