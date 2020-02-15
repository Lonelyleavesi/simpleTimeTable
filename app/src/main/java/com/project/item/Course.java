package com.project.item;

import org.litepal.crud.LitePalSupport;

public class Course extends LitePalSupport {
    private int id;
    private String name;   //课程名称
    private String teacherName; //任课教师名称
    private String classRoom; //教室名称
    private int day;       //星期几
    private int start_time;        //开始课程时间
    private int end_time;        //结束课程时间
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
}
