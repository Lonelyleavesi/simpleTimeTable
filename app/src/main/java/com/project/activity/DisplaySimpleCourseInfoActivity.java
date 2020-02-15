package com.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.adapter.CoursesInfoAdapter;
import com.project.fragment.DisplayAllCourseFragment;
import com.project.item.Course;
import com.project.item.CourseSimpleInfo;
import com.project.tools.DebugHelper;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class DisplaySimpleCourseInfoActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_simple_courseinfo);
        initMember();
        displayView();
        boundListener();
        setListContextMenu();
        getSupportActionBar().hide();
    }


    private void initMember(){
        Intent intent = getIntent();
        courseSimpleInfos = new ArrayList<>();
        str_courseName = intent.getStringExtra("courseName");
        tv_courseName=findViewById(R.id.textView_simple_course_Name);
        button_back = findViewById(R.id.button_infocourse_back);
        lv_courseInfo = findViewById(R.id.listView_course_infos);
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
    List<CourseSimpleInfo> courseSimpleInfos;
    private void displayListView() {
        courseSimpleInfos.clear();
        List<Course>  courses = LitePal.where("name = ?",str_courseName).find(Course.class);
        if (courses.size() == 0)
            courses.add(new Course());
        Log.d("TimeTable","Courses size is " + courses.size());
        CourseSimpleInfo tempInfo = new CourseSimpleInfo(courses.get(0));
        for (int i = 1; i < courses.size() ; i++){
            Course course = courses.get(i);
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

    private void boundListener() {
        button_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_infocourse_back:{
                finish();
            }break;
       }
    }

    /**
     *创建上下文菜单
     */
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= this.getMenuInflater();
        inflater.inflate(R.menu.menu_showsimple_courseinfo,menu);
    }

    /**
     *为List绑定上下文菜单
     */
    private void  setListContextMenu(){
        registerForContextMenu(lv_courseInfo);
    }

    /**
     * 点击菜单注册事件
     * @param item 被点击的菜单选项
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        CourseSimpleInfo info = courseSimpleInfos.get(menuInfo.position);
        Log.d("TimeTable", String.valueOf( menuInfo.position));
        Log.d("TimeTable","You have just check day "+ info.getDay());
        switch (item.getItemId()){
            case R.id.menu_item_modifyCourseInfo:{
                Log.d("TimeTable","You Check modify course...");
                Intent intent = new Intent(this,ModifyCourseInfoActivity.class);
                startActivity(intent);
            }break;
            case R.id.menu_item_deleteCourseInfo:{
                LitePal.deleteAll(Course.class,"name = ? and day = ? and start_time = ? and end_time = ? and classroom = ? and teachername = ?",
                        str_courseName,info.getDay(),info.getCourseStart().toString(),info.getCourseEnd().toString(),info.getCourseRoom(),info.getTeacherName());
                displayListView();
                DisplayAllCourseFragment.updateCourseList();
            }break;
        }
        return true;
    }
}
