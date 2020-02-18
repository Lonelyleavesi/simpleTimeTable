package com.project.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.activity.R;
import com.project.adapter.SetCourseTimeAdapter;
import com.project.item.Course;
import com.project.item.CourseTime;
import com.project.tools.DataBaseCustomTools;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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
public class SetInterfaceFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemSelectedListener{
    private final int CLASS_MAX_NUM = 14;
    private final String TAG = "TimeTable";

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
        currentWeek = DataBaseCustomTools.getCurrentWeek(getContext());
        courseTimeList=new ArrayList<>();
        initCourseTimeList();
        recycleCoursesTime = (RecyclerView) view.findViewById(R.id.recycle_setClassesTime);
        layoutManager = new LinearLayoutManager(getContext());
        courseTimeAdapter = new SetCourseTimeAdapter(courseTimeList,getContext());
        confirmButton = (Button) view.findViewById(R.id.button_set_confirm);
        currentWeekSpinner = (Spinner) view.findViewById(R.id.spinner_setCurrentWeek);
        initCurrentSpinner();
        currentWeekSpinner.setSelection(currentWeek -1);
    }

    private Spinner currentWeekSpinner;
    public static int currentWeek;
    /**
     * 初始化选择第几周的spinner 默认周数为1~25周
     * @author chen yujie
     */
    private void initCurrentSpinner(){
        List<String> weekList= new ArrayList<String>();
        for (int i = 1 ; i <= 25 ; i++){
            weekList.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,weekList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currentWeekSpinner.setAdapter(adapter);
        currentWeekSpinner.setOnItemSelectedListener(this);
    }

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
        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_set_confirm:{
                for (CourseTime time : courseTimeList)
                {
                    Log.d(TAG, "onClick: start time of "+time.getNo()+" is "+time.start_time
                            +" end time is "+ time.end_time);
                }
                Log.d(TAG, "onClick: currentWeek "+currentWeek);
                if (checkingCourseTime()){
                    try {
                        saveSetting();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }break;
        }
    }

    /**
     * 如果每节课的结束时间都早于下节课开始时间，则时间通过
     * @return
     */
    private  boolean checkingCourseTime(){

        return true;
    }

    /**
     * 写进当前设置
     * @throws IOException
     */
    private void saveSetting() throws IOException {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        out = getContext().openFileOutput("currentWeek", Context.MODE_PRIVATE);
        writer = new BufferedWriter(new OutputStreamWriter(out));
        writer.write(getCurrentData());
        writer.close();
        out = getContext().openFileOutput("courseTime",Context.MODE_PRIVATE);
        writer = new BufferedWriter(new OutputStreamWriter(out));
        for (CourseTime courseTime : courseTimeList)
        {
            writer.write(courseTime.toString()+"\n");
        }
        writer.close();
    }

    /**
     * 取得当前时间；按照 “当前周，年-月-日，周几”格式储存
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    private String getCurrentData(){
        StringBuffer re = new StringBuffer(currentWeek+",");
        //默认取得当前时间
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = rightNow.getTime();
        String format = sdf.format(date);
        re.append(format+",");
        int dayOfWeek = rightNow.get(Calendar.DAY_OF_WEEK);
        re.append(dayOfWeek);
        return re.toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_setCurrentWeek:{
                String str_week=parent.getItemAtPosition(position).toString();
                currentWeek = Integer.parseInt(str_week);
            }break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
