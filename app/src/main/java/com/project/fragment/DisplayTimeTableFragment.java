package com.project.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.activity.R;
import com.project.item.Course;
import com.project.item.CourseTime;
import com.project.tools.CalendarTranslate;
import com.project.tools.CustomTime;
import com.project.tools.DataBaseCustomTools;
import com.project.tools.DebugHelper;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.VALUE_RINGTONE_SILENT;

public class DisplayTimeTableFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    static final int MONDAY_IN_WEEK = 2;
    static final int DAYS_NUM_IN_ONE_WEEK = 7;
    static final int TABLE_COURSE_ITEM_HEIGHT = 300;
    static final int TABLE_COLUMN_NUM = 8;
    static final int TABLE_MIN_ROW_NUM = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_display_timetable,container,false);
       initMember(view);
       bindListener();
       bindArrayToLayout();
       upDateTimeTable(currentCheckWeek);
       return view;
    }

    static int currentWeek = 0;
    Button buttonSetAlarm;
    static Context context;
    static TableRow timeTable;
    static LinearLayout [ ] daysLayoutInWeek;
    /**
     * 初始化成员变量
     * @param view 碎片的布局对象
     */
    private void initMember(View view) {
        context = getContext();
        buttonSetAlarm = view.findViewById(R.id.button_set_alarm);
        data_row = (TableRow) view.findViewById(R.id.tableRow_date);
        timeTable = (TableRow) view.findViewById(R.id.tableRow_timeTable);
        daysLayoutInWeek = new LinearLayout[TABLE_COLUMN_NUM];

        DataBaseCustomTools.updateWeekInfo(getContext());
        currentWeek = DataBaseCustomTools.getCurrentWeek(getContext());
        currentCheckWeek = currentWeek;

        currentWeekSpinner = (Spinner) view.findViewById(R.id.spinner_currentweek);
        updateCurrentSpinner();
        currentWeekSpinner.setSelection(currentCheckWeek);

        updateDataRow();
    }

    private static Spinner currentWeekSpinner;
    public static int currentCheckWeek;
    static List<String> spinnerWeekList =new ArrayList<>();
    /**
     * 初始化选择第几周的spinner 默认周数为1~25周
     * @author chen yujie
     */
    private void updateCurrentSpinner(){
        updateWeekList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinnerWeekList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currentWeekSpinner.setAdapter(adapter);
        currentWeekSpinner.setOnItemSelectedListener(this);
    }
    private static void updateWeekList(){
        spinnerWeekList= new ArrayList<String>();
        if (currentCheckWeek == 0){
            spinnerWeekList.add("假期中");
        }else{
            spinnerWeekList.add("0");
        }
        for (int i = 1 ; i <= 25 ; i++){
            if ( i == currentCheckWeek)
            {
                spinnerWeekList.add(i+",本周");
            }else{
                spinnerWeekList.add(Integer.toString(i));
            }
        }
    }

    public void bindListener(){
        buttonSetAlarm.setOnClickListener(this);
    }


    private void bindArrayToLayout() {
        for (int i = 0 ; i < timeTable.getChildCount(); i ++){
            daysLayoutInWeek[i] = (LinearLayout) timeTable.getChildAt(i);
        }
    }

    /**
     *  根据数据库中的数据更新
     * @author chen yujie
     * @param week  表示第几周
     */
    public static void upDateTimeTable( int week){
        clearTable();
        updateWeekList();
        updateDataRow();
        updateTable(week);
    }

    private static void clearTable() {
       for (int i = 0 ; i < daysLayoutInWeek.length ; i ++){
           daysLayoutInWeek[i].removeAllViews();
        }
    }

    private static Calendar[] calendars;  //用于储存一周7天的日期
    private static TableRow   data_row;   //星期栏的textView组

    /**
     * 更新星期栏
     */
    @TargetApi(Build.VERSION_CODES.N)
    private static void updateDataRow() {
        updateCalendar();
        TextView monthTextView = (TextView) data_row.getChildAt(0);
        monthTextView.setText(calendars[0].get(Calendar.MONTH)+1+"月");
        for (int i = 1 ; i < data_row.getChildCount(); i++){
            TextView temp = (TextView) data_row.getChildAt(i);
            int month = (calendars[i-1].get(Calendar.MONTH)+1);
            int day = calendars[i-1].get(Calendar.DAY_OF_MONTH);
            temp.setText("星期"+i+"\n"+month+"/"+day);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static void updateCalendar(){
        calendars = new Calendar[7];
        for (int i = 0 ; i < 7 ; i++){
            calendars[i] = Calendar.getInstance();
            //定位星期到周一
            calendars[i].add(Calendar.DAY_OF_YEAR,MONDAY_IN_WEEK-calendars[i].get(Calendar.DAY_OF_WEEK));
           //定位周到当前查看的周
            calendars[i].add(Calendar.DAY_OF_YEAR,(currentCheckWeek-currentWeek)*DAYS_NUM_IN_ONE_WEEK);
            calendars[i].add(Calendar.DAY_OF_YEAR,i);
        }
    }

    private static void updateTable(int week) {
        int maxRowNum = TABLE_MIN_ROW_NUM;
        //按天寻找每一个
        for (int i = 1 ; i <= DAYS_NUM_IN_ONE_WEEK ; i ++){
            List<Course> coursesInDay = LitePal.where("weekno = ? and day = ?",currentCheckWeek+"",i+"")
                                                .order("start_time")
                                                .find(Course.class);
            int start_no = 1;
            for (Course course : coursesInDay){
                DebugHelper.showCourse(course);
                maxRowNum = course.getEnd_time() > maxRowNum ? course.getEnd_time() : maxRowNum;
                while (start_no < course.getStart_time()){
                    start_no ++;
                    addBlankToDayLinearLayout(daysLayoutInWeek[i]);
                }
                addCourseToDay(course,daysLayoutInWeek[i]);
                start_no = course.getEnd_time()+1;
            }
        }
        addCourseNoToNoLayout(maxRowNum,daysLayoutInWeek[0]);
    }

    /**
     * 为课程表某一列添加课程
     * @param course
     * @param dayLayout
     */
    private static void addCourseToDay(Course course, LinearLayout dayLayout){
        TextView courseTextView = new TextView(context);
        int viewLength = course.getEnd_time() - course.getStart_time() +1;
        courseTextView.setHeight(TABLE_COURSE_ITEM_HEIGHT * viewLength);
        courseTextView.setGravity(Gravity.CENTER);
        courseTextView.setBackgroundResource(R.drawable.coursetable_courseitem_border);
        courseTextView.setText(course.getName()+"\n"+course.getClassRoom());
        dayLayout.addView(courseTextView);
    }

    /**
     * 为课程表某一列添加空行
     * @param dayLayout
     */
    private static void addBlankToDayLinearLayout( LinearLayout dayLayout){
        TextView blank = new TextView(context);
        blank.setHeight(TABLE_COURSE_ITEM_HEIGHT);
        blank.setVisibility(View.INVISIBLE);
        dayLayout.addView(blank);
    }

    /**
     * 为课程表添加序号
     * @param num
     * @param courseNoLayout
     */
    private static void addCourseNoToNoLayout(int num, LinearLayout courseNoLayout) {
        for (int i = 1 ; i <= num; i ++){
            TextView courseNo = new TextView(context);
            courseNo.setHeight(TABLE_COURSE_ITEM_HEIGHT);
            courseNo.setText(i+"");
            courseNo.setGravity(Gravity.CENTER);
            courseNo.setBackgroundResource(R.drawable.coursetable_courseno_border);
            courseNoLayout.addView(courseNo);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_currentweek:{
                String str_week=parent.getItemAtPosition(position).toString();
                if (str_week == "假期中")
                {
                    currentCheckWeek =0;
                }else {
                    currentCheckWeek = Integer.parseInt(str_week.split(",")[0]);
                }
                upDateTimeTable(currentCheckWeek);
            }break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_set_alarm:{
                if (currentWeek == 0){
                    Toast.makeText(getContext(),"假期中无法设置提醒功能..",Toast.LENGTH_SHORT).show();
                    return ;
                }
                ConfirmDialogFragment dialog = new ConfirmDialogFragment();
                dialog.setContent("设置提醒功能时请务必查看设置中各节课上课时间，会为本周接下来所有课程基于设置的上课时间设置闹钟。点击确定则设置提醒闹钟");
                dialog.setDialogClickListener(new ConfirmDialogFragment.onDialogClickListener() {
                    @Override
                    public void onSureClick() {
                        createAlarms();
                    }
                    @Override
                    public void onCancelClick() {
                        //这里是取消操作
                    }
                });
                dialog.show(getFragmentManager(),"");

            }break;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void createAlarms() {
        //包括三项，是否响铃，是否震动，提前时长
        String [] alarmsetting = DataBaseCustomTools.getAlarmSetting(getContext());
        if (alarmsetting == null){
            ConfirmDialogFragment dialog = new ConfirmDialogFragment();
            dialog.setContent("请于设置页面添加相关课程提醒设置再使用此功能。");
            dialog.show(getFragmentManager(),"");
            return;
        }
        ArrayList<CourseTime> courseTimes = DataBaseCustomTools.getCourseTime(getContext());
        Calendar rightNow = Calendar.getInstance();
        int todayInWeek = CalendarTranslate.calendarDayToActualDay(rightNow.get(Calendar.DAY_OF_WEEK));
        List<Course> needAlarm = LitePal.where( "weekno = ? and day >= ?",currentWeek+"",todayInWeek+"")
                                        .find(Course.class);
        for (Course course : needAlarm){
            DebugHelper.showCourse(course);
            createSimpleAlarm(course,alarmsetting,courseTimes);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void createSimpleAlarm(Course course, String [] alarmSetting, ArrayList<CourseTime> courseTimes){
        Calendar rightNow = Calendar.getInstance();
        int todayInWeek = CalendarTranslate.calendarDayToActualDay(rightNow.get(Calendar.DAY_OF_WEEK));
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);

        CustomTime alarmTime = courseTimes.get(course.getStart_time()-1).start_time;
        alarmTime.addMinute(0-Integer.parseInt(alarmSetting[2]));
        if( course.getDay() == todayInWeek && alarmTime.hour <= hour){
            if (alarmTime.minute <= minute){
              return ;
            }
        }
        //设置时间
        Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarm .putExtra(AlarmClock.EXTRA_HOUR, alarmTime.hour);
        alarm .putExtra(AlarmClock.EXTRA_MINUTES, alarmTime.minute);

        //设置天
        ArrayList<Integer> alarmDay = new ArrayList<>();
        alarmDay.add(CalendarTranslate.actualDayToCalendarDay(course.getDay()));
        alarm.putExtra(AlarmClock.EXTRA_DAYS,alarmDay);

        //设置是否响铃
        if (Integer.parseInt(alarmSetting[0]) == 0){
            alarm.putExtra(AlarmClock.EXTRA_RINGTONE, VALUE_RINGTONE_SILENT);
        }
        //设置震动
        if (Integer.parseInt(alarmSetting[1]) == 1){
            alarm .putExtra(AlarmClock.EXTRA_VIBRATE, true);
        }
        //闹铃信息
        alarm.putExtra(AlarmClock.EXTRA_MESSAGE, course.getName()+" 课程提醒");
        //如果为true，则调用startActivity()不会进入手机的闹钟设置界面
        alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);

        if (alarm.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(alarm);
        }
    }
}
