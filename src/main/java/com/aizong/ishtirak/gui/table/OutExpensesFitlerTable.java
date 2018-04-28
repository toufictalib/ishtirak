package com.aizong.ishtirak.gui.table;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.form.OutExpensesForm;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.OutExpensesLog;

@SuppressWarnings("serial")
public class OutExpensesFitlerTable extends CommonFilterTable {

    public OutExpensesFitlerTable(String title) {
	super(title, new MyTableListener() {

	    @Override
	    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
		WindowUtils.createDialog(owner, message("expenses.form.new"),
			new OutExpensesForm(Mode.NEW, null, new SavingCallback() {

			    @Override
			    public void onSuccess(Object o) {
				refreshTableInterface.refreshTable();

			    }
			}));

	    }

	    @Override
	    public void view(Window owner, Long id) {
		OutExpensesLog expenseLog = ServiceProvider.get().getSubscriberService().getOutExpensesById(id);
		if (expenseLog != null) {
		    WindowUtils.createDialog(owner, message("expenses.form.view"),
			    new OutExpensesForm(Mode.VIEW, expenseLog, null));
		}
	    }

	    @Override
	    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		OutExpensesLog expenseLog = ServiceProvider.get().getSubscriberService().getOutExpensesById(id);
		if (expenseLog != null) {
		    WindowUtils.createDialog(owner, message("expenses.form.edit"),
			    new OutExpensesForm(Mode.UPDATE, expenseLog, new SavingCallback() {

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
		    ServiceProvider.get().getSubscriberService().deleteOutExpenses(ids);
		    refreshTableInterface.refreshTable();
		}
	    }
	});
    }

    @Override
    public ReportTableModel getReportTableModel() {
	return ServiceProvider.get().getReportServiceImpl().getOutExpenses();
    }

    @Override
    protected String getAddTooltip() {
	return message("expenses.form.add");
    }
    
    @Override
    protected int getTotalTargetedColumn() {
        return 3;
    }
    
}