package com.project.tools;

import android.content.Context;

import com.project.item.Course;
import com.project.item.CourseTime;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DataBaseCustomTools {
    public static void deleteExistCourse(Course course){
        LitePal.deleteAll(Course.class,"name = ? and day = ? and start_time = ? " +
                        "and end_time = ? and classroom = ? and teachername = ? and weekno = ?",
                course.getName(),course.getDay()+"",course.getStart_time()+"",course.getEnd_time()+"",
                course.getClassRoom(),course.getTeacherName(),course.getWeekNo()+"");
    }

    public static int getCurrentWeek(Context context){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder contect  = new StringBuilder();
        String [ ] timeData;
        int currentWeek = 0;
        try {
            in = context.openFileInput("currentWeek");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                timeData = line.split(",");
                currentWeek = Integer.parseInt(timeData[0]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return currentWeek;
    }

    public static ArrayList<CourseTime> getCourseTime(Context context){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder contect  = new StringBuilder();
        String [ ] courseData;
        ArrayList<CourseTime> courseTimeArrayList = new ArrayList<>();
        try {
            in = context.openFileInput("courseTime");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                CourseTime tempTime = new CourseTime();
                courseData = line.split(",");
                tempTime.setNo(Integer.parseInt(courseData[0]));
                tempTime.start_time = new CustomTime(Integer.parseInt(courseData[1]),Integer.parseInt(courseData[2]));
                tempTime.end_time = new CustomTime(Integer.parseInt(courseData[3]),Integer.parseInt(courseData[4]));
                courseTimeArrayList.add(tempTime);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        Collections.sort(courseTimeArrayList);
        return courseTimeArrayList;
    }
}
