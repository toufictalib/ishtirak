package com.aizong.ishtirak.gui.table;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.form.BundleForm;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.Bundle;

@SuppressWarnings("serial")
public abstract class BundleFilterTable extends CommonFilterTable {

    
    public BundleFilterTable(String title, boolean monthly) {
	super(title, new MyTableListener() {

	    @Override
	    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
		WindowUtils.createDialog(owner,
			message("bundle.form.new"),
			new BundleForm(Mode.NEW, new SavingCallback() {

			    @Override
			    public void onSuccess(Object o) {
				refreshTableInterface.refreshTable();

			    }
			}, monthly));

	    }

	    @Override
	    public void view(Window owner, Long id) {
		Bundle subscriber = ServiceProvider.get().getSubscriberService().getBundleById(id);
		if (subscriber != null) {
		    WindowUtils.createDialog(owner,
			    message("bundle.report.buttons.view"),
			    new BundleForm(Mode.VIEW, subscriber, null, monthly));
		}
	    }

	    @Override
	    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		Bundle subscriber = ServiceProvider.get().getSubscriberService().getBundleById(id);
		if (subscriber != null) {
		    WindowUtils.createDialog(owner,
			    message("bundle.report.buttons.edit",
				    subscriber.getName()),
			    new BundleForm(Mode.UPDATE, subscriber, new SavingCallback() {

				@Override
				public void onSuccess(Object o) {
				    refreshTableInterface.refreshTable();

				}
			    }, monthly));
		}
	    }

	    @Override
	    public void delete(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		boolean yes = MessageUtils.showConfirmationMessage(owner,
			message("bundle.report.buttons.delete"), message("delete"));
		if (yes) {
		    List<Long> ids = new ArrayList<>();
		    ids.add(id);
		    ServiceProvider.get().getSubscriberService().deleteBundles(ids);
		    refreshTableInterface.refreshTable();
		}
	    }
	});
    }

   @Override
protected String getAddTooltip() {
    return message("bundle.form.add");
}
}
