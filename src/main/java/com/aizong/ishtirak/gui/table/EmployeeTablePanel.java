package com.aizong.ishtirak.gui.table;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.SearchPanel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Employee;
import com.jgoodies.forms.builder.DefaultFormBuilder;

@SuppressWarnings("serial")
public class EmployeeTablePanel extends ReportTablePanel {

    public EmployeeTablePanel(String title) {
	super(title, null);
	start();
    }

    @Override
    protected JPanel initUI() {

	JButton btnSearch = ButtonFactory.createBtnSearch();

	List<Employee> employees = ServiceProvider.get().getSubscriberService().getEmployees();
	ExCombo<Employee> combo = new ExCombo<>(message("all"), true, employees);
	combo.setPreferredSize(new Dimension(200, combo.getPreferredSize().height));

	DateRange startEndDateOfCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	SearchPanel createDefault = SearchPanel.createDefault(startEndDateOfCurrentMonth.getStartDate(),
		startEndDateOfCurrentMonth.getEndDate());

	btnSearch.addActionListener(e -> {
	    try {
		ReportTableModel reportTableModel = ServiceProvider.get().getReportServiceImpl()
			.getEmployeesPayments(combo.getValue()==null ? null : combo.getValue().getId(), createDefault.toSearchBean());
		fillTable(reportTableModel);
	    } catch (Exception e1) {
		e1.printStackTrace();
		MessageUtils.showErrorMessage(getOwner(), e1.getMessage());
	    }
	});
	
	String leftToRightSpecs = "fill:p:grow";
	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, "p,p,fill:240dlu:grow,p");
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);
	JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel.add(createDefault);
	panel.add(new JLabel(message("empolyees")));
	panel.add(combo);
	panel.add(btnSearch);

	builder.append(panel, builder.getColumnCount());
	// builder.append(txtFE, 3);

	JScrollPane scrollPane = new JScrollPane(table);
	// scrollPane.setPreferredSize(ComponentUtils.getDimension(60, 60));
	builder.append(scrollPane, builder.getColumnCount());

	builder.append(txtRowCount, builder.getColumnCount());

	// builder.append("المجموع", txtTotal);

	return builder.getPanel();
    };

    @Override
    protected int getTotalTargetedColumn() {
        return 4;
    }

}
