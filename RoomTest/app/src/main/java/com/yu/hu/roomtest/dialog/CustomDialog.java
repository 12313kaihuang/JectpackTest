package com.yu.hu.roomtest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.yu.hu.roomtest.R;

/**
 * @author hy
 * @Date 2019/10/16 0016
 **/
public class CustomDialog extends Dialog {

    private onBtnClickListener mOnModifyBtnClickListener;

    private onBtnClickListener mOnDeleteBtnClickListener;


    public CustomDialog(@NonNull Context context) {
        this(context, R.style.TransparentNoTitleDialog);
    }

    @SuppressWarnings("WeakerAccess")
    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_dialog);

        Window window = getWindow();
        @SuppressWarnings("ConstantConditions")
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(R.style.pop_anim_style);

        initView();
    }

    private void initView() {

        Button modifyBtn = findViewById(R.id.btn_modify);
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnModifyBtnClickListener != null) {
                    mOnModifyBtnClickListener.onBtnClicked(v);
                }
                dismiss();
            }
        });

        Button deleteBtn = findViewById(R.id.btn_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteBtnClickListener != null) {
                    mOnDeleteBtnClickListener.onBtnClicked(v);
                }
                dismiss();
            }
        });

        Button cancelBtn = findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public CustomDialog setOnModifyBtnClickListener(onBtnClickListener mOnModifyBtnClickListener) {
        this.mOnModifyBtnClickListener = mOnModifyBtnClickListener;
        return this;
    }

    public CustomDialog setOnDeleteBtnClickListener(onBtnClickListener mOnDeleteBtnClickListener) {
        this.mOnDeleteBtnClickListener = mOnDeleteBtnClickListener;
        return this;
    }

    public interface onBtnClickListener {
        void onBtnClicked(View view);
    }
}
