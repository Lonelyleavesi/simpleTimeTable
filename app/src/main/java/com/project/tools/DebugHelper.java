package com.project.tools;

import android.util.Log;

import com.project.item.Course;

public class DebugHelper {
    public static void showCourse(Course course){
        Log.d("getCourse", "  ");
        Log.d("getCourse", "  ");
        Log.d("getCourse", "   ");
        Log.d("getCourse", "Course name is :"+course.getName());
        Log.d("getCourse", "Course teacher is :"+course.getTeacherName());
        Log.d("getCourse", "Course room is :"+course.getClassRoom());
        Log.d("getCourse", "Course day is :"+course.getDay());
        Log.d("getCourse", "Course start is :"+course.getStart());
        Log.d("getCourse", "Course end is :"+course.getEnd());
        Log.d("getCourse", "Course weekNo is :"+course.getWeekNo());
    }
}
