package com.igordubrovin.trainstimetable.utils;

import java.util.Calendar;

/**
 * Created by Игорь on 01.03.2017.
 */

public class CurrentDate {

    public static String getCurrentData(){
        String date;
        Calendar calendar = Calendar.getInstance();
        int dayMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        date = String.format("%02d", dayMonth) + "." + String.format("%02d", month) + "." + year + " " + getDayOfWeek(dayOfWeek);
        return date;
    }

    private static String getDayOfWeek(int dayOfWeek){
        switch (dayOfWeek){
            case Calendar.MONDAY:
                return "Понедельник";
            case Calendar.TUESDAY:
                return "Вторник";
            case Calendar.WEDNESDAY:
                return "Среда";
            case Calendar.THURSDAY:
                return "Четверг";
            case Calendar.FRIDAY:
                return "Пятница";
            case Calendar.SATURDAY:
                return "Суббота";
            case Calendar.SUNDAY:
                return "Воскресенье";
        }
        return null;
    }
}
