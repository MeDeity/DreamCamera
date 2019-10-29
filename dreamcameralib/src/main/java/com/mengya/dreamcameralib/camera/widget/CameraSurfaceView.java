package com.mengya.dreamcameralib.camera.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

public class CameraSurfaceView extends SurfaceView {
    //是否由用户自定义大小
    boolean isCustomSize = false;
    //预览宽度
    int previewWidth;
    //预览高度
    int previewHeight;
    //测量宽度
    private int mMeasuredWidth;
    //测量高度
    private int mMeasuredHeight;



    public CameraSurfaceView(Context context) {
        super(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isCustomSize) {
            doMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(mMeasuredWidth, mMeasuredHeight);
            setCameraDistance(0.5f);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setCustomSize(boolean customSize) {
        isCustomSize = customSize;
    }

    public void setPreviewSize(int previewWidth,int previewHeight){
        this.previewWidth = previewWidth;
        this.previewHeight = previewHeight;
    }

    /**
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.getDefaultSize(previewWidth, widthMeasureSpec);
        int height = View.getDefaultSize(previewHeight, heightMeasureSpec);

        if (previewWidth > 0 && previewHeight > 0) {
            int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
            float specAspectRatio = (float) widthSpecSize / (float) heightSpecSize;
            float displayAspectRatio = (float) previewWidth / (float) previewHeight;
            boolean shouldBeWider = displayAspectRatio > specAspectRatio;

            if (shouldBeWider) {
                // not high enough, fix height
                height = heightSpecSize;
                width = (int) (height * displayAspectRatio);
            } else {
                // not wide enough, fix width
                width = widthSpecSize;
                height = (int) (width / displayAspectRatio);
            }
        }
        mMeasuredWidth = width;
        mMeasuredHeight = height;
    }

}
