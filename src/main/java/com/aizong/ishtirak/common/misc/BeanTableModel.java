/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author User
 */
public class BeanTableModel<T> extends AbstractTableModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7261474884426956514L;

	private List<Column> columns;

    private List<List<Object>> rows;

    public BeanTableModel(ModelHolder holder) {
       this(holder, new ReportBeanParser());
    }
    
    public BeanTableModel(List<Column> columns,List<List<Object>> rows)
    {
        this.columns = columns;
        this.rows = rows;
    }
    
    
      public BeanTableModel(ModelHolder holder,BeanParsing beanParsing) {
        beanParsing.prepare(holder);
        beanParsing.init();

        columns = beanParsing.getColumns();
        rows = beanParsing.getRows();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int col) {
        return getColumn(col).getName();
    }

    public Column getColumn(int col) {
        return columns.get(col);
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return convertToWraper(getColumn(col).getType());
    }

    @SuppressWarnings("rawtypes")
	private Class convertToWraper(Class clazz) {
        if (clazz == int.class) {
            return Integer.class;
        } else if (clazz == boolean.class) {
            return Boolean.class;
        }
        return clazz;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int col) {
        return false;
    }
    

    @Override
    public Object getValueAt(int rowIndex, int col) {
        return rows.get(rowIndex).get(col);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int col) {
        rows.get(rowIndex).set(col, aValue);
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    public List<Object> getRow(int row) {
        return rows.get(row);
    }

    public void updateRow(int rowIndex, List<Object> row) {
        if (rowIndex >= rows.size()) {
            throw new IllegalArgumentException(rowIndex + " should be less than " + rows.size());
        }

        if (row.size() != columns.size()) {
            throw new IllegalArgumentException("update row is failed because number of column " + row.size() + " is differenctr to current number of column " + columns.size());
        }
        rows.set(rowIndex, row);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void addRow(List<Object> row) {
        if (row.size() != columns.size()) {
            throw new IllegalArgumentException("update row is failed because number of column " + row.size() + " is differenctr to current number of column " + columns.size());

        }

        int rowSize = rows.size();

        rows.add(row);
        fireTableRowsInserted(rowSize, rowSize);
    }

    public void removeRow(int rowIndex) {
        rows.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public List<T> revert(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<T>();

        for (List<Object> objects : rows) {
            T newInstance = clazz.newInstance();
            ReportBeanParser.fillValue(newInstance, columns, objects);
            list.add(newInstance);
        }

        return list;

    }

}
