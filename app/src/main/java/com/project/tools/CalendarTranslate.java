package com.project.tools;

public class CalendarTranslate {
    /**
     *由于日历中的天数记法与现实记法有区别，如星期天视为第一天，所以需要转换
     * @param actualDay
     * @return
     */
    static public int actualDayToCalendarDay(int actualDay){
        return (actualDay + 1)%7 ;
    }

    /**
     *由于日历中的天数记法与现实记法有区别，如星期天视为第一天，所以需要转换
     * @return
     */
    static public int calendarDayToActualDay(int calendarDay){
        int actualday = calendarDay - 1 ;
        if (actualday == 0){
            actualday = 7;
        }
        return actualday;
    }
}
