package com.aizong.ishtirak.subscriber;

import com.aizong.ishtirak.subscriber.form.Enums.SearchCustomerType;

public class SearchCustomerCriteria {

    private String text;
    private SearchCustomerType customerType;

    public SearchCustomerCriteria(String text) {
	super();
	this.text = text;
    }

    public SearchCustomerCriteria(String text, SearchCustomerType customerType) {
	super();
	this.text = text;
	this.customerType = customerType;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public SearchCustomerType getCustomerType() {
	return customerType;
    }

    public void setCustomerType(SearchCustomerType customerType) {
	this.customerType = customerType;
    }

}
