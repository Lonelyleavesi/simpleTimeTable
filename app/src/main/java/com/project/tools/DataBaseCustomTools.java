package com.project.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import com.project.item.Course;
import com.project.item.CourseTime;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class DataBaseCustomTools {
    private final static int MONDAY_IN_WEEK=2;
    private final static String TAG = "TimeTable";

    public static void deleteExistCourse(Course course){
        LitePal.deleteAll(Course.class,"name = ? and day = ? and start_time = ? " +
                        "and end_time = ? and classroom = ? and teachername = ? and weekno = ?",
                course.getName(),course.getDay()+"",course.getStart_time()+"",course.getEnd_time()+"",
                course.getClassRoom(),course.getTeacherName(),course.getWeekNo()+"");
    }


    /**
     * 如果当前的日期比储存的周信息大于等于7天，则说明到了下一周，需要更新
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static void updateWeekInfo(Context context){
        Calendar rightNow = Calendar.getInstance();
        int LastFlagWeek = 0;
        Date lastFlagDate = null;
        String [] oldTimeData = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
                oldTimeData = getLastWeekInfo(context);
                LastFlagWeek = Integer.parseInt(oldTimeData[0]);
                lastFlagDate = sdf.parse(oldTimeData[1]);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar lastFlagTime = Calendar.getInstance();
        lastFlagTime.setTime(lastFlagDate);
        //用文件中储存的日期和当前的日期进行比较，并把当前周的周一存进去,如果当前周超过25周，则视为假期中
        LastFlagWeek = LastFlagWeek +(rightNow.get(Calendar.DAY_OF_YEAR)-lastFlagTime.get(Calendar.DAY_OF_YEAR))/7;
        if (LastFlagWeek > 25)
            LastFlagWeek = 0;
        int dayOfWeek = rightNow.get(Calendar.DAY_OF_WEEK);
        rightNow.add(Calendar.DAY_OF_YEAR,MONDAY_IN_WEEK-dayOfWeek);
        String [] newTimeData = {LastFlagWeek+"",sdf.format(rightNow.getTime()),rightNow.get(Calendar.DAY_OF_WEEK)+""};
        try {
            saveCurrentWeekInfo(newTimeData,context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 从currentWeek文件中读取，返回包含周，日期，星期几，的字符串数组
     */
    private static String [] getLastWeekInfo(Context context)throws IOException{
        String [ ] timeData = new String[3];
        FileInputStream in = null;
        BufferedReader reader = null;
        in = context.openFileInput("currentCheckWeek");
        reader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        in = context.openFileInput("currentCheckWeek");
        reader = new BufferedReader(new InputStreamReader(in));
        while ((line = reader.readLine()) != null){
            timeData = line.split(",");
        }
        reader.close();
        return timeData;
    }

    public static void  saveCurrentWeekInfo(String [] dataInfo,Context context) throws IOException{
        StringBuilder sb = new StringBuilder();
        for (String str : dataInfo)
        {
            sb.append(str+",");
        }
        FileOutputStream out = null;
        BufferedWriter writer = null;
        out = context.openFileOutput("currentCheckWeek", Context.MODE_PRIVATE);
        writer = new BufferedWriter(new OutputStreamWriter(out));
        Log.d(TAG, "saveCurrentWeekInfo: "+sb.toString());
        writer.write(sb.toString());
        writer.close();
    }

    /**
     * 取得数据文件CurrentWeek中当前周
     * @param context
     * @return  当前周
     */
    public static int getCurrentWeek(Context context){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder contect  = new StringBuilder();
        String [ ] timeData;
        int FlagWeek = 0;
        try {
            in = context.openFileInput("currentCheckWeek");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                timeData = line.split(",");
                FlagWeek = Integer.parseInt(timeData[0]);
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return FlagWeek;
    }

    /**
     * 从数据文件中读取出各个课上课时间
     * @param context
     * @return   各课程上课时间组成的CourseTime对象
     */
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
