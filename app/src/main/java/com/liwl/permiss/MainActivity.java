package com.liwl.permiss;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.liwl.permiss.utils.PermissionUtils;
import com.liwl.permiss.utils.PermissionsPageUtils;

public class MainActivity extends AppCompatActivity implements DoubleButtonDialog.IOnClickListener, View
        .OnClickListener {

    private DoubleButtonDialog permissDialog;
    private EditText roomIdEditText;
    private EditText nickNameEditText;
    private EditText roomPasswordEditText;
    private EditText serverAddressEditText;
    private EditText serverPortEditText;
    private Button loginButton;
    private Button changeButton;
    private LinearLayout roomLayout;

    private LinearLayout loginLayout;
    private EditText userAccountEditText;
    private EditText userPasswordEditText;
    private EditText userServerAddressEditText;
    private EditText userServerPortEditText;
    private Button userLoginButton;
    private Button changeRoomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        roomIdEditText = findViewById(R.id.et_roomid);
        nickNameEditText = findViewById(R.id.et_nickname);
        roomPasswordEditText = findViewById(R.id.et_room_password);
        serverAddressEditText = findViewById(R.id.et_server_address);
        serverPortEditText = findViewById(R.id.et_server_port);
        loginButton = findViewById(R.id.bt_login);
        changeButton = findViewById(R.id.bt_change);
        roomLayout = findViewById(R.id.room_linearLayout);
        loginButton.setOnClickListener(this);
        changeButton.setOnClickListener(this);

        userAccountEditText = findViewById(R.id.et_user_account);
        userPasswordEditText = findViewById(R.id.et_user_password);
        userServerAddressEditText = findViewById(R.id.et_user_server_address);
        userServerPortEditText = findViewById(R.id.et_user_server_port);
        userLoginButton = findViewById(R.id.bt_user_login);
        changeRoomButton = findViewById(R.id.bt_change_room);
        loginLayout = findViewById(R.id.login_layout);
        userLoginButton.setOnClickListener(this);
        changeRoomButton.setOnClickListener(this);
    }

    /**
     * 在Onstart做权限请求的原因如下：
     * 1. 系统启动一定会走onStart方法
     * 2. 手机黑屏后，该Activity会走onStop方法，亮屏会重新走onStart方法
     * 3. 跳转到设置页面后，会走onStop方法，跳转回到该Activity后，会走onStart方法
     */
    @Override
    protected void onStart() {
        super.onStart();
        PermissionUtils.requestAllPermissions(this);
    }

    /**
     * onStop方法执行后，必须得把弹框隐藏，如果弹框未消失的情况下就onStop掉，会报不崩溃的异常
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (permissDialog != null && permissDialog.isShowing()) {
            permissDialog.dismiss();
        }
    }

    /**
     * 权限申请回调
     * 这里必须实现父类的 super.onRequestPermissionsResult
     * 不然会导致在Fragment中申请权限时，Fragment中的onRequestPermissionsResult没有调用
     *
     * @param requestCode  请求码
     * @param permissions  所申请的权限集合
     * @param grantResults 权限申请结果的集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!PermissionUtils.checkPermissions(this)) {
            if (permissDialog == null) {
                initPermissDialog();
            }
            permissDialog.show();
        }
    }

    /**
     * 启动其他Activity后，返回该Activity后的回调
     * 注意：1.必须是通过startActivityForResult启动另外一个Activity后，返回才会有回调
     * 2.requestCode是启动Activity时，传过去的RequestCode，用于识别是哪个Activity启动后的回调
     * 3.从设置页面返回后，应该重新检验下是否有权限，没有的话，继续弹框
     *
     * @param requestCode 请求码
     * @param resultCode  识别码
     * @param data        返回携带的数据
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsPageUtils.REQUEST_CODE) {
            if (!PermissionUtils.checkPermissions(this)) {
                if (permissDialog == null) {
                    initPermissDialog();
                }
                permissDialog.show();
            }
        }
    }

    private void initPermissDialog() {
        permissDialog = new DoubleButtonDialog(this, "提示消息", "应用缺少必要的运行权限，请前往设置开启权限，否则将无法正常运行", "取消", "立即设置");
        permissDialog.setOnClickListener(this);
    }

    /**
     * 正常流程是，不给权限就退出程序
     *
     * @param dialog
     */
    @Override
    public void leftButtonClick(Dialog dialog) {
        finish();
    }

    /**
     * 点击前往设置页面
     *
     * @param dialog
     */
    @Override
    public void rightButtonClick(Dialog dialog) {
        PermissionsPageUtils permissionsPageUtils = new PermissionsPageUtils(this);
        permissionsPageUtils.jumpPermissionPage();
    }

    /**
     * 登录好视通视频会议
     */
    private void gotoLoginRoom(){
        //例如：用户名：hst 密码:111
        ComponentName apkComponent = new ComponentName("com.inpor.fastmeetingcloud", "com.inpor.fastmeetingcloud.ui" +
                ".StartTheMiddleTierActivity");
        Intent mIntent = new Intent();
        Bundle mbundle = new Bundle();
        mbundle.putString("userName", userAccountEditText.getText().toString());
        mbundle.putString("userPwd", userPasswordEditText.getText().toString());
        mbundle.putString("svrAddress", userServerAddressEditText.getText().toString());
        mbundle.putString("svrPort", userServerPortEditText.getText().toString());
        mIntent.putExtras(mbundle);
        mIntent.setComponent(apkComponent);
        startActivity(mIntent);
    }

    /**
     * 登录好视频视频会议会议室
     */
    private void gotoMeetingRoom() {
        //例如：用户名：hst 密码:111
        ComponentName apkComponent = new ComponentName("com.inpor.fastmeetingcloud", "com.inpor.fastmeetingcloud.ui" +
                ".StartTheMeetingRoomActivity");
        Intent mIntent = new Intent();
        Bundle mbundle = new Bundle();
        mbundle.putLong("roomId", Long.parseLong(roomIdEditText.getText().toString()));
        mbundle.putString("nickname", nickNameEditText.getText().toString());
        mbundle.putString("roomPassword", roomPasswordEditText.getText().toString());
        mbundle.putString("svrAddress", serverAddressEditText.getText().toString());
        mbundle.putString("svrPort", serverPortEditText.getText().toString());
        mIntent.putExtras(mbundle);
        mIntent.setComponent(apkComponent);
        startActivity(mIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                gotoMeetingRoom();
                break;
            case R.id.bt_user_login:
                gotoLoginRoom();
                break;
            case R.id.bt_change:
                loginLayout.setVisibility(View.VISIBLE);
                roomLayout.setVisibility(View.GONE);
                break;
            case R.id.bt_change_room:
                loginLayout.setVisibility(View.GONE);
                roomLayout.setVisibility(View.VISIBLE);
                break;

        }
    }
}
