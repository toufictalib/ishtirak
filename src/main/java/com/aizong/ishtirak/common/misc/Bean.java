/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc;

import java.util.List;

/**
 *
 * @author User
 */
public class Bean {

    private int modelColumn = -1;
    private int modelRow = -1;
    private List<Column> columns;
    private List<Object> row;
    private boolean newRow;
    

    public Bean(int modelColumn, int modelRow, List<Column> columns, List<Object> row) {
        this.modelColumn = modelColumn;
        this.modelRow = modelRow;
        this.columns = columns;
        this.row = row;
    }

    public Bean() {
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Object> getRow() {
        return row;
    }

    public void setRow(List<Object> row) {
        this.row = row;
    }

    public int getModelColumn() {
        return modelColumn;
    }

    public void setModelColumn(int modelColumn) {
        this.modelColumn = modelColumn;
    }

    public int getModelRow() {
        return modelRow;
    }

    public void setModelRow(int modelRow) {
        this.modelRow = modelRow;
    }

    public Column getColumn(int index) {
        return columns.get(index);
    }

    public Object getValue(int index) {
        if (index < row.size()) {
            return row.get(index);
        }
        return null;
    }

    public boolean isNewRow() {
        return newRow;
    }

    public void setNewRow(boolean newRow) {
        this.newRow = newRow;
    }

}
