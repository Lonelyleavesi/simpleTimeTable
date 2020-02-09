package com.project.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.project.activity.R;
import com.project.item.course;
import com.project.tools.DebugHelper;

import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;

public class timeTableFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.table_fragment,container,false);
       initMember(view);
       initCurrentSpinner();
       bindViewToArray();
       upDateTimeTable(currentWeek);
        return view;
    }

    /**
     * 初始化成员变量
     * @param view 碎片的布局对象
     */
    private void initMember(View view){
        courseArray = new ArrayList<>();
        currentWeekSpinner = (Spinner) view.findViewById(R.id.spinner_currentweek);
        currentWeek = 1;
        courseTable = (TableLayout) view.findViewById(R.id.tableLayout_coursetable);
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

    private TableLayout courseTable;
    public static ArrayList<ArrayList<TextView>>  courseArray;
    /**
     * 将CourseTable.xml中的各个课程位置的textview放入到MainActivit中arrary中方便后续操作
     * @author chen yujie
     */
    private void bindViewToArray(){
        TableRow[] childsRow = new TableRow[courseTable.getChildCount()];
        for(int i=0;i<childsRow.length;i++){
            childsRow[i] = (TableRow) courseTable.getChildAt(i);
            ArrayList<TextView> simpleRowView = new ArrayList<TextView>();
            for (int j = 0 ; j < childsRow[i].getChildCount(); j++){
                TextView temp = (TextView) childsRow[i].getChildAt(j);
                simpleRowView.add(temp);
            }
            courseArray.add(simpleRowView);
            Log.d("TimeTable","The course Size is "+courseArray.size()+"; And " +
                    "the size of a item is "+ courseArray.get(0).size());
        }
    }

    /**
     *  根据数据库中的数据更新
     * @author chen yujie
     * @param week  表示第几周
     */
    public static void upDateTimeTable( int week){
        clearTimeTable();
        List<course> courses = LitePal.where("weekNo = ?",week+"").find(course.class);
        for (course course : courses){
            DebugHelper.showCourse(course);
            String[] day_and_course = course.getDay_and_course().split(",");
            int day = Integer.parseInt(day_and_course[0]);
            int start = Integer.parseInt(day_and_course[1]);
            int end = Integer.parseInt(day_and_course[2]);
            for (int i =start ; i <= end ; i++)
            {
                setCourseTableItem(course.getName(),i,day);
            }
        }
    }

    /**
     * 使timeTable全刷新为空
     */
    private static void clearTimeTable(){
        for (int i = 0 ; i < courseArray.size(); i ++)
        {
            for (int j = 1 ; j < courseArray.get(i).size();j++){
                TextView temp = courseArray.get(i).get(j);
                temp.setText("  ");
                temp.setBackgroundResource(0);
            }
        }
    }
    /**
     * 在课程表view中对应的位置放置对应的课程
     * @param courseName  课程名
     * @param no   上课的序号
     * @param day  星期几
     */
    private static void setCourseTableItem(String courseName, int no, int day){
        TextView temp = courseArray.get(no-1).get(day);
        temp.setText(courseName);
        temp.setBackgroundResource(R.drawable.coursetable_courseitem_border);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_currentweek:{
                String str_week=parent.getItemAtPosition(position).toString();
                currentWeek = Integer.parseInt(str_week);
                upDateTimeTable(currentWeek);
            }break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
