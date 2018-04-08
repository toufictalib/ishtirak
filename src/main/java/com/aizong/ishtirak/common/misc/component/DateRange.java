package com.aizong.ishtirak.common.misc.component;

import java.util.Date;

import com.aizong.ishtirak.common.misc.utils.DateUtil;

public class DateRange {

    private final Date startDate;
    private final Date endDate;

    public DateRange(Date startDate, Date endDate) {
	super();
	this.startDate = startDate;
	this.endDate = endDate;
    }

    public Date getStartDate() {
	return startDate;
    }

    public Date getEndDate() {
	return endDate;
    }
    
    public String getStartDateAsString() {
	return DateUtil.formatShortSqlDate(getStartDate());
    }
    
    public String getEndDateAsString() {
   	return DateUtil.formatShortSqlDate(getEndDate());
       }

}
