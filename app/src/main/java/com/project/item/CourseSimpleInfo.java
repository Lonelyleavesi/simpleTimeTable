package com.project.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class CourseSimpleInfo {
    public CourseSimpleInfo(Course course) {
        this.day = course.getDay()+"";
        this.courseStart = course.getStart();
        this.courseEnd = course.getEnd();
        this.teacherName = course.getTeacherName();
        this.courseRoom = course.getClassRoom();
        weeks = new TreeSet<>();
        weeks.add(course.getWeekNo());
    }

    String day;
    Integer courseStart;
    Integer courseEnd;
    String teacherName;
    String courseRoom;
    public Set<Integer> weeks;

    public String getDay() {
        return day;
    }

    public Integer getCourseStart() {
        return courseStart;
    }

    public Integer getCourseEnd() {
        return courseEnd;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getCourseRoom() {
        return courseRoom;
    }

    public Set<Integer> getWeeks() {
        return weeks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseSimpleInfo info = (CourseSimpleInfo) o;
        return  day.equals(info.day)  &&
                courseStart.equals(info.courseStart) &&
                courseEnd.equals(info.courseEnd) &&
                teacherName.equals(info.teacherName) &&
                courseRoom.equals(info.courseRoom);
    }

}
