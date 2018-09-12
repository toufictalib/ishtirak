package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import com.aizong.ishtirak.bean.OrderBean;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ProgressBar;
import com.aizong.ishtirak.common.misc.utils.ProgressBar.ProgressBarListener;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.gui.table.FilterTable;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

@SuppressWarnings("serial")
public class SortingSubscribersPanel extends BasicForm {

    protected ExCombo<Village> comboVillages;
    private JButton btnSearch;
    private MyTable myTable;
    private JButton btnClose;
    private JButton btnSave;

    private List<Village> villages;

    private MyTableModel tableModel;

    public SortingSubscribersPanel(List<Village> villages) {
	this.villages = villages;
	initializePanel();
    }

    @Override
    protected void initComponents() {
	comboVillages = new ExCombo<>(villages);
	btnSearch = ButtonFactory.createBtnSearch();
	btnSearch.addActionListener(search());
	btnClose = ButtonFactory.createBtnClose();
	btnClose.addActionListener(e -> closeWindow());

	btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(saveOrders());
	myTable = new MyTable(message("contract.order.subsribers"));

    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("contract.order.sep"));
	builder.append(message("contract.order.region"), comboVillages, btnSearch);
	builder.append(myTable, builder.getColumnCount());
	builder.appendSeparator();
	builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder.getColumnCount());
	return builder.getPanel();
    }

    private ActionListener search() {
	return e -> {

	    Village selectedVillage = comboVillages.getValue();

	    if (selectedVillage != null) {
		ProgressBar.execute(new ProgressBarListener<List<OrderBean>>() {

		    @Override
		    public List<OrderBean> onBackground() throws Exception {
			return ServiceProvider.get().getSubscriberService()
				.getContractsForOrderingPurpose(selectedVillage.getId());

		    }

		    @Override
		    public void onDone(List<OrderBean> rows) {

			if (!rows.isEmpty()) {
			    tableModel = new MyTableModel(new LinkedList<>(rows), getCols());

			    myTable.fillTable(tableModel);
			    getOwner().pack();
			}
		    }

		}, SortingSubscribersPanel.this);
	    } else {
		MessageUtils.showWarningMessage(SortingSubscribersPanel.this,
			message("contract.order.missing.village"));
	    }
	};
    }

    private ActionListener saveOrders() {
	return e -> {
	    if (tableModel != null) {
		ProgressBar.execute(new ProgressBarListener<Void>() {

		    @Override
		    public Void onBackground() throws Exception {
			ServiceProvider.get().getSubscriberService().updateContactOrdering(tableModel.getRows());
			return null;
		    }

		    @Override
		    public void onDone(Void response) {
			MessageUtils.showInfoMessage(SortingSubscribersPanel.this,
				message("contact.order.save.success"));
			closeWindow();

		    }

		}, SortingSubscribersPanel.this);

	    } else {
		MessageUtils.showWarningMessage(SortingSubscribersPanel.this, message("contact.order.save.error"));
	    }
	};
    }

    private List<String> getCols() {
	List<String> cols = new ArrayList<>();
	cols.add(message("codeId"));
	cols.add(message("identifier"));
	cols.add(message("order_index"));
	return cols;
    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:300dlu:grow, 4dlu, fill:p:grow";
    }

    private class MyTable extends FilterTable {

	public MyTable(String title) {
	    super(title);
	}

    }

    public class MyTableModel extends AbstractTableModel {

	private LinkedList<OrderBean> rows;

	private List<String> cols;

	public MyTableModel(LinkedList<OrderBean> rows, List<String> cols) {
	    int ctr = 1;

	    // it is useful for first time only, in case the order is null
	    for (OrderBean orderBean : rows) {
		if (orderBean.getOrderIndex() == null || orderBean.getOrderIndex() == 0) {
		    orderBean.setOrderIndex(ctr++);
		}
	    }

	    this.rows = rows;
	    this.cols = cols;

	}

	@Override
	public int getColumnCount() {
	    return cols.size();
	}

	@Override
	public String getColumnName(int column) {
	    return cols.get(column);
	}

	@Override
	public int getRowCount() {
	    return rows.size();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
	    if (col == 2) {
		return true;
	    }
	    return super.isCellEditable(row, col);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
	    switch (columnIndex) {
	    case 0:
	    case 1:
		return String.class;
	    case 2:
		return Integer.class;
	    default:
		break;
	    }
	    return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

	    OrderBean orderBean = rows.get(rowIndex);
	    switch (columnIndex) {
	    case 0:
		return orderBean.getContractUniqueCode();
	    case 1:
		return orderBean.getIdentifier();
	    case 2:
		return orderBean.getOrderIndex();
	    default:
		break;
	    }
	    return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	    OrderBean orderBean = rows.get(rowIndex);

	    if (columnIndex == 2) {
		Integer order = (Integer) aValue;
		if (order != null && order.intValue() != orderBean.getOrderIndex().intValue() && order.intValue() > 0
			&& order.intValue() <= rows.size()) {

		    // remove modified orderBean
		    rows.remove(rowIndex);
		    fireTableRowsDeleted(rowIndex, rowIndex);

		    // Actual index is the order - 1
		    int newOrderIndex = order - 1;
		    // Set the new order of orderBean
		    orderBean.setOrderIndex(order);

		    // add it to rows
		    rows.add(newOrderIndex, orderBean);
		    fireTableRowsInserted(newOrderIndex, newOrderIndex);

		    // reorder other values automatically
		    for (int i = 0; i < rows.size(); i++) {
			rows.get(i).setOrderIndex((i + 1));
		    }
		}

	    }
	}

	public List<OrderBean> getRows() {
	    return new ArrayList<>(rows);
	}

    }
}
