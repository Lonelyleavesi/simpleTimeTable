package com.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.fragment.ConfirmDialogFragment;
import com.project.item.Course;
import com.project.tools.DataBaseCustomTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 复用addCourseActivity 从而使修改课程方便
 */
public class ModifyCourseInfoActivity extends AddCourseActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modifyInit();
        modifyButtonListen();
    }

    private void modifyInit(){
        deletedCourse = new ArrayList<>();
        TextView title = findViewById(R.id.textView_addCourse_title);
        title.setText("修改课程");
        Button finishButton = (Button)findViewById(R.id.button_addcourse_finish);
        finishButton.setText("修改课程");
    }

    List<Course> deletedCourse;
    private void modifyButtonListen() {
        Button finishButton = (Button)findViewById(R.id.button_addcourse_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialogFragment dialog = new ConfirmDialogFragment();
                dialog.setContent("确认修改课程吗？一旦确认无法撤销操作。");
                dialog.setDialogClickListener(new ConfirmDialogFragment.onDialogClickListener() {
                    @Override
                    public void onSureClick() {
                        for(Integer old_week : old_weeks){
                            deletedCourse.add(new Course(old_courseName,old_teacher,old_courseRoom,old_day,old_courseStart,old_courseEnd,old_week));
                        }
                        addCourse();
                        DisplaySimpleCourseInfoActivity.displayListView();
                    }
                    @Override
                    public void onCancelClick() {
                        //这里是取消操作
                    }
                });
                dialog.show(getSupportFragmentManager(),"");
            }
        });
    }

    String old_courseName;
    String old_courseRoom;
    String old_teacher;
    Integer old_courseStart;
    Integer old_courseEnd;
    Integer old_day;
    Set<Integer> old_weeks;
    /**
     * 在addCourse活动的基础上，取得Intent传来的数据，保存修改前的数据，添加课程的时候删除旧数据，再添加新数据
     */
    @Override
    protected void initMember() {
        super.initMember();
        intent = getIntent();
        old_courseName =intent.getStringExtra("courseName");
        editAddCourseName.setText(old_courseName);
        old_courseRoom = intent.getStringExtra("courseRoom");
        editAddCourseRoom.setText(old_courseRoom);
        old_teacher = intent.getStringExtra("teacher");
        editAddCourseTeacher.setText(old_teacher);

        old_courseStart = Integer.parseInt(intent.getStringExtra("courseStart"));
        courseStart = old_courseStart;
        old_courseEnd = Integer.parseInt(intent.getStringExtra("courseEnd"));
        courseEnd = old_courseEnd;

        old_day = Integer.parseInt(intent.getStringExtra("day"));
        daySelected.add(old_day);
        updateSelectDayAndNoButton();

        String [] weeks = intent.getStringExtra("weeks").split(",");
        old_weeks = new TreeSet<>();
        for (String week : weeks){
            old_weeks.add(Integer.parseInt(week));
        }
        weekSelected = old_weeks;
        updateSelectWeekButton();
    }

    @Override
    protected boolean addCourse() {
        for (Course deleteCourse : deletedCourse)
        {
            DataBaseCustomTools.deleteExistCourse(deleteCourse);
        }
       if( !super.addCourse()){
           for (Course deleteCourse : deletedCourse)
           {
              deleteCourse.save();
           }
       }
        return true;
    }
}
