package com.aizong.ishtirak.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Information implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3466709784752671004L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    protected Long id;

    @Embedded
    private Address address = new Address();

    @Column(name = "main_phone")
    private String mainPhone;

    @Column(name = "alternative_phone")
    private String alternativePhone;

    @Column(name = "land_line")
    private String landLine;

    @Column(name = "email")
    private String email;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Information() {
	super();
    }

    public Information(Long id) {
	this.id = id;
    }

    public String getMainPhone() {
	return mainPhone;
    }

    public void setMainPhone(String mainPhone) {
	this.mainPhone = mainPhone;
    }

    public String getAlternativePhone() {
	return alternativePhone;
    }

    public void setAlternativePhone(String alternativePhone) {
	this.alternativePhone = alternativePhone;
    }

    public String getLandLine() {
	return landLine;
    }

    public void setLandLine(String landLine) {
	this.landLine = landLine;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Long getVillageId() {
	return address.getVillageId();
    }

    public void setVillageId(Long villageId) {
	address.setVillageId(villageId);
    }

    public String getRegion() {
	return address.getRegion();
    }

    public void setRegion(String region) {
	address.setRegion(region);
    }

    public String getDetailedAddress() {
	return address.getDetailedAddress();
    }

    public void setDetailedAddress(String detailedAddress) {
	address.setDetailedAddress(detailedAddress);
    }

    public Address getAddress() {
	return address;
    }

    public void setAddress(Address address) {
	this.address = address;
    }

}
