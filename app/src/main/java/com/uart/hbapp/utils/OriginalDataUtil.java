package com.uart.hbapp.utils;

import com.blankj.utilcode.util.FileUtils;
import com.uart.hbapp.bean.SleepDataInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OriginalDataUtil {
    public static boolean writeListIntoCache(File cacheDir,String fileName, Object[] list) {
        File file = new File(cacheDir,fileName);
        if (!FileUtils.createOrExistsFile(file)) return false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            for (Object content:list){
                bw.write(content.toString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static SleepDataInfo parseSleepData(String data){
//		String data = "AAAA2002C8831818D48B13D1690258C1173BDC02500003CB9D036D3B037E8904000500D5";
        //             aaaa200264831802621616ffd9022d3e043fd60302980235490147181055ea0400050065
        //aaaa 20 02
        // 6483180452db120f1d014b5c03586104
        // a10a03f10401d7f60cdcd4040005008d
        SleepDataInfo sleepDataInfo = new SleepDataInfo();
        try {
            Long[] data_arr = new Long[36];
            int j = 0;
            for(int i = 0; i < data.length(); i = i+2){
                String element = data.substring(i,i+2);
                data_arr[j] = Long.parseLong(element,16);
                j++;
            }

            long delta = data_arr[10] << 16 |  data_arr[11] << 8 | data_arr[12];
            long theta = data_arr[13] << 16 |  data_arr[14] << 8 | data_arr[15];
            long lowAlpha = data_arr[16] << 16 |  data_arr[17] << 8 | data_arr[18];
            long highAlpha = data_arr[19] << 16 |  data_arr[20] << 8 | data_arr[21];
            long lowBeta = data_arr[22] << 16 |  data_arr[23] << 8 | data_arr[24];
            long highBeta = data_arr[25] << 16 |  data_arr[26] << 8 | data_arr[27];
            long lowGamma = data_arr[28] << 16 |  data_arr[29] << 8 | data_arr[30];
            long middleGamma = data_arr[31] << 16 |  data_arr[32] << 8 | data_arr[33];
//			long attention = data_arr[32]; //专注度
            long meditation = data_arr[34]; //放松度

            /*
             * 计算睡眠状态
             */
            if(lowAlpha > delta && lowAlpha > theta && lowAlpha > highAlpha && lowAlpha > lowBeta && lowAlpha > highBeta
                    && lowAlpha > lowGamma && lowAlpha > middleGamma) {
                sleepDataInfo.setQuality(2);//浅睡
            }else if(delta > lowAlpha || delta > highAlpha || theta > lowAlpha || lowAlpha > highAlpha) {
                sleepDataInfo.setQuality(1);//导眠
            }else{
                sleepDataInfo.setQuality(0);//清醒
            }

            sleepDataInfo.setRelax(Long.valueOf(meditation).intValue());

        } catch (Exception e) {
            //Date date = new Date();
            //log.error("睡眠数据处理错误:" + date.getTime());
            e.printStackTrace();
        }

        return sleepDataInfo;
    }

}
