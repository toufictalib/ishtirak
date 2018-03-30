package com.aizong.ishtirak.common.misc.component;

import java.util.Date;

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

}
