package com.aizong.ishtirak.gui.table;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.misc.MessageUtils;
import com.aizong.ishtirak.common.misc.Mode;
import com.aizong.ishtirak.common.misc.ServiceProvider;
import com.aizong.ishtirak.common.misc.WindowUtils;
import com.aizong.ishtirak.gui.form.EngineForm;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.Engine;

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