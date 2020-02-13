package com.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.activity.R;
import com.project.item.CourseSimpleInfo;

import java.util.List;

public class CoursesInfoAdapter extends ArrayAdapter<CourseSimpleInfo> {

    private int resourceId;
    public CoursesInfoAdapter(Context context, int resource, List<CourseSimpleInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    /**
     * 滚动listview所需要重写的函数
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseSimpleInfo info = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.initView(view);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.setInfo(info);
        return view;
    }

    /**
     * 类种类，用于保存一个item的TextView，主要包括初始化绑定view以及为view赋予String两个功能
     */
    class ViewHolder{
        TextView tv_day;
        TextView tv_courseStart;
        TextView tv_courseEnd;
        TextView tv_teacherName;
        TextView tv_courseRoom;
        TextView tv_courseWeeks;

        public void initView(View view){
            tv_day = view.findViewById(R.id.textView_item_courseDay);
            tv_courseStart = view.findViewById(R.id.textView_item_courseStart);
            tv_courseEnd = view.findViewById(R.id.textView_item_courseEnd);
            tv_teacherName = view.findViewById(R.id.textView_item_teacherName);
            tv_courseRoom = view.findViewById(R.id.textView_item_courseRoom);
            tv_courseWeeks =view.findViewById(R.id.textView_item_weeks);
        }

        public void setInfo(CourseSimpleInfo info){
            tv_day.setText("星期 "+info.getDay());
            tv_courseStart.setText(info.getCourseStart().toString());
            tv_courseEnd.setText(info.getCourseEnd().toString());
            tv_teacherName.setText(info.getTeacherName());
            tv_courseRoom.setText(info.getCourseRoom());
            StringBuffer weeks = new StringBuffer();
            for (Integer week : info.getWeeks()){
                weeks.append(week+" ");
            }
            tv_courseWeeks.setText(weeks.toString());
        }
    }
}
