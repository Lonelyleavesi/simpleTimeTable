package com.project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.fragment.AddCourseFragment;
import com.project.fragment.DisplayAllCourseFragment;
import com.project.fragment.DisplayTimeTableFragment;
import com.project.fragment.SetInterfaceFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMember();
        boundButtonToListener();
        replaceFragment(new DisplayTimeTableFragment());
        getSupportActionBar().hide();
    }

    private void initMember(){
        displayTimeTableFragment = new DisplayTimeTableFragment();
        addCourseFragment = new AddCourseFragment();
        displayAllCourseFragment = new DisplayAllCourseFragment();
        setInterfaceFragment = new SetInterfaceFragment();
    }

    /**
     * 绑定按钮和监听器逻辑
     * @author chen yujie
     */
    private void boundButtonToListener(){
        Button displayTable = (Button) findViewById(R.id.button_displaytable);
        displayTable.setOnClickListener(this);
        Button addCourse = (Button)findViewById(R.id.button_addCourse);
        addCourse.setOnClickListener(this);
        Button showAllCourses = (Button) findViewById(R.id.button_showAllCourse);
        showAllCourses.setOnClickListener(this);
        Button setInterface = (Button) findViewById(R.id.button_setInterface);
        setInterface.setOnClickListener(this);
    }

    /**
     * 替换主页面所使用的的函数，将主页面变为传入的fragment
     * @param fragment   需要展示在主页面的fragment
     * @author chen yujie
     */
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout,fragment);
        transaction.commit();
    }


    DisplayTimeTableFragment displayTimeTableFragment;
    DisplayAllCourseFragment displayAllCourseFragment;
    AddCourseFragment addCourseFragment;
    SetInterfaceFragment setInterfaceFragment;
    /**
     * 设置按钮的监听器
     * @author chen yujie
     * @param v 被按下的按钮的view实例
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_displaytable:{
                replaceFragment(displayTimeTableFragment);
            }break;
            case R.id.button_addCourse:{
                replaceFragment(addCourseFragment);
            }break;
            case R.id.button_showAllCourse:{
                replaceFragment(displayAllCourseFragment);
            }break;
            case R.id.button_setInterface:{
                replaceFragment(setInterfaceFragment);
            }
        }
    }

    private long firstTime = 0;
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }
}
