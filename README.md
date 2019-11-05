# DreamCamera
a custom camera library

# 如何使用
1. 添加依赖
```
implementation 'com.mengya:dream-camera:1.0.0'
```

2. 调用相机功能
```
DreamCameraHelper.startTakePhoto(activity,Constants.DEFAULT_REQUEST_CODE, Camera.CameraInfo.CAMERA_FACING_FRONT, mFileSavePath);
```

3. 监听图片保存的地址
```
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
```

4. 你可以自行处理权限处理问题
```
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //将请求结果传递EasyPermission库处理
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
}

@Override
public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    Toast.makeText(MainActivity.this,"申请成功,您可以正常使用相机了",Toast.LENGTH_LONG).show();
}

@Override
public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    Toast.makeText(MainActivity.this,"请求失败",Toast.LENGTH_LONG).show();
    if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
        new AppSettingsDialog.Builder(this).setTitle("我们需要相机权限")
                .setRationale(R.string.permission_request).build().show();
    }
}
```

