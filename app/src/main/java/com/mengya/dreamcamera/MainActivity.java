package com.mengya.dreamcamera;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.mengya.dreamcameralib.camera.DreamCameraHelper;

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
                        1000,"", mFileSavePath);
            }
        });
    }
}
