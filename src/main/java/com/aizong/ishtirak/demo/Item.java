package com.aizong.ishtirak.demo;

import java.math.BigDecimal;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class Item {
	private String description;
	private Integer quantity;
	private BigDecimal unitprice;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(BigDecimal unitprice) {
		this.unitprice = unitprice;
	}
}