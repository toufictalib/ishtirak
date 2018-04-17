package com.aizong.ishtirak.gui.form;

import java.awt.Component;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.gui.SummaryTablePanel;
import com.aizong.ishtirak.gui.table.EmployeeTablePanel;
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
	JideButton btnMonthlyReports = button(message("reports.subscirption.employee"), "subreport.png");
	btnMonthlyReports.addActionListener(e -> {
	    MainFrame.openWindow(GeneralReportButtonsPanel.this.getOwner(), e.getActionCommand(),
		    new EmployeeTablePanel(btnMonthlyReports.getActionCommand()));

	});

	
	
	builder.append(btnMonthlyReports);
	
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
