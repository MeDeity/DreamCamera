package com.mengya.dreamcameralib.camera;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mengya.dreamcameralib.R;
import com.mengya.dreamcameralib.camera.data.Constants;
import com.mengya.dreamcameralib.camera.listener.CameraOrientationListener;
import com.mengya.dreamcameralib.camera.utils.CommonUtils;

import java.io.File;

/**
 * 自定义相机类
 * create by fengwenhua at 2019-10-29 12:58:30
 */
public class DreamCameraActivity extends AppCompatActivity {

    ///是前置摄像头还是后置摄像头
    private int camerMmode;
    private String mFileSavePath;

    private CameraOrientationListener orientationListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_camera);
        initIntent();
        initOrientationListener();
        CommonUtils.configFullScreen(DreamCameraActivity.this);//全屏模式
        if(null==savedInstanceState){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_dream_camera,DreamCameraFragment.getInstance(camerMmode,mFileSavePath))
                    .commit();
        }
    }

    //图片旋转矫正
    private void initOrientationListener() {
        orientationListener = new CameraOrientationListener(this);
        orientationListener.enable();
    }

    private void initIntent() {
        Intent intent = getIntent();
        camerMmode = intent.getIntExtra(Constants.KEY_MODE, Camera.CameraInfo.CAMERA_FACING_BACK);
        mFileSavePath = intent.getStringExtra(Constants.KEY_SAVE_PATH);
        if(TextUtils.isEmpty(mFileSavePath)){
            mFileSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPackageName();
        }
    }

    public int getOrientation() {
        if (orientationListener != null) {
            return orientationListener.getOrientation();
        }
        return 0;
    }

    /**
     * 返回上一个Fragment页面
     */
    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * 返回图片路径
     */
    public void returnPhotoPath(String photoPath) {
        Intent data = new Intent();
        data.putExtra(Constants.RESULT_DATA, photoPath);
        if (getParent() == null) {
            setResult(RESULT_OK, data);
        } else {
            getParent().setResult(RESULT_OK, data);
        }
        finish();
    }


}
