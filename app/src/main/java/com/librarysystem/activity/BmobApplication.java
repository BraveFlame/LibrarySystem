package com.librarysystem.activity;

import android.app.Application;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by g on 2017/3/20.
 */

public class BmobApplication extends Application {
    /**
     * SDK初始化也可以放到Application中
     */
    public static String APPID ="32d94fb0a064700f838f59bc0083ad70";

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,APPID,"demo");
        BmobConfig config =new BmobConfig.Builder(this)
        //设置appkey
        .setApplicationId(APPID)
        //请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        //文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        //文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);

    }
}
