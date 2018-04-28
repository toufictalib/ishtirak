package com.aizong.ishtirak.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.aizong.ishtirak.common.misc.utils.BaseEntity;

@Entity
@Table(name = "contract")
public class Contract extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -6787533896757992742L;

    @Column(name = "contract_unique_code")
    private String contractUniqueCode;

    @Column(name = "subscriber_id")
    private long subscriberId;

    @Column(name = "bundle_id")
    private long bundleId;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "engine_id")
    private long engineId;
    
    @Column(name = "closed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closeDate;

    @Embedded
    private Address address;

    public String getContractUniqueCode() {
	return contractUniqueCode;
    }

    public void setContractUniqueCode(String contractUniqueCode) {
	this.contractUniqueCode = contractUniqueCode;
    }

    public long getSubscriberId() {
	return subscriberId;
    }

    public void setSubscriberId(long subscriberId) {
	this.subscriberId = subscriberId;
    }

    public long getBundleId() {
	return bundleId;
    }

    public void setBundleId(long bundleId) {
	this.bundleId = bundleId;
    }

    public boolean isActive() {
	return active;
    }

    public void setActive(boolean active) {
	this.active = active;
    }

    public long getEngineId() {
	return engineId;
    }

    public void setEngineId(long engineId) {
	this.engineId = engineId;
    }

    public Address getAddress() {
	return address;
    }

    public void setAddress(Address address) {
	this.address = address;
    }

    @Override
    public String toString() {
	return contractUniqueCode;
    }

    public Date getCloseDate() {
	return closeDate;
    }

    public void setCloseDate(Date closeDate) {
	this.closeDate = closeDate;
    }

}
