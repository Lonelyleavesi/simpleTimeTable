package com.project.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.project.activity.R;

public class SelectCourseTimeDialogFragment extends DialogFragment implements View.OnClickListener {
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
        boundListener();
        return fragment_view;
    }

    TimePicker timePicker ;
    Button button_confirm;
    public int m_hour;
    public int m_minute;
    @TargetApi(Build.VERSION_CODES.M)
    private void initView( ) {
        timePicker =  fragment_view.findViewById(R.id.timepicker_courseTime_dialog);
        timePicker.setIs24HourView(true);
        button_confirm = fragment_view.findViewById(R.id.button_timePickConfirm);
        timePicker.setHour(m_hour);
        timePicker.setMinute(m_minute);
    }

    private void boundListener() {
        button_confirm.setOnClickListener(this);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                m_hour = hourOfDay;
                m_minute = minute;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_timePickConfirm:{
                if (mListener != null) {
                    mListener.onSureClick();
                }
                dismiss();
            }break;
        }
    }

    public interface SelectTimeClickListener {
        public void onSureClick();
    }

    private SelectTimeClickListener mListener;

    public void setDialogClickListener(SelectTimeClickListener mListener) {
        this.mListener = mListener;
    }
}
