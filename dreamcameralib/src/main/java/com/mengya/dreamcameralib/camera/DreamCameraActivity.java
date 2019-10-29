package com.mengya.dreamcameralib.camera;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mengya.dreamcameralib.R;
import com.mengya.dreamcameralib.camera.listener.CameraOrientationListener;
import com.mengya.dreamcameralib.camera.utils.CommonUtils;

/**
 * 自定义相机类
 * create by fengwenhua at 2019-10-29 12:58:30
 */
public class DreamCameraActivity extends AppCompatActivity {
    private CameraOrientationListener orientationListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_camera);
        CommonUtils.configFullScreen(DreamCameraActivity.this);//全屏模式
        if(null==savedInstanceState){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_dream_camera,DreamCameraFragment.getInstance(1))
                    .commit();
        }
    }

    public int getOrientation() {
        if (orientationListener != null) {
            return orientationListener.getOrientation();
        }
        return 0;
    }


}
