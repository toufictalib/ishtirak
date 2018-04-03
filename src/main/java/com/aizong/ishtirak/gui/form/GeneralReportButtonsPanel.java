package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.utils.ImageHelperCustom;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
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
	JideButton btnMonthlyReports = button(message("reports.subscirption.contract"), "48px_customer.png");
	btnMonthlyReports.addActionListener(e -> {
	    WindowUtils.createDialog(GeneralReportButtonsPanel.this.getOwner(), e.getActionCommand(),
		    new EmployeeTablePanel(btnMonthlyReports.getActionCommand()));

	});

	builder.append(btnMonthlyReports);
	for (ExpensesType expensesType : ExpensesType.values()) {
	    JideButton button = new JideButton(enumMessage(expensesType.name(), ExpensesType.class));
	    button.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    WindowUtils.createDialog(GeneralReportButtonsPanel.this.getOwner(), e.getActionCommand(),
			    new ExpensesTablePanel(e.getActionCommand(), expensesType));
		}
	    });
	    builder.append(button);
	}

	return builder.getPanel();
    }

    private JideButton button(String text, String imagePath) {
	JideButton btnEngineManagement = new JideButton(text,
		ImageHelperCustom.get().getImageIcon("menus/" + imagePath));
	return btnEngineManagement;
    }

    @Override
    protected String getLayoutSpecs() {
	return "p,30dlu,p";
    }

}
