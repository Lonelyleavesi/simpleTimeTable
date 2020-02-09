package com.project.tools;

import android.util.Log;

import com.project.item.course;

public class DebugHelper {
    public static void showCourse(course course){
        Log.d("getCourse", "  ");
        Log.d("getCourse", "  ");
        Log.d("getCourse", "submitCourse:   ");
        Log.d("getCourse", "course name is :"+course.getName());
        Log.d("getCourse", "course teacher is :"+course.getTeacherName());
        Log.d("getCourse", "course room is :"+course.getClassRoom());
        Log.d("getCourse", "course day_and_course is :"+course.getDay_and_course());
        Log.d("getCourse", "course weekNo is :"+course.getWeekNo());
    }
}
