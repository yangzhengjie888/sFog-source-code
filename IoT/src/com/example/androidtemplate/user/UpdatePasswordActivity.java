package com.example.androidtemplate.user;


import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidtemplate.Http.HttpUtil;
import com.example.androidtemplate.R;
import com.example.androidtemplate.common.BaseActivity;
import com.example.androidtemplate.common.T;
import com.example.androidtemplate.manager.ManagerComm;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePasswordActivity extends BaseActivity {

    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_tv)
    TextView rightTv;
    @Bind(R.id.title_ll)
    LinearLayout titleLl;
    @Bind(R.id.original_password_et)
    EditText originalPasswordEt;
    @Bind(R.id.password_et)
    EditText passwordEt;
    @Bind(R.id.password2_et)
    EditText password2Et;
    @Bind(R.id.update_password_btn)
    Button updatePasswordBtn;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;

    @Override
    protected void initData() {
        setContentView(R.layout.a_activity_update_password);
        ButterKnife.bind(this);
    }

    @Override
    protected void recycle() {

    }


    @OnClick({R.id.left_tv, R.id.right_tv, R.id.update_password_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_tv:
                finish();
                break;
            case R.id.right_tv:
                break;
            case R.id.update_password_btn:
                String originalPassword = originalPasswordEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String password2 = password2Et.getText().toString().trim();
                if(TextUtils.isEmpty(password) || TextUtils.isEmpty(password2) || TextUtils.isEmpty(originalPassword)){
                    T.showToast(this_,"请将信息填写完整");
                    return;
                }else{
                    if(!password.equals(password2)){
                        T.showToast(this_,"两次输入密码不一样");
                    }else{
                        if(!originalPassword.equals(ManagerComm.loginUser.getPasswd())){
                            T.showToast(this_,"原密码不正确");
                        }else{
                            RequestParams params  = new RequestParams();
                            params.put("action","updatePassword");
                            params.put("username",ManagerComm.loginUser.getUsername());
                            params.put("newPasswd",password);
                            HttpUtil.post("ClientServlet", params, new TextHttpResponseHandler() {
                                @Override
                                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

                                }

                                @Override
                                public void onSuccess(int i, Header[] headers, String s) {
                                    if(isFail(s)){
                                        T.showToast(this_,"密码修改失败");
                                    }else{
                                        T.showToast(this_,"密码修改成功");
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                }
                break;
        }
    }
}
