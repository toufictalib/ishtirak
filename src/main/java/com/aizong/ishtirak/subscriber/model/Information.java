package com.aizong.ishtirak.subscriber.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.aizong.ishtirak.common.BaseEntity;

@Entity(name = "information")
public class Information extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 3466709784752671004L;

    @Column(name = "id_village")
    private Long VillageId;

    @Column(name = "region")
    private String region;

    @Column(name = "address")
    private String address;

    @Column(name = "main_phone")
    private String mainPhone;

    @Column(name = "alternative_phone")
    private String alternativePhone;

    @Column(name = "land_line")
    private String landLine;

    @Column(name = "email")
    private String email;
    
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Subscriber subscriber;

    public Information() {
	super();
    }

    public Information(Long id) {
	super(id);
    }

    public Long getVillage() {
	return VillageId;
    }

    public void setVillage(Long village) {
	this.VillageId = village;
    }

    public String getRegion() {
	return region;
    }

    public void setRegion(String region) {
	this.region = region;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
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

    public Long getVillageId() {
	return VillageId;
    }

    public void setVillageId(Long villageId) {
	VillageId = villageId;
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

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    
}
