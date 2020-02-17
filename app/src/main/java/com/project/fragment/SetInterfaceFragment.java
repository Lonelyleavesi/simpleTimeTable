package com.project.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.activity.R;
import com.project.adapter.SetCourseTimeAdapter;
import com.project.item.Course;
import com.project.item.CourseTime;

import java.util.ArrayList;
import java.util.List;

public class SetInterfaceFragment extends Fragment implements View.OnClickListener {
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
        courseTimeList=new ArrayList<>();
        initCourseTimeList();
        recycleCoursesTime = (RecyclerView) view.findViewById(R.id.recycle_setClassesTime);
        layoutManager = new LinearLayoutManager(getContext());
        courseTimeAdapter = new SetCourseTimeAdapter(courseTimeList,getContext());
        confirmButton = (Button) view.findViewById(R.id.button_set_confirm);
    }

    private void displayRecycleView() {
        recycleCoursesTime.setLayoutManager(layoutManager);
        recycleCoursesTime.setAdapter(courseTimeAdapter);
    }

    private  void initCourseTimeList(){
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
                    Log.d(TAG, "onClick: start time of "+time.getNo()+" is "+time.getStart_hour()+":"+time.getStart_minute()
                            +" end time is "+ time.getEnd_hour()+":"+time.getEnd_minute());
                }
            }break;
        }
    }
}
