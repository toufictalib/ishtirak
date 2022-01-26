package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.table.CounterConsumptionSumTablePanel;
import com.aizong.ishtirak.gui.table.EmployeeTablePanel;
import com.aizong.ishtirak.gui.table.SearchTablePanel;
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

	
	
	 JideButton button = button(message("contract.report"), "subreport.png");
	    button.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    SearchTablePanel searchTablePanel = new SearchTablePanel(e.getActionCommand());
		    searchTablePanel.setPreferredSize(ComponentUtils.getDimension(90, 85));
	            WindowUtils.createDialog(GeneralReportButtonsPanel.this.getOwner(), e.getActionCommand(),searchTablePanel);
		}
	    });
	    
	    JideButton counterConsumptionSumTablePanelButton = button(message("report.counter.consumption.sum"), "subreport.png");
	    counterConsumptionSumTablePanelButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			CounterConsumptionSumTablePanel consumptionSumTablePanel = new CounterConsumptionSumTablePanel(e.getActionCommand());
			consumptionSumTablePanel.setPreferredSize(ComponentUtils.getDimension(90, 85));
	            WindowUtils.createDialog(GeneralReportButtonsPanel.this.getOwner(), e.getActionCommand(),consumptionSumTablePanel);
		}
	    });
	    
	
	
	builder.append(btnMonthlyReports);
	builder.append(button);
	builder.append(counterConsumptionSumTablePanelButton);
	
	
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
