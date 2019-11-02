package com.mengya.dreamcameralib.camera;

import android.app.Activity;
import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.mengya.dreamcameralib.R;
import com.mengya.dreamcameralib.camera.data.Constants;

/**
 * 整个相机的入口门面
 * create by fengwenhua at 2019-10-29 15:36:24
 */
public class DreamCameraHelper {


    private static void launch(Activity activity, int requestCode, int mode, String mFileSavePath){
        Intent intent = new Intent(activity, DreamCameraActivity.class);
        intent.putExtra(Constants.KEY_MODE, mode);
        intent.putExtra(Constants.KEY_SAVE_PATH, mFileSavePath);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.take_photo_anim_up_in, R.anim.take_photo_anim_up_out);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, compat.toBundle());
    }

    ///拍照入口
    public static void startTakePhoto(Activity activity, int requestCode,int cameraMode,String mFileSavePath) {
        launch(activity, requestCode,cameraMode,mFileSavePath);
    }


}
