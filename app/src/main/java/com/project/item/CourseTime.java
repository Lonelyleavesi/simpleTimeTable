package com.project.item;


import com.project.tools.CustomTime;

/**
 * 用于储存设置页面中 第x节课的开始时间和结束时间的item
 */
public class CourseTime implements Comparable{
    int no; //序号
    public CustomTime start_time;
    public CustomTime end_time;

    public  CourseTime(){
        no = 0;
        start_time = new CustomTime();
        end_time = new CustomTime();
    }
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return no+","+start_time.toString()+","+end_time.toString();
    }

    @Override
    public int compareTo(Object o) {
        CourseTime s = (CourseTime) o;
        if (this.no >= s.no)
            return 1;
        else
            return 0;
    }
}
