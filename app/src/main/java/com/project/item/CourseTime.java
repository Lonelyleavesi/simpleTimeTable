package com.project.item;


/**
 * 用于储存设置页面中 第x节课的开始时间和结束时间的item
 */
public class CourseTime {
    int no; //序号
    int start_hour;
    int start_minute;
    int end_hour;
    int end_minute;

    public  CourseTime(){
        no = 0;
        start_hour = 0 ;
        start_minute = 0;
        end_hour = 0;
        end_minute = 0 ;
    }
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }

    public int getStart_minute() {
        return start_minute;
    }

    public void setStart_minute(int start_minute) {
        this.start_minute = start_minute;
    }

    public int getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(int end_hour) {
        this.end_hour = end_hour;
    }

    public int getEnd_minute() {
        return end_minute;
    }

    public void setEnd_minute(int end_minute) {
        this.end_minute = end_minute;
    }
}
