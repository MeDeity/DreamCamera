package com.mengya.dreamcameralib.camera.controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;

import com.mengya.dreamcameralib.camera.DreamCameraActivity;
import com.mengya.dreamcameralib.camera.callback.DreamCameraCallback;
import com.mengya.dreamcameralib.camera.callback.TakePhotoCallback;
import com.mengya.dreamcameralib.camera.utils.CommonUtils;
import com.mengya.dreamcameralib.camera.widget.CameraSurfaceView;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 相机控制类
 * create by fengwenhua at 2019-10-30 15:11:31
 */
public class DreamCameraController implements IDreamCameraController {
    private int previewWidth = 640;//预览宽
    private int previewHeight = 480;//预览高
    private int pictureWidth = 1920; // 拍照宽
    private int pictureHeight = 1080; // 拍照高
    public static final int FLASH_MODE_OFF = 0;
    public static final int FLASH_MODE_ON = 1;
    public int flashType = FLASH_MODE_OFF;

    private Camera mCamera;//camera对象
    private boolean isTakeing;
    ///文件保存路径
    private String mFileSavePath;
    private Activity mActivity;
    private CameraSurfaceView cameraSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private int mCameraId;//摄像头方向id
    private boolean mIsPreviewing;  //是否预览
    private DreamCameraCallback callback;

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

    public DreamCameraController(Activity mActivity,String mFileSavePath, CameraSurfaceView cameraSurfaceView,DreamCameraCallback callback) {
        this.mActivity = mActivity;
        this.mFileSavePath = mFileSavePath;
        this.cameraSurfaceView = cameraSurfaceView;
        this.callback = callback;
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
                    isTakeing = true;
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
                    CommonUtils.saveBitmap2File(bitmap, mFileSavePath, "test.jpg", new TakePhotoCallback() {
                        @Override
                        public void result(File mFile) {
                            isTakeing = false;
                            if (callback != null) {
                                callback.takePhotoResult(mFile);
                            }
                        }

                        @Override
                        public void error(Exception e) {
                            isTakeing = false;
                            e.printStackTrace();
                        }
                    });

                } catch (Exception e) {
                    isTakeing = false;
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getCameraFacing() {
        return 0;
    }

    /**
     * 设置camera 的 Parameters
     */
    private void setCameraParameter() {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(previewWidth, previewHeight);
        parameters.setPictureSize(pictureWidth, pictureHeight);
        parameters.setJpegQuality(100);
        List<String> supportedFocus = parameters.getSupportedFocusModes();
        boolean isHave = supportedFocus == null ? false :
                supportedFocus.indexOf(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO) >= 0;
        if (isHave) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        parameters.setFlashMode(flashType == FLASH_MODE_ON ?
                Camera.Parameters.FLASH_MODE_TORCH :
                Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameters);
    }

    @Override
    public void startCameraPreview(SurfaceHolder holder) {
        mIsPreviewing = false;
        setCameraParameter();
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            destroyCamera();
            return;
        }
        mCamera.startPreview();
        mIsPreviewing = true;
        cameraSurfaceView.setPreviewSize(previewHeight, previewWidth);
        cameraSurfaceView.requestLayout();
    }

    @Override
    public void changeCameraFacing() {

    }

    @Override
    public void destroyCamera() {

    }
}
