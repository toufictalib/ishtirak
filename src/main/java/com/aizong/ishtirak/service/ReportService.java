package com.aizong.ishtirak.service;

import java.util.List;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SearchBean;
import com.aizong.ishtirak.bean.SummaryBean;
import com.aizong.ishtirak.common.misc.component.DateRange;

public interface ReportService {

    ReportTableModel getSubscribers();

    ReportTableModel getEngines();

    ReportTableModel getEmployees();

    ReportTableModel getVillages();

    ReportTableModel getEmployeeTypes();

    ReportTableModel getSubscriptionsIncomeReport();

    ReportTableModel getExpenses(String startDate, String endDate);

    ReportTableModel getOutExpenses();

    ReportTableModel getSubscriptionsHistory(Long contractId, SearchBean searchBean) throws Exception;
    
    ReportTableModel getActiveIshtirakInfo(List<Long> contractIds);

    ReportTableModel getActiveIshtirakWithoutReceipts();
    
    ReportTableModel getEmployeesPayments(Long employeeId, SearchBean searchBean);
    
    ReportTableModel getCounterHistory(Long subscriberId, DateRange dateRange);

    ReportTableModel getExpenses(ExpensesType expensesType, SearchBean searchBean);
    
    SummaryBean getSummaryResult(String fromDate, String toDate);
    
    ReportTableModel getContractHistoryPerContractOrALl(List<String> uniqueContractIds, String fromDate, String toDate, Boolean paid);
    
    ReportTableModel getExportedFiles(boolean counterInput);

    ReportTableModel getCounterReport(String fromDate, String toDate);
}
