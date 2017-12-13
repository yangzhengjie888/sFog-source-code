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
import com.example.androidtemplate.common.D;
import com.example.androidtemplate.common.T;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class FindPasswordActivity extends BaseActivity {
    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_tv)
    TextView rightTv;
    @Bind(R.id.title_ll)
    LinearLayout titleLl;
    @Bind(R.id.username_et)
    EditText usernameEt;
    @Bind(R.id.email_et)
    EditText emailEt;
    @Bind(R.id.find_btn)
    Button findBtn;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;

    @Override
    protected void initData() {
        setContentView(R.layout.a_activity_find_password);
        ButterKnife.bind(this);

    }

    @Override
    protected void recycle() {

    }

    @OnClick({R.id.left_tv, R.id.find_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_tv:
                finish();
                break;
            case R.id.find_btn:
                String username = usernameEt.getText().toString().trim();
                final String email = emailEt.getText().toString().trim();
                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(email)){
                    T.showToast(this_,"请填写用户名或邮箱");
                }else{
                    RequestParams params = new RequestParams();
                    params.put("action","findPasswd");
                    params.put("username",username);
                    params.put("toEmail",email);
                    HttpUtil.post("ClientServlet", params, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                            D.out(throwable);
                        }

                        @Override
                        public void onSuccess(int i, Header[] headers, String s) {
                            if(isFail(s)){
                                T.showToast(this_,"找回密码错误");
                            }else {
                                T.showToast(this_,"邮件已发送，请查收");
                            }
                        }
                    });
                }
                break;
        }
    }
}
