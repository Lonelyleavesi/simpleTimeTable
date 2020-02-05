package com.project.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.timetabletest.MainActivity;
import com.project.timetabletest.R;

import java.util.ArrayList;

public class timeTableFragment extends Fragment {

    TableLayout courseTable;
    public static ArrayList<ArrayList<TextView>>  courseArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.table_fragment,container,false);
       courseArray = new ArrayList<>();
       bindViewToArray(view);
        upDateTimeTable();
       return view;
    }

    /*
      将CourseTable.xml中的各个课程位置的textview放入到MainActivit中arrary中方便后续操作
     */
    private void bindViewToArray(View view){
        courseTable = (TableLayout) view.findViewById(R.id.id_CourseTable);
        TableRow[] childsRow = new TableRow[courseTable.getChildCount()];
        for(int i=0;i<childsRow.length;i++){
            childsRow[i] = (TableRow) courseTable.getChildAt(i);
            LinearLayout simpleLinearLayout = (LinearLayout) childsRow[i].getChildAt(0);
            ArrayList<TextView> simpleRowView = new ArrayList<TextView>();
            for (int j = 0 ; j < simpleLinearLayout.getChildCount(); j++){
                TextView temp = (TextView) simpleLinearLayout.getChildAt(j);
                simpleRowView.add(temp);
            }
            courseArray.add(simpleRowView);
            Log.d("TimeTable","The course Size is "+courseArray.size()+"; And " +
                    "the size of a item is "+ courseArray.get(0).size());
        }
    }

    /*
    根据数据库中的数据更新
     */
    private void upDateTimeTable(){
        /*
        demo
         */
        for (int i = 0 ; i < courseArray.size()-1; i ++)
            for (int j = 1 ; j < courseArray.get(i).size();j++){
                TextView temp = courseArray.get(i).get(j);
                temp.setText("ababfdabfabfdabfabfd  ");
            }
    }
}
