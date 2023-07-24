package com.fivesigma.backend.util;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Andy
 * @date 2022/11/4
 */
@Component
public class DateUtil {

    private static String format = "yyyy-MM-dd";
    private static  String format1 = "yyy-MM-dd-HH:mm";
    private static DateFormat dateFormat = new SimpleDateFormat(format);
    private static DateFormat dateFormat1 = new SimpleDateFormat(format1);
    public static Date parseDate(String dataString) throws ParseException {
        return dateFormat.parse(dataString);
    }

    public static boolean isFuture(String dateString) {
        Date now = new Date();
        try {
            Date target = parseDate(dateString);
            return now.before(target);
        } catch (ParseException e) {
            return false;
        }
    }
    public static String createNewDate(){
        return dateFormat.format(new Date());
    }
    public static String createNewTimeStamp(){
        return dateFormat1.format(new Date());
    }

    public static boolean inProgress(String end_date){
        if (end_date == null) return false;
        try {
            Date deadline = parseDate(end_date);
            Calendar next_week = Calendar.getInstance();
            next_week.add(Calendar.DATE, 7);
            if (deadline.before(next_week.getTime())){
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return false;
    }
}
