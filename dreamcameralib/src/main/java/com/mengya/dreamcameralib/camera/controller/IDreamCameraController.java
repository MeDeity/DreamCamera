package com.mengya.dreamcameralib.camera.controller;

import android.view.SurfaceHolder;

public interface IDreamCameraController {
    //拍照功能
    void takePhoto();

    //获取摄像头方向
    int getCameraFacing();

    //开启摄像头预览
    void startCameraPreview(SurfaceHolder holder);

    //变更摄像头的方向
    void changeCameraFacing();

    //释放句柄
    void destroyCamera();
}
