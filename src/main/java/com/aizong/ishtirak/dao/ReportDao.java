package com.aizong.ishtirak.dao;

import java.util.Date;
import java.util.List;

public interface ReportDao extends GenericDao<Object>{

    List<Object[]> getSubscriptionsIncomeReport(Date startDate, Date endDate);
    
    List<Object[]> getExpenses(Date startDate, Date endDate);

    List<Object[]> getOutExpenses(Date startDate, Date endDate);

    
}
