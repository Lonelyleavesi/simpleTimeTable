package com.project.tools;

import android.util.Log;

public class CustomTime {
    public int hour;
    public int minute;

    public CustomTime() {
        this.hour = 0;
        this.minute = 0;
    }

    public  CustomTime (CustomTime ct)
    {
        this.hour = ct.getHour();
        this.minute = ct.getMinute();
    }


    public CustomTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public void addMinute(int n){
        minute = minute + n;
        if (minute >= 60){
            minute = minute - 60 ;
            hour = hour + 1 ;
        }
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    /**
     * 输出当前时间 xx：xx的格式
     * @return
     */
    public String formatTime(){
        StringBuffer re;
        if (hour < 10){
            re = new StringBuffer("0"+hour);
        }
        else{
            re = new StringBuffer(hour+"");
        }
        re.append(":");
        if (minute < 10){
            re.append("0"+minute);
        }else {
            re.append(minute);
        }
        return re.toString();
    }

    @Override
    public String toString() {
        return hour+","+minute;
    }
}
