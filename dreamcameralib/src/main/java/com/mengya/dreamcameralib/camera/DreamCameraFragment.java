package com.mengya.dreamcameralib.camera;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mengya.dreamcameralib.R;
import com.mengya.dreamcameralib.camera.callback.DreamCameraCallback;
import com.mengya.dreamcameralib.camera.controller.DreamCameraController;
import com.mengya.dreamcameralib.camera.utils.CommonUtils;
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
    private ImageView image_button_take_photo;
    private ImageView fragment_iv_close;
    private ImageView image_button_switch;
    private ImageView fragment_iv_focus;
    private RotateAnimation rotateAnimation;
    private RelativeLayout fragment_top_container;

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
        image_button_take_photo = view.findViewById(R.id.image_button_take_photo);
        fragment_top_container = view.findViewById(R.id.fragment_top_container);
        fragment_iv_close = view.findViewById(R.id.fragment_iv_close);
        image_button_switch = view.findViewById(R.id.image_button_switch);
        fragment_iv_focus = view.findViewById(R.id.fragment_iv_focus);
        Glide.with(this).asGif().load(R.drawable.ic_focus).into(fragment_iv_focus);
        image_button_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDreamCameraController.takePhoto();
            }
        });
        image_button_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDreamCameraController.changeCameraFacing(image_button_switch);
            }
        });
        fragment_iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        startRotateSelf(image_button_take_photo);
        fragment_top_container.setPadding(0, CommonUtils.getStatusBarHeight(getActivity()), 0, 0);
    }

    private void initParams() {
        mDreamCameraController = new DreamCameraController(getActivity(), mFileSavePath, mCameraSurfaceView, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mode = bundle.getInt(MODE, 0);
        mFileSavePath = bundle.getString(SAVE_PATH);
        View view = inflater.inflate(R.layout.fragment_dream_camera, container, false);
        initViews(view);
        initParams();
//        startPreview();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void startRotateSelf(View view){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(4000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(rotateAnimation);
    }

    /**
     * 打开预览的fragment进行预览  照片/ 视频
     */
    private void startPreview(String absolutePath) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_dream_camera,
                        new DreamCameraPreviewFragment(absolutePath))
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
                    .setRenameListener(new OnRenameListener() {
                        @Override
                        public String rename(String filePath) {
                            return CommonUtils.getUUID() + ".jpg";
                        }
                    })
                    .setCompressListener(new OnCompressListener() {
                        ProgressDialog dialog;

                        @Override
                        public void onStart() {
                            dialog = ProgressDialog.show(getContext(), "提示", "正在处理图片中...", false, false);
                        }

                        @Override
                        public void onSuccess(File file) {
                            if (dialog != null) dialog.dismiss();
                            startPreview(file.getAbsolutePath());//切换fragment 预览刚刚的拍照//VideoPlayFragment.FILE_TYPE_PHOTO, file.getAbsolutePath()
                            if (photo.exists()) photo.delete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (dialog != null) dialog.dismiss();
                            startPreview(photo.getAbsolutePath());// 如果压缩失败  直接使用原图 //VideoPlayFragment.FILE_TYPE_PHOTO, photo.getAbsolutePath()
                        }
                    })
                    .launch();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
