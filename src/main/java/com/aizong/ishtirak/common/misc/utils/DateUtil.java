/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc.utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import com.aizong.ishtirak.LoginForm;
import com.aizong.ishtirak.common.misc.component.DateRange;

/**
 *
 * @author User
 */
public class DateUtil {

    public static final int DAYS_OF_NEXT_MONTH = 5;
    public static final int START_MONTH = 6;
    public static final String SHORT_SQL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String LONG_SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat(SHORT_SQL_DATE_FORMAT);

    public static String[] monthes;
    
    public static Date addDays(Date date, int days) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(Calendar.DAY_OF_YEAR, days); // minus number would decrement the
					     // days
	return cal.getTime();
    }

    public static Date minusDays(Date date, int days) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(Calendar.DAY_OF_YEAR, -days); // minus number would decrement
					      // the days
	return cal.getTime();
    }

    public static Date addMonths(Date date, int months) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(Calendar.MONTH, months); // minus number would decrement the
					 // days
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

    public static LocalDate localDate(Date date) {
	 return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
     * use to convert date to timestamp with 23 hour and 59 minutes and 59
     * second only
     * 
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

    public static Date mergeDate1WithTimeOfDate2(Date date1, Date date2) {
	Calendar cal = Calendar.getInstance();
	Calendar cal2 = Calendar.getInstance();

	cal.setTime(date1);
	cal2.setTime(date2);

	cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
	cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
	cal.set(Calendar.SECOND, cal2.get(Calendar.SECOND));
	return cal.getTime();
    }

    public static String formatShortSqlDate(Date date) {
	return SHORT_FORMAT.format(date);
    }
    
    public static String formatTimestampSqlDate(Date date) {
   	return new SimpleDateFormat(LONG_SQL_DATE_FORMAT).format(date);
       }

    public static Date fromLocalDate(LocalDate localDate) {
	return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static DateRange getStartEndDateOfCurrentMonth() {
	return getStartEndDateOfCurrentMonth(LocalDate.now());
    }
    
    public static DateRange getStartEndDateOfCurrentMonth(LocalDate localDate) {
	LocalDate initial = localDate;
	if(initial.getDayOfMonth()<6) {
	    initial = initial.minusMonths(1);
	}
	LocalDate start = initial.withDayOfMonth(START_MONTH);
	LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth()).plusDays(DAYS_OF_NEXT_MONTH);

	return new DateRange(fromLocalDate(start), fromLocalDate(end));
      }
    
    

    /**
     * effective month is between 6 of previous month and 5 of current month
     * @return
     */
    public static int getEffectiveMonth() {
	LocalDateTime currentTime = LocalDateTime.now();
	Month month = currentTime.getMonth();
	int currentMonth = month.getValue();
	if (currentTime.getDayOfMonth() < 6) {
	    currentMonth--;
	}
	
	if(currentMonth==0) {
	    currentMonth = 12;
	}
	return currentMonth;
    }
    
    
    public static String getContractDate() {
	if (Boolean.valueOf(ServiceProvider.get().getMessage().getMessage("day.consider.active"))) {
	    String message = ServiceProvider.get().getMessage().getMessage("day.consider.current");
	    int dayOfMonth = Integer.parseInt(message) + 1;
	    LocalDate now = LocalDate.now();
	    LocalDate date = LocalDate.of(now.getYear(), getEffectiveMonth(), dayOfMonth);
	    return date.format(DateTimeFormatter.ofPattern(SHORT_SQL_DATE_FORMAT));
	}
	return getStartEndDateOfCurrentMonth().getEndDateAsString();
    }

    public static String getCurrentMonthLabel() {
	return getCurrentMonthLabel(LocalDate.now());
    }
    
    public static String getCurrentMonthLabel(LocalDate date) {
	if(date.getDayOfMonth()<6) {
	    date = date.minusMonths(1);
	}
	String monthName = getMonthName(date.getMonthValue()-1);
	return date.getYear()+"/"+monthName;
    }
    public static boolean isCountedAsCurrentMonth(LocalDate localDate) {
	if (Boolean.valueOf(ServiceProvider.get().getMessage().getMessage("day.consider.active"))) {
	    
	    //today
	    LocalDate today = LocalDate.now();
	    
	    String message = ServiceProvider.get().getMessage().getMessage("day.consider.current");
	    return localDate.getDayOfMonth() < (Integer.parseInt(message) + 1) && today.getMonth()==localDate.getMonth() &&
		    today.getYear() == localDate.getYear();
	}
	return true;

    }
    
    public static String getMonthName(int month) {
	try {
	    if (monthes == null) {
		monthes = ServiceProvider.get().getMessage().getMessage("monthes").split(",");
	    }
	    if (month < monthes.length) {
		return monthes[month];
	    }
	    return DateFormatSymbols.getInstance(LoginForm.getCurrentLocale()).getMonths()[month];
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return month+"";
    }
    
    
    public static void main(String[] args) {
	LocalDate date = LocalDate.of(2018, 05, 02);
	System.out.println(DateUtil.getStartEndDateOfCurrentMonth(date).getStartDateAsString());
	System.out.println(DateUtil.getStartEndDateOfCurrentMonth(date).getEndDateAsString());
    }
    
    public static Date toDate(String date) throws ParseException {
	return new SimpleDateFormat(SHORT_SQL_DATE_FORMAT).parse(date);
    }
    
 /*   public static void main(String[]args) {
   	DateRange startEndDateOfCurrentMonth = getStartEndDateOfCurrentMonth();
   	System.out.println(startEndDateOfCurrentMonth.getStartDateAsString());
   	System.out.println(startEndDateOfCurrentMonth.getEndDateAsString());
   	
       }*/
}
