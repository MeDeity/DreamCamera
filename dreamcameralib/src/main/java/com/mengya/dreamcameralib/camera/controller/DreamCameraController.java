package com.mengya.dreamcameralib.camera.controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;

import com.mengya.dreamcameralib.camera.DreamCameraActivity;
import com.mengya.dreamcameralib.camera.callback.ErrorCallback;
import com.mengya.dreamcameralib.camera.utils.CommonUtils;
import com.mengya.dreamcameralib.camera.widget.CameraSurfaceView;

import java.io.File;

public class DreamCameraController implements IDreamCameraController {

    private Camera mCamera;//camera对象
    private boolean isTakeing;
    ///文件保存路径
    private String mFileSavePath;
    private Activity mActivity;
    private CameraSurfaceView cameraSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private int mCameraId;//摄像头方向id
    private boolean mIsPreviewing;  //是否预览

    private SurfaceHolder.Callback surfaceCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            try {
                mSurfaceHolder = surfaceHolder;
                if (surfaceHolder.getSurface() == null) {
                    return;
                }
                if (mCamera == null) {
                    if (Build.VERSION.SDK_INT < 9) {
                        mCamera = Camera.open();
                    } else {
                        mCamera = Camera.open(mCameraId);
                    }
                }
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
                mIsPreviewing = false;
//                handleSurfaceChanged(mCamera);
                startCameraPreview(mSurfaceHolder);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                destroyCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public DreamCameraController(String mFileSavePath, CameraSurfaceView cameraSurfaceView) {
        this.mActivity = mActivity;
        this.mFileSavePath = mFileSavePath;
        this.cameraSurfaceView = cameraSurfaceView;
        this.cameraSurfaceView.setCustomSize(true);
        mSurfaceHolder = this.cameraSurfaceView.getHolder();
        mSurfaceHolder.addCallback(surfaceCallBack);

        // 判断路径是否存在
        File saveFile = new File(mFileSavePath);
        if (!saveFile.exists()) saveFile.mkdirs();
        //这里设置当摄像头数量大于1的时候就直接设置后摄像头  否则就是前摄像头
        if (Build.VERSION.SDK_INT > 8) {
            if (Camera.getNumberOfCameras() > 1) {
                mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            } else {
                mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            }
        }
    }


    @Override
    public void takePhoto() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    int orientation = ((DreamCameraActivity) mActivity).getOrientation();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 将图片保存在 DIRECTORY_DCIM 内存卡中
                    Matrix matrix = new Matrix();
                    if (getCameraFacing() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        matrix.setRotate(-90 - orientation);
                        matrix.postScale(-1, 1);
                    } else {
                        matrix.setRotate(90 + orientation);
                    }
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    CommonUtils.saveBitmap2File(bitmap, mFileSavePath, "test.jpg", new ErrorCallback() {
                        @Override
                        public void error(Exception e) {
                            e.printStackTrace();
                        }
                    });
                    isTakeing = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getCameraFacing() {
        return 0;
    }

    @Override
    public void startCameraPreview(SurfaceHolder holder) {

    }

    @Override
    public void changeCameraFacing() {

    }

    @Override
    public void destroyCamera() {

    }
}
