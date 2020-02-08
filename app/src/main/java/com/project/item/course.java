package com.project.item;

import org.litepal.crud.LitePalSupport;

public class course extends LitePalSupport {
    private int id;
    private String name;   //课程名称
    private String teacherName; //任课教师名称
    private String classRoom; //教室名称
    private String day_and_course;       //星期几的第几节课 用 a,b,c的方法储存星期a 第b节课至第c节课
    private int weekNo;    //第几周

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

    public String getDay_and_course() {   return day_and_course;   }

    public void setDay_and_course(String day_and_course) { this.day_and_course = day_and_course;    }
}
