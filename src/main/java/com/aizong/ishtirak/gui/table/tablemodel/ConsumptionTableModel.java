package com.aizong.ishtirak.gui.table.tablemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;

@SuppressWarnings("serial")
public class ConsumptionTableModel extends AbstractTableModel {

    Map<String, Map<String, Double>> map;

    List<String> cols = new ArrayList<>();

    int row = 0;
    
    public ConsumptionTableModel(Map<String, Map<String, Double>> map) {
	super();
	cols.add(message("expenses"));
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
	return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {
	if (col == 0) {
		    return ServiceProvider.get().getMessage().getEnumLabel(row==0 ? ExpensesType.DIESEL.name() :  ExpensesType.FILTER_OIL_CHANGING.name(), ExpensesType.class);
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

	Map<String, Double> map2 = map.get(getColumnName(col));
	Double double1 = map2.get(getValueAt(row, 0));
	return double1;
    }

    private String message(String string) {
	return ServiceProvider.get().getMessage().getMessage(string);
    }

}