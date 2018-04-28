package com.aizong.ishtirak.gui.table;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Date;
import java.util.Optional;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
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
import com.aizong.ishtirak.common.misc.utils.ImageUtils;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ProgressBar;
import com.aizong.ishtirak.common.misc.utils.ProgressBar.ProgressBarListener;
import com.aizong.ishtirak.common.misc.utils.reporting.ReportUtils;
import com.aizong.ishtirak.common.misc.utils.TableUtils;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.jgoodies.forms.builder.DefaultFormBuilder;

import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

@SuppressWarnings("serial")
public abstract class CommonFilterTable extends BasicPanel implements RefreshTableInterface {

    protected JTable table;
    protected TableModel model;
    protected TableRowSorter<TableModel> sorter;
    protected String title;
    protected JTextField txtFE;
    protected JLabel txtRowCount;
    protected JButton btnAdd;
    protected MyTableListener listener;
    private TableFilterHeader filterHeader;
    protected JButton btnExportExcel;
    protected JButton btnPrint;
    protected JLabel txtTotal;

    private final static Logger LOG = Logger.getLogger(CommonFilterTable.class.getSimpleName());

    public CommonFilterTable(String title, boolean init, MyTableListener listener) {
	super();
	this.title = title;
	this.listener = listener;

	if (init) {
	    start();
	}

    }

    public CommonFilterTable(String title, MyTableListener listener) {
	this(title, true, listener);
    }

    protected final void start() {
	initComponents();
	fillTable();
	add(initUI());
    }

