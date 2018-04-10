package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.common.misc.utils.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "expenses_log")
public class ExpensesLog extends BaseEntity {

    @Column(name = "description")
    private String desc;

    @Column(name = "amount")
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "maintenace_type")
    private ExpensesType maintenanceType;

    @Column(name = "engine")
    private Long engineId;

    @Column(name = "employee")
    private Long employeeId;
    
    @Column(name="diesel_consumption")
    private Integer dieselConsupmtion;   
    
    @Column(name="oil_consumption")
    private Integer oilConsumption;   

    @Lob
    @Column(name = "note")
    private String note;

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

    public ExpensesType getMaintenanceType() {
	return maintenanceType;
    }

    public void setMaintenanceType(ExpensesType maintenanceType) {
	this.maintenanceType = maintenanceType;
    }

    public Long getEngineId() {
	return engineId;
    }

    public void setEngineId(Long engineId) {
	this.engineId = engineId;
    }

    public Long getEmployeeId() {
	return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
	this.employeeId = employeeId;
    }

    public String getNote() {
	return note;
    }

    public void setNote(String note) {
	this.note = note;
    }

    public Integer getDieselConsupmtion() {
        return dieselConsupmtion;
    }

    public void setDieselConsupmtion(Integer dieselConsupmtion) {
        this.dieselConsupmtion = dieselConsupmtion;
    }

    public Integer getOilConsumption() {
        return oilConsumption;
    }

    public void setOilConsumption(Integer oilConsumption) {
        this.oilConsumption = oilConsumption;
    }
    
    

    
}
