package com.project.activity;

import android.os.Bundle;

public class ModifyCourseInfoActivity extends AddCourseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcourse);
        initMember();
        boundButtonToListener();
        getSupportActionBar().hide();
    }

    @Override
    protected void initMember() {
        super.initMember();

    }
}
