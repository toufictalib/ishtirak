package com.aizong.ishtirak.demo;

import com.aizong.ishtirak.bean.CurrencyManager.SupportedCurrency;

public class ReceiptBean {

    private String name;
    private String village;
    private String address;
    private String date;
    private Long oldCounter;
    private Long newCounter;
    private String subscriptionType;
    private boolean monthlySubscription;
    private String counterCode;
    private Double amountTopay;
    private  Double subscriptionFees;
	private  SupportedCurrency selectedCurrency;
    
    public ReceiptBean() {
	
    }
    public ReceiptBean(String name, String village, String address, String date, Long oldCounter,
		       Long newCounter, String subscriptionType, boolean monthlySubscription, String counterCode, Double amountTopay, Double subscriptionFees,
		       SupportedCurrency selectedCurrency) {
	super();
	this.name = name;
	this.village = village;
	this.address = address;
	this.date = date;
	this.oldCounter = oldCounter;
	this.newCounter = newCounter;
	this.subscriptionType = subscriptionType;
	this.monthlySubscription = monthlySubscription;
	this.counterCode = counterCode;
	this.amountTopay = amountTopay;
	this.subscriptionFees = subscriptionFees;
	this.selectedCurrency = selectedCurrency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVillage() {
	return village;
    }

    public void setVillage(String village) {
	this.village = village;
    }

    public String getAddress() {
	return getVillage();
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
	receiptBean.setNewCounter(150000L);
	receiptBean.setOldCounter(100000L);
	receiptBean.setVillage("السفيرة");
	receiptBean.setSubscriptionType("5 أمبير");
	receiptBean.setMonthlySubscription(monthlySubscription);
	receiptBean.setCounterCode("ب م 100");
	receiptBean.setAmountTopay(16D);
	receiptBean.setSubscriptionFees(23000D);
	receiptBean.setSelectedCurrency(SupportedCurrency.DOLLAR);
	return receiptBean;
    }

    public String getFullName() {
	return "توفيق طالب";
    }
  
    public Double getAmountTopay() {
		return amountTopay;
	}
	public Double getSubscriptionFees() {
		return subscriptionFees;
	}
	public SupportedCurrency getSelectedCurrency() {
		return selectedCurrency;
	}
	public void setAmountTopay(Double amountTopay) {
        this.amountTopay = amountTopay;
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
    
    public void setSubscriptionFees(Double subscriptionFees) {
		this.subscriptionFees = subscriptionFees;
	}
	public void setSelectedCurrency(SupportedCurrency selectedCurrency) {
		this.selectedCurrency = selectedCurrency;
	}
	public boolean  isMonthlySubscription() {
	return monthlySubscription;
    }

    public String getCounterCode() {
        return counterCode;
    }
    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    
}
