package com.project.fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.activity.DisplaySimpleCourseInfoActivity;
import com.project.activity.R;
import com.project.item.Course;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DisplayAllCourseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_all_course,container,false);
        initMember(view);
        setViewListener();
        updateCourseList();
        setListContextMenu();
        return view;
    }

    /**
     * 初始化类成员
     */
    private void initMember(View view){
        allCourseName = new ArrayList<>();
        allCourseList = view.findViewById(R.id.listview_showAllCourse);
        showCourseContext = getActivity();
        adapter = new ArrayAdapter<String>(showCourseContext,android.R.layout.simple_list_item_1,allCourseName);
    }


    /**
     * 为LIstview设置监听器，点击触发checkCourseInfo函数，切换页面和传递数据
     */
    private void setViewListener(){
        allCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = allCourseName.get(position);
                checkCourseInfo(name);
            }
        });
    }

    /**
     * 开启某个课程的具体信息活动，并传递课程名称数据
     * @param name 课程名称
     */
    private  void checkCourseInfo(String name){
        Intent intent = new Intent(showCourseContext, DisplaySimpleCourseInfoActivity.class);
        intent.putExtra("courseName",name);
        startActivity(intent);
    }

    private static List<String> allCourseName = new ArrayList<>();
    /**
     * 取得课程数据,从数据库出去，通过一个临时变量Set去重，然后再添加到list里面方便展示
     */
    public static void updateCourseList(){
        List <Course> courses = LitePal.select("name").find(Course.class);
        Set<String> courseName = new TreeSet<>();
        allCourseName.clear();
        for (Course course : courses){
            courseName.add(course.getName());
        }
        for (String name : courseName){
            allCourseName.add(name);
        }
        displayList();
    }


    private  static ArrayAdapter <String> adapter ;
    private  static Context showCourseContext;
    private  static ListView allCourseList;
    /**
     * 展示list中的内容，即所有课程的名称
     */
    private static void displayList(){
        if (adapter == null)
            return;
        allCourseList.setAdapter(adapter);
    }

    /**
     *为List绑定上下文菜单
     */
    private void  setListContextMenu(){
        registerForContextMenu(allCourseList);
    }

    /**
     *创建上下文菜单
     */
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_showallcourse_list,menu);
    }

    /**
     * 点击菜单注册事件
     * @param item 被点击的菜单选项
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Log.d("TimeTable", String.valueOf( menuInfo.position));
        Log.d("TimeTable", allCourseName.get(menuInfo.position));
        switch (item.getItemId()){
            case R.id.menu_item_infoCourse:{
                checkCourseInfo(allCourseName.get(menuInfo.position));
            }break;
            case R.id.menu_item_deleteCourse:{
                deleteCourse(allCourseName.get(menuInfo.position));
            }break;
        }
        return true;
    }

    private void deleteCourse(final String courseName){
        ConfirmDialogFragment dialog = new ConfirmDialogFragment();
        dialog.setContent("确认删除吗？");
        dialog.setDialogClickListener(new ConfirmDialogFragment.onDialogClickListener() {
            @Override
            public void onSureClick() {
                LitePal.deleteAll(Course.class,"name = ?",courseName);
                updateCourseList();
                DisplayTimeTableFragment.upDateTimeTable(DisplayTimeTableFragment.currentCheckWeek);
            }
            @Override
            public void onCancelClick() {
            //这里是取消操作
            }
        });
        dialog.show(getFragmentManager(),"");
    }

}
