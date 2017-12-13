package com.example.androidtemplate.user;


import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidtemplate.R;
import com.example.androidtemplate.common.BaseActivity;
import com.example.androidtemplate.manager.ManagerApp;
import com.example.androidtemplate.manager.ManagerConf;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalActivity extends BaseActivity {


    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_tv)
    TextView rightTv;
    @Bind(R.id.title_ll)
    LinearLayout titleLl;
    @Bind(R.id.user_update_tv)
    TextView userUpdateTv;
    @Bind(R.id.update_password_tv)
    TextView updatePasswordTv;
    @Bind(R.id.logout_tv)
    TextView logoutTv;
    @Bind(R.id.exit_tv)
    TextView exitTv;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;

    @Override
    protected void initData() {
        setContentView(R.layout.a_activity_personal);
        ButterKnife.bind(this);

    }

    @Override
    protected void recycle() {

    }


    @OnClick({R.id.left_tv, R.id.right_tv, R.id.user_update_tv, R.id.update_password_tv, R.id.logout_tv, R.id.exit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_tv:
                finish();
                break;
            case R.id.right_tv:
                break;
            case R.id.user_update_tv:
                startActivity(new Intent(this_, UpdateUserActivity.class));
                break;
            case R.id.update_password_tv:
                startActivity(new Intent(this_, UpdatePasswordActivity.class));
                break;
            case R.id.logout_tv:
                ManagerConf.saveToLocal("login_user_kaoshi", "");
                ManagerApp.logout();
                startActivity(new Intent(this_, LoginActivity.class));
                break;
            case R.id.exit_tv:
                ManagerApp.exitApp();
                break;
        }
    }

}
