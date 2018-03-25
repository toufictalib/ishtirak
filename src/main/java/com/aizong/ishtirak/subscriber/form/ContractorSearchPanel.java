package com.aizong.ishtirak.subscriber.form;

import java.util.List;

import com.aizong.ishtirak.common.ServiceProvider;
import com.aizong.ishtirak.common.WindowUtils;
import com.aizong.ishtirak.subscriber.SearchCustomerCriteria;
import com.aizong.ishtirak.subscriber.form.Enums.SearchCustomerType;
import com.aizong.ishtirak.subscriber.model.Subscriber;

public class ContractorSearchPanel extends CustomerSearchPanel {

    private static final long serialVersionUID = 1L;

    @Override
    public void onCreate(Subscriber subscriber) {
	WindowUtils.createDialog(ContractorSearchPanel.this.getOwner(), "إدخال عداد", new CounterHistoryForm(subscriber));
	
    }

    @Override
    public List<Subscriber> onSearch(String text, SearchCustomerType customerType) {
	SearchCustomerCriteria criteria = new SearchCustomerCriteria(text);
	criteria.setCustomerType(customerType);

	List<Subscriber> searchSubscribers = ServiceProvider.get().getSubscriberService().searchSubscribers(criteria);
	return searchSubscribers;
    }
}
