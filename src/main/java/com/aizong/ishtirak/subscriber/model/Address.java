package com.aizong.ishtirak.subscriber.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {
    
    @Column(name = "id_village")
    private Long VillageId;

    @Column(name = "region")
    private String region;

    @Column(name = "address")
    private String detailedAddress;

    public Long getVillageId() {
	return VillageId;
    }

    public void setVillageId(Long villageId) {
	VillageId = villageId;
    }

    public String getRegion() {
	return region;
    }

    public void setRegion(String region) {
	this.region = region;
    }

    public String getDetailedAddress() {
	return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
	this.detailedAddress = detailedAddress;
    }

}
