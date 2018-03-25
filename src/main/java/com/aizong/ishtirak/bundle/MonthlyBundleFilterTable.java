package com.aizong.ishtirak.bundle;

import com.aizong.ishtirak.common.ServiceProvider;
import com.aizong.ishtirak.table.ReportTableModel;

@SuppressWarnings("serial")
public class MonthlyBundleFilterTable extends BundleFilterTable {

    public MonthlyBundleFilterTable(String title) {
	super(title, true);
    }
    
    @Override
    public ReportTableModel getReportTableModel() {
	return ServiceProvider.get().getReportServiceImpl().getMonthlyBundles(true);
    }

}
