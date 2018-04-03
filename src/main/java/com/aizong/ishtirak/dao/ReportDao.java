package com.aizong.ishtirak.dao;

import java.util.Date;
import java.util.List;

import com.aizong.ishtirak.bean.TransactionType;

public interface ReportDao extends GenericDao<Object>{

    List<Object[]> getSubscriptionsIncomeReport(Date startDate, Date endDate);
    
    List<Object[]> getExpenses(Date startDate, Date endDate);

    List<Object[]> getOutExpenses(Date startDate, Date endDate);

    List<Object[]> getSubscribers();

    List<Object[]> getSubscriptionsHistory(Long contractId, String fromDate, String endDate, TransactionType transactionType);

    
}
