package com.project.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.project.activity.R;

public class SelectCourseTimeDialogFrament extends DialogFragment {
    View fragment_view;

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM; // 显示在底部
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度填充满屏
        window.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); // 不显示标题栏
        fragment_view = inflater.inflate(R.layout.fragment_select_time, container, false);
        initView();
        return fragment_view;
    }

    TimePicker timePicker ;
    private void initView( ) {
        timePicker =  fragment_view.findViewById(R.id.timepicker_courseTime_dialog);
        timePicker.setIs24HourView(true);
    }
}
