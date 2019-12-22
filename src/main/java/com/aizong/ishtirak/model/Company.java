package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.aizong.ishtirak.common.misc.utils.BaseEntity;

@Entity
@Table(name = "company")
public class Company extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "land_line")
    private String landLine;

    @Column(name = "main_mobile_phone")
    private String mainMobilePhone;

    @Column(name = "other_phone")
    private String otherMobilePhone;

    @Column(name = "maitenance_number")
    private String maintenanceNumber;
    
    @Column(name = "note")
    private String note;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getLandLine() {
	return landLine;
    }

    public void setLandLine(String landLine) {
	this.landLine = landLine;
    }

    public String getMainMobilePhone() {
	return mainMobilePhone;
    }

    public void setMainMobilePhone(String mainMobilePhone) {
	this.mainMobilePhone = mainMobilePhone;
    }

    public String getOtherMobilePhone() {
	return otherMobilePhone;
    }

    public void setOtherMobilePhone(String otherMobilePhone) {
	this.otherMobilePhone = otherMobilePhone;
    }

    public String getMaintenanceNumber() {
	return maintenanceNumber;
    }

    public void setMaintenanceNumber(String maintenanceNumber) {
	this.maintenanceNumber = maintenanceNumber;
    }

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

    
}
