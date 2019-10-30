package com.mengya.dreamcameralib.camera.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mengya.dreamcameralib.camera.callback.TakePhotoCallback;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 工具类
 * create by fengwenhua at 2019-10-29 13:01:51
 */

public class CommonUtils {

    //设置Activity 全屏
    public static void configFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置透明状态栏,这样才能让 ContentView 向上
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    //Bitmap转File 工具类
    public static void saveBitmap2File(Bitmap bitmap, String mFilePath, String fileName, TakePhotoCallback callback){
        try {
            File mediaStorageDir = new File(mFilePath);
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }
            File mediaFile = new File(mFilePath+File.separator+fileName);
            FileOutputStream stream = new FileOutputStream(mediaFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
            callback.result(mediaFile);
        }catch (Exception e){
            callback.error(e);
        }
    }


}
