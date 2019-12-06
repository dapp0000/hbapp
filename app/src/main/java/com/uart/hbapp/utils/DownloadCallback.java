package com.uart.hbapp.utils;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.FileConvert;

import java.io.File;

import okhttp3.Response;

public abstract class DownloadCallback extends AbsCallback<File> {

    private FileConvert convert;    //文件转换类
    public void setFileConvert(String destFileDir, String destFileName){
        convert = new FileConvert(destFileDir, destFileName);
        convert.setCallback(this);
    }



    @Override
    public File convertResponse(Response response) throws Throwable {
        File file = convert.convertResponse(response);
        response.close();
        return file;
    }
}