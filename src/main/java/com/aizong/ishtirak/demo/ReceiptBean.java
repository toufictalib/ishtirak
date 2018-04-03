package com.aizong.ishtirak.demo;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JacksonInject.Value;

public class ReceiptBean {

    private String title;
    private String village;
    private String address;
    private String date;
    private Long oldCounter;
    private Long newCounter;

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

    public static ReceiptBean create() {
	ReceiptBean receiptBean = new ReceiptBean();
	receiptBean.setAddress("السفيرة, بناية عويضة");
	receiptBean.setDate("2018/01");
	receiptBean.setNewCounter(1800L);
	receiptBean.setOldCounter(1000L);
	receiptBean.setTitle("اشتراكات الجرد");
	receiptBean.setVillage("السفيرة");
	return receiptBean;
    }

    public String getFullName() {
	return "توفيق طالب";
    }

    public Object getCounterId() {
	return "100002";
    }

    public String getAmountToPay() {
	return formatCurrency(new Locale("ar", "LB"), 120000d);
    }

    static public String formatCurrency(Locale currentLocale, Double value) {

	Double currencyAmount = new Double(value);
	NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);

	return currencyFormatter.format(currencyAmount);
    }
    
    public static void main(String[]args) {
	
	Locale locale = new Locale("ar", "LB");
	formatCurrency(locale, 120000d);
    }

}
