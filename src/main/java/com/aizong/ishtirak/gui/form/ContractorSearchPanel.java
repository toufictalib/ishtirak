package com.aizong.ishtirak.gui.form;

import java.util.List;

import javax.swing.JDialog;

import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.bean.Enums.SearchCustomerType;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.model.Subscriber;

public class ContractorSearchPanel extends CustomerSearchPanel {

    private static final long serialVersionUID = 1L;

    @Override
    public void onCreate(Subscriber subscriber) {
	JDialog createDialog = null;
	try {
	    createDialog = WindowUtils.createDialog(ContractorSearchPanel.this.getOwner(), "إدخال عداد", new CounterHistoryForm(subscriber.getId(), Mode.NEW));
	} catch (Exception e) {
	    if(createDialog!=null) {
		MessageUtils.showInfoMessage(getOwner(), message("counterHistory.noContract"));
		createDialog.dispose();
	    }
	}
	
    }

    @Override
    public List<Subscriber> onSearch(String text, SearchCustomerType customerType) {
	SearchCustomerCriteria criteria = new SearchCustomerCriteria(text);
	criteria.setCustomerType(customerType);

	List<Subscriber> searchSubscribers = ServiceProvider.get().getSubscriberService().searchSubscribers(criteria);
	return searchSubscribers;
    }
}
