package com.aizong.ishtirak.dao;

import java.util.Date;
import java.util.List;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.TransactionType;

public interface ReportDao extends GenericDao<Object>{

    List<Object[]> getSubscriptionsIncomeReport(Date startDate, Date endDate);
    
    List<Object[]> getExpenses(ExpensesType expensesType, Date startDate, Date endDate);

    List<Object[]> getOutExpenses(Date startDate, Date endDate);

    List<Object[]> getSubscribers();

    List<Object[]> getSubscriptionsHistory(Long contractId, String fromDate, String endDate, TransactionType transactionType);

    List<Object[]> getActiveIshtirakInfo(List<Long> contractIds);

    List<Long> getActiveContractWithoutReceipts();

    /**
     * 
     * @param employeeId in case the value is null, get all employees
     * @return
     */
    List<Object[]> getEmployeesPayments(Long employeeId, String fromDate, String endDate);

    List<Object[]> getCounterHistory(Long subscriberId, String fromDate, String endDate);

    
}
