package com.aizong.ishtirak.bundle;

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
import com.aizong.ishtirak.utils.MessageUtils;

@SuppressWarnings("serial")
public abstract class BundleFilterTable extends CommonFilterTable {

    
    public BundleFilterTable(String title, boolean monthly) {
	super(title, new MyTableListener() {

	    @Override
	    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
		WindowUtils.createDialog(owner,
			ServiceProvider.get().getMessage().getMessage("bundle.report.buttons.add"),
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
			    ServiceProvider.get().getMessage().getMessage("bundle.report.buttons.view"),
			    new BundleForm(Mode.VIEW, subscriber, null, monthly));
		}
	    }

	    @Override
	    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		Bundle subscriber = ServiceProvider.get().getSubscriberService().getBundleById(id);
		if (subscriber != null) {
		    WindowUtils.createDialog(owner,
			    ServiceProvider.get().getMessage().getMessage("bundle.report.buttons.edit",
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
			ServiceProvider.get().getMessage().getMessage("bundle.report.buttons.delete"), "حذف");
		if (yes) {
		    List<Long> ids = new ArrayList<>();
		    ids.add(id);
		    ServiceProvider.get().getSubscriberService().deleteBundles(ids);
		    refreshTableInterface.refreshTable();
		}
	    }
	});
    }

   
}
