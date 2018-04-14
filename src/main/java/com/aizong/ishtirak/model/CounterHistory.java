package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.aizong.ishtirak.common.misc.utils.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "counter_history")
public class CounterHistory extends BaseEntity {

    @Column(name = "contract_unique_code")
    private String contractUniqueCode;

    @Column(name = "consumption")
    private Long consumption;
    
    public CounterHistory() {
	super();
    }

    public CounterHistory(Long id) {
	super(id);
    }

    public String getContractUniqueCode() {
        return contractUniqueCode;
    }

    public void setContractUniqueCode(String contractUniqueCode) {
        this.contractUniqueCode = contractUniqueCode;
    }

    public Long getConsumption() {
        return consumption;
    }

    public void setConsumption(Long consumption) {
        this.consumption = consumption;
    }
    
    

}
