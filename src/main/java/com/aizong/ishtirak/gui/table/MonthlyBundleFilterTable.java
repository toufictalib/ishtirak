package com.aizong.ishtirak.gui.table;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;

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
