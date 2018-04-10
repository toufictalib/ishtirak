package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.gui.SummaryTablePanel;
import com.aizong.ishtirak.gui.table.EmployeeTablePanel;
import com.aizong.ishtirak.gui.table.ExpensesTablePanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

@SuppressWarnings("serial")
public class GeneralReportButtonsPanel extends BasicForm {

    public GeneralReportButtonsPanel() {
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
	    MainFrame.openWindow(GeneralReportButtonsPanel.this.getOwner(), e.getActionCommand(),
		    new EmployeeTablePanel(btnMonthlyReports.getActionCommand()));

	});

	JideButton btnSummary = button(message("reports.subscirption.incomeExpenses"), "subreport.png");
	btnSummary.addActionListener(e -> {
	    MainFrame.openWindowAsFrame(GeneralReportButtonsPanel.this.getOwner(), e.getActionCommand(),
		    new SummaryTablePanel(btnMonthlyReports.getActionCommand()));

	});
	
	builder.append(btnMonthlyReports);
	builder.append(btnSummary);
	
	for (ExpensesType expensesType : ExpensesType.values()) {
	    JideButton button = button(enumMessage(expensesType.name(), ExpensesType.class), "subreport.png");
	    button.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    MainFrame.openWindow(GeneralReportButtonsPanel.this.getOwner(), e.getActionCommand(),
			    new ExpensesTablePanel(e.getActionCommand(), expensesType));
		}
	    });
	    builder.append(button);
	}

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
