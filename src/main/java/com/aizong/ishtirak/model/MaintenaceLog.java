package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.aizong.ishtirak.bean.MaintenanceType;
import com.aizong.ishtirak.common.misc.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "maintenance_log")
public class MaintenaceLog extends BaseEntity {

    @Column(name="description")
    private String desc;
    
    @Column(name="amount")
    private double amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name="maintenace_type")
    private MaintenanceType maintenanceType;
    
    @Column(name="engine")
    private Long engineId;
    
    @Lob
    @Column(name="note")
    private String note;
    
    @Column(name="modifierId")
    private Long modifierId;

    public String getDesc() {
	return desc;
    }

    public void setDesc(String desc) {
	this.desc = desc;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public MaintenanceType getMaintenanceType() {
	return maintenanceType;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
	this.maintenanceType = maintenanceType;
    }

    public Long getEngineId() {
	return engineId;
    }

    public void setEngineId(Long engineId) {
	this.engineId = engineId;
    }

    public Long getModifierId() {
	return modifierId;
    }

    public void setModifierId(Long modifierId) {
	this.modifierId = modifierId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    

}
