package com.example.myapplication.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class CommonUtils {

    //读写权限
        public static String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE }; //Android6.0以后操作系统的动态权限申请

    //申请录音权限
    private static final int GET_RECODE_AUDIO = 1;
    private static String[] PERMISSION_AUDIO = {
            Manifest.permission.RECORD_AUDIO
    };
    //申请相机权限

    private static String[] PERMISSION_Camera = {
            Manifest.permission.CAMERA
    };
        /**
         * 用于Android6.0以后的操作系统，动态申请存储的读写权限
         * @param context
         */
        public static void requestPermissions(Activity context){
            //用于Android6.0以后的操作系统，动态申请存储的读写权限
            int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(context,PERMISSIONS_STORAGE, 1 );
            }
        }
    /*
     * 申请录音权限*/
    public static void requestVideoPermissions(Activity context) {
        int permission = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, PERMISSION_AUDIO,
                    GET_RECODE_AUDIO);
        }
    }
    /*
     * 申请相机权限*/
    public static void requestCameraPermissions(Activity context) {
        int permission = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, PERMISSION_Camera,
                    GET_RECODE_AUDIO);
        }
    }

}
