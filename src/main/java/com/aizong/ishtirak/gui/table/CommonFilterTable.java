package com.aizong.ishtirak.gui.table;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.form.BasicPanel;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ImageUtils;
import com.aizong.ishtirak.common.misc.utils.TableUtils;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.jgoodies.forms.builder.DefaultFormBuilder;

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

    public CommonFilterTable(String title, MyTableListener listener) {

	this.title = title;
	this.listener = listener;
	start();

    }

    private void start() {
	initComponents();
	fillTable();
	add(initUI());
    }

    private void initComponents() {
	table = new JTable();
	table.setRowHeight(50);
	table.setFillsViewportHeight(true);
	table.setDefaultRenderer(JPanel.class, new RssFeedCell());
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

	txtFE = new JTextField(25);
	txtFE.addKeyListener(new KeyAdapter() {

	    @Override
	    public void keyReleased(KeyEvent arg0) {
		String expr = txtFE.getText();
		sorter.setRowFilter(RowFilter.regexFilter(expr));
		sorter.setSortKeys(null);
	    }

	});

	btnAdd = ButtonFactory.createBtnAdd();
	ButtonFactory.makeButtonAsIcon(btnAdd);
	btnAdd.setToolTipText("أضف مشترك");
	btnAdd.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		listener.add(getOwner(), CommonFilterTable.this);

	    }
	});

	txtRowCount = new JLabel();
    }

    private void setTxtRowCount(int row) {
	txtRowCount.setText("العدد : " + row);
    }

    protected JPanel initUI() {

	String leftToRightSpecs = "fill:p:grow,5dlu,p";

	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs);
	builder.setDefaultDialogBorder();
	
	builder.appendSeparator(title);

	builder.append(txtFE, btnAdd);

	builder.append(new JScrollPane(table), 3);

	builder.append(txtRowCount);

	return builder.getPanel();
    }

    private void fillTable() {
	ReportTableModel reportTableModel = getReportTableModel();
	Object[] columns = reportTableModel.getCols();
	columns = add(columns, "معاينة");

	model = new DefaultTableModel(reportTableModel.getRowsAsArray(), columns);
	table.setModel(model);
	sorter.setModel(model);
	table.setRowSorter(sorter);
	setTxtRowCount(model.getRowCount());
	applyRenderer();

	TableUtils.resizeColumnWidth(table);
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
	    JButton btnView = new JButton("معاينة", ImageUtils.getCheckupIcon());
	    btnView.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
		    if (selectedValue instanceof Long) {
			listener.view(getOwner(), (Long) selectedValue);
		    }

		}
	    });
	    btnView.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    JButton btnEdit = new JButton("تعديل", ImageUtils.getInfoIcon());
	    btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));

	    btnEdit.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
		    if (selectedValue instanceof Long) {
			listener.edit(getOwner(), (Long) selectedValue, CommonFilterTable.this);
		    }

		}
	    });
	    JButton btnDelete = new JButton("حذف", ImageUtils.getDeleteIcon());
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

    public abstract ReportTableModel getReportTableModel();

}