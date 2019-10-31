package com.mengya.dreamcameralib.camera;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mengya.dreamcameralib.R;
import com.mengya.dreamcameralib.camera.data.Constants;
import com.mengya.dreamcameralib.camera.listener.CameraOrientationListener;
import com.mengya.dreamcameralib.camera.utils.CommonUtils;

/**
 * 自定义相机类
 * create by fengwenhua at 2019-10-29 12:58:30
 */
public class DreamCameraActivity extends AppCompatActivity {
    public static final String MODE = "MODE";
    public static final String SAVE_PATH = "SAVE_PATH";

    private int mode;
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
                    .replace(R.id.layout_dream_camera,DreamCameraFragment.getInstance(1,mFileSavePath))
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
        mode = intent.getIntExtra(MODE, Constants.Mode.RECORD_MODE_PHOTO);
        mFileSavePath = intent.getStringExtra(SAVE_PATH);
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
