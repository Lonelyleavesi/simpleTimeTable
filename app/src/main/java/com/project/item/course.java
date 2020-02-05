package com.project.item;

import org.litepal.crud.LitePalSupport;

public class course extends LitePalSupport {
    private int id;
    private String name;   //课程名称
    private int day;       //星期几
    private int lessonNo;  //第几节课
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getLessonNo() {
        return lessonNo;
    }

    public void setLessonNo(int lessonNo) {
        this.lessonNo = lessonNo;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }
}
