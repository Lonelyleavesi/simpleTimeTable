package com.project.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.project.activity.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SelectWeekDialogFragment extends DialogFragment  implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

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
        fragment_view = inflater.inflate(R.layout.fragment_seclect_weeks, container, false);
        initMember(fragment_view);
        boundClickButtonToArray();
        boundWeekButtonsToListener();
        boundButtonToListener();
        return fragment_view;
    }

    /**
     * 初始化成员变量
     */
    private void initMember(View view) {
        weekButtonArray = new ArrayList<>();
        weekButtonState = new Boolean[5][5];
        if (weeksSelected == null)
            weeksSelected = new TreeSet<>();
        else{
            setWeekButtonState();
            Log.d("TimeTable","init Week selected is not null");
        }
        clickWeekTableLayout = (TableLayout) view.findViewById(R.id.tableLayout_clickweek);
        oddWeekBox = (CheckBox) view.findViewById(R.id.checkBox_oddweek);
        evenWeekBox= (CheckBox) view.findViewById(R.id.checkBox_evenweek);
        allWeekBox = (CheckBox) view.findViewById(R.id.checkBox_allweek);
        button_confirm_week = (Button) view.findViewById(R.id.button_confirm_week);
    }

    private void setWeekButtonState(){
        clearWeekButtonState();
        for (Integer weekNum : weeksSelected)
        {
            weekButtonState[(weekNum-1)/5][(weekNum-1)%5] = true;
        }
        updateButtonState();
    }

    private TableLayout clickWeekTableLayout;
    private ArrayList<ArrayList<Button>> weekButtonArray;
    private Boolean [][] weekButtonState;
    /**
     * 将添加课程的周数Button的实例储存在ArrayList中方便后续操作
     */
    private void boundClickButtonToArray() {
        TableRow[] weeksRow = new TableRow[clickWeekTableLayout.getChildCount()];
        for(int i=0;i<weeksRow.length;i++){
            weeksRow[i] = (TableRow) clickWeekTableLayout.getChildAt(i);
            ArrayList<Button> simpleRowView = new ArrayList<Button>();
            for (int j = 0 ; j < weeksRow[i].getChildCount(); j++){
                Button temp = (Button) weeksRow[i].getChildAt(j);
                simpleRowView.add(temp);
            }
            weekButtonArray.add(simpleRowView);
        }
        Log.d("TimeTable","The WeekButtonArray Size is "+ weekButtonArray.size()+"; And " +
                "the size of a item is "+ weekButtonArray.get(0).size());
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

    Button button_confirm_week;
    /**
     * 为所有按钮添加或绑定监听器
     * @author chen yujie
     */
    private void boundButtonToListener(){
        button_confirm_week.setOnClickListener(this);
        boundCheckBoxToListener();
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
     * 此函数用于通过传入周数更新该周数被点击的状态，
     * @author chen yujie
     * @param weekNum 被点击的周数
     */
    private void clickButtonByNum( int weekNum){
        weekButtonState[(weekNum-1)/5][(weekNum-1)%5] = !weekButtonState[(weekNum-1)/5][(weekNum-1)%5];
        Log.d("TimeTable","You click Week Button "+weekNum+" And its state is "+ weekButtonState[(weekNum-1)/5][(weekNum-1)%5]);
        updateButtonState();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_confirm_week:{
                if (mListener != null) {
                    getSelectWeek();
                    mListener.onSureClick();
                }
                dismiss();
            }break;
            default:{
                Button self = (Button) fragment_view.findViewById(v.getId());
                int weekNum = Integer.parseInt(self.getText().toString());
                clickButtonByNum(weekNum);
            }
        }
    }

    public Set<Integer> weeksSelected;
    public void getSelectWeek(){
        for (int m = 0 ; m < weekButtonState.length; m ++)
            for (int n = 0; n < weekButtonState[m].length ; n++){
                int week = m*weekButtonState.length+n+1;
                if (weekButtonState[m][n]){
                    weeksSelected.add(week);
                }else {
                    weeksSelected.remove(week);
                }
            }
    }

    public interface SelectWeeksClickListener {
        public void onSureClick();
    }

    private SelectWeeksClickListener mListener;

    public void setDialogClickListener(SelectWeeksClickListener mListener) {
        this.mListener = mListener;
    }
}
