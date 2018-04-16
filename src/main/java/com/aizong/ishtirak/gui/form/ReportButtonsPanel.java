package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.gui.table.ReportTablePanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

@SuppressWarnings("serial")
public class ReportButtonsPanel extends BasicForm {

    public ReportButtonsPanel() {
	super();
	initializePanel();
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder = new DefaultFormBuilder(new FormLayout(getLayoutSpecs()));
	builder.setDefaultDialogBorder();
	JideButton btnMonthlyReports = button(message("reports.subscirption.contract"), "subreport.png");
	btnMonthlyReports.addActionListener(e -> {
	   openWindow( e.getActionCommand(),
		    new ReportTablePanel(e.getActionCommand()) {

			@Override
			public ReportTableModel getReportTableModel() {
			    return ServiceProvider.get().getReportServiceImpl().getSubscriptionsIncomeReport();
			}
		    });

	});

	JideButton btnMonthlyExpenses = button(message("reports.subscirption.expenses"), "subreport.png");
	btnMonthlyExpenses.addActionListener(e -> {
	   openWindow( e.getActionCommand(),
		    new ReportTablePanel(e.getActionCommand()) {

			@Override
			public ReportTableModel getReportTableModel() {
			    DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth();
			    return ServiceProvider.get().getReportServiceImpl().getExpenses(dateRange.getStartDateAsString(), dateRange.getEndDateAsString());
			}
			
			@Override
			protected int getTotalTargetedColumn() {
			    return 2;
			}
		    });

	});

	JideButton btnIshtirakReport = button(message("reports.subscirption.activeIshtirak"), "subreport.png");
	btnIshtirakReport.addActionListener(e -> {
	   openWindow( e.getActionCommand(),
		    new ReportTablePanel(e.getActionCommand()) {

			@Override
			public ReportTableModel getReportTableModel() {
			    return ServiceProvider.get().getReportServiceImpl()
				    .getActiveIshtirakInfo(new ArrayList<>());
			}
		    });

	});

	JideButton btnIshtirakReportWithoutReceipts = button(
		message("reports.subscirption.activeIshtirakWithoutReceipts"), "subreport.png");
	btnIshtirakReportWithoutReceipts.addActionListener(e -> {
	   openWindow( e.getActionCommand(),
		    new ReportTablePanel(e.getActionCommand()) {

			@Override
			public ReportTableModel getReportTableModel() {
			    return ServiceProvider.get().getReportServiceImpl().getActiveIshtirakWithoutReceipts();
			}
		    });

	});

	builder.append(btnMonthlyReports);
	builder.append(btnMonthlyExpenses);
	builder.append(btnIshtirakReport);
	builder.append(btnIshtirakReportWithoutReceipts);
	return builder.getPanel();
    }

    private void openWindow(String text, JPanel component) {
	MainFrame.openWindow(ReportButtonsPanel.this.getOwner(), text, component);
    }

    private JideButton button(String text, String imagePath) {
	return (JideButton) MainFrame.button(text, imagePath);
    }

    @Override
    protected String getLayoutSpecs() {
	return "p,30dlu,p";
    }

}
