package com.aizong.ishtirak.service;

import java.util.List;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SearchBean;
import com.aizong.ishtirak.bean.SummaryBean;

public interface ReportService {

    ReportTableModel getSubscribers();

    ReportTableModel getEngines();

    ReportTableModel getEmployees();

    ReportTableModel getVillages();

    ReportTableModel getEmployeeTypes();

    ReportTableModel getSubscriptionsIncomeReport();

    ReportTableModel getExpenses();

    ReportTableModel getOutExpenses();

    ReportTableModel getSubscriptionsHistory(Long contractId, SearchBean searchBean) throws Exception;
    
    ReportTableModel getActiveIshtirakInfo(List<Long> contractIds);

    ReportTableModel getActiveIshtirakWithoutReceipts();
    
    ReportTableModel getEmployeesPayments(Long employeeId, SearchBean searchBean);
    
    ReportTableModel getCounterHistory(Long subscriberId,  SearchBean searchBean);

    ReportTableModel getExpenses(ExpensesType expensesType, SearchBean searchBean);
    
    SummaryBean getSummaryResult(String fromDate, String toDate);
}
