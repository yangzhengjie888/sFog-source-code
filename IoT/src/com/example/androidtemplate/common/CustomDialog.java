package com.example.androidtemplate.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidtemplate.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class CustomDialog extends Dialog {

    private static CustomDialog customDialog;
    @Bind(R.id.hint_msg_tv)
    public TextView hintMsgTv;
    @Bind(R.id.ok_btn)
    public Button okBtn;
    @Bind(R.id.cancel_btn)
    Button cancelBtn;
    @Bind(R.id.password_et)
    EditText passwordEt;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;

    // 单例
    public static CustomDialog getInstance(Context context) {
        customDialog = new CustomDialog(context);
        return customDialog;
    }

    public CustomDialog(Context context) {
        super(context);
        setTitle("温馨提示");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_dialog_custom);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cancel_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
        }
    }

    public void setHintMsg(String hintMsg) {
        hintMsgTv.setText(hintMsg);
    }

    public void setOkBtnOnClick(View.OnClickListener onClick) {
        okBtn.setOnClickListener(onClick);
    }

    public String getPassword(){
        return passwordEt.getText().toString().trim();
    }
}
