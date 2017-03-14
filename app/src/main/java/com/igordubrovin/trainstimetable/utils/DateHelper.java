package com.igordubrovin.trainstimetable.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Игорь on 01.03.2017.
 */

public class DateHelper {

    public static String getStrCurrentDate(){
        String date;
        List<Integer> dateList;
        dateList = getListCurrentDate();
        date = String.format("%02d", dateList.get(0)) + "." + String.format("%02d", dateList.get(1)) + "." + dateList.get(2);
        return date;
    }

    public static List<Integer> getListCurrentDate(){
        List<Integer> dateList = new ArrayList<>(3);
        Calendar calendar = Calendar.getInstance();
        dateList.add(calendar.get(Calendar.DAY_OF_MONTH));
        dateList.add(calendar.get(Calendar.MONTH) + 1);
        dateList.add(calendar.get(Calendar.YEAR));
        return dateList;
    }

    public static String getDayOfWeek(){
        return strDayOfWeek(intDayOfWeek());
    }

    public static String getMonthStr(int month){
        switch (month){
            case 0: return "Январь";
            case 1: return "Февраль";
            case 2: return "Март";
            case 3: return "Апрель";
            case 4: return "Май";
            case 5: return "Июнь";
            case 6: return "Июль";
            case 7: return "Август";
            case 8: return "Сентябрь";
            case 9: return "Октябрь";
            case 10: return "Ноябрь";
            case 11: return "Сентябрь";
            default: return null;
        }
    }

    private static int intDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private static String strDayOfWeek(int dayOfWeek){
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
