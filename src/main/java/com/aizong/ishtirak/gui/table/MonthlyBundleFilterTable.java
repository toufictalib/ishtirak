package com.aizong.ishtirak.gui.table;

import com.aizong.ishtirak.common.misc.ServiceProvider;
import com.aizong.ishtirak.common.table.ReportTableModel;

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
