package com.project.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.fragment.ConfirmDialogFragment;
import com.project.fragment.DisplayAllCourseFragment;
import com.project.fragment.DisplayTimeTableFragment;
import com.project.fragment.SelectDayAndNoDialogFragment;
import com.project.fragment.SelectWeekDialogFragment;
import com.project.item.Course;
import com.project.tools.DebugHelper;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener{

    protected EditText editAddCourseName;
    protected EditText editAddCourseTeacher;
    protected EditText editAddCourseRoom;
    protected Button getButtonSelectDayAndNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcourse);
        initMember();
        boundButtonToListener();
        getSupportActionBar().hide();
    }

    /**
     * 初始化成员变量
     */
    protected void initMember(){
        weekSelected = new TreeSet<>();
        daySelected = new TreeSet<>();
        editAddCourseName = (EditText) findViewById(R.id.edit_addCourse_name);
        editAddCourseTeacher = (EditText) findViewById(R.id.edit_addCourse_teacherName);
        editAddCourseRoom = (EditText)  findViewById(R.id.edit_addCourse_classRoom);
        getButtonSelectDayAndNo = (Button) findViewById(R.id.button_select_day_and_no);
        buttonSelectWeeks = (Button) findViewById(R.id.button_select_week);
        courseEnd = 0;
        courseStart = 0;
    }



    /**
     * 为所有按钮添加或绑定监听器
     * @author chen yujie
     */
    protected void boundButtonToListener(){
        Button backButton =  (Button) findViewById(R.id.button_addcourse_back);
        backButton.setOnClickListener(this);
        Button finishButton = (Button)findViewById(R.id.button_addcourse_finish);
        finishButton.setOnClickListener(this);
        buttonSelectWeeks.setOnClickListener(this);
        getButtonSelectDayAndNo.setOnClickListener(this);
    }


    protected Integer courseEnd;
    protected Integer courseStart;
    protected Set<Integer> daySelected;
    protected Set<Integer> weekSelected;
    /**
     * 如果为返回按钮则返回，如果为添加课程按钮则添加课程
     * @param v  被点击的按钮对象
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_addcourse_back:{
                ConfirmDialogFragment dialog = new ConfirmDialogFragment();
                dialog.setContent("确认放弃添加课程吗？");
                dialog.setDialogClickListener(new ConfirmDialogFragment.onDialogClickListener() {
                    @Override
                    public void onSureClick() {
                        finish();
                    }
                    @Override
                    public void onCancelClick() {
                        //这里是取消操作
                    }
                });
                dialog.show(getSupportFragmentManager(),"");
            }break;
            case R.id.button_addcourse_finish:{
                ConfirmDialogFragment dialog = new ConfirmDialogFragment();
                dialog.setContent("确认添加课程吗？一旦确认无法撤销操作。");
                dialog.setDialogClickListener(new ConfirmDialogFragment.onDialogClickListener() {
                    @Override
                    public void onSureClick() {
                        addCourse();
                    }
                    @Override
                    public void onCancelClick() {
                        //这里是取消操作
                    }
                });
                dialog.show(getSupportFragmentManager(),"");
            }break;
            case R.id.button_select_day_and_no:{
                final SelectDayAndNoDialogFragment fragment = new SelectDayAndNoDialogFragment();
                fragment.daySelected = daySelected;
                fragment.courseStart = courseStart;
                fragment.courseEnd = courseEnd;
                fragment.setDialogClickListener(new SelectDayAndNoDialogFragment.SelectDayAndNoClickListener() {
                    @Override
                    public void onSureClick() {
                        daySelected = fragment.daySelected ;
                        courseStart =fragment.courseStart;
                        courseEnd = fragment.courseEnd ;
                        updateSelectDayAndNoButton();
                    }
                });
                fragment.show(getSupportFragmentManager(),"");
            }break;
            case R.id.button_select_week:{
                final SelectWeekDialogFragment fragment = new SelectWeekDialogFragment();
                fragment.weeksSelected=weekSelected;
                fragment.setDialogClickListener(new SelectWeekDialogFragment.SelectWeeksClickListener() {
                    @Override
                    public void onSureClick() {
                        weekSelected = fragment.weeksSelected;
                        updateSelectWeekButton();
                    }
                });
                fragment.show(getSupportFragmentManager(),"");
            }break;
            default:{

            }
        }
    }


    /**
     * 根据当前的页面内容添加课程至数据库中,对于每一天，每一周都算一条数据。
     */
    protected boolean addCourse(){
        if (editAddCourseName.getText().toString().isEmpty()){
            Toast.makeText(this,"课程名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (courseEnd < courseStart){
            Toast.makeText(this,"课程结束课号不得小于课程开始课号",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (daySelected.isEmpty() || weekSelected.isEmpty()){
            Toast.makeText(this,"添加课程失败，周与星期不可为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        List<Course> courses = new ArrayList<>();
        for (Integer day : daySelected)
            for (Integer week: weekSelected)
            {
                if (haveConflict(week,day,courseStart,courseEnd))
                {
                    Toast.makeText(this,"添加课程失败，第"+week+"周,星期"+(day)+"存在课程冲突",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                Course course = new Course();
                course.setName(editAddCourseName.getText().toString());
                course.setTeacherName(editAddCourseTeacher.getText().toString());
                course.setClassRoom(editAddCourseRoom.getText().toString());
                course.setDay(day);
                course.setStart_time(courseStart);
                course.setEnd_time(courseEnd);
                course.setWeekNo(week);
                courses.add(course);
                DebugHelper.showCourse(course);
            }
        //不存在冲突 则开始添加课程
        submitCourse(courses);
        Toast.makeText(this,"添加课程成功",Toast.LENGTH_SHORT).show();
        DisplayTimeTableFragment.upDateTimeTable(DisplayTimeTableFragment.currentCheckWeek);
        DisplayAllCourseFragment.updateCourseList();
        finish();
        return true;
    }

    /**
     *传入周数，天数，上课的开始以及结束，检查是否已经有课与新添加的课程冲突
     * @return
     */
    protected boolean haveConflict(int week, int day, int start,int end){
        List<Course> courses = LitePal.where("weekNo = ? and day = ?", week+"",day+"").find(Course.class);
        for (Course course : courses){
            DebugHelper.showCourse(course);
            if (!(course.getStart_time()> end || course.getEnd_time() < start))
            {
                return true;
            }
        }
        return false;
    }

    private void submitCourse(List<Course> cours){
        for (Course course: cours){
            course.save();
        }
    }

    /**
     * 更新周数按钮的显示
     */
    protected Button buttonSelectWeeks;
    protected void updateSelectWeekButton(){
        StringBuffer sb = new StringBuffer("周数: ");
        for (Integer week : weekSelected)
        {
            sb.append(week+" ");
        }
        buttonSelectWeeks.setText(sb.toString());
    }

    /**
     * 更新天数按钮的显示
     */
    protected void updateSelectDayAndNoButton(){
        StringBuffer sb = new StringBuffer("节数: 星期(");
        for (Integer day : daySelected){
            sb.append( day+" ");
        }
        sb.append(")；第").append(courseStart).append("至").append(courseEnd).append("节课");
        getButtonSelectDayAndNo.setText(sb.toString());
    }

    @Override
    public void onBackPressed() {
        ConfirmDialogFragment dialog = new ConfirmDialogFragment();
        dialog.setContent("确认放弃添加课程吗？");
        dialog.setDialogClickListener(new ConfirmDialogFragment.onDialogClickListener() {
            @Override
            public void onSureClick() {
                finish();
            }
            @Override
            public void onCancelClick() {
                //这里是取消操作
            }
        });
        dialog.show(getSupportFragmentManager(),"");
    }

}
