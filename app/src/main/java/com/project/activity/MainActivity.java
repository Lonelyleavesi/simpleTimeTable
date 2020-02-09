package com.project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.fragment.timeTableFragment;
import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boundButtonToListener();
        replaceFragment(new timeTableFragment());
        LitePal.getDatabase();
        getSupportActionBar().hide();
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
    }

    static boolean init = true;
    /**
     * 替换主页面所使用的的函数，将主页面变为传入的fragment
     * 在已经替换成课程表碎片的情况下继续点课程表将只进行刷新操作并返回，为了防止第一次运行时也返回，添加init变量
     * @param fragment   需要展示在主页面的fragment
     * @author chen yujie
     */
    private void replaceFragment(Fragment fragment){
        if (fragment.getClass().equals(timeTableFragment.class) && !init)
        {
            timeTableFragment.upDateTimeTable(timeTableFragment.currentWeek);
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout,fragment);
        transaction.commit();
        init = false;
    }

    /**
     * 设置按钮的监听器
     * @author chen yujie
     * @param v 被按下的按钮的view实例
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_displaytable:{
                replaceFragment(new timeTableFragment());
            }break;
            case R.id.button_addCourse:{
                Intent intent = new Intent(MainActivity.this, addCourseActivity.class);
                startActivity(intent);
            }break;
        }
    }
}
