package com.example.androidtemplate;


import com.example.androidtemplate.common.BaseActivity;

import butterknife.ButterKnife;

public class CommonActivity extends BaseActivity {

    @Override
    protected void initData() {
        setContentView(R.layout.activity_doctor);
        ButterKnife.bind(this);
    }

    @Override
    protected void recycle() {

    }

}
