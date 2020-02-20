package com.project.fragment;

import android.annotation.TargetApi;
import android.icu.util.Calendar;
import android.icu.util.ChineseCalendar;
import android.os.Build;
import android.os.Bundle;
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
import com.project.item.Course;
import com.project.tools.DataBaseCustomTools;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DisplayTimeTableFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    static final int MONDAY_IN_WEEK = 2;
    static final int DAY_NUM_IN_ONE_WEEK = 7;
    static int currentWeek = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_display_timetable,container,false);
       initMember(view);
       bindViewToArray();
       upDateTimeTable(currentCheckWeek);
       return view;
    }

    /**
     * 初始化成员变量
     * @param view 碎片的布局对象
     */
    private void initMember(View view) {
        courseArray = new ArrayList<>();
        DataBaseCustomTools.updateWeekInfo(getContext());
        currentWeek = DataBaseCustomTools.getCurrentWeek(getContext());
        currentCheckWeek = currentWeek;
        courseTable = (TableLayout) view.findViewById(R.id.tableLayout_coursetable);
        currentWeekSpinner = (Spinner) view.findViewById(R.id.spinner_currentweek);
        updateCurrentSpinner();
        currentWeekSpinner.setSelection(currentCheckWeek);
        data_row = (TableRow) view.findViewById(R.id.tableRow_date);
        updateDataRow();
    }

    private static Spinner currentWeekSpinner;
    public static int currentCheckWeek;
    static List<String> spinnerWeekList =new ArrayList<>();
    /**
     * 初始化选择第几周的spinner 默认周数为1~25周
     * @author chen yujie
     */
    private void updateCurrentSpinner(){
        updateWeekList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinnerWeekList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currentWeekSpinner.setAdapter(adapter);
        currentWeekSpinner.setOnItemSelectedListener(this);
    }
    private static void updateWeekList(){
        spinnerWeekList= new ArrayList<String>();
        if (currentCheckWeek == 0){
            spinnerWeekList.add("假期中");
        }else{
            spinnerWeekList.add("0");
        }
        for (int i = 1 ; i <= 25 ; i++){
            if ( i == currentCheckWeek)
            {
                spinnerWeekList.add(i+",本周");
            }else{
                spinnerWeekList.add(Integer.toString(i));
            }
        }
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
        }
    }

    /**
     *  根据数据库中的数据更新
     * @author chen yujie
     * @param week  表示第几周
     */
    public static void upDateTimeTable( int week){
        clearTimeTable();
        updateWeekList();
        updateDataRow();
        LitePal.getDatabase();
        List<Course> cours;
        if(LitePal.isExist(Course.class)){
            cours = LitePal.where("weekNo = ?",week+"").find(Course.class);
        }else
        {
            return ;
        }
        for (Course course : cours){
            int day = course.getDay();
            int start = course.getStart_time();
            int end = course.getEnd_time();
            String courseRoom = course.getClassRoom();
            for (int i =start ; i <= end ; i++)
            {
                setCourseTableItem(course.getName(),i,day,courseRoom);
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
    private static void setCourseTableItem(String courseName, int no, int day,String courseRoom){
        TextView temp = courseArray.get(no-1).get(day);
        temp.setText(courseName+"\n"+courseRoom);
        temp.setBackgroundResource(R.drawable.coursetable_courseitem_border);
    }

    private static Calendar[] calendars;  //用于储存一周7天的日期
    private static TableRow   data_row;   //星期栏的textView组
    @TargetApi(Build.VERSION_CODES.N)
    private static void updateDataRow() {
        updateCalendar();
        TextView monthTextView = (TextView) data_row.getChildAt(0);
        monthTextView.setText(calendars[0].get(Calendar.MONTH)+1+"月");
        for (int i = 1 ; i < data_row.getChildCount(); i++){
            TextView temp = (TextView) data_row.getChildAt(i);
            int month = (calendars[i-1].get(Calendar.MONTH)+1);
            int day = calendars[i-1].get(Calendar.DAY_OF_MONTH);
            temp.setText("星期"+i+"\n"+month+"/"+day);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static void updateCalendar(){
        calendars = new Calendar[7];
        for (int i = 0 ; i < 7 ; i++){
            calendars[i] = Calendar.getInstance();
            //定位星期到周一
            calendars[i].add(Calendar.DAY_OF_YEAR,MONDAY_IN_WEEK-calendars[i].get(Calendar.DAY_OF_WEEK));
           //定位周到当前查看的周
            calendars[i].add(Calendar.DAY_OF_YEAR,(currentCheckWeek-currentWeek)*DAY_NUM_IN_ONE_WEEK);
            calendars[i].add(Calendar.DAY_OF_YEAR,i);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_currentweek:{
                String str_week=parent.getItemAtPosition(position).toString();
                if (str_week == "假期中")
                {
                    currentCheckWeek =0;
                }else {
                    currentCheckWeek = Integer.parseInt(str_week.split(",")[0]);
                }
                upDateTimeTable(currentCheckWeek);
            }break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
