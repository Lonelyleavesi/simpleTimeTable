package com.project.tools;

import com.project.item.Course;

import org.litepal.LitePal;

public class DataBaseCostumTools {
    public static void deleteExistCourse(Course course){
        LitePal.deleteAll(Course.class,"name = ? and day = ? and start_time = ? " +
                        "and end_time = ? and classroom = ? and teachername = ? and weekno = ?",
                course.getName(),course.getDay()+"",course.getStart_time()+"",course.getEnd_time()+"",
                course.getClassRoom(),course.getTeacherName(),course.getWeekNo()+"");
    }
}
