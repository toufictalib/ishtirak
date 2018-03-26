package com.aizong.ishtirak.common.table;

import java.util.List;

public class ReportTableModel {

    private final String[] cols;
    private final List<Object[]> rows;
    private final Class<?>[] clazzes;

    public ReportTableModel(String[] cols, List<Object[]> rows, Class<?>[] clazzes) {
	super();
	this.cols = cols;
	this.rows = rows;
	this.clazzes = clazzes;
    }

    public String[] getCols() {
	return cols;
    }

    public List<Object[]> getRows() {
	return rows;
    }
    
    public Object[][] getRowsAsArray() {
	Object [][] array = new Object[rows.size()][cols.length];
	
	for(int i=0;i<rows.size();i++) {
	    array[i] = rows.get(i);
	}
   	return array;
       }

    public Class<?>[] getClazzes() {
	return clazzes;
    }

    public int getRowCount() {
	return rows.size();
    }

}
