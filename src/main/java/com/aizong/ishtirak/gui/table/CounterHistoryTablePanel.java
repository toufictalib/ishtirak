package com.aizong.ishtirak.gui.table;

import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.MySwingWorker;
import com.aizong.ishtirak.common.misc.utils.ProgressAction;
import com.aizong.ishtirak.common.misc.utils.SearchMonthPanel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.jgoodies.forms.builder.DefaultFormBuilder;

@SuppressWarnings("serial")
public class CounterHistoryTablePanel extends ReportTablePanel {

    private Long subscriberId;
    private SearchMonthPanel searchMonthPanel;

    private DateRange fromDateRange;
    private DateRange toDateRange;

    public CounterHistoryTablePanel(String title, Long subscriberId) {
	super(title, null);
	this.subscriberId = subscriberId;
	start();
    }

    @Override
    protected JPanel initUI() {

	JButton btnSearch = ButtonFactory.createBtnSearch();

	DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth();
	searchMonthPanel = SearchMonthPanel.createDefault(dateRange.getStartDate(), dateRange.getStartDate());

	String leftToRightSpecs = "fill:p:grow";

	btnSearch.addActionListener(e -> {
	    try {

		LocalDate fromLocaleDate = LocalDate.of(searchMonthPanel.getSelectedFromYear(),
			searchMonthPanel.getSelectedFromMonth(), DateUtil.START_MONTH);
		fromDateRange = DateUtil.getStartEndDateOfCurrentMonth(fromLocaleDate);

		LocalDate toLocaleDate = LocalDate.of(searchMonthPanel.getSelectedToYear(),
			searchMonthPanel.getSelectedToMonth(), DateUtil.START_MONTH);
		toDateRange = DateUtil.getStartEndDateOfCurrentMonth(toLocaleDate);

		ReportTableModel reportTableModel = ServiceProvider.get().getReportServiceImpl().getCounterHistory(
			subscriberId, new DateRange(fromDateRange.getStartDate(), toDateRange.getEndDate()));
		fillTable(reportTableModel);
	    } catch (Exception e1) {
		e1.printStackTrace();
		MessageUtils.showErrorMessage(getOwner(), e1.getMessage());
	    }
	});

	JButton btnDelete = ButtonFactory.createBtnDelete();
	btnDelete.addActionListener(e -> {
	    Optional<Long> selectedRowId = getSelectedRowId();
	    if (selectedRowId.isPresent()) {
	    boolean yes = MessageUtils.showConfirmationMessage(CounterHistoryTablePanel.this.getOwner(),
		    message("deleteRow.confirmation"), message("delete"));
	    if (yes) {
		
		
		    MySwingWorker.execute(new ProgressAction<Void>() {

			@Override
			public Void action() {
			    ServiceProvider.get().getSubscriberService()
				    .deleteCounterHistory(Arrays.asList(selectedRowId.get()));
			    return null;
			}

			@Override
			public void success(Void t) {
			    MessageUtils.showInfoMessage(CounterHistoryTablePanel.this, message("delete.success"));
			    btnSearch.doClick();
			}

			@Override
			public void failure(Exception e) {
			    MessageUtils.showErrorMessage(CounterHistoryTablePanel.this, message("delete.failure"));

			}
		    });
		}
	    }
	});

	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, "p,p,p,p,fill:p:grow,p");
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);
	JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel.add(searchMonthPanel);
	panel.add(btnSearch);
	builder.append(panel, builder.getColumnCount());
	
	builder.appendSeparator();
	JPanel secondPanel  = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	secondPanel.add(btnDelete);
	builder.append(secondPanel, builder.getColumnCount());
	// builder.append(txtFE, 3);

	JScrollPane scrollPane = new JScrollPane(table);
	// scrollPane.setPreferredSize(ComponentUtils.getDimension(60, 60));
	builder.append(scrollPane, builder.getColumnCount());

	builder.append(txtRowCount, builder.getColumnCount());

	// builder.append("المجموع", txtTotal);

	return builder.getPanel();
    };

    protected Optional<Long> getSelectedRowId() {
	return getSelectedRowId(true);
    }

    protected Optional<Long> getSelectedRowId(boolean warn) {

	if (table.getSelectedRows().length > 1) {
	    MessageUtils.showWarningMessage(CounterHistoryTablePanel.this, message("choose.single"));
	    return Optional.empty();
	}
	int selectedRow = table.getSelectedRow();
	if (selectedRow >= 0) {
	    Object valueAt = table.getModel().getValueAt(table.convertRowIndexToModel(selectedRow), 0);
	    if (valueAt instanceof Long) {
		return Optional.of((Long) valueAt);
	    }
	} else {
	    if (warn) {
		MessageUtils.showInfoMessage(getOwner(), message("table.row.select.missing"));
	    }
	}
	return Optional.empty();
    }

}
