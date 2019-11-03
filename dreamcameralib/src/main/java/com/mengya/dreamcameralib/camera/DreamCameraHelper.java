package com.mengya.dreamcameralib.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import com.mengya.dreamcameralib.R;
import com.mengya.dreamcameralib.camera.data.Constants;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 整个相机的入口门面
 * create by fengwenhua at 2019-10-29 15:36:24
 */
public class DreamCameraHelper{
    private static String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};
    private static WeakReference<Activity> weakReference;
    private static int mCameraMode;
    private static String mFileSavePath;

    @AfterPermissionGranted(113)
    private static void launch(Activity activity, int requestCode, int cameraMode, String fileSavePath) {
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            Intent intent = new Intent(activity, DreamCameraActivity.class);
            intent.putExtra(Constants.KEY_MODE, cameraMode);
            intent.putExtra(Constants.KEY_SAVE_PATH, fileSavePath);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.take_photo_anim_up_in, R.anim.take_photo_anim_up_out);
            ActivityCompat.startActivityForResult(activity, intent, requestCode, compat.toBundle());
        } else {
            EasyPermissions.requestPermissions(activity, "请先申请以获取相关权限", requestCode, permissions);
        }
    }

    ///拍照入口
    public static void startTakePhoto(Activity activity, int requestCode, int cameraMode, String fileSavePath) {
        weakReference = new WeakReference<>(activity);
        mCameraMode = cameraMode;
        mFileSavePath = fileSavePath;
        launch(activity, requestCode, cameraMode, fileSavePath);
    }


}
