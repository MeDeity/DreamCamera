package com.mengya.dreamcameralib.camera.listener;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;

/**
 * 当方向改变时，将调用侦听器onOrientationChanged(int)
 */
public class CameraOrientationListener extends OrientationEventListener {

    private int mCurrentNormalizedOrientation;
    private int mRememberedNormalOrientation = -1;

    public CameraOrientationListener(Context context) {
        super(context, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onOrientationChanged(final int orientation) {
        if (orientation != ORIENTATION_UNKNOWN) {
            mCurrentNormalizedOrientation = normalize(orientation);
        }
        // 避免重复
        if (mRememberedNormalOrientation != mCurrentNormalizedOrientation) {
            mRememberedNormalOrientation = mCurrentNormalizedOrientation;
        }
    }

    private int normalize(int degrees) {
        if (degrees > 315 || degrees <= 45) {
            return 0;
        }

        if (degrees > 45 && degrees <= 135) {
            return 90;
        }

        if (degrees > 135 && degrees <= 225) {
            return 180;
        }

        if (degrees > 225 && degrees <= 315) {
            return 270;
        }
        throw new RuntimeException("The physics as we know them are no more. Watch out for anomalies.");
    }

    public int getOrientation() {
        return mRememberedNormalOrientation;
    }
}