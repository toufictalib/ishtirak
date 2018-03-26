package com.aizong.ishtirak.bundle;

import com.aizong.ishtirak.common.ServiceProvider;
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
