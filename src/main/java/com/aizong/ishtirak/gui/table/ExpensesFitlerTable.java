package com.aizong.ishtirak.gui.table;

import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ProgressBar;
import com.aizong.ishtirak.common.misc.utils.ProgressBar.ProgressBarListener;
import com.aizong.ishtirak.common.misc.utils.SearchPanel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.form.ExpensesForm;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.ExpensesLog;
import com.jgoodies.forms.builder.DefaultFormBuilder;

@SuppressWarnings("serial")
public class ExpensesFitlerTable extends CommonFilterTable {

    private JButton btnSearch;
    
    public ExpensesFitlerTable(String title) {
	super(title, new MyTableListener() {
	    
	    @Override
	    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
		WindowUtils.createDialog(owner, message("expenses.form.new"),
			new ExpensesForm(Mode.NEW, null, new SavingCallback() {

			    @Override
			    public void onSuccess(Object o) {
				refreshTableInterface.refreshTable();

			    }
			}));

	    }

	    @Override
	    public void view(Window owner, Long id) {
		ExpensesLog expenseLog = ServiceProvider.get().getSubscriberService().getExpensesById(id);
		if (expenseLog != null) {
		    WindowUtils.createDialog(owner, message("expenses.form.view"),
			    new ExpensesForm(Mode.VIEW, expenseLog, null));
		}
	    }

	    @Override
	    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		ExpensesLog expenseLog = ServiceProvider.get().getSubscriberService().getExpensesById(id);
		if (expenseLog != null) {
		    WindowUtils.createDialog(owner, message("expenses.form.edit"),
			    new ExpensesForm(Mode.UPDATE, expenseLog, new SavingCallback() {

				@Override
				public void onSuccess(Object o) {
				    refreshTableInterface.refreshTable();

				}
			    }));
		}
	    }

	    @Override
	    public void delete(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		boolean yes = MessageUtils.showConfirmationMessage(owner, message("bundle.report.buttons.delete"),
			message("delete"));
		if (yes) {
		    List<Long> ids = new ArrayList<>();
		    ids.add(id);
		    ServiceProvider.get().getSubscriberService().deleteExpenses(ids);
		    refreshTableInterface.refreshTable();
		}
	    }
	});
    }

    @Override
    protected int getTotalTargetedColumn() {
        return 2;
    }
    @Override
    protected JPanel initUI() {

	String leftToRightSpecs = "fill:p:grow,5dlu,p";

	String row = "p,p,p,fill:p:grow,p,p,p,p";
	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, row);
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);

	btnSearch = ButtonFactory.createBtnSearch();

	DateRange startEndDateOfCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	SearchPanel createDefault = SearchPanel.createDefault(startEndDateOfCurrentMonth.getStartDate(),
		startEndDateOfCurrentMonth.getEndDate());

	btnSearch.addActionListener(e -> {
	    ProgressBar.execute(new ProgressBarListener<ReportTableModel>() {

		@Override
		public ReportTableModel onBackground() throws Exception {
		    
		    return ServiceProvider.get().getReportServiceImpl().getExpenses(createDefault.toSearchBean().getFromDate(),
			    createDefault.toSearchBean().getEndDate());
		}

		@Override
		public void onDone(ReportTableModel reportTableModel) {
		    reloadTable(reportTableModel);
		}
	    }, ExpensesFitlerTable.this);
	});

	JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	searchPanel.add(createDefault);
	searchPanel.add(btnSearch);

	builder.append(searchPanel, builder.getColumnCount());

	builder.append(txtFE, btnAdd);

	JScrollPane scrollPane = new JScrollPane(table);
	scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	builder.append(scrollPane, 3);

	builder.append(txtRowCount, builder.getColumnCount());

	builder.append(txtTotal, 3);

	builder.appendSeparator();

	JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel.add(btnExportExcel);
	panel.add(btnPrint);
	builder.append(panel, builder.getColumnCount());

	return builder.getPanel();
    }

    @Override
    public ReportTableModel getReportTableModel() {
	return null;
    }

    @Override
    protected String getAddTooltip() {
	return message("expenses.form.add");
    }
    
    @Override
    public void refreshTable() {
	btnSearch.doClick();
    }
}