package com.aizong.ishtirak.gui.table.tablemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;

@SuppressWarnings("serial")
public class SubscriptionFeesTableModel extends AbstractTableModel {

	Map<String, Map<String, Double>> map;

	List<String> cols = new ArrayList<>();

	public SubscriptionFeesTableModel(Map<String, Map<String, Double>> map) {
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
		return 1;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			switch (row) {
			case 0:
			case 1:
			case 2:
				return ServiceProvider.get().getMessage().getEnumLabel(TransactionType.COUNTER_PAYMENT.name(),
						TransactionType.class);
			case 3:
				return message("total");
			case 4:
				return message("payment");
			case 5:
				return message("remain");

			default:
				break;
			}

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
		if (row == 3) {
			Double total = null;
			for (int i = 0; i < 3; i++) {
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