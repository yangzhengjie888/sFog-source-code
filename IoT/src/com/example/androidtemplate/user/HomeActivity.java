package com.example.androidtemplate.user;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidtemplate.R;
import com.example.androidtemplate.common.BaseActivity;
import com.example.androidtemplate.common.CommonAdapter;
import com.example.androidtemplate.common.T;
import com.example.androidtemplate.common.ViewHolder;
import com.example.androidtemplate.manager.ManagerApp;
import com.example.androidtemplate.mo.Home;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_tv)
    TextView rightTv;
    @Bind(R.id.title_ll)
    LinearLayout titleLl;
    @Bind(R.id.home_gv)
    GridView homeGv;

    @Override
    protected void initData() {
        setContentView(R.layout.a_activity_home);
        ButterKnife.bind(this);
        List<Home> homeList = new ArrayList<Home>();
        homeList.add(new Home(R.drawable.notice,"功能"));
        homeList.add(new Home(R.drawable.me,"个人中心"));
        homeGv.setAdapter(new CommonAdapter<Home>(this_,homeList,R.layout.a_item_home) {
            @Override
            public void convert(ViewHolder helper, Home item) {
                helper.setImageResource(R.id.home_icon_iv,item.getResourId());
                helper.setText(R.id.home_title_tv,item.getTitle());
            }
        });
        homeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:

                        break;
                    case 1:
                        startActivity(new Intent(this_,PersonalActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    protected void recycle() {

    }

    @OnClick(R.id.left_tv)
    public void onClick() {
        onBackPressed();
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {

        if((System.currentTimeMillis()-exitTime) > 2000){
            T.showToast(this_,"再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {

            super.onBackPressed();
            ManagerApp.exitApp();
        }

    }

}
