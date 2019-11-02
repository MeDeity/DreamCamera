package com.mengya.dreamcameralib.camera.data;

/**
 * 系统常量
 * create by fengwenhua at 2019-10-31 10:45:05
 */
public class Constants {
    ///lib与应用层的交互码
    public static final int DEFAULT_REQUEST_CODE = 1000;
    //用于返回照片的保存路径
    public static final String RESULT_DATA = "RESULT_DATA";


//    //便于后期扩展,目前仅支持拍照
//    public interface Mode {
//        int CAMERA_FACING_BACK = 0;
//        int CAMERA_FACING_FRONT = 1;
//    }


    ///INTENT KEY
    //前置摄像头还是后置摄像头
    public static final String KEY_MODE = "KEY_MODE";
    //拍照文件保存地址
    public static final String KEY_SAVE_PATH = "KEY_SAVE_PATH";
}
