package com.aizong.ishtirak.service;

import com.aizong.ishtirak.bean.ReportTableModel;

public interface ReportService {

    ReportTableModel getSubscribers();

    ReportTableModel getEngines();

    ReportTableModel getEmployees();

    ReportTableModel getVillages();

    ReportTableModel getEmployeeTypes();

    ReportTableModel getSubscriptionsIncomeReport();

    ReportTableModel getExpenses();

    ReportTableModel getOutExpenses();
}
