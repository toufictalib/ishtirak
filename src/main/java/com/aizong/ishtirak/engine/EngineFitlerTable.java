package com.aizong.ishtirak.engine;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import com.aizong.ishtirak.common.CommonFilterTable;
import com.aizong.ishtirak.common.Mode;
import com.aizong.ishtirak.common.ServiceProvider;
import com.aizong.ishtirak.common.WindowUtils;
import com.aizong.ishtirak.common.l.MyTableListener;
import com.aizong.ishtirak.common.l.RefreshTableInterface;
import com.aizong.ishtirak.subscriber.form.SavingCallback;
import com.aizong.ishtirak.table.ReportTableModel;
import com.aizong.ishtirak.utils.MessageUtils;

@SuppressWarnings("serial")
public class EngineFitlerTable extends CommonFilterTable {

    public EngineFitlerTable(String title) {
	super(title, new MyTableListener() {
	    
	    @Override
	    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
		WindowUtils.createDialog(owner, "مولد جديد", new EngineForm(Mode.NEW, new SavingCallback() {

		    @Override
		    public void onSuccess(Object o) {
			refreshTableInterface.refreshTable();

		    }
		}));

	    }

	    @Override
	    public void view(Window owner, Long id) {
		Engine engine = ServiceProvider.get().getSubscriberService().getEngineById(id);
		if (engine != null) {
		    WindowUtils.createDialog(owner, "عرض المولد", new EngineForm(Mode.VIEW, engine, null));
		}
	    }

	    @Override
	    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		Engine engine = ServiceProvider.get().getSubscriberService().getEngineById(id);
		if (engine != null) {
		    WindowUtils.createDialog(owner, " تعديل المولد " + engine.getName(),
			    new EngineForm(Mode.UPDATE, engine, new SavingCallback() {

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
		    ServiceProvider.get().getSubscriberService().deleteEngines(ids);
		    refreshTableInterface.refreshTable();
		}
	    }
	});
    }

    @Override
    public ReportTableModel getReportTableModel() {
	return ServiceProvider.get().getReportServiceImpl().getEngines();
    }}