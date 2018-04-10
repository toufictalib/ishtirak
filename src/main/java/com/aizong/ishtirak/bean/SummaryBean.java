package com.aizong.ishtirak.bean;

import java.util.List;

import com.aizong.ishtirak.model.Engine;

public class SummaryBean {

    private final List<Engine> engines;
    private final List<Object[]> expenses;
    private final List<Object[]> consumptions;
    private final List<Object[]> income;

    public SummaryBean(List<Engine> engines, List<Object[]> expenses, List<Object[]> consumptions, List<Object[]> income) {
	super();
	this.engines = engines;
	this.expenses = expenses;
	this.consumptions = consumptions;
	this.income = income;
    }

    
    public List<Engine> getEngines() {
        return engines;
    }


    public List<Object[]> getExpenses() {
	return expenses;
    }

    public List<Object[]> getConsumptions() {
	return consumptions;
    }

    public List<Object[]> getIncome() {
	return income;
    }

}
