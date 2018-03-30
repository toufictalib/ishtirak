package com.aizong.ishtirak.gui.table;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;

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
