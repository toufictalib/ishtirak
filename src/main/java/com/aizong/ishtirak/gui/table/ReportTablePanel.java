package com.aizong.ishtirak.gui.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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

import org.apache.log4j.Logger;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.form.BasicPanel;
import com.aizong.ishtirak.common.misc.component.HeaderRenderer;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.CurrencyUtils;
import com.aizong.ishtirak.common.misc.utils.DateCellRenderer;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.TableUtils;
import com.aizong.ishtirak.common.misc.utils.reporting.ReportUtils;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.jgoodies.forms.builder.DefaultFormBuilder;

import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

@SuppressWarnings("serial")
public abstract class ReportTablePanel extends BasicPanel implements RefreshTableInterface {

    private final static Logger LOG = Logger.getLogger(ReportTablePanel.class.getSimpleName());

    protected JTable table;
    protected TableModel model;
    protected TableRowSorter<TableModel> sorter;
    protected String title;
    protected JTextField txtFE;
    protected JLabel txtRowCount;
    protected JLabel txtTotal;
    protected JButton btnExportExcel;
    protected JButton btnPrint;

    private TableFilterHeader filterHeader;
    
    private final ReportTableModel reportTableModel;

   
    public ReportTablePanel(String title, ReportTableModel reportTableModel, boolean init) {
	this.title = title;
	this.reportTableModel = reportTableModel;
	if(init) {
	    start();
	}
	
    }
    public ReportTablePanel(String title, ReportTableModel reportTableModel) {
	this(title, reportTableModel, true);
	
    }

    protected void start() {
	initComponents();
	fillTable();

	DefaultFormBuilder builder = BasicForm.createBuilder("fill:p:grow", "fill:p:grow,p,p");
	builder.append(initUI());
	builder.appendSeparator();
	JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel.add(btnExportExcel);
	panel.add(btnPrint);
	panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

	builder.append(panel);
	add(builder.getPanel(), BorderLayout.CENTER);

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
	table.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	table.setDefaultRenderer(Date.class, new DateCellRenderer());

	table.setRowSorter(sorter);
	table.setSelectionMode(0);

	sorter = new TableRowSorter<TableModel>();
	sorter.addRowSorterListener(new RowSorterListener() {

	    @Override
	    public void sorterChanged(RowSorterEvent arg0) {
		int newRowCount = table.getRowCount();
		setTxtRowCount(newRowCount);
		sumAmount(table);

	    }
	});

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

	btnExportExcel = ButtonFactory.createBtnExportExcel();
	btnExportExcel.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(new File(title + ".xls"));
		int returnVal = fc.showSaveDialog(ReportTablePanel.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    try {
			ReportUtils.writeToExcel(table.getModel(), file.getPath(), title);
			MessageUtils.showInfoMessage(ReportTablePanel.this, message("file.exported.success"));
		    } catch (Exception e1) {
			e1.printStackTrace();
			MessageUtils.showErrorMessage(ReportTablePanel.this, message("file.exported.error"));
		    }
		}

	    }
	});

	btnPrint = ButtonFactory.createBtnPrint();

	btnPrint.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		ReportUtils.printTable(table, ReportTablePanel.this.getOwner());
	    }
	});

    }

    private void setTxtRowCount(int row) {
	txtRowCount.setText(message("table.rowCount", row));
    }

    protected JPanel initUI() {

	String leftToRightSpecs = "fill:p:grow,5dlu,p";

	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, "p,fill:p:grow,p,p");
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);

	// builder.append(txtFE, 3);

	JScrollPane scrollPane = new JScrollPane(table);
	scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	// scrollPane.setPreferredSize(ComponentUtils.getDimension(60, 60));
	builder.append(scrollPane, 3);

	builder.append(txtRowCount, 3);

	if (showTotal()) {
	    builder.append(txtTotal, 3);
	}
	return builder.getPanel();
    }

    private boolean showTotal() {
	int totalTargetedColumn = getTotalTargetedColumn();
	if (totalTargetedColumn < 0 || table.getColumnCount() == 0 || totalTargetedColumn >= table.getColumnCount()) {
	    System.out.println("Total not added");
	    return false;
	}
	return true;
    }

    protected int getTotalTargetedColumn() {
	return -1;
    }

    private void fillTable() {
	fillTable(reportTableModel);
	sumAmount(table);
    }

    protected void fillTable(ReportTableModel reportTableModel) {

	if (reportTableModel != null) {
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
	} else {
	    model = new DefaultTableModel(5, 6);
	}

	table.setModel(model);

	sorter.setModel(model);
	table.setRowSorter(sorter);
	setTxtRowCount(model.getRowCount());
	applyRenderer();

	TableUtils.resizeColumnWidth(table);

	filterHeader.updateUI();
	filterHeader.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    private void sumAmount(JTable table) {

	if (!showTotal()) {
	    return;
	}
	int colToSum = getTotalTargetedColumn();
	int numberOfRaw = table.getRowCount();
	Double total = 0d;
	for (int i = 0; i < numberOfRaw; i++) {
	    try {
		double value = Double.valueOf(table.getValueAt(i, colToSum).toString());
		total += value;
	    } catch (Exception e) {
		LOG.warn(ReportTablePanel.class.getSimpleName() + " sum total error: " + e.getMessage());
	    }

	}
	txtTotal.setText(message("table.total", CurrencyUtils.format(total)));
    }

    private void applyRenderer() {

	if (table.getModel().getColumnCount() > 0) {
	    TableColumn column = table.getColumnModel().getColumn(table.getModel().getColumnCount() - 1);
	    column.setMinWidth(150);
	}

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


}