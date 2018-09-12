package com.aizong.ishtirak.bean;

public class OrderBean {

    private Long contractId;
    private String contractUniqueCode;
    private String identifier;
    private String name;
    private Integer orderIndex;

    public OrderBean() {
	super();
    }

    public OrderBean(Long contractId, String contractUniqueCode, String name, String identifier, Integer orderIndex) {
	super();
	this.contractId = contractId;
	this.contractUniqueCode = contractUniqueCode;
	this.name = name;
	this.identifier = identifier;
	this.orderIndex = orderIndex;
    }

    public Long getContractId() {
	return contractId;
    }

    public void setContractId(Long contractId) {
	this.contractId = contractId;
    }

    public String getContractUniqueCode() {
	return contractUniqueCode;
    }

    public void setContractUniqueCode(String contractUniqueCode) {
	this.contractUniqueCode = contractUniqueCode;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getOrderIndex() {
	return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
	this.orderIndex = orderIndex;
    }

}
