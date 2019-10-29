package com.mengya.dreamcameralib.camera.controller;

public interface IDreamCameraController {
    //拍照功能
    void takePhote();

    //获取摄像头方向
    int getCameraFacing();

    //开启摄像头预览
    void startCameraPreview();
}
