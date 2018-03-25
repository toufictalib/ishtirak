package com.aizong.ishtirak.table;

import com.aizong.ishtirak.common.CommonFilterTable;
import com.aizong.ishtirak.common.ServiceProvider;
import com.aizong.ishtirak.common.l.SubscriberTableListener;

@SuppressWarnings("serial")
public class SubscriberFilterTable extends CommonFilterTable  {

    public SubscriberFilterTable(String title) {
	super(title, new SubscriberTableListener());
    }

    @Override
    public ReportTableModel getReportTableModel() {
	return ServiceProvider.get().getReportServiceImpl().getSubscribers();
    }
}
