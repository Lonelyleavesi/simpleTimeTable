package com.project.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.activity.R;
import com.project.adapter.SetCourseTimeAdapter;
import com.project.item.CourseTime;
import com.project.tools.DataBaseCustomTools;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author  chen yujie
 */
public class SetInterfaceFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    private final int CLASS_MAX_NUM = 14;
    private final String TAG = "TimeTable";
    private final int MONDAY_IN_WEEK = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_interface,container,false);
        initMember(view);
        setViewListener();
        displayRecycleView();
        return view;
    }

    RecyclerView recycleCoursesTime;
    LinearLayoutManager layoutManager;
    List<CourseTime>  courseTimeList;
    SetCourseTimeAdapter courseTimeAdapter;
    Button confirmButton;
    private void initMember(View view) {
        int_alarmAdvanceTime = 0;
        currentWeek = DataBaseCustomTools.getCurrentWeek(getContext());
        courseTimeList=new ArrayList<>();
        initCourseTimeList();
        recycleCoursesTime = (RecyclerView) view.findViewById(R.id.recycle_setClassesTime);
        layoutManager = new LinearLayoutManager(getContext());
        courseTimeAdapter = new SetCourseTimeAdapter(courseTimeList,getContext());
        confirmButton = (Button) view.findViewById(R.id.button_set_confirm);
        currentWeekSpinner = (Spinner) view.findViewById(R.id.spinner_setCurrentWeek);
        spinner_alarmAdvanceTime = (Spinner) view.findViewById(R.id.spinner_alarm_adavance_time);
        updateCurrentSpinner();
        currentWeekSpinner.setSelection(currentWeek);
        alarmMode_ring = (CheckBox)  view.findViewById(R.id.checkBox_alarm_ringMode);
        alarmMode_vibrate = (CheckBox)  view.findViewById(R.id.checkBox_alarm_vibrateMode);
    }

    private Spinner currentWeekSpinner;
    public static int currentWeek;
    private Spinner spinner_alarmAdvanceTime;
    private int int_alarmAdvanceTime;
    /**
     * 初始化选择第几周的spinner 默认周数为1~25周
     * 以及设置闹钟提前时间的spinner
     * @author chen yujie
     */
    private void updateCurrentSpinner(){
        List<String> weekList= new ArrayList<String>();
        weekList.add("假期中");
        for (int i = 1 ; i <= 25 ; i++){
            weekList.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,weekList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currentWeekSpinner.setAdapter(adapter);
        currentWeekSpinner.setOnItemSelectedListener(this);

        List<String> alarmAdvancedTime= new ArrayList<String>();
        for (int i = 0 ; i <= 60 ; i = i + 15){
            alarmAdvancedTime.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,alarmAdvancedTime);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_alarmAdvanceTime.setAdapter(adapter2);
        spinner_alarmAdvanceTime.setOnItemSelectedListener(this);
    }

    /**
     * 展示选择课程时间的recycleView
     */
    private void displayRecycleView() {
        recycleCoursesTime.setLayoutManager(layoutManager);
        recycleCoursesTime.setAdapter(courseTimeAdapter);
    }

    private  void initCourseTimeList(){
        courseTimeList = DataBaseCustomTools.getCourseTime(getContext());
        if (courseTimeList.size() != 0)
            return ;
        for (int i = 0 ; i < CLASS_MAX_NUM ; i ++)
        {
            CourseTime temp = new CourseTime();
            temp.setNo(i+1);
            courseTimeList.add(temp);
        }
    }

    private void setViewListener() {
        alarmMode_ring.setOnCheckedChangeListener(this);
        alarmMode_vibrate.setOnCheckedChangeListener(this);
        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_set_confirm:{
                    saveSetting();
            }break;
        }
    }

    /**
     * 写进当前设置
     * @throws IOException
     */
    private void saveSetting() {
        try {
            saveCurrentWeek();
            saveCoursesTimes();
            saveAlarmSetting();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void saveCurrentWeek()throws IOException{
        FileOutputStream out = null;
        BufferedWriter writer = null;
        out = getContext().openFileOutput("currentWeek", Context.MODE_PRIVATE);
        writer = new BufferedWriter(new OutputStreamWriter(out));
        writer.write(getCurrentData());
        writer.close();
    }

    private void saveCoursesTimes()throws IOException {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        out = getContext().openFileOutput("courseTime",Context.MODE_PRIVATE);
        writer = new BufferedWriter(new OutputStreamWriter(out));
        for (CourseTime courseTime : courseTimeList)
        {
            writer.write(courseTime.toString()+"\n");
        }
        writer.close();
    }

    private void saveAlarmSetting() throws IOException{
        FileOutputStream out = null;
        BufferedWriter writer = null;
        out = getContext().openFileOutput("alarmSetting",Context.MODE_PRIVATE);
        writer = new BufferedWriter(new OutputStreamWriter(out));
        writer.write(alarmIsRing+","+ alarmIsVibrate +","+int_alarmAdvanceTime);
        writer.close();
    }

    /**
     * 取得当前时间,取得本周周一的日期；按照 “当前周，年-月-日，周几”格式储存
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    private String getCurrentData(){
        StringBuffer re = new StringBuffer(currentWeek+",");
        //默认取得当前时间
        Calendar rightNow = Calendar.getInstance();
        int dayOfWeek = rightNow.get(Calendar.DAY_OF_WEEK);
        rightNow.add(Calendar.DAY_OF_YEAR,MONDAY_IN_WEEK-dayOfWeek);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = rightNow.getTime();
        String format = sdf.format(date);
        re.append(format+",");
        dayOfWeek = rightNow.get(Calendar.DAY_OF_WEEK);
        re.append(dayOfWeek);
        return re.toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_setCurrentWeek:{
                String str_week=parent.getItemAtPosition(position).toString();
                if (str_week == "假期中")
                {
                    currentWeek = 0;
                    break;
                }
                currentWeek = Integer.parseInt(str_week);
            }break;
            case R.id.spinner_alarm_adavance_time:{
                int_alarmAdvanceTime = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private CheckBox alarmMode_ring;
    private int alarmIsRing;
    private CheckBox alarmMode_vibrate;
    private int alarmIsVibrate;
    /**
     * 用于取得设置闹钟提醒类型的设置。包括铃声和震动两个checkbox；
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkBox_alarm_ringMode:{
                if (isChecked){
                    alarmIsRing = 1;
                } else
                {
                    alarmIsRing = 0;
                }
            }break;
            case R.id.checkBox_alarm_vibrateMode:{
                if (isChecked){
                    alarmIsVibrate = 1;
                } else
                {
                    alarmIsVibrate = 0;
                }
            }break;
        }
    }
}
