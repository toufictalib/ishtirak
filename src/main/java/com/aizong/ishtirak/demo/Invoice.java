package com.aizong.ishtirak.demo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class Invoice {
	private Integer id;
	private BigDecimal shipping;
	private Double tax;
	private Customer billTo;
	private Customer shipTo;
	private List<Item> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getShipping() {
		return shipping;
	}

	public void setShipping(BigDecimal shipping) {
		this.shipping = shipping;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Customer getBillTo() {
		return billTo;
	}

	public void setBillTo(Customer billTo) {
		this.billTo = billTo;
	}

	public Customer getShipTo() {
		return shipTo;
	}

	public void setShipTo(Customer shipTo) {
		this.shipTo = shipTo;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}