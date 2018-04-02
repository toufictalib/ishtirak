package com.aizong.ishtirak.gui.table;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.form.BasicPanel;
import com.aizong.ishtirak.common.misc.component.HeaderRenderer;
import com.aizong.ishtirak.common.misc.utils.DateCellRenderer;
import com.aizong.ishtirak.common.misc.utils.TableUtils;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.jgoodies.forms.builder.DefaultFormBuilder;

import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

@SuppressWarnings("serial")
public abstract class ReportTablePanel extends BasicPanel implements RefreshTableInterface {

    protected JTable table;
    protected TableModel model;
    protected TableRowSorter<TableModel> sorter;
    protected String title;
    protected JTextField txtFE;
    protected JLabel txtRowCount;
    protected JLabel txtTotal;

    private TableFilterHeader filterHeader;

    public ReportTablePanel(String title) {

	this.title = title;
	start();

    }

    private void start() {
	initComponents();
	fillTable();
	add(initUI());
    }

    private void initComponents() {

	table = new JTable() {
	    @Override
	    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		Component comp = super.prepareRenderer(renderer, row, col);
		if (comp instanceof JLabel) {
		    ((JLabel) comp).setHorizontalAlignment(JLabel.RIGHT);
		}
		return comp;
	    }
	};
	table.setPreferredScrollableViewportSize(table.getPreferredSize());
	
	table.setRowHeight(50);
	table.setFillsViewportHeight(true);
	table.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	table.setDefaultRenderer(Date.class, new DateCellRenderer());

	sorter = new TableRowSorter<TableModel>();
	sorter.addRowSorterListener(new RowSorterListener() {

	    @Override
	    public void sorterChanged(RowSorterEvent arg0) {
		int newRowCount = table.getRowCount();
		setTxtRowCount(newRowCount);

	    }
	});
	table.setRowSorter(sorter);
	table.setSelectionMode(0);
	
	JTableHeader header = table.getTableHeader();
	header.setDefaultRenderer(new HeaderRenderer(table));

	txtFE = new JTextField(25);
	txtFE.addKeyListener(new KeyAdapter() {

	    @Override
	    public void keyReleased(KeyEvent arg0) {
		String expr = txtFE.getText();
		sorter.setRowFilter(RowFilter.regexFilter(expr));
		sorter.setSortKeys(null);
	    }

	});

	txtRowCount = new JLabel();

	txtTotal = new JLabel();

	filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);
	filterHeader.setRowHeightDelta(10);

    }

    private void setTxtRowCount(int row) {
	txtRowCount.setText(message("table.rowCount", row));
    }

    protected JPanel initUI() {

	String leftToRightSpecs = "fill:p:grow,5dlu,p";

	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs,"p,fill:p:grow,p,p");
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);

	//builder.append(txtFE, 3);

	JScrollPane scrollPane = new JScrollPane(table);
	// scrollPane.setPreferredSize(ComponentUtils.getDimension(60, 60));
	builder.append(scrollPane, 3);

	builder.append(txtRowCount, 3);

	builder.append("المجموع", txtTotal);

	return builder.getPanel();
    }

    private void fillTable() {
	ReportTableModel reportTableModel = getReportTableModel();
	String[] columns = reportTableModel.getCols();

	Object[] internalisationCols = new Object[reportTableModel.getCols().length];
	for (int i = 0; i < columns.length; i++) {
	    internalisationCols[i] = message.getMessage(columns[i]);
	}

	model = new DefaultTableModel(reportTableModel.getRowsAsArray(), internalisationCols) {
	    @Override
	    public boolean isCellEditable(int arg0, int arg1) {
		return false;
	    }

	    @Override
	    public Class<?> getColumnClass(int arg0) {
		if (reportTableModel.getClazzes().length == columns.length) {
		    return reportTableModel.getClazzes()[arg0];
		}
		return super.getColumnClass(arg0);
	    }
	};

	table.setModel(model);
	/*
	 * model.addTableModelListener(new TableModelListener() {
	 * 
	 * @Override public void tableChanged(TableModelEvent e) {
	 * sumAmount(table); }
	 * 
	 * 
	 * });
	 */

	sorter.setModel(model);
	table.setRowSorter(sorter);
	setTxtRowCount(model.getRowCount());
	applyRenderer();

	TableUtils.resizeColumnWidth(table);
	table.setPreferredScrollableViewportSize(table.getPreferredSize());

	filterHeader.updateUI();
	filterHeader.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	
	// sumAmount(table);
    }

    @SuppressWarnings("unused")
    private void sumAmount(JTable table) {
	TableModel model = table.getModel();
	int numberOfRaw = model.getRowCount();
	double total = 0;
	for (int i = 0; i < numberOfRaw; i++) {
	    // if checkbox is checked
	    double value = Double.valueOf(model.getValueAt(i, 5).toString());
	    total += value;
	}
	txtTotal.setText(Double.toString(total));
    }

    private void applyRenderer() {
	TableColumn column = table.getColumnModel().getColumn(table.getModel().getColumnCount() - 1);
	column.setMinWidth(150);

	// table.setDefaultRenderer(Boolean.class, new );

    }

    public static Object[] add(Object[] arr, Object... elements) {
	Object[] tempArr = new Object[arr.length + elements.length];
	System.arraycopy(arr, 0, tempArr, 0, arr.length);

	for (int i = 0; i < elements.length; i++)
	    tempArr[arr.length + i] = elements[i];
	return tempArr;

    }

    @Override
    public void refreshTable() {
	fillTable();

    }

    public abstract ReportTableModel getReportTableModel();

}