package com.project.tools;

import android.util.Log;

import com.project.item.course;

public class DebugHelper {
    public static void showCourse(course course){
        Log.d("getCourse", "  ");
        Log.d("getCourse", "  ");
        Log.d("getCourse", "   ");
        Log.d("getCourse", "course name is :"+course.getName());
        Log.d("getCourse", "course teacher is :"+course.getTeacherName());
        Log.d("getCourse", "course room is :"+course.getClassRoom());
        Log.d("getCourse", "course day is :"+course.getDay());
        Log.d("getCourse", "course start is :"+course.getStart());
        Log.d("getCourse", "course end is :"+course.getEnd());
        Log.d("getCourse", "course weekNo is :"+course.getWeekNo());
    }
}
