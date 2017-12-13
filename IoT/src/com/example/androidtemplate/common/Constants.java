package com.example.androidtemplate.common;


import android.os.Environment;

public class Constants {

    public static final String FAIL_STATE = "fail";
    public static final String SUCCESS_STATE = "success";

    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final String ImageTempPath = Environment.getExternalStorageDirectory() + "/temp.jpg";

    public static final int ALBUM_REQUEST_CODE = 100;
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CROP_REQUEST_CODE = 102;


    public static final String filePath = Environment.getExternalStorageDirectory() + "/auto_wifi/";

}
