package com.project.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class addCourseActivity extends AppCompatActivity implements View.OnClickListener{


    public static ArrayList<ArrayList<Button>> weekButtonArray;
    public static Boolean [][] weekButtonState;
    private TableLayout clickWeekTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcourse);
        weekButtonArray = new ArrayList<>();
        weekButtonState = new Boolean[5][5];
        boundClickButtonToArray();
        boundButtonToListener();
        getSupportActionBar().hide();
    }

    /**
     * 将添加课程的周数储存在ArrayList中方便后续操作
     */
    private void boundClickButtonToArray() {
        clickWeekTableLayout = (TableLayout) findViewById(R.id.tableLayout_clickweek);
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
     * @author chen yujie
     * 绑定按钮和监听器逻辑
     */
    private void boundButtonToListener(){
        Button backButton =  (Button) findViewById(R.id.button_addcourse_back);
        backButton.setOnClickListener(this);
        Button finishButton = (Button)findViewById(R.id.button_addcourse_finish);
        finishButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_addcourse_back:{
                    finish();
            }
            case R.id.button_addcourse_finish:{

            }
        }
    }

    /**
     * 初始化按钮上的文字以及根据当前按钮被选中的状态改变按钮的颜色，
     */
    private void updateWeekButtonState(){
        int count = 1;
        for (int i = 0; i < weekButtonArray.size(); i ++){
            for (int j = 0 ; j < weekButtonArray.get(i).size(); j ++)
            {
                weekButtonArray.get(i).get(j).setText(count+"");
                count++;
            }
        }
        Log.d("TimeTable"," "+weekButtonState[4][4]+" ");
    }
}
