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
     * @author chen yujie
     * 绑定按钮和监听器逻辑
     */
    private void boundButtonToListener(){
        Button displayTable = (Button) findViewById(R.id.button_displaytable);
        displayTable.setOnClickListener(this);
        Button addCourse = (Button)findViewById(R.id.button_addcourse);
        addCourse.setOnClickListener(this);
    }

    /**
     * @param fragment   需要展示在主页面的fragment
     * @author chen yujie
     * 替换主页面所使用的的函数，将主页面变为传入的fragment
     */
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout,fragment);
        if (fragment.getClass().equals(addCourseActivity.class))
        {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    /**
     * @author chen yujie
     * @param v 被按下的按钮的view实例
     * 设置按钮的监听器
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_displaytable:{
                replaceFragment(new timeTableFragment());
            }
            case R.id.button_addcourse:{
                Intent intent = new Intent(MainActivity.this, addCourseActivity.class);
                startActivity(intent);
            }
        }
    }
}
