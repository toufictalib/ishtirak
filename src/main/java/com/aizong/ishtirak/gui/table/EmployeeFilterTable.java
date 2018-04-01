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
import com.aizong.ishtirak.gui.form.EmployeeForm;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.Employee;

@SuppressWarnings("serial")
public class EmployeeFilterTable extends CommonFilterTable {

    
    public EmployeeFilterTable(String title) {
	super(title, new MyTableListener() {

	    @Override
	    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
		WindowUtils.createDialog(owner,
			message("employee.button.add"),
			new EmployeeForm(Mode.NEW, new SavingCallback() {

			    @Override
			    public void onSuccess(Object o) {
				refreshTableInterface.refreshTable();

			    }
			}));

	    }

	    @Override
	    public void view(Window owner, Long id) {
		Employee subscriber = ServiceProvider.get().getSubscriberService().getEmployeeById(id);
		if (subscriber != null) {
		    WindowUtils.createDialog(owner,
			    message("employee.button.view"),
			    new EmployeeForm(Mode.VIEW, subscriber, null));
		}
	    }

	    @Override
	    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		Employee subscriber = ServiceProvider.get().getSubscriberService().getEmployeeById(id);
		if (subscriber != null) {
		    WindowUtils.createDialog(owner,
			    message("employee.button.edit",
				    subscriber.getName()),
			    new EmployeeForm(Mode.UPDATE, subscriber, new SavingCallback() {

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
			message("bundle.report.buttons.delete"), message("delete"));
		if (yes) {
		    List<Long> ids = new ArrayList<>();
		    ids.add(id);
		    ServiceProvider.get().getSubscriberService().deleteEmployees(ids);
		    refreshTableInterface.refreshTable();
		}
	    }
	});
    }

    @Override
    public ReportTableModel getReportTableModel() {
	return ServiceProvider.get().getReportServiceImpl().getEmployees();
    }

    @Override
    protected String getAddTooltip() {
	return message("employee.button.add");
    }

   
}
