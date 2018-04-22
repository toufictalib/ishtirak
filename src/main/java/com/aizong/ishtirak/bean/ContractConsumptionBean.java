package com.aizong.ishtirak.bean;

import java.util.Optional;

public class ContractConsumptionBean {

    private String contractUniqueCode;
    private Long previousCounterValue;
    private Long currentCounterValue;

    public ContractConsumptionBean() {
	super();
    }

    public ContractConsumptionBean(String contractUniqueCode, Long previousCounterValue, Long currentCounterValue) {
	super();
	this.contractUniqueCode = contractUniqueCode;
	this.previousCounterValue = previousCounterValue;
	this.currentCounterValue = currentCounterValue;
    }

    public String getContractUniqueCode() {
	return contractUniqueCode;
    }

    public void setContractUniqueCode(String contractUniqueCode) {
	this.contractUniqueCode = contractUniqueCode;
    }

    public Long getPreviousCounterValue() {
	return previousCounterValue;
    }

    public void setPreviousCounterValue(Long previousCounterValue) {
	this.previousCounterValue = previousCounterValue;
    }

    public Long getCurrentCounterValue() {
	return currentCounterValue;
    }

    public void setCurrentCounterValue(Long currentCounterValue) {
	this.currentCounterValue = currentCounterValue;
    }

    public boolean isValid() {
	return currentCounterValue != null && previousCounterValue != null;
    }

    public Optional<Long> getConsumption() {
	if (isValid()) {
	    return Optional.of(Math.abs(currentCounterValue - previousCounterValue));
	}
	return Optional.empty();
    }
}
