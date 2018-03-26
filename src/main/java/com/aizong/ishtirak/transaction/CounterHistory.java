package com.aizong.ishtirak.transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.aizong.ishtirak.common.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "counter_history")
public class CounterHistory extends BaseEntity {

    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "consumption")
    private Long consumption;

    public CounterHistory() {
	super();
    }

    public CounterHistory(Long id) {
	super(id);
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getConsumption() {
        return consumption;
    }

    public void setConsumption(Long consumption) {
        this.consumption = consumption;
    }
    
    

}
