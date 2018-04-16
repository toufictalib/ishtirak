package com.aizong.ishtirak.gui.table;

import java.awt.Window;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.form.ExpensesForm;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.ExpensesLog;

@SuppressWarnings("serial")
public class ExpensesFitlerTable extends CommonFilterTable {

    public ExpensesFitlerTable(String title) {
	super(title, new MyTableListener() {
	    
	    @Override
	    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
		WindowUtils.createDialog(owner, message("expenses.form.new"), new ExpensesForm(Mode.NEW,null, new SavingCallback() {

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
		    WindowUtils.createDialog(owner, message("expenses.form.view"), new ExpensesForm(Mode.VIEW, expenseLog, null));
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
		boolean yes = MessageUtils.showConfirmationMessage(owner, message("bundle.report.buttons.delete"), message("delete"));
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
    public ReportTableModel getReportTableModel() {
	DateRange dateRange = getRange();
	return ServiceProvider.get().getReportServiceImpl().getExpenses(dateRange.getStartDateAsString(), dateRange.getEndDateAsString());
    }
    
    public DateRange getRange() {
	LocalDate initial = LocalDate.now();
	LocalDate start = initial.withDayOfMonth(6);
	LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth()).plusDays(5);

	return new DateRange(DateUtil.fromLocalDate(start), DateUtil.fromLocalDate(end));
    }
    

    @Override
    protected String getAddTooltip() {
	return message("expenses.form.add");
    }}