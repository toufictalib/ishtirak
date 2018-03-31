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
import com.aizong.ishtirak.gui.form.MaintenaceForm;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.MaintenaceLog;

@SuppressWarnings("serial")
public class ExpensesFitlerTable extends CommonFilterTable {

    public ExpensesFitlerTable(String title) {
	super(title, new MyTableListener() {
	    
	    @Override
	    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
		WindowUtils.createDialog(owner, "إضافة مصروف", new MaintenaceForm(Mode.NEW,null, new SavingCallback() {

		    @Override
		    public void onSuccess(Object o) {
			refreshTableInterface.refreshTable();

		    }
		}));

	    }

	    @Override
	    public void view(Window owner, Long id) {
		MaintenaceLog expenseLog = ServiceProvider.get().getSubscriberService().getExpensesById(id);
		if (expenseLog != null) {
		    WindowUtils.createDialog(owner, "عرض المصروف", new MaintenaceForm(Mode.VIEW, expenseLog, null));
		}
	    }

	    @Override
	    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		MaintenaceLog expenseLog = ServiceProvider.get().getSubscriberService().getExpensesById(id);
		if (expenseLog != null) {
		    WindowUtils.createDialog(owner, " تعديل المصروف " + expenseLog.getId(),
			    new MaintenaceForm(Mode.UPDATE, expenseLog, new SavingCallback() {

				@Override
				public void onSuccess(Object o) {
				    refreshTableInterface.refreshTable();

				}
			    }));
		}
	    }

	    @Override
	    public void delete(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		boolean yes = MessageUtils.showConfirmationMessage(owner, "هل تريد حذف هذا القيد؟", "حذف");
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
	return ServiceProvider.get().getReportServiceImpl().getTransactions();
    }}