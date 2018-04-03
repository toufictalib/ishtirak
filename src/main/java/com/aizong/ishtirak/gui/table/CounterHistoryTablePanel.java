package com.aizong.ishtirak.gui.table;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
public class CounterHistoryTablePanel extends ReportTablePanel {

    private Long subscriberId;
    
    public CounterHistoryTablePanel(String title, Long subscriberId) {
	super();
	this.title = title;
	this.subscriberId = subscriberId;
	start();
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
			.getCounterHistory(subscriberId, createDefault.toSearchBean());
		fillTable(reportTableModel);
	    } catch (Exception e1) {
		e1.printStackTrace();
		MessageUtils.showErrorMessage(getOwner(), e1.getMessage());
	    }
	});
	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, "p,p,fill:p:grow,p");
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

	// builder.append("المجموع", txtTotal);

	return builder.getPanel();
    };

    @Override
    public ReportTableModel getReportTableModel() {
	return new ReportTableModel(new String[] {}, new ArrayList<Object[]>(), new Class[] {});
    }

}
