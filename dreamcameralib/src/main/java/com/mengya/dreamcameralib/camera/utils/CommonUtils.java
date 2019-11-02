package com.mengya.dreamcameralib.camera.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mengya.dreamcameralib.camera.callback.TakePhotoCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * 工具类
 * create by fengwenhua at 2019-10-29 13:01:51
 */

public class CommonUtils {

    /**
     * 获取uuid
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-", "").toUpperCase();
        return uuid;
    }


    /**
     * 获取状态栏高度
     * @param context context 上下文
     * @return 状态栏高度px
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

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

    private static class NumInverted implements Comparator<Integer> {
        @Override
        public int compare(Integer integer, Integer t1) {
            return t1 - integer;
        }
    }

    public static final List<Integer> getSupportedPreviewFrameRates(Camera camera) {
        List<Integer> supportedPreviewFrameRates = camera.getParameters().getSupportedPreviewFrameRates();
        Collections.sort(supportedPreviewFrameRates, new NumInverted());
        return supportedPreviewFrameRates;
    }

    /**
     * 预览的比较类   正序  从小到大
     */
    private static class PreviewSizeComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.height != rhs.height) {
                return lhs.height - rhs.height;
            }else {
                return lhs.width - rhs.width;
            }
        }
    }

    /**
     * 获取手机摄像头支持预览的尺寸集合
     */
    public static List<Camera.Size> getSupportedPreviewSizes(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Collections.sort(previewSizes, new CommonUtils.PreviewSizeComparator());
        return previewSizes;
    }

    /**
     * 拍摄图片的比较类 倒序 从大到小
     */
    private static class PictureSizeComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.height != rhs.height) {
                return rhs.height - lhs.height;
            }else {
                return rhs.width - lhs.width;
            }
        }
    }

    /**
     * 获取手机摄像头支持拍摄照片像素的集合
     *
     * @param camera
     * @return
     */
    public static List<Camera.Size> getSupportedPictureSizes(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPictureSizes();
        Collections.sort(previewSizes, new CommonUtils.PictureSizeComparator());
        return previewSizes;
    }

}
