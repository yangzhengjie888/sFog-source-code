package com.example.androidtemplate;


import com.example.androidtemplate.common.BaseActivity;

import butterknife.ButterKnife;

public class CommonActivity extends BaseActivity {

    @Override
    protected void initData() {
        setContentView(R.layout.activity_doctor);
        ButterKnife.bind(this);


        //////  显示 Spinner //////
        List<String> strings = new ArrayList<String>();
        for(Course section: ManagerComm.courses){
            strings.add(section.getName());
        }
        ArrayAdapter adapter = new ArrayAdapter(this_, android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSp.setAdapter(adapter);






    }

    @Override
    protected void recycle() {

    }

}
