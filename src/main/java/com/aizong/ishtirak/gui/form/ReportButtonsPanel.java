package com.aizong.ishtirak.gui.form;

import java.awt.Component;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.utils.ImageHelperCustom;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.table.ReportTablePanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
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
	builder.setDefaultDialogBorder();
	JideButton btnMonthlyReports = button("تقرير الإشتركات", "48px_customer.png");
	btnMonthlyReports.addActionListener(e -> {
	    WindowUtils.createDialog(ReportButtonsPanel.this.getOwner(), e.getActionCommand(),new ReportTablePanel(e.getActionCommand()) {
	        
	        @Override
	        public ReportTableModel getReportTableModel() {
	    	return ServiceProvider.get().getReportServiceImpl().getSubscriptionsIncomeReport();
	        }
	    });
	   
	});
	
	JideButton btnMonthlyExpenses = button("تقرير المصاريف", "48px_customer.png");
	btnMonthlyExpenses.addActionListener(e -> {
	    WindowUtils.createDialog(ReportButtonsPanel.this.getOwner(), e.getActionCommand(),new ReportTablePanel(e.getActionCommand()) {
	        
	        @Override
	        public ReportTableModel getReportTableModel() {
	    	return ServiceProvider.get().getReportServiceImpl().getTransactions();
	        }
	    });
	   
	});
	
	builder.append(btnMonthlyReports);
	builder.append(btnMonthlyExpenses);
	return builder.getPanel();
    }

    private JideButton button(String text, String imagePath) {
   	JideButton btnEngineManagement = new JideButton(text,
   		ImageHelperCustom.get().getImageIcon("menus/" + imagePath));
   	return btnEngineManagement;
       }
    @Override
    protected String getLayoutSpecs() {
	return "p,15dlu,p";
    }

}
