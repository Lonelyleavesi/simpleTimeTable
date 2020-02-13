package com.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.project.adapter.CoursesInfoAdapter;
import com.project.item.Course;
import com.project.item.CourseSimpleInfo;
import com.project.tools.DebugHelper;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class DisplaySimpleCourseInfo extends AppCompatActivity implements View.OnClickListener {


    Button button_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_simple_courseinfo);
        initMember();
        boundListener();
        displayView();
        getSupportActionBar().hide();
    }


    private void initMember(){
        Intent intent = getIntent();
        str_courseName = intent.getStringExtra("courseName");
        tv_courseName=findViewById(R.id.textView_simple_course_Name);
        button_back = findViewById(R.id.button_infocourse_back);
        lv_courseInfo = findViewById(R.id.listView_course_infos);
    }

    private void boundListener() {
        button_back.setOnClickListener(this);
    }

    private void displayView(){
        displayCourseName();
        displayListView();
    }

    TextView tv_courseName;
    String str_courseName;
    private void displayCourseName() {
        tv_courseName.setText(str_courseName);
    }

    ListView lv_courseInfo;
    private void displayListView() {
        List<Course>  courses = LitePal.where("name = ?",str_courseName).find(Course.class);
        List<CourseSimpleInfo> courseSimpleInfos = new ArrayList<>();
        List<Integer> weeks = new ArrayList<>();
        CourseSimpleInfo tempInfo = new CourseSimpleInfo(courses.get(0));
        Course course = courses.get(0);
        for (int i = 1; i < courses.size() ; i++){
            course = courses.get(i);
            if (tempInfo.equals(new CourseSimpleInfo(course))){
                tempInfo.weeks.add(course.getWeekNo());
            }else{
                DebugHelper.showCourse(course);
                courseSimpleInfos.add(tempInfo);
                tempInfo = new CourseSimpleInfo(course);
            }
        }
        courseSimpleInfos.add(tempInfo);
        CoursesInfoAdapter coursesInfoAdapter = new CoursesInfoAdapter(this,R.layout.item_coursetime_in_courseinfo,courseSimpleInfos);
        lv_courseInfo.setAdapter(coursesInfoAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_infocourse_back:{
                finish();
            }break;
       }
    }
}
