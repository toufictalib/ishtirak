package com.aizong.ishtirak.bean;

import java.util.Optional;

public class ContractConsumptionBean {

    private Long contractId;
    private Long previousCounterValue;
    private Long currentCounterValue;

    public ContractConsumptionBean() {
	super();
    }

    public ContractConsumptionBean(Long contractId, Long previousCounterValue, Long currentCounterValue) {
	super();
	this.contractId = contractId;
	this.previousCounterValue = previousCounterValue;
	this.currentCounterValue = currentCounterValue;
    }

    public Long getContractId() {
	return contractId;
    }

    public void setContractId(Long contractId) {
	this.contractId = contractId;
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
