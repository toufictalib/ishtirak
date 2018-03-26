/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author User
 */
public class DateUtil {

	public static final String SHORT_SQL_DATE_FORMAT = "yyyy-MM-dd"; 
	public static final String LONG_SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"; 
	
	public static final SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat(SHORT_SQL_DATE_FORMAT);
	
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months); //minus number would decrement the days
        return cal.getTime();
    }

    public static boolean isBeforeToday(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return d.before(calendar.getTime());
    }

    public static boolean isAfterToday(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return d.after(calendar.getTime());
    }

    public static boolean isAfterTodayWihtoutEqual(Date d) {
        return isToday(d) || isAfterToday(d);
    }

    public static boolean isBeforeTodayWihtoutEqual(Date d) {
        return isToday(d) || isBeforeToday(d);
    }

    /**
     * check if date is today
     *
     * @param d
     * @return
     */
    public static boolean isToday(Date d) {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeInMillis(d.getTime());
        return (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * check if mainDate equal dateToCompare or dateToCompare is after mainDate
     *
     * @param mainDate
     * @param dateToCompare
     * @return
     */
    public static boolean isEqualOrAfter(Date mainDate, Date dateToCompare) {
        mainDate = toShortDate(mainDate);
        dateToCompare = toShortDate(dateToCompare);
        return dateToCompare.equals(mainDate) || dateToCompare.after(mainDate);
    }

    /**
     * Date only contains day of week,month and year
     *
     * @param date
     * @return
     */
    public static Date toShortDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();

    }
    
     public static Date toYearAndMonthOnly(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toShortDate(date));
        calendar.set(Calendar.MONTH, 0);

        return calendar.getTime();

    }

    /**
     * Hold only year,month and day
     *
     * @param date
     * @return Date
     */
    public static Date removeExternal24(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

  


    
    /**
     * use to convert date to timestamp with 23 hour and 59 minutes and 59 second  only 
     * @param date
     * @return 
     */
    public static Date getFormattedToDateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
    
    public static Date mergeDate1WithTimeOfDate2(Date date1,Date date2)
    {
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal.setTime(date1);
        cal2.setTime(date2);

        cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal2.get(Calendar.SECOND));
        return cal.getTime();
    }
    
    public static String formatShortSqlDate(Date date)
    {
    	return SHORT_FORMAT.format(date);
    }
}
