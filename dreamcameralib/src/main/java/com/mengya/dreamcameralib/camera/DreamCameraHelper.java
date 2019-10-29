package com.mengya.dreamcameralib.camera;

import android.app.Activity;
import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.mengya.dreamcameralib.R;

/**
 * 整个相机的入口门面
 * create by fengwenhua at 2019-10-29 15:36:24
 */
public class DreamCameraHelper {

    private static void lauch(Activity activity,int requestCode){
        Intent intent = new Intent(activity, DreamCameraActivity.class);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.take_photo_anim_up_in, R.anim.take_photo_anim_up_out);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, compat.toBundle());
    }

    ///拍照入口
    public static void startTakePhoto(Activity activity, int requestCode, String savePath) {
        lauch(activity, requestCode);
    }


}
