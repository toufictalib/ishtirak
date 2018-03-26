package com.aizong.ishtirak.gui.table;

import com.aizong.ishtirak.common.l.SubscriberTableListener;
import com.aizong.ishtirak.common.misc.ServiceProvider;
import com.aizong.ishtirak.common.table.CommonFilterTable;
import com.aizong.ishtirak.common.table.ReportTableModel;

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
