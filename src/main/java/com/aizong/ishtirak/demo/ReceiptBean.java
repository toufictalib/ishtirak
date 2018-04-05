package com.aizong.ishtirak.demo;

import java.util.Locale;

import com.aizong.ishtirak.common.misc.utils.CurrencyUtils;

public class ReceiptBean {

    private String name;
    private String title;
    private String village;
    private String address;
    private String date;
    private Long oldCounter;
    private Long newCounter;
    private String subscriptionType;
    private boolean monthlySubscription;

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getVillage() {
	return village;
    }

    public void setVillage(String village) {
	this.village = village;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public Long getOldCounter() {
	return oldCounter;
    }

    public void setOldCounter(Long oldCounter) {
	this.oldCounter = oldCounter;
    }

    public Long getNewCounter() {
	return newCounter;
    }

    public void setNewCounter(Long newCounter) {
	this.newCounter = newCounter;
    }

    public static ReceiptBean create(boolean monthlySubscription) {
	ReceiptBean receiptBean = new ReceiptBean();
	receiptBean.setName("توفيق طالب");
	receiptBean.setAddress("السفيرة, بناية عويضة");
	receiptBean.setDate("كانون الثاني  2018");
	receiptBean.setNewCounter(1555555L);
	receiptBean.setOldCounter(150000L);
	receiptBean.setTitle("اشتراكات الجرد");
	receiptBean.setVillage("السفيرة");
	receiptBean.setSubscriptionType("5 أمبير");
	receiptBean.setMonthlySubscription(monthlySubscription);
	return receiptBean;
    }

    public String getFullName() {
	return "توفيق طالب";
    }

    public String getCounterId() {
	return "ب م 150";
    }

    public String getAmountToPay() {
	return CurrencyUtils.formatCurrency(new Locale("ar", "LB"), 120000d);
    }

    
    public static void main(String[]args) {
	
	Locale locale = new Locale("ar", "LB");
	CurrencyUtils.formatCurrency(locale, 120000d);
    }
    
    
    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public void setMonthlySubscription(boolean monthlySubscription) {
        this.monthlySubscription = monthlySubscription;
    }

    public boolean  isMonthlySubscription() {
	return monthlySubscription;
    }

    public String getMaintenanceNumber() {
	return "76 619869";
    }

}
