package com.project.activity;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.fragment.timeTableFragment;
import com.project.item.course;
import com.project.tools.DebugHelper;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class addCourseActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener{

    private EditText addCourseName;
    private EditText addCourseTeacher;
    private EditText addCourseRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcourse);
        initMember();
        initSpinner();
        boundClickButtonToArray();
        boundButtonToListener();

        getSupportActionBar().hide();
    }

    /**
     * 初始化成员变量
     */
    private void initMember(){
        addCourseName = (EditText) findViewById(R.id.edit_addCourse_name);
        addCourseTeacher = (EditText) findViewById(R.id.edit_addCourse_teacherName);
        addCourseRoom = (EditText)  findViewById(R.id.edit_addCourse_classRoom);
        addCourseNoStart = (Spinner) findViewById(R.id.spinner_addcourseNo_start);
        courseStart = 0;
        addCourseNoEnd = (Spinner) findViewById(R.id.spinner_addcourseNo_end);
        courseEnd = 0;
        weekButtonArray = new ArrayList<>();
        weekButtonState = new Boolean[5][5];
        dayButtonArray = new ArrayList<>();
        dayButtonState = new Boolean[7];
        clickWeekTableLayout = (TableLayout) findViewById(R.id.tableLayout_clickweek);
        oddWeekBox = (CheckBox) findViewById(R.id.checkBox_oddweek);
        evenWeekBox= (CheckBox) findViewById(R.id.checkBox_evenweek);
        allWeekBox = (CheckBox) findViewById(R.id.checkBox_allweek);
    }

    private Spinner  addCourseNoStart;
    private int courseStart;
    private Spinner  addCourseNoEnd;
    private int courseEnd;
    /**
     * 初始化Spinner （用于选第几节课）
     */
    private void initSpinner(){
        List<String> courseNo= new ArrayList<String>();
        for (int i = 1 ; i <= 14 ; i++){
            courseNo.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,courseNo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addCourseNoStart.setAdapter(adapter);
        addCourseNoEnd.setAdapter(adapter);
        addCourseNoStart.setOnItemSelectedListener(this);
        addCourseNoEnd.setOnItemSelectedListener(this);
    }

    private TableLayout clickWeekTableLayout;
    private ArrayList<ArrayList<Button>> weekButtonArray;
    private Boolean [][] weekButtonState;
    private ArrayList<Button> dayButtonArray;
    private Boolean [] dayButtonState;
    /**
     * 将添加课程的周数储存在ArrayList中方便后续操作
     */
    private void boundClickButtonToArray() {
        TableRow[] weeksRow = new TableRow[clickWeekTableLayout.getChildCount()];
        for(int i=0;i<weeksRow.length;i++){
            weeksRow[i] = (TableRow) clickWeekTableLayout.getChildAt(i);
            ArrayList<Button> simpleRowView = new ArrayList<Button>();
            for (int j = 0 ; j < weeksRow[i].getChildCount(); j++){
                Button temp = (Button) weeksRow[i].getChildAt(j);
                simpleRowView.add(temp);
                weekButtonState[i][j] = false;
            }
            weekButtonArray.add(simpleRowView);
        }
        Log.d("TimeTable","The WeekButtonArray Size is "+ weekButtonArray.size()+"; And " +
                "the size of a item is "+ weekButtonArray.get(0).size());
        TableRow dayRow = findViewById(R.id.tableRow_day);
        for (int i = 0 ; i < dayRow.getChildCount() ; i ++)
        {
            Button temp = (Button) dayRow.getChildAt(i);
            dayButtonArray.add(temp);
            dayButtonState[i] = false;
        }
        Log.d("TimeTable","The DayButtonArray Size is "+ dayButtonArray.size());
        updateButtonState();
    }

    /**
     * 根据储存的按钮状态更新按钮的颜色。
     */
    private void updateButtonState(){
        int count = 1;
        for (int i = 0; i < weekButtonArray.size(); i ++){
            for (int j = 0 ; j < weekButtonArray.get(i).size(); j ++)
            {
                if (weekButtonState[i][j])
                {
                    weekButtonArray.get(i).get(j).setBackgroundResource(R.drawable.addcourse_weekbutton_clicked);
                }else
                {
                    weekButtonArray.get(i).get(j).setBackgroundResource(R.drawable.addcourse_weekbutton_unclicked);
                }
                weekButtonArray.get(i).get(j).setText(count+"");
                count++;
            }
        }
        for (int i = 0 ; i < dayButtonArray.size() ; i ++){
            if (dayButtonState[i])
            {
                dayButtonArray.get(i).setBackgroundResource(R.drawable.addcourse_day_clicked);
            }else{
                dayButtonArray.get(i).setBackgroundResource(R.drawable.addcourse_day_unclicked);
            }
        }
    }

    /**
     * 为所有按钮添加或绑定监听器
     * @author chen yujie
     */
    private void boundButtonToListener(){
        Button backButton =  (Button) findViewById(R.id.button_addcourse_back);
        backButton.setOnClickListener(this);
        Button finishButton = (Button)findViewById(R.id.button_addcourse_finish);
        finishButton.setOnClickListener(this);
        boundDayButtonsToListener();
        boundWeekButtonsToListener();
        boundCheckBoxToListener();
    }

    /**
     * 为每一个dayButton添加监听器
     */
    private  void boundDayButtonsToListener(){
        for (int i = 0 ; i < dayButtonArray.size() ; i ++){
                dayButtonArray.get(i).setOnClickListener(this);
        }
    }

    /**
     * 为每一个weekButton添加监听器，用于改变选中状态
     */
    private void boundWeekButtonsToListener(){
        //为weekButton添加监听器
        for (int i = 0; i < weekButtonArray.size(); i ++) {
            for (int j = 0; j < weekButtonArray.get(i).size(); j++) {
                weekButtonArray.get(i).get(j).setOnClickListener(this);
            }
        }
    }

    private CheckBox oddWeekBox;
    private CheckBox evenWeekBox;
    private CheckBox allWeekBox;
    /**
     * 用于为单双周 checkbox 添加监听器
     */
    private void boundCheckBoxToListener(){
        oddWeekBox.setOnCheckedChangeListener(this);
        evenWeekBox.setOnCheckedChangeListener(this);
        allWeekBox.setOnCheckedChangeListener(this);
    }

    /**
     * 使所有按钮状态归为未选中并更新；
     */
    private void clearWeekButtonState(){
        for (int i = 0 ;  i < weekButtonState.length; i++){
            for (int j = 0 ; j < weekButtonState[i].length ; j++){
                weekButtonState[i][j] = false;
            }
        }
        updateButtonState();
    }

    /**
     * 如果为返回按钮则返回，如果为添加课程按钮则添加课程
     * @param v  被点击的按钮对象
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_addcourse_back:{
                    finish();
            }break;
            case R.id.button_addcourse_finish:{
                addCourse();
            }break;
            case R.id.button_addCourse_day1:
            case R.id.button_addCourse_day2:
            case R.id.button_addCourse_day3:
            case R.id.button_addCourse_day4:
            case R.id.button_addCourse_day5:
            case R.id.button_addCourse_day6:
            case R.id.button_addCourse_day7:{
                Button self = (Button) findViewById(v.getId());
                int dayNum = Integer.parseInt(self.getText().toString());
                dayButtonState[dayNum-1] = !dayButtonState[dayNum-1];
                Log.d("TimeTable","You click Day Button "+dayNum+" And its state is "+ dayButtonState[dayNum-1]);
                updateButtonState();
            }break;
            default:{
                Button self = (Button) findViewById(v.getId());
                int weekNum = Integer.parseInt(self.getText().toString());
                clickButtonByNum(weekNum);
            }
        }
    }

    /**
     * 根据当前的页面内容添加课程至数据库中,对于每一天，每一周都算一条数据。
     */
    private void addCourse(){
        if (addCourseName.getText().toString().isEmpty()){
            Toast.makeText(this,"课程名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (courseEnd < courseStart){
            Toast.makeText(this,"课程结束课号不得小于课程开始课号",Toast.LENGTH_SHORT).show();
            return;
        }
        boolean dayAndWeekIsEmpty = true;
        List<course> courses = new ArrayList<>();
        for (int i = 0 ; i < dayButtonState.length ; i++){
            if (dayButtonState[i])
                for (int m = 0 ; m < weekButtonState.length; m ++)
                    for (int n = 0; n < weekButtonState[m].length ; n++)
                    {
                        if (weekButtonState[m][n])
                        {
                            int week = m*weekButtonState.length+n+1;
                            if (haveConflict(week,i+1,courseStart,courseEnd))
                            {
                                Toast.makeText(this,"添加课程失败，第"+week+"周,星期"+(i+1)+"存在课程冲突",
                                        Toast.LENGTH_SHORT).show();
                                return ;
                            }
                            dayAndWeekIsEmpty = false;
                            course course = new course();
                            course.setName(addCourseName.getText().toString());
                            course.setTeacherName(addCourseTeacher.getText().toString());
                            course.setClassRoom(addCourseRoom.getText().toString());
                            course.setDay(i+1);
                            course.setStart(courseStart);
                            course.setEnd(courseEnd);
                            course.setWeekNo(week);
                            courses.add(course);
                            DebugHelper.showCourse(course);
                        }
                    }
        }
        if (dayAndWeekIsEmpty){
            Toast.makeText(this,"添加课程失败，周与星期不可为空",Toast.LENGTH_SHORT).show();
            return;
        }
        //不存在冲突 则开始添加课程
        submitCourse(courses);
        Toast.makeText(this,"添加课程成功",Toast.LENGTH_SHORT).show();
        timeTableFragment.upDateTimeTable(timeTableFragment.currentWeek);
        finish();
    }

    /**
     *传入周数，天数，上课的开始以及结束，检查是否已经有课与新添加的课程冲突
     * @return
     */
    private boolean haveConflict(int week, int day, int start,int end){
        List<course> courses = LitePal.where("weekNo = ? and day = ?", week+"",day+"").find(course.class);
        for (course course : courses){
            DebugHelper.showCourse(course);
            if (!(course.getStart()> end || course.getEnd() < start))
            {
                return true;
            }
        }
        return false;
    }

    private void submitCourse(List<course> courses){
        for (course course:courses){
            course.save();
        }
    }
    /**
     * 此函数用于通过传入周数更新该周数被点击的状态，
     * @author chen yujie
     * @param weekNum 被点击的周数
     */
    private void clickButtonByNum( int weekNum){
        weekButtonState[(weekNum-1)/5][(weekNum-1)%5] = !weekButtonState[(weekNum-1)/5][(weekNum-1)%5];
        Log.d("TimeTable","You click Week Button "+weekNum+" And its state is "+ weekButtonState[(weekNum-1)/5][(weekNum-1)%5]);
        updateButtonState();
    }

    /**
     *为单双周，全部周checkbox 添加监听器
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkBox_oddweek:{
                        if (isChecked){
                            evenWeekBox.setChecked(false);
                            allWeekBox.setChecked(false);
                            for (int i = 0 ;  i < weekButtonState.length; i++){
                                for (int j = 0 ; j < weekButtonState[i].length ; j++){
                                    if ((j+i) %2 == 0 )
                                        weekButtonState[i][j]=true;
                                    else
                                        weekButtonState[i][j]=false;
                                }
                                updateButtonState();
                            }
                        } else
                        {
                            clearWeekButtonState();
                        }
                    }break;
            case R.id.checkBox_evenweek:{
                        if (isChecked){
                            oddWeekBox.setChecked(false);
                            allWeekBox.setChecked(false);
                            for (int i = 0 ;  i < weekButtonState.length; i++){
                                for (int j = 0 ; j < weekButtonState[i].length ; j++){
                                    if ((j+i) %2 != 0 )
                                        weekButtonState[i][j]=true;
                                    else
                                        weekButtonState[i][j]=false;
                                }
                                updateButtonState();
                            }
                        } else
                        {
                            clearWeekButtonState();
                        }break;
                    }
            case R.id.checkBox_allweek:{
                        if (isChecked){
                            oddWeekBox.setChecked(false);
                            evenWeekBox.setChecked(false);
                            for (int i = 0 ;  i < weekButtonState.length; i++){
                                for (int j = 0 ; j < weekButtonState[i].length ; j++){
                                        weekButtonState[i][j]=true;
                                }
                                updateButtonState();
                            }
                        } else
                        {
                            clearWeekButtonState();
                        }break;
                    }
        }
    }

    /**
     * spinner的监听器
     * @param parent 用于区别点击的哪个spinner，通过parent.getId 对比
     * @param view   无
     * @param position 点击的item元素
     * @param id    是你选中的某个Spinner中的某个下来值所在的行，一般自上而下从0开始，
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_addcourseNo_start:{
                String str =parent.getItemAtPosition(position).toString();
                courseStart = Integer.parseInt(str);
            }break;
            case R.id.spinner_addcourseNo_end:{
                String str =parent.getItemAtPosition(position).toString();
                courseEnd = Integer.parseInt(str);
            }break;
        }
        Log.d("TimeTable", "onItemSelected: courseStart is " + courseStart + " courseEnd is "+courseEnd);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
