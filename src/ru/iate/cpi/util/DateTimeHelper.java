package ru.iate.cpi.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sanea on 12.06.15.
 */
public class DateTimeHelper {
    public static Date FirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //set first date
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date LastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //set last date
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date GetPreviousDatePeriod(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //set previous month
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public static Date GetDayStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        //set 00:00:00
        cal.set(year, month, day, 0, 0, 0);
        return cal.getTime();
    }

    public static Date GetDayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        //set 23:59:59
        cal.set(year, month, day, 23, 59, 59);
        return cal.getTime();
    }
}
