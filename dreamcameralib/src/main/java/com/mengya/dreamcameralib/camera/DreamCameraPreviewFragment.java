package com.mengya.dreamcameralib.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.chrisbanes.photoview.PhotoView;
import com.mengya.dreamcameralib.R;
import com.mengya.dreamcameralib.camera.utils.CommonUtils;

import java.io.File;

public class DreamCameraPreviewFragment extends Fragment {
    private ImageView fragment_iv_close;
    private ImageView iv_take_photo_success;
    private PhotoView iv_show_result;
    private RelativeLayout fragment_top_container;

    private String mFileSavePath;


    public DreamCameraPreviewFragment(String mFileSavePath) {
        this.mFileSavePath = mFileSavePath;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream_camera_preview, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        fragment_iv_close = view.findViewById(R.id.fragment_iv_close);
        iv_take_photo_success = view.findViewById(R.id.iv_take_photo_success);
        iv_show_result = view.findViewById(R.id.iv_show_result);
        fragment_top_container = view.findViewById(R.id.fragment_top_container);
        ((View) fragment_iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DreamCameraActivity) getActivity()).popBackStack();// 重新返回至预览的fragment
            }
        });
        ((View) iv_take_photo_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });
        showImage();
        fragment_top_container.setPadding(0, CommonUtils.getStatusBarHeight(getActivity()), 0, 0);
    }

    private void showImage(){
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(mFileSavePath);
            iv_show_result.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void selectPhoto(){
        DreamCameraActivity activity = (DreamCameraActivity) getActivity();
        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(new File(mFileSavePath));
        mediaScannerIntent.setData(fileContentUri);
        activity.sendBroadcast(mediaScannerIntent);
        activity.returnPhotoPath(mFileSavePath);
    }


}
