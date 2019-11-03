package com.mengya.dreamcamera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mengya.dreamcameralib.camera.DreamCameraHelper;
import com.mengya.dreamcameralib.camera.data.Constants;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button btn_take_photo;
    String mFileSavePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFileSavePath =Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPackageName();
        btn_take_photo = findViewById(R.id.btn_take_photo);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DreamCameraHelper.startTakePhoto(MainActivity.this,
                        Constants.DEFAULT_REQUEST_CODE, Camera.CameraInfo.CAMERA_FACING_FRONT, mFileSavePath);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case Constants.DEFAULT_REQUEST_CODE:
                if(resultCode == RESULT_OK&&null!=data){
                    String path = data.getStringExtra(Constants.RESULT_DATA);
                    Toast.makeText(this, "图片保存在:"+path, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, "可能遇到错误了,啥也没接收到", Toast.LENGTH_SHORT).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
