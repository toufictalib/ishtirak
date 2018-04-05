package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.util.ArrayList;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
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
	    WindowUtils.createDialog(ReportButtonsPanel.this.getOwner(), e.getActionCommand(),new ReportTablePanel(e.getActionCommand()) {
	        
	        @Override
	        public ReportTableModel getReportTableModel() {
	    	return ServiceProvider.get().getReportServiceImpl().getSubscriptionsIncomeReport();
	        }
	    });
	   
	});
	
	JideButton btnMonthlyExpenses = button(message("reports.subscirption.expenses"), "subreport.png");
	btnMonthlyExpenses.addActionListener(e -> {
	    WindowUtils.createDialog(ReportButtonsPanel.this.getOwner(), e.getActionCommand(),new ReportTablePanel(e.getActionCommand()) {
	        
	        @Override
	        public ReportTableModel getReportTableModel() {
	    	return ServiceProvider.get().getReportServiceImpl().getExpenses();
	        }
	    });
	   
	});
	
	JideButton btnCounterHistory = button(message("reports.subscirption.counterHistory"), "subreport.png");
	btnCounterHistory.addActionListener(e -> {
	    WindowUtils.createDialog(ReportButtonsPanel.this.getOwner(), e.getActionCommand(), new CustomerSearchPanel());
	   
	});
	
	JideButton btnIshtirakReport = button(message("reports.subscirption.activeIshtirak"), "subreport.png");
	btnIshtirakReport.addActionListener(e -> {
	    WindowUtils.createDialog(ReportButtonsPanel.this.getOwner(), e.getActionCommand(),
		    new ReportTablePanel(e.getActionCommand()) {

			@Override
			public ReportTableModel getReportTableModel() {
			    return ServiceProvider.get().getReportServiceImpl()
				    .getActiveIshtirakInfo(new ArrayList<>());
			}
		    });

	});
	
	JideButton btnIshtirakReportWithoutReceipts = button(message("reports.subscirption.activeIshtirakWithoutReceipts"), "subreport.png");
	btnIshtirakReportWithoutReceipts.addActionListener(e -> {
	    WindowUtils.createDialog(ReportButtonsPanel.this.getOwner(), e.getActionCommand(),
		    new ReportTablePanel(e.getActionCommand()) {

			@Override
			public ReportTableModel getReportTableModel() {
			    return ServiceProvider.get().getReportServiceImpl()
				    .getActiveIshtirakWithoutReceipts();
			}
		    });

	});
	
	builder.append(btnMonthlyReports);
	builder.append(btnMonthlyExpenses);
	builder.append(btnCounterHistory);
	builder.append(btnIshtirakReport);
	builder.append(btnIshtirakReportWithoutReceipts);
	return builder.getPanel();
    }

    private JideButton button(String text, String imagePath) {
   	return (JideButton) MainFrame.button(text, imagePath);
       }
    @Override
    protected String getLayoutSpecs() {
	return "p,30dlu,p";
    }

}
