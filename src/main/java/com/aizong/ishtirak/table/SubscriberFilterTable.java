package com.aizong.ishtirak.table;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

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

import com.aizong.ishtirak.common.BasicPanel;
import com.aizong.ishtirak.common.ButtonFactory;
import com.aizong.ishtirak.common.ImageUtils;
import com.aizong.ishtirak.common.Mode;
import com.aizong.ishtirak.common.ServiceProvider;
import com.aizong.ishtirak.common.WindowUtils;
import com.aizong.ishtirak.subscriber.form.OrientationUtils;
import com.aizong.ishtirak.subscriber.form.SavingCallback;
import com.aizong.ishtirak.subscriber.form.SubscriberFormSwing;
import com.aizong.ishtirak.subscriber.model.Subscriber;
import com.aizong.ishtirak.utils.MessageUtils;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SubscriberFilterTable extends BasicPanel {

    private JTable table;
    private TableModel model;
    private TableRowSorter<TableModel> sorter;
    private String title;
    private JTextField txtFE;
    private JLabel txtRowCount;
    private JButton btnAdd;

    public SubscriberFilterTable(String title) {

	this.title = title;
	start();

    }

    private void start() {
	initComponents();
	fillTable();
	initUI();
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
	btnAdd.setToolTipText("إضافة مشترك");
	btnAdd.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		WindowUtils.createDialog(getOwner(), "مشترك جديد",
			new SubscriberFormSwing(Mode.NEW, new SavingCallback() {

			    @Override
			    public void onSuccess(Object o) {
				fillTable();

			    }
			}));

	    }
	});

	txtRowCount = new JLabel();
    }

    private void setTxtRowCount(int row) {
	txtRowCount.setText("العدد : " + row);
    }

    private void initUI() {

	String leftToRightSpecs = "fill:p:grow,5dlu,p";
	FormLayout layout = new FormLayout(OrientationUtils.flipped(leftToRightSpecs), new RowSpec[] {});
	DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
	builder.setDefaultDialogBorder();
	builder.setLeftToRight(false);

	builder.appendSeparator(title);

	builder.append(txtFE, btnAdd);

	builder.append(new JScrollPane(table), 3);

	builder.append(txtRowCount);

    }

    private void fillTable() {
	ReportTableModel reportTableModel = ServiceProvider.get().getReportServiceImpl().getSubscribers();
	Object[] columns = reportTableModel.getCols();
	columns = add(columns, "معاينة");

	model = new DefaultTableModel(reportTableModel.getRowsAsArray(), columns);
	table.setModel(model);
	sorter.setModel(model);
	table.setRowSorter(sorter);
	setTxtRowCount(model.getRowCount());
	applyRenderer();
    }

    private void applyRenderer() {
	TableColumn column = table.getColumnModel().getColumn(5);
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
			Subscriber subscriber = ServiceProvider.get().getSubscriberService()
				.getSubscriberById((Long) selectedValue);
			if (subscriber != null) {
			    WindowUtils.createDialog(getOwner(), "عرض المشترك",
				    new SubscriberFormSwing(Mode.VIEW, subscriber));
			}
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
			Subscriber subscriber = ServiceProvider.get().getSubscriberService()
				.getSubscriberById((Long) selectedValue);
			if (subscriber != null) {
			    WindowUtils.createDialog(getOwner(), " تعديل المشترك " + subscriber.getName(),
				    new SubscriberFormSwing(Mode.UPDATE, subscriber));
			}
		    }

		}
	    });
	    JButton btnDelete = new JButton("حذف", ImageUtils.getDeleteIcon());
	    btnDelete.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
		    boolean yes = MessageUtils.showConfirmationMessage(getOwner(), "هل تريد حذف هذا القيد؟", "حذف");
		    if (yes) {
			if (selectedValue instanceof Long) {
			    List<Long> ids = new ArrayList<>();
			    ids.add((Long) selectedValue);
			    ServiceProvider.get().getSubscriberService().deleteSubscribers(ids);
			    fillTable();
			}
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

    public static void main(String[] args) {

	String[] cols = { "الرقم", "الاسم", "اسم الأب", "العائلة", "اللقب" };
	List<Object[]> rows = new ArrayList<>();
	rows.add(new Object[] { 1, "توفيق", "طالب", "أحمد", "الملك" });
	rows.add(new Object[] { 2, "جهاد", "عثمان", "علي", "الأستاذ" });
	SubscriberFilterTable subscriberFilterTable = new SubscriberFilterTable("المشتركون");
	/*
	 * new ReportTableModel(cols, rows, new Class<?>[] { String.class,
	 * String.class, String.class, String.class }
	 */
	WindowUtils.createDialog(null, "المشتركون", subscriberFilterTable);
    }

}