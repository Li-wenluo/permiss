package com.liwl.permiss.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwl on 2019/3/14.
 */
public class PermissionUtils {
    private static final String TAG = "PermissionUtils";

    /**
     * 申请权限
     *
     * @param activity    Activity
     * @param permission  权限
     * @param requestCode 请求码
     */
    public static void requestPermissions(final Activity activity, final String permission, final int requestCode) {
        if (activity == null || TextUtils.isEmpty(permission)) {
            throw new NullPointerException("传入参数不能为空!");
        }

        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }
    }

    /**
     * 请求所有权限(在Android6.0中，需要动态申请的权限有：传感器、
     * 日历、摄像头、通讯录、地理位置、麦克风、电话、短信、存储空间
     * 。其余的权限在AndroidManifest.xml声明即可，目前该接口仅申
     * 请好视通APP所需的权限。)
     *
     * @param activity Activity
     */
    public static void requestAllPermissions(final Activity activity) {
        if (activity == null) {
            throw new NullPointerException("传入参数不能为空!");
        }

        if (VERSION.SDK_INT < VERSION_CODES.M) {
            return;
        }

        // 在某些机型上请求权限可能会抛出异常，进行捕捉
        try {
            List<String> permissionList = new ArrayList<>();

            if (!checkPermissions(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (!checkPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!checkPermissions(activity, Manifest.permission.RECORD_AUDIO)) {
                permissionList.add(Manifest.permission.RECORD_AUDIO);
            }

            // 捕捉位置信息权限的异常，以防止某些设备不支持
            try {
                if (!checkPermissions(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }

            try {
                if (!checkPermissions(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }

            if (!checkPermissions(activity, Manifest.permission.CALL_PHONE)) {
                permissionList.add(Manifest.permission.CALL_PHONE);
            }

            if (!checkPermissions(activity, Manifest.permission.READ_PHONE_STATE)) {
                permissionList.add(Manifest.permission.READ_PHONE_STATE);
            }

            if (!checkPermissions(activity, Manifest.permission.CAMERA)) {
                permissionList.add(Manifest.permission.CAMERA);
            }

            if (permissionList.size() >= 1) {
                String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(activity, permissions, 1);
            }

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    /**
     * 检查权限是否开启
     *
     * @param context    Context
     * @param permission 权限
     * @return true 有权限；false 没有权限
     */
    public static boolean checkPermissions(final Context context, final String permission) {
        if (context == null || TextUtils.isEmpty(permission)) {
            throw new NullPointerException("传入参数不能为空");
        }

        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermissions(final Context context) {
        if (context == null) {
            throw new NullPointerException("传入参数不能为空");
        }
        if (!checkPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return false;
        }

        if (!checkPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return false;
        }

        if (!checkPermissions(context, Manifest.permission.RECORD_AUDIO)) {
            return false;
        }

        // 捕捉位置信息权限的异常，以防止某些设备不支持
        try {
            if (!checkPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                return false;
            }
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }

        try {
            if (!checkPermissions(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                return false;
            }
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }

        if (!checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
            return false;
        }

        return false;
    }
}