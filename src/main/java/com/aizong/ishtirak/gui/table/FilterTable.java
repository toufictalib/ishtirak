package com.aizong.ishtirak.gui.table;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.form.BasicPanel;
import com.aizong.ishtirak.common.misc.component.HeaderRenderer;
import com.aizong.ishtirak.common.misc.utils.DateCellRenderer;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.TableUtils;
import com.jgoodies.forms.builder.DefaultFormBuilder;

import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

@SuppressWarnings("serial")
public abstract class FilterTable extends BasicPanel {

    protected JTable table;
    protected TableModel model;
    protected TableRowSorter<TableModel> sorter;
    protected String title;
    protected JTextField txtFE;
    protected JLabel txtRowCount;
    private TableFilterHeader filterHeader;

    public FilterTable(String title, boolean init) {
	super();
	this.title = title;

	if (init) {
	    start();
	}

    }

    public FilterTable(String title) {
	this(title, true);
    }

    protected final void start() {
	initComponents();
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
	// table.setPreferredScrollableViewportSize(table.getPreferredSize());
	table.setRowHeight(50);
	table.setFillsViewportHeight(true);
	table.setDefaultRenderer(Date.class, new DateCellRenderer());
	table.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

	sorter = new TableRowSorter<TableModel>();
	sorter.addRowSorterListener(new RowSorterListener() {

	    @Override
	    public void sorterChanged(RowSorterEvent arg0) {
		int newRowCount = table.getRowCount();
		setTxtRowCount(newRowCount);
	    }
	});
	table.setRowSorter(sorter);

	JTableHeader header = table.getTableHeader();
	header.setDefaultRenderer(new HeaderRenderer(table));

	filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);
	filterHeader.setRowHeightDelta(10);

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

    }

    private void setTxtRowCount(int row) {
	txtRowCount.setText(message("table.rowCount", row));
    }

    protected JPanel initUI() {

	String leftToRightSpecs = "fill:p:grow,5dlu,p";

	String row = "p,p,fill:p:grow,p,p,p";
	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, row);
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);

	JScrollPane scrollPane = new JScrollPane(table);
	scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	builder.append(scrollPane, 3);

	builder.append(txtRowCount, builder.getColumnCount());

	return builder.getPanel();
    }

    public void fillTable(TableModel model) {

	table.setModel(model);
	sorter.setModel(model);
	table.setRowSorter(sorter);
	setTxtRowCount(model.getRowCount());

	TableUtils.resizeColumnWidth(table);
	filterHeader.updateUI();
	filterHeader.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

    }

    public void warnNoSelectedRow() {
	MessageUtils.showInfoMessage(getOwner(), message("table.row.select.missing"));
    }

    protected Optional<Long> getSelectedRowId() {
	int selectedRow = table.getSelectedRow();
	if (selectedRow >= 0) {
	    Object valueAt = table.getModel().getValueAt(table.convertRowIndexToModel(selectedRow), 0);
	    if (valueAt instanceof Long) {
		return Optional.of((Long) valueAt);
	    }
	} else {
	    warnNoSelectedRow();
	}
	return Optional.empty();
    }

}