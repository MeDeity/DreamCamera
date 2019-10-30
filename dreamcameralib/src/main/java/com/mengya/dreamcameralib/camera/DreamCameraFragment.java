package com.mengya.dreamcameralib.camera;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mengya.dreamcameralib.R;
import com.mengya.dreamcameralib.camera.callback.DreamCameraCallback;
import com.mengya.dreamcameralib.camera.controller.DreamCameraController;
import com.mengya.dreamcameralib.camera.widget.CameraSurfaceView;

import java.io.File;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

/**
 * 自定义相机Fragment
 * create by fengwenhua at 2019-10-29 14:50:41
 */
public class DreamCameraFragment extends Fragment implements DreamCameraCallback {

    ///前置相机还是后置相机
    public static final String MODE = "MODE";
    //保存地址
    public static final String SAVE_PATH = "SAVE_PATH";

    private String mFileSavePath;
    private CameraSurfaceView mCameraSurfaceView;
    private ImageView fragment_take_photo;


    ///前置相机还是后置相机 0->后置相机 1->前置相机
    private int mode;
    private DreamCameraController mDreamCameraController;

    public static DreamCameraFragment getInstance(int mode,String mFileSavePath) {
        Bundle args = new Bundle();
        args.putInt(MODE, mode);
        args.putString(SAVE_PATH, mFileSavePath);
        DreamCameraFragment fragment = new DreamCameraFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initViews(View view) {
        mCameraSurfaceView = view.findViewById(R.id.fragment_camera_surface_view);
        fragment_take_photo = view.findViewById(R.id.fragment_take_photo);
        fragment_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDreamCameraController.takePhoto();
            }
        });
    }

    private void initParams() {
        mDreamCameraController = new DreamCameraController(getActivity(), mFileSavePath, mCameraSurfaceView, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mode = bundle.getInt(MODE, 0);
        View view = inflater.inflate(R.layout.fragment_dream_camera, container, false);
        initViews(view);
        initParams();
//        startPreview();
        return view;
    }

    /**
     * 打开预览的fragment进行预览  照片/ 视频
     */
    private void startPreview() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_dream_camera,
                        new DreamCameraPreviewFragment())
                .addToBackStack(null)
                .commit();
    }



    @Override
    public void takePhotoResult(final File photo) {
        try {
            Luban.with(getContext())
                    .load(photo)
                    .ignoreBy(200)
                    .setTargetDir(mFileSavePath)
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String path) {
                            return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                        }
                    })
//                    .setRenameListener(new OnRenameListener() {
//                        @Override
//                        public String rename(String filePath) {
//                            return RecordVideoUtils.getUUID() + ".jpg";
//                        }
//                    })
                    .setCompressListener(new OnCompressListener() {
                        ProgressDialog dialog;

                        @Override
                        public void onStart() {
                            dialog = ProgressDialog.show(getContext(), "提示", "正在处理图片中...", false, false);
                        }

                        @Override
                        public void onSuccess(File file) {
                            if (dialog != null) dialog.dismiss();
                            startPreview();//切换fragment 预览刚刚的拍照//VideoPlayFragment.FILE_TYPE_PHOTO, file.getAbsolutePath()
                            if (photo.exists()) photo.delete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (dialog != null) dialog.dismiss();
                            startPreview();// 如果压缩失败  直接使用原图 //VideoPlayFragment.FILE_TYPE_PHOTO, photo.getAbsolutePath()
                        }
                    })
                    .launch();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
