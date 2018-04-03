package com.aizong.ishtirak.service;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SearchBean;

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
}
