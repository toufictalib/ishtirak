package com.aizong.ishtirak.demo;

import static java.awt.BorderLayout.CENTER;
import static java.awt.Color.BLUE;
import static java.awt.Color.CYAN;
import static java.awt.Color.GREEN;
import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.MAGENTA;
import static java.awt.Color.ORANGE;
import static java.awt.Color.PINK;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;
import static javax.swing.JTable.AUTO_RESIZE_OFF;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.CellRendererPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Demo application for JTable with footer.
 * 
 * @author Martin Uhlén
 */
public class TableFooterBorderDemo extends JFrame {
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		new TableFooterBorderDemo().setVisible(true);
	    }
	});
    }

    private TableFooterBorderDemo() {
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	setLayout(new BorderLayout());

	JTable table = createTable();
	JScrollPane scroll = new JScrollPane(table);
	add(scroll, CENTER);
	TableFooter.install(scroll, table);
	pack();
	positionAtMiddleOfScreen();
    }

    private void positionAtMiddleOfScreen() {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	setLocation((int) ((screenSize.getWidth() / 2) - (getWidth() / 2)),
		(int) ((screenSize.getHeight() / 2) - (getHeight() / 2)));
    }

    private JTable createTable() {
	Object[][] data = new Object[][] { { "A", "*", "*", "*", "*", "*" }, { "*", "B", "*", "*", "*", "*" },
		{ "*", "*", "C", "*", "*", "*" }, { "*", "*", "*", "D", "*", "*" }, { "*", "*", "*", "*", "E", "*" },
		{ "*", "*", "*", "*", "*", "F" } };
	Object[] columns = new Object[] { "A", "B", "C", "D", "E", "F" };
	DefaultTableModel model = new DefaultTableModel(data, columns);
	JTable table = new JTable(model);
	table.setAutoResizeMode(AUTO_RESIZE_OFF);
	return table;
    }

    /**
     * A Border for JScrollPane that paints a footer for JTable.
     */
    private static class TableFooter implements Border {
	private static final Color[] COLORS = { RED, GREEN, BLUE, YELLOW, PINK, CYAN, LIGHT_GRAY, MAGENTA, ORANGE,
		WHITE };

	private final JScrollPane scroll;
	private final JTable table;
	private final CellRendererPane cellRendererPane;

	TableFooter(JScrollPane scroll, JTable table) {
	    this.scroll = scroll;
	    this.table = table;
	    cellRendererPane = new CellRendererPane();
	}

	public static TableFooter install(JScrollPane scroll, JTable table) {
	    verify(scroll, table);
	    TableFooter footer = new TableFooter(scroll, table);

	    RepaintListener repainter = new RepaintListener(scroll);
	    scroll.getViewport().addChangeListener(repainter);
	    scroll.getHorizontalScrollBar().addAdjustmentListener(repainter);
	    table.getColumnModel().addColumnModelListener(repainter);
	    scroll.setViewportBorder(footer);
	    return footer;
	}

	private static void verify(JScrollPane scroll, JTable table) {
	    if (scroll.getViewport().getView() != table) {
		throw new IllegalArgumentException("Given table must be inside given scroll pane");
	    }
	}

	/**
	 * @see javax.swing.border.Border#paintBorder(java.awt.Component,
	 *      java.awt.Graphics, int, int, int, int)
	 */
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	    System.out.println("x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);
	    System.out.println("viewRect: " + scroll.getViewport().getViewRect());
	    paintFooter(g, x, y, width, height);
	}

	private void paintFooter(Graphics g, int x, int y, int width, int height) {
	    Color oldColor = null;
	    Component cellRendererComponent = null;
	    int columnWidths = x - scroll.getViewport().getViewRect().x;
	    for (int column = 0; column < table.getColumnCount(); column++) {
		TableCellRenderer cellRenderer = table.getCellRenderer(0, column);
		cellRendererComponent = cellRenderer.getTableCellRendererComponent(table, getFooterValueAt(column),
			false, false, 0, column);
		if (oldColor == null) {
		    oldColor = cellRendererComponent.getBackground();
		}
		int columnWidth = table.getColumnModel().getColumn(column).getWidth();
		cellRendererComponent.setBackground(COLORS[column % COLORS.length]);
		cellRendererPane.paintComponent(g, cellRendererComponent, scroll, columnWidths, y, columnWidth, height);
		columnWidths += columnWidth;
	    }
	    if (cellRendererComponent != null) {
		cellRendererComponent.setBackground(oldColor);
	    }
	}

	private Object getFooterValueAt(int viewColumn) {
	    return "Column " + viewColumn;
	}

	@Override
	public Insets getBorderInsets(Component c) {
	    return new Insets(0, 0, table.getRowHeight(), 0);
	}

	@Override
	public boolean isBorderOpaque() {
	    return true;
	}
    }

    /**
     * Repaints JScrollPane when needed.
     */
    private static class RepaintListener implements ChangeListener, AdjustmentListener, TableColumnModelListener {
	private final JScrollPane scroll;

	RepaintListener(JScrollPane scroll) {
	    this.scroll = scroll;
	}

	@Override
	public void columnAdded(TableColumnModelEvent e) {
	    repaint();
	}

	@Override
	public void columnRemoved(TableColumnModelEvent e) {
	    repaint();
	}

	@Override
	public void columnMoved(TableColumnModelEvent e) {
	    repaint();
	}

	@Override
	public void columnMarginChanged(ChangeEvent e) {
	    repaint();
	}

	@Override
	public void columnSelectionChanged(ListSelectionEvent e) {
	    repaint();
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
	    repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
	    repaint();
	}

	private void repaint() {
	    scroll.repaint();
	}
    }
}