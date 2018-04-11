package com.aizong.ishtirak.gui.table.tablemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;

@SuppressWarnings("serial")
public class IncomeTableModel extends AbstractTableModel {

    Map<String, Map<String, Double>> map;

    List<String> cols = new ArrayList<>();

    public IncomeTableModel(Map<String, Map<String, Double>> map) {
	super();
	cols.add(message("income"));
	cols.addAll(map.keySet());
	cols.add(message("total"));
	this.map = map;
    }

    @Override
    public int getColumnCount() {
	return cols.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
	if (columnIndex != 0) {
	    return Double.class;
	}
	return super.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
	return cols.get(column);
    }

    @Override
    public int getRowCount() {
	return TransactionType.values().length + 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
	if (col == 0) {
	    if (row < getRowCount() - 1) {
		return ServiceProvider.get().getMessage().getEnumLabel(TransactionType.values()[row].toString(), TransactionType.class);
	    }
	    return message("total");
	}

	if (col == cols.size() - 1) {

	    Double total = null;
	    for (int i = 1; i < cols.size() - 1; i++) {
		if (getValueAt(row, i) instanceof Double) {
		    if (total == null) {
			total = 0d;
		    }
		    total += (Double) getValueAt(row, i);
		}
	    }
	    return total;
	}
	if (row == getRowCount() - 1) {
	    Double total = null;
	    for (int i = 0; i < getRowCount() - 1; i++) {
		if (getValueAt(i, col) instanceof Double) {
		    if (total == null) {
			total = 0d;
		    }
		    total += (Double) getValueAt(i, col);
		}
	    }
	    return total;
	}
	Map<String, Double> map2 = map.get(getColumnName(col));
	Double double1 = map2.get(getValueAt(row, 0));
	return double1;
    }

    private String message(String string) {
	return ServiceProvider.get().getMessage().getMessage(string);
    }

}