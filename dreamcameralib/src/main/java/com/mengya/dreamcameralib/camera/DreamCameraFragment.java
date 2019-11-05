package com.mengya.dreamcameralib.camera;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mengya.dreamcameralib.R;
import com.mengya.dreamcameralib.camera.callback.DreamCameraCallback;
import com.mengya.dreamcameralib.camera.controller.DreamCameraController;
import com.mengya.dreamcameralib.camera.data.Constants;
import com.mengya.dreamcameralib.camera.utils.CommonUtils;
import com.mengya.dreamcameralib.camera.widget.CameraSurfaceView;

import java.io.File;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

import static android.app.Activity.RESULT_OK;

/**
 * 自定义相机Fragment
 * create by fengwenhua at 2019-10-29 14:50:41
 */
public class DreamCameraFragment extends Fragment implements DreamCameraCallback {
    ///选择照片请求码
    private static final int SELECT_PHOTO_ACTION = 999;

    private String mFileSavePath;
    private CameraSurfaceView mCameraSurfaceView;
    private ImageView image_button_take_photo;
    private ImageView fragment_iv_close;
    private ImageView image_button_switch;
    private ImageView fragment_iv_focus;
    private ImageView image_button_select;
    private RelativeLayout fragment_top_container;

    ///前置相机还是后置相机 0->后置相机 1->前置相机
    private int cameraMode;
    private DreamCameraController mDreamCameraController;

    public static DreamCameraFragment getInstance(int cameraMode, String mFileSavePath) {
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_MODE, cameraMode);
        args.putString(Constants.KEY_SAVE_PATH, mFileSavePath);
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
        image_button_select = view.findViewById(R.id.image_button_select);
        Glide.with(this).asGif().load(R.drawable.ic_focus).into(fragment_iv_focus);
        image_button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
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
        fragment_top_container.setPadding(0, CommonUtils.getStatusBarHeight(getActivity()), 0, 0);
    }

    private void initParams() {
        mDreamCameraController = new DreamCameraController(getActivity(), mFileSavePath, mCameraSurfaceView,cameraMode, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case SELECT_PHOTO_ACTION:
                if (resultCode == RESULT_OK && null != data) {
                    String mFilePath;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        mFilePath = handleImageOnKitkat(data);
                    } else {
                        mFilePath = handleImageBeforeKitkat(data);
                    }
                    if(null!=mFilePath){
                        startPreview(mFilePath);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(19)
    private String handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    private String handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        return imagePath;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void selectPhoto() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, SELECT_PHOTO_ACTION);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        cameraMode = bundle.getInt(Constants.KEY_MODE, Camera.CameraInfo.CAMERA_FACING_BACK);
        mFileSavePath = bundle.getString(Constants.KEY_SAVE_PATH);
        View view = inflater.inflate(R.layout.fragment_dream_camera, container, false);
        initViews(view);
        initParams();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
