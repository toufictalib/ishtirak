package com.aizong.ishtirak.gui.table;

import com.aizong.ishtirak.common.misc.ServiceProvider;
import com.aizong.ishtirak.common.table.ReportTableModel;

@SuppressWarnings("serial")
public class SubscriptionBundleFilterTable extends BundleFilterTable {

    public SubscriptionBundleFilterTable(String title) {
	super(title, false);
    }
    
    @Override
    public ReportTableModel getReportTableModel() {
	return ServiceProvider.get().getReportServiceImpl().getMonthlyBundles(false);
    }

}
