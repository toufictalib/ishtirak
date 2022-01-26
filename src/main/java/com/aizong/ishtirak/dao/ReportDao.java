package com.aizong.ishtirak.dao;

import java.util.Date;
import java.util.List;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.misc.component.DateRange;

public interface ReportDao extends GenericDao<Object>{

    List<Object[]> getSubscriptionsIncomeReport(String startDate, String endDate);
    
    List<Object[]> getExpenses(ExpensesType expensesType, String startDate, String endDate);

    List<Object[]> getOutExpenses(Date startDate, Date endDate);

    List<Object[]> getSubscribers();

    List<Object[]> getSubscriptionsHistory(String  contractUniqueCode, String fromDate, String endDate, TransactionType transactionType);

    List<Object[]> getActiveIshtirakInfo(List<Long> contractIds);

    List<Long> getActiveContractWithoutReceipts(String startDate, String endDate);

    /**
     * 
     * @param employeeId in case the value is null, get all employees
     * @return
     */
    List<Object[]> getEmployeesPayments(Long employeeId, String fromDate, String endDate);

    List<Object[]> getCounterHistory(Long subscriberId, String fromDate, String endDate);

    boolean hasCounterBundle(String contractUniqueCode);

    List<Object[]> getContractHistoryPerContractOrALl(List<String> uniqueContractIds, String fromDate, String toDate, Boolean paid);

    List<Object[]> getExpensesPerEngine(String engine, String fromDate, String toDate);

    List<Object[]> getConsumptionPerEngine(String engine, String fromDate, String toDate);

    List<Object[]> getIncomePerEngine(String engine, String fromDate, String toDate);

    /**
     * 
     * @param previousCounterDate this date is precisely the start of current date because we need
     * to get the previous counter that it is the first one before the current date
     * @param date
     * @return
     */
    List<Object[]> getExportedFiles(DateRange dateRangeOfCurrentMonth);

    List<Object[]> getCounterReport(String fromDate, String toDate);

    List<Object[]> getPaymentExportedFiles(String previousCounterDate, String date);

	List<Object[]> getIncomePerEnginePaid(String engine, String fromDate, String toDate);

	List<Object[]> getCounterConsumptionSumByEngine(String fromDate, String toDate);
	
	List<Object[]> getCounterConsumptionFeesSumByEngine(String fromDate, String toDate);
    
}
