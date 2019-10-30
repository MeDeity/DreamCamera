package com.mengya.dreamcameralib.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mengya.dreamcameralib.R;
import com.mengya.dreamcameralib.camera.controller.DreamCameraController;
import com.mengya.dreamcameralib.camera.widget.CameraSurfaceView;

/**
 * 自定义相机Fragment
 * create by fengwenhua at 2019-10-29 14:50:41
 */
public class DreamCameraFragment extends Fragment {

    ///前置相机还是后置相机
    public static final String MODE = "MODE";
    private String mFileSavePath = "test";
    private CameraSurfaceView mCameraSurfaceView;


    ///前置相机还是后置相机 0->后置相机 1->前置相机
    private int mode;
    private DreamCameraController mDreamCameraController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViews(View view){
        mCameraSurfaceView = view.findViewById(R.id.fragment_camera_surface_view);
    }

    private void initParams(){
        mDreamCameraController = new DreamCameraController(getActivity(),mFileSavePath,mCameraSurfaceView);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mode = bundle.getInt(MODE,0);
        View view = inflater.inflate(R.layout.fragment_dream_camera, container, false);
        initViews(view);
        initParams();
//        startPreview();
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
