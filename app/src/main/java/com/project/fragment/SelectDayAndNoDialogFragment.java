package com.project.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.project.activity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SelectDayAndNoDialogFragment extends DialogFragment implements  AdapterView.OnItemSelectedListener,View.OnClickListener{

    private static final int NUM_COURSE = 14;
    View fragment_view;
    Button button_confirm_day_and_no;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); // 不显示标题栏
        fragment_view = inflater.inflate(R.layout.fragment_select_day_and_no, container, false);
        initMember(fragment_view);
        boundClickButtonToArray();
        boundButtonToListener();
        return fragment_view;
    }

    /**
     * 初始化成员变量
     */
    private void initMember(View view){

        if(daySelected == null)
            daySelected = new TreeSet<>();
        spinnerAddCourseNoStart = (Spinner) view.findViewById(R.id.spinner_addcourseNo_start);
        spinnerAddCourseNoEnd = (Spinner) view.findViewById(R.id.spinner_addcourseNo_end);
        initSpinner();
        dayButtonArray = new ArrayList<>();
        dayButtonState = new Boolean[7];
        button_confirm_day_and_no = (Button) view.findViewById(R.id.button_confirm_dayAndNo);
        spinnerAddCourseNoStart.setSelection(courseStart-1,true);
        spinnerAddCourseNoEnd.setSelection(courseEnd-1,true);
        setDayButtonState();
    }

    private void setDayButtonState(){
        for (int i = 0 ; i < dayButtonState.length ; i ++){
            dayButtonState[i] = false;
        }
        for (Integer dayNum : daySelected)
        {
            dayButtonState[dayNum-1] = true;
        }
        updateButtonState();
    }


    private Spinner spinnerAddCourseNoStart;
    public int courseStart;
    private Spinner spinnerAddCourseNoEnd;
    public int courseEnd;
    /**
     * 初始化Spinner （用于选第几节课）
     */
    private void initSpinner(){
        List<String> courseNo= new ArrayList<String>();
        for (int i = 1 ; i <= NUM_COURSE ; i++){
            courseNo.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,courseNo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddCourseNoStart.setAdapter(adapter);
        spinnerAddCourseNoEnd.setAdapter(adapter);
        spinnerAddCourseNoStart.setOnItemSelectedListener(this);
        spinnerAddCourseNoEnd.setOnItemSelectedListener(this);
    }

    private ArrayList<Button> dayButtonArray;
    private Boolean [] dayButtonState;
    /**
     * 将添加课程的周数Button的实例储存在ArrayList中方便后续操作
     */
    private void boundClickButtonToArray() {
        TableRow dayRow = fragment_view.findViewById(R.id.tableRow_day);
        for (int i = 0 ; i < dayRow.getChildCount() ; i ++)
        {
            Button temp = (Button) dayRow.getChildAt(i);
            dayButtonArray.add(temp);
        }
        Log.d("TimeTable","The DayButtonArray Size is "+ dayButtonArray.size());
        updateButtonState();
    }

    /**
     * 根据储存的按钮状态更新按钮的颜色。
     */
    private void updateButtonState(){
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
        button_confirm_day_and_no.setOnClickListener(this);
        boundDayButtonsToListener();
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
                if (courseEnd < courseStart){
                    spinnerAddCourseNoEnd.setSelection(courseStart-1,true);
                }
            }break;
            case R.id.spinner_addcourseNo_end:{
                String str =parent.getItemAtPosition(position).toString();
                courseEnd = Integer.parseInt(str);
                if (courseEnd < courseStart){
                    spinnerAddCourseNoStart.setSelection(courseEnd-1,true);
                }
            }break;
        }
        Log.d("TimeTable", "onItemSelected: courseStart is " + courseStart + " courseEnd is "+courseEnd);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_addCourse_day1:
            case R.id.button_addCourse_day2:
            case R.id.button_addCourse_day3:
            case R.id.button_addCourse_day4:
            case R.id.button_addCourse_day5:
            case R.id.button_addCourse_day6:
            case R.id.button_addCourse_day7:{
                Button self = (Button) fragment_view.findViewById(v.getId());
                int dayNum = Integer.parseInt(self.getText().toString());
                dayButtonState[dayNum-1] = !dayButtonState[dayNum-1];
                Log.d("TimeTable","You click Day Button "+dayNum+" And its state is "+ dayButtonState[dayNum-1]);
                updateButtonState();

            }break;
            case R.id.button_confirm_dayAndNo:{
                if (mListener != null) {
                    getSelectDay();
                    mListener.onSureClick();
                }
                dismiss();
            }
        }
    }

    public Set<Integer> daySelected;
    public void getSelectDay(){
        for (int i = 0 ; i < dayButtonState.length ; i ++){
            if (dayButtonState[i])
            {
                daySelected.add(i+1);
            }else{
                daySelected.remove(i+1);
            }
        }
    }

    public interface SelectDayAndNoClickListener {
        public void onSureClick();
    }

    private SelectDayAndNoClickListener mListener;

    public void setDialogClickListener(SelectDayAndNoClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
