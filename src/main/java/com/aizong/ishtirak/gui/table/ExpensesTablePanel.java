package com.aizong.ishtirak.gui.table;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.SearchPanel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.jgoodies.forms.builder.DefaultFormBuilder;

@SuppressWarnings("serial")
public class ExpensesTablePanel extends ReportTablePanel {

    private ExpensesType expensesType;
    
    public ExpensesTablePanel(String title, ExpensesType expensesType) {
	super();
	this.title = title;
	this.expensesType = expensesType;
	start();
    }

    @Override
    protected int getTotalTargetedColumn() {
        return 2;
    
    }
    
    @Override
    protected JPanel initUI() {

	JButton btnSearch = ButtonFactory.createBtnSearch();

	DateRange startEndDateOfCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	SearchPanel createDefault = SearchPanel.createDefault(startEndDateOfCurrentMonth.getStartDate(),
		startEndDateOfCurrentMonth.getEndDate());

	String leftToRightSpecs = "fill:p:grow";

	btnSearch.addActionListener(e -> {
	    try {
		ReportTableModel reportTableModel = ServiceProvider.get().getReportServiceImpl()
			.getExpenses(expensesType, createDefault.toSearchBean());
		fillTable(reportTableModel);
	    } catch (Exception e1) {
		e1.printStackTrace();
		MessageUtils.showErrorMessage(getOwner(), e1.getMessage());
	    }
	});
	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, "p,p,fill:p:grow,p,p,p");
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);
	JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel.add(createDefault);
	panel.add(btnSearch);

	builder.append(panel, builder.getColumnCount());
	// builder.append(txtFE, 3);

	JScrollPane scrollPane = new JScrollPane(table);
	// scrollPane.setPreferredSize(ComponentUtils.getDimension(60, 60));
	builder.append(scrollPane, builder.getColumnCount());

	builder.append(txtRowCount, builder.getColumnCount());

	 builder.append(txtTotal,builder.getColumnCount());

	return builder.getPanel();
    };

    @Override
    public ReportTableModel getReportTableModel() {
	return null;
    }

}
