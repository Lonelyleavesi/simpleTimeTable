package com.project.item;

import org.litepal.crud.LitePalSupport;

public class Course extends LitePalSupport implements Comparable {
    private int id;
    private String name;   //课程名称
    private String teacherName; //任课教师名称
    private String classRoom; //教室名称
    private int day;       //星期几
    private int start_time;        //开始课程节数
    private int end_time;        //结束课程节数
    private int weekNo;    //第几周

    public Course() {
    }

    public Course(String name, String teacherName, String classRoom, int day, int start_time, int end_time, int weekNo) {
        this.name = name;
        this.teacherName = teacherName;
        this.classRoom = classRoom;
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.weekNo = weekNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }

    public String getTeacherName() {  return teacherName;    }

    public void setTeacherName(String teacherName) { this.teacherName = teacherName;   }

    public String getClassRoom() {   return classRoom;    }

    public void setClassRoom(String classRoom) {    this.classRoom = classRoom;   }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;
        if (id != course.id) return false;
        if (day != course.day) return false;
        if (start_time != course.start_time) return false;
        if (end_time != course.end_time) return false;
        if (weekNo != course.weekNo) return false;
        if (!name.equals(course.name)) return false;
        if (teacherName != null ? !teacherName.equals(course.teacherName) : course.teacherName != null)
            return false;
        return classRoom != null ? classRoom.equals(course.classRoom) : course.classRoom == null;
    }

    @Override
    public String toString() {
        return "课程名称： "+ name+"\n"
                +"任课老师："+teacherName+"\n"
                +"教室名称："+classRoom+"\n"
                +"星期 "+day+"\n"
                +"开始节数 "+start_time+"\n"
                +"结束结束 "+end_time+"\n"
                +"周数 " + weekNo;
    }

    @Override
    public int compareTo(Object o) {
        Course course = (Course) o;
        if (!this.teacherName.equals(course.teacherName))
            return this.teacherName.compareTo(course.teacherName);
        if (!this.classRoom.equals(course.classRoom))
            return this.classRoom.compareTo(course.classRoom);

        int dayResult = this.day - course.day;
        if (dayResult != 0)
            return dayResult;

        int startResult = this.start_time - course.start_time;
        if (startResult != 0)
            return startResult;

        int endtimeResult = this.end_time - course.end_time;
        if (endtimeResult != 0)
            return endtimeResult;

        int weekResult = this.weekNo - course.weekNo;
        if (weekResult != 0)
            return weekResult;

        return this.name.compareTo(course.name);
    }
}
