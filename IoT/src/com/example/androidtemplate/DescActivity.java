package com.example.androidtemplate;


import android.net.wifi.ScanResult;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidtemplate.common.BaseActivity;
import com.example.androidtemplate.manager.ManagerConf;
import com.example.androidtemplate.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DescActivity extends BaseActivity {

    ScanResult scanResult;
    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_tv)
    TextView rightTv;
    @Bind(R.id.title_ll)
    LinearLayout titleLl;
    @Bind(R.id.ssid_tv)
    TextView ssidTv;
    @Bind(R.id.bssid_et)
    TextView bssidEt;
    @Bind(R.id.frequency_tt)
    TextView frequencyTt;
    @Bind(R.id.leave_tv)
    TextView leaveTv;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;

    @Override
    protected void initData() {
        setContentView(R.layout.b_activity_desc);
        ButterKnife.bind(this);
        scanResult = getIntent().getParcelableExtra("wifi");
        ssidTv.setText(scanResult.SSID + "");
        bssidEt.setText(scanResult.BSSID + "");
        frequencyTt.setText(scanResult.frequency + "");
        leaveTv.setText(scanResult.level + "");

    }

    @Override
    protected void recycle() {

    }


    @OnClick({R.id.left_tv, R.id.right_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_tv:
                finish();
                break;
            case R.id.right_tv:
                break;
        }
    }

}
