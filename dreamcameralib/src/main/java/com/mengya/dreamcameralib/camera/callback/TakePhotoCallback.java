package com.mengya.dreamcameralib.camera.callback;

import java.io.File;

public interface TakePhotoCallback {

    void result(File mFile);

    void error(Exception e);
}
