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
        coursesInfoAdapter = new CoursesInfoAdapter(this,R.layout.item_coursetime_in_courseinfo,courseSimpleInfos);
    }

    /**
     * 展示页面，包括标题以及下面的listView
     */
    private void displayView(){
        displayCourseName();
        displayListView();
    }

    TextView tv_courseName;
    static String str_courseName;
    private void displayCourseName() {
        tv_courseName.setText(str_courseName);
    }

    static ListView lv_courseInfo;
    static List<CourseSimpleInfo> courseSimpleInfos;
    static CoursesInfoAdapter coursesInfoAdapter;
    /**
     * 展示listView，先根据课程名查找出所有Course对象，然后封装成CourseInfo对象，然后加入list展示
     */
    public static void displayListView() {
        if (courseSimpleInfos == null ||  str_courseName == null || lv_courseInfo == null)
            return ;
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
        lv_courseInfo.setAdapter(coursesInfoAdapter);
    }

    private void boundListener() {
        button_back.setOnClickListener(this);
        lv_courseInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseSimpleInfo info = courseSimpleInfos.get(position);
                Intent intent = getModifyIntent(info);
                startActivity(intent);
            }
        });
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
        switch (item.getItemId()){
            case R.id.menu_item_modifyCourseInfo:{
                Intent intent = getModifyIntent(info);
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

    /**
     * 进入修改课程信息页面时传递数据，其中week通过 ， 符号将所有week连成一个字符串
     * @param info  需要进行修改的数据，即课程的部分信息
     * @return  进入修改页面的intent对象
     */
    private Intent getModifyIntent(CourseSimpleInfo info){
        Intent intent = new Intent(this,ModifyCourseInfoActivity.class);
        intent.putExtra("courseName",str_courseName);
        intent.putExtra("day",info.getDay());
        intent.putExtra("courseStart",info.getCourseStart().toString());
        intent.putExtra("courseEnd",info.getCourseEnd().toString());
        intent.putExtra("courseRoom",info.getCourseRoom());
        intent.putExtra("teacher",info.getTeacherName());
        StringBuffer sb = new StringBuffer();
        for(Integer week : info.getWeeks()){
            sb.append(week.toString()+",");
        }
        intent.putExtra("weeks",sb.toString());
        return intent;
    }
}
