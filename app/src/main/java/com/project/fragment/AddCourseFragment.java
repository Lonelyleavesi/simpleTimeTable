package com.project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.activity.AddLocalCourseActivity;
import com.project.activity.R;

public class AddCourseFragment extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addcourse,container,false);
        initMember(view);
        bindListener();
        return view;
    }

    Button addLocalCourse;
    Button importCourse;
    Intent addCourseActivity;
    private void initMember(View view) {
        addLocalCourse = view.findViewById(R.id.button_addCourse_local);
        importCourse = view.findViewById(R.id.button_importFromWeb);
        addCourseActivity = new Intent(getContext(), AddLocalCourseActivity.class);
    }

    private void bindListener() {
        addLocalCourse.setOnClickListener(this);
        importCourse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_addCourse_local :{
                startActivity(addCourseActivity);
            }break;
            case R.id.button_importFromWeb:{

            }break;
        }
    }
}
