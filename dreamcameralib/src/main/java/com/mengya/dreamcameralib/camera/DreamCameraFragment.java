package com.mengya.dreamcameralib.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mengya.dreamcameralib.R;

/**
 * 自定义相机Fragment
 * create by fengwenhua at 2019-10-29 14:50:41
 */
public class DreamCameraFragment extends Fragment {

    ///前置相机还是后置相机
    public static final String MODE = "MODE";


    ///前置相机还是后置相机 0->后置相机 1->前置相机
    private int mode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mode = bundle.getInt(MODE,0);
        View view = inflater.inflate(R.layout.fragment_dream_camera, container, false);
        return view;
    }

    public static DreamCameraFragment getInstance(int mode){
        Bundle args = new Bundle();
        args.putInt(MODE, mode);
        DreamCameraFragment fragment = new DreamCameraFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
