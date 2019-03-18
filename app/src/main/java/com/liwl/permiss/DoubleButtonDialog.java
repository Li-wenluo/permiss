package com.liwl.permiss;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Money on 2017/10/30.
 */

public class DoubleButtonDialog extends Dialog implements View.OnClickListener {

    TextView titleTextView;
    TextView contentTextView;
    Button leftButton;
    Button rightButton;

    private String title;
    private String content;
    private String leftButtonText;
    private String rightButtonText;
    private IOnClickListener listener;

    /**
     * 构造函数
     *
     * @param context           上下文
     * @param titleId           标题
     * @param contentId         内容
     * @param leftButtonTextId  左按钮文案
     * @param rightButtonTextId 右按钮文案
     */
    public DoubleButtonDialog(Context context, int titleId, int contentId,
                              int leftButtonTextId, int rightButtonTextId) {
        this(context, context.getString(titleId), context.getString(contentId),
                context.getString(leftButtonTextId), context.getString(rightButtonTextId));
    }

    /**
     * 构造函数
     *
     * @param context         上下文
     * @param title           标题
     * @param content         内容
     * @param leftButtonText  左按钮文案
     * @param rightButtonText 右按钮文案
     */
    public DoubleButtonDialog(Context context, @NonNull String title, @NonNull String content,
                              @NonNull String leftButtonText, @NonNull String rightButtonText) {
        super(context, R.style.NormalDialog);
        setContentView(R.layout.dialog_double_button);

        this.title = title;
        this.content = content;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButtonText;

        initViews();
        initValues();
        initListener();
    }

    private void initViews() {

        titleTextView = findViewById(R.id.title_textview);
        contentTextView = findViewById(R.id.content_textview);
        leftButton = findViewById(R.id.left_button);
        rightButton = findViewById(R.id.right_button);
    }

    /**
     * 隐藏左边按钮
     */
    public void hideLeftButton() {
        leftButton.setVisibility(View.GONE);
    }

    protected void initValues() {
        setCancelable(false);
        titleTextView.setText(title);
        contentTextView.setText(content);
        leftButton.setText(leftButtonText);
        rightButton.setText(rightButtonText);
    }

    protected void initListener() {
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                if (listener != null) {
                    listener.leftButtonClick(this);
                }
                break;

            case R.id.right_button:
                if (listener != null) {
                    listener.rightButtonClick(this);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 设置按钮监听事件，将点击事件回调至使用者
     *
     * @param listener 事件回调监听器
     */
    public void setOnClickListener(@NonNull IOnClickListener listener) {
        this.listener = listener;
    }

    public interface IOnClickListener {
        void leftButtonClick(Dialog dialog);

        void rightButtonClick(Dialog dialog);
    }
}
