package com.aizong.ishtirak.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee extends Person {

    /**
     * 
     */
    private static final long serialVersionUID = -6792056122438657050L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_type_id")
    private EmployeeType employeeType;

    @Column(name = "salary")
    private long salary;

    @Column(name = "is_active")
    private boolean active;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "employee")
    private EmployeeInformation information;
    
    public EmployeeType getEmployeeTypeId() {
	return employeeType;
    }

    public void setEmployeeTypeId(EmployeeType employeeType) {
	this.employeeType = employeeType;
    }

    public long getSalary() {
	return salary;
    }

    public void setSalary(long salary) {
	this.salary = salary;
    }

    public boolean isActive() {
	return active;
    }

    public void setActive(boolean active) {
	this.active = active;
    }

    public EmployeeInformation getInformation() {
        return information;
    }

    public void setInformation(EmployeeInformation information) {
        this.information = information;
    }
    
    

}
