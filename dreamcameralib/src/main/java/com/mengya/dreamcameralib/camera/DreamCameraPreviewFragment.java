package com.mengya.dreamcameralib.camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mengya.dreamcameralib.R;

import java.io.File;

public class DreamCameraPreviewFragment extends Fragment {
    private ImageView fragment_iv_close;
    private ImageView iv_take_photo_success;

    private String mFilePath;

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
    }

    private void selectPhoto(){

        DreamCameraActivity activity = (DreamCameraActivity) getActivity();

        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(new File(mFilePath));
        mediaScannerIntent.setData(fileContentUri);
        activity.sendBroadcast(mediaScannerIntent);
        activity.returnPhotoPath(mFilePath);
    }


}
