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
import com.aizong.ishtirak.gui.form.EmployeeTypeForm;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.EmployeeType;

@SuppressWarnings("serial")
public class EmployeeTypeFilterTable extends CommonFilterTable {

    
    public EmployeeTypeFilterTable(String title) {
	super(title, new MyTableListener() {

	    @Override
	    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
		WindowUtils.createDialog(owner,
			ServiceProvider.get().getMessage().getMessage("bundle.report.buttons.add"),
			new EmployeeTypeForm(Mode.NEW, new SavingCallback() {

			    @Override
			    public void onSuccess(Object o) {
				refreshTableInterface.refreshTable();

			    }
			}));

	    }

	    @Override
	    public void view(Window owner, Long id) {
		EmployeeType subscriber = ServiceProvider.get().getSubscriberService().getEmployeeTypeById(id);
		if (subscriber != null) {
		    WindowUtils.createDialog(owner,
			    ServiceProvider.get().getMessage().getMessage("bundle.report.buttons.view"),
			    new EmployeeTypeForm(Mode.VIEW, subscriber, null));
		}
	    }

	    @Override
	    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		EmployeeType subscriber = ServiceProvider.get().getSubscriberService().getEmployeeTypeById(id);
		if (subscriber != null) {
		    WindowUtils.createDialog(owner,
			    ServiceProvider.get().getMessage().getMessage("bundle.report.buttons.edit",
				    subscriber.getName()),
			    new EmployeeTypeForm(Mode.UPDATE, subscriber, new SavingCallback() {

				@Override
				public void onSuccess(Object o) {
				    refreshTableInterface.refreshTable();

				}
			    }));
		}
	    }

	    @Override
	    public void delete(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		boolean yes = MessageUtils.showConfirmationMessage(owner,
			ServiceProvider.get().getMessage().getMessage("bundle.report.buttons.delete"), "حذف");
		if (yes) {
		    List<Long> ids = new ArrayList<>();
		    ids.add(id);
		    ServiceProvider.get().getSubscriberService().deleteEmployeeTypes(ids);
		    refreshTableInterface.refreshTable();
		}
	    }
	});
    }

    @Override
    public ReportTableModel getReportTableModel() {
	return ServiceProvider.get().getReportServiceImpl().getEmployeeTypes();
    }

   
}
