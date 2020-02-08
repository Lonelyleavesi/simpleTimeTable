package com.project.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class addCourseActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    public static ArrayList<ArrayList<Button>> weekButtonArray;
    public static Boolean [][] weekButtonState;
    private TableLayout clickWeekTableLayout;
    private CheckBox oddWeekBox;
    private CheckBox evenWeekBox;
    private CheckBox allWeekBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcourse);
        initMember();
        boundClickButtonToArray();
        boundButtonToListener();
        getSupportActionBar().hide();
    }

    private void initMember(){
        weekButtonArray = new ArrayList<>();
        weekButtonState = new Boolean[5][5];
        clickWeekTableLayout = (TableLayout) findViewById(R.id.tableLayout_clickweek);
        oddWeekBox = (CheckBox) findViewById(R.id.checkBox_oddweek);
        evenWeekBox= (CheckBox) findViewById(R.id.checkBox_evenweek);
        allWeekBox = (CheckBox) findViewById(R.id.checkBox_allweek);
    }

    /**
     * 将添加课程的周数储存在ArrayList中方便后续操作
     */
    private void boundClickButtonToArray() {
        TableRow[] childsRow = new TableRow[clickWeekTableLayout.getChildCount()];
        for(int i=0;i<childsRow.length;i++){
            childsRow[i] = (TableRow) clickWeekTableLayout.getChildAt(i);
            ArrayList<Button> simpleRowView = new ArrayList<Button>();
            for (int j = 0 ; j < childsRow[i].getChildCount(); j++){
                Button temp = (Button) childsRow[i].getChildAt(j);
                simpleRowView.add(temp);
                weekButtonState[i][j] = false;
            }
            weekButtonArray.add(simpleRowView);
            Log.d("TimeTable","The clieckable Size is "+ weekButtonArray.size()+"; And " +
                    "the size of a item is "+ weekButtonArray.get(0).size());
        }
        updateWeekButtonState();
    }

    /**
     * 根据储存的按钮状态更新按钮的颜色。
     */
    private void updateWeekButtonState(){
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
     * 为所有按钮添加或绑定监听器
     * @author chen yujie
     */
    private void boundButtonToListener(){
        Button backButton =  (Button) findViewById(R.id.button_addcourse_back);
        backButton.setOnClickListener(this);
        Button finishButton = (Button)findViewById(R.id.button_addcourse_finish);
        finishButton.setOnClickListener(this);
        boundWeekButtonToListener();
        boundCheckBoxToListener();
    }

    /**
     * 为每一个weekButton添加监听器，用于改变选中状态
     */
    private void boundWeekButtonToListener(){
        //为weekButton添加监听器
        for (int i = 0; i < weekButtonArray.size(); i ++) {
            for (int j = 0; j < weekButtonArray.get(i).size(); j++) {
                weekButtonArray.get(i).get(j).setOnClickListener(this);
            }
        }
    }
    /**
     * 此函数用于更新周数被点击的状态，通过传入周数
     * @author chen yujie
     * @param weekNum 被点击的周数
     */
    private void clickButtonByNum( int weekNum){
        weekButtonState[(weekNum-1)/5][(weekNum-1)%5] = !weekButtonState[(weekNum-1)/5][(weekNum-1)%5];
        Log.d("TimeTable","You click Button "+weekNum+" And its state is "+ weekButtonState[(weekNum-1)/5][(weekNum-1)%5]);
        updateWeekButtonState();
    }

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
        updateWeekButtonState();
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

            }break;
            default:{
                Button self = (Button) findViewById(v.getId());
                int weekNum = Integer.parseInt(self.getText().toString());
                clickButtonByNum(weekNum);
            }
        }
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
                                updateWeekButtonState();
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
                                updateWeekButtonState();
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
                                updateWeekButtonState();
                            }
                        } else
                        {
                            clearWeekButtonState();
                        }break;
                    }
        }
    }
}