    private void initComponents() {

	txtTotal = new JLabel();

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
	table.setDefaultRenderer(JPanel.class, new RssFeedCell());
	table.setDefaultRenderer(Date.class, new DateCellRenderer());
	table.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

	sorter = new TableRowSorter<TableModel>();
	sorter.addRowSorterListener(new RowSorterListener() {

	    @Override
	    public void sorterChanged(RowSorterEvent arg0) {
		int newRowCount = table.getRowCount();
		setTxtRowCount(newRowCount);
		sumAmount(table);
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

	btnAdd = new JButton(getAddTooltip(), ImageUtils.getAddIcon());
	btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnAdd.setToolTipText(btnAdd.getText());
	btnAdd.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		listener.add(getOwner(), CommonFilterTable.this);

	    }
	});

	txtRowCount = new JLabel();

	btnExportExcel = ButtonFactory.createBtnExportExcel();
	btnExportExcel.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(new File(title + ".xls"));
		int returnVal = fc.showSaveDialog(CommonFilterTable.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    try {
			ReportUtils.writeToExcel(table.getModel(), file.getPath(), title);
			MessageUtils.showInfoMessage(CommonFilterTable.this, message("file.exported.success"));
		    } catch (Exception e1) {
			e1.printStackTrace();
			MessageUtils.showErrorMessage(CommonFilterTable.this, message("file.exported.error"));
		    }
		}

	    }
	});

	btnPrint = ButtonFactory.createBtnPrint();

	btnPrint.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		ReportUtils.printTable(table, CommonFilterTable.this.getOwner());
	    }
	});

    }

    protected abstract String getAddTooltip();

    private void setTxtRowCount(int row) {
	txtRowCount.setText(message("table.rowCount", row));
    }

    protected JPanel initUI() {

	String leftToRightSpecs = "fill:p:grow,5dlu,p";

	String row = "p,p,fill:p:grow,p,p,p";
	if (showTotal()) {
	    row += ",p";
	}
	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, row);
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);

	builder.append(txtFE, btnAdd);

	JScrollPane scrollPane = new JScrollPane(table);
	scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	builder.append(scrollPane, 3);

	builder.append(txtRowCount, builder.getColumnCount());

	if (showTotal()) {
	    builder.append(txtTotal, 3);
	}

	builder.appendSeparator();

	JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel.add(btnExportExcel);
	panel.add(btnPrint);
	builder.append(panel, builder.getColumnCount());

	return builder.getPanel();
    }

    protected void fillTable() {
	ProgressBar.execute(new ProgressBarListener<ReportTableModel>() {

	    @Override
	    public ReportTableModel onBackground() throws Exception {
		return getReportTableModel();
	    }

	    @Override
	    public void onDone(ReportTableModel response) {

		reloadTable();
	    }

	}, CommonFilterTable.this);

    }
    
    protected void reloadTable() {
	ReportTableModel reportTableModel = getReportTableModel();
	Object[] columns = reportTableModel.getCols();
	columns = add(columns, "btnView");
	
	Object[] internalisationCols = new Object[columns.length];
	for (int i = 0; i < columns.length; i++) {
	    if (columns[i] != null) {
		internalisationCols[i] = message.getMessage(columns[i].toString());
	    } else {
		internalisationCols[i] = columns[i];
	    }
	}
	
	model = new DefaultTableModel(reportTableModel.getRowsAsArray(), internalisationCols) {
	    @Override
	    public boolean isCellEditable(int arg0, int arg1) {
		return arg1 == (table.getModel().getColumnCount() - 1);
	    }
	    
	    @Override
	    public Class<?> getColumnClass(int arg0) {
		if (arg0 < reportTableModel.getClazzes().length) {
		    return reportTableModel.getClazzes()[arg0];
		}
		return super.getColumnClass(arg0);
	    }
	};
	table.setModel(model);
	sorter.setModel(model);
	table.setRowSorter(sorter);
	setTxtRowCount(model.getRowCount());
	applyRenderer();
	
	TableUtils.resizeColumnWidth(table);
	filterHeader.updateUI();
	filterHeader.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	
	sumAmount(table);
    }

    private void applyRenderer() {
	TableColumn column = table.getColumnModel().getColumn(table.getModel().getColumnCount() - 1);
	column.setCellEditor(new RssFeedCell());
	column.setMinWidth(150);
	column.setCellRenderer(new RssFeedCell());
    }

    public static Object[] add(Object[] arr, Object... elements) {
	Object[] tempArr = new Object[arr.length + elements.length];
	System.arraycopy(arr, 0, tempArr, 0, arr.length);

	for (int i = 0; i < elements.length; i++)
	    tempArr[arr.length + i] = elements[i];
	return tempArr;

    }

    public class RssFeedCellComponent extends JPanel {

	JButton showButton;
	JLabel text;

	public RssFeedCellComponent() {
	    showButton = new JButton("View Articles");
	    showButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		    JOptionPane.showMessageDialog(null, "Reading");
		}
	    });

	    text = new JLabel();
	    add(text);
	    add(showButton);
	}

    }

    public class RssFeedCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
	JPanel buttons;

	Object selectedValue;

	public RssFeedCell() {
	    buttons = new JPanel();
	    buttons.setOpaque(true);
	    JButton btnView = ButtonFactory.createBtnView();
	    btnView.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
		    if (selectedValue instanceof Long) {
			listener.view(getOwner(), (Long) selectedValue);
		    }

		}
	    });
	    btnView.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    JButton btnEdit = ButtonFactory.createBtnEdit();
	    btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));

	    btnEdit.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
		    if (selectedValue instanceof Long) {
			listener.edit(getOwner(), (Long) selectedValue, CommonFilterTable.this);
		    }

		}
	    });
	    JButton btnDelete = ButtonFactory.createBtnDelete();
	    btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));

	    btnDelete.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
		    if (selectedValue instanceof Long) {
			listener.delete(getOwner(), (Long) selectedValue, CommonFilterTable.this);
		    }

		}
	    });
	    ButtonFactory.makeButtonAsIcon(btnView);
	    ButtonFactory.makeButtonAsIcon(btnEdit);
	    ButtonFactory.makeButtonAsIcon(btnDelete);
	    buttons.add(btnDelete);
	    buttons.add(btnEdit);
	    buttons.add(btnView);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.
	 * swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
		int column) {
	    updateData(table, isSelected, row);
	    return buttons;
	}

	private void updateData(JTable table, boolean isSelected, int row) {
	    if (isSelected) {
		buttons.setBackground(table.getSelectionBackground());
	    } else {
		buttons.setBackground(table.getBackground());
	    }

	    this.selectedValue = table.getModel().getValueAt(table.convertRowIndexToModel(row), 0);
	}

	public Object getCellEditorValue() {
	    return null;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
		int row, int column) {
	    updateData(table, isSelected, row);
	    return buttons;
	}
    }

    @Override
    public void refreshTable() {
	fillTable();

    }

    public void warnNoSelectedRow() {
	MessageUtils.showInfoMessage(getOwner(), message("table.row.select.missing"));
    }

    public abstract ReportTableModel getReportTableModel();

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

    private boolean showTotal() {
	int totalTargetedColumn = getTotalTargetedColumn();
	if (totalTargetedColumn < 0) {
	    System.out.println("Total not added");
	    return false;
	}
	return true;
    }

    protected int getTotalTargetedColumn() {
	return -1;
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

}