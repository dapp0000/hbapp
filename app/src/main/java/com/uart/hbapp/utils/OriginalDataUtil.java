package com.uart.hbapp.utils;

import com.blankj.utilcode.util.FileUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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

}
