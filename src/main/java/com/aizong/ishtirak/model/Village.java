package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.aizong.ishtirak.common.misc.utils.BaseEntity;

@Entity(name = "village")
public class Village extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 7885874793830935824L;
    private String name;

    @Column(name = "order_index")
    private int orderIndex;

    public Village() {
	super();
    }

    public Village(Long id) {
	this(id, null);
    }

    public Village(Long id, String name) {
	super(id);
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return name;
    }

    public int getOrderIndex() {
	return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
	this.orderIndex = orderIndex;
    }

}
