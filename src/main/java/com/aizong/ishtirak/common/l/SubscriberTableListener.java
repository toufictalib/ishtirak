package com.aizong.ishtirak.common.l;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import com.aizong.ishtirak.common.MessageUtils;
import com.aizong.ishtirak.common.Mode;
import com.aizong.ishtirak.common.ServiceProvider;
import com.aizong.ishtirak.common.WindowUtils;
import com.aizong.ishtirak.subscriber.form.SavingCallback;
import com.aizong.ishtirak.subscriber.form.SubscriberForm;
import com.aizong.ishtirak.subscriber.model.Subscriber;

public class SubscriberTableListener implements MyTableListener{

    @Override
    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
	WindowUtils.createDialog(owner, "مشترك جديد", new SubscriberForm(Mode.NEW, new SavingCallback() {

	    @Override
	    public void onSuccess(Object o) {
		refreshTableInterface.refreshTable();

	    }
	}));

    }

    @Override
    public void view(Window owner, Long id) {
	Subscriber subscriber = ServiceProvider.get().getSubscriberService().getSubscriberById(id);
	if (subscriber != null) {
	    WindowUtils.createDialog(owner, "عرض المشترك", new SubscriberForm(Mode.VIEW, subscriber, null));
	}
    }

    @Override
    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
	Subscriber subscriber = ServiceProvider.get().getSubscriberService().getSubscriberById(id);
	if (subscriber != null) {
	    WindowUtils.createDialog(owner, " تعديل المشترك " + subscriber.getName(),
		    new SubscriberForm(Mode.UPDATE, subscriber, new SavingCallback() {

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
	    ServiceProvider.get().getSubscriberService().deleteSubscribers(ids);
	    refreshTableInterface.refreshTable();
	}
    }

}
