/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SortOrder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableRowSorter;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 *
 * @author User
 */
public class FilterTableFrame extends JpanelTemplate  {

    protected JTable table;
    JTextField filterText;


    JLabel txtRowCount;

    private boolean allowUpdate = true;
    
    protected Dimension tableDimension  = new Dimension(750,350);

    //Table Components
    private BeanTableModel beanTableModel;
    private TableRowSorter<BeanTableModel> sorter;
    //make filter sensitive
    private JCheckBox cbSensitive;
    //private JButton btnRefresh;

    private Vector<String> columnNames;

    public FilterTableFrame() throws HeadlessException {

    }
    
    @Override
    public void lazyInitalize( )
    {
    	super.lazyInitalize();
    }

    public void setTableDimension(Dimension tableDimension) {
        this.tableDimension = tableDimension;
      reBuildPanel();
    }

    
    @Override
    public void init() {

        JScrollPane jScrollPane = new JScrollPane(table);
        if (tableDimension != null) {
            jScrollPane.setPreferredSize(tableDimension);
        }
        
        add(getControllerPanel(), BorderLayout.PAGE_START);
        add(jScrollPane, BorderLayout.CENTER);

    }
    

    public void fillValues(BeanTableModel beanTableModel) {

        this.beanTableModel = beanTableModel;
        sorter.setModel(beanTableModel);
        table.setModel(beanTableModel);

        TableUtils.resizeColumnWidth(table);
        setTxtRowCount(table.getRowCount());

    }

    @Override
    public void initComponents() {
        sorter = new TableRowSorter<BeanTableModel>() {

            @Override
            public void toggleSortOrder(int column) {
                List<? extends SortKey> sortKeys = getSortKeys();
                if (sortKeys.size() > 0) {
                    if (sortKeys.get(0).getSortOrder() == SortOrder.DESCENDING) {
                        setSortKeys(null);
                        return;
                    }
                }
                super.toggleSortOrder(column);
            }

        };
        
        sorter.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
                int newRowCount = table.getRowCount();
                setTxtRowCount(newRowCount);
            }

        });

        table = new JTable() {
            public boolean getScrollableTracksViewportWidth() {
                return getPreferredSize().width < getParent().getWidth();
            }

        ;

        };
        

        table.addMouseListener(CreateTableClickListener());

        table.setDefaultRenderer(Date.class, new DateCellRenderer());
        table.setDefaultRenderer(boolean.class, table.getDefaultRenderer(Boolean.class));

        table.setRowSorter(sorter);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(true);

        filterText = new JTextField();
        //Whenever filterText changes, invoke newFilter.
        filterText.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newFilter();
                    }

                    public void insertUpdate(DocumentEvent e) {
                        newFilter();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        newFilter();
                    }
                });

        cbSensitive = new JCheckBox("Sensitive case");

        txtRowCount = new JLabel();
//        btnRefresh = new JButton("Refresh");
//        btnRefresh.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                refresh();
//            }
//        });
    }

    private MouseListener CreateTableClickListener() {
        return new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    JTable table = (JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    int col = table.columnAtPoint(p);
                    if (row != -1 && col != -1) {
                        int modelRow = table.convertRowIndexToModel(row);
                        int modelCol = table.convertColumnIndexToModel(col);
                        if (modelRow != -1 && modelCol != -1) {

                           /* Bean bean = new Bean();
                            bean.setModelRow(modelRow);
                            bean.setBeanComplexElement(beanComplexElement);
                            bean.setModelColumn(modelCol);
                            bean.setColumns(beanTableModel.getColumns());
                            bean.setRow(beanTableModel.getRow(modelRow));
                            tableListener(bean);*/
                        }
                    }
                }
            }
        };
    }

    public JTable getTable() {
        return table;
    }

    private void setTxtRowCount(int row) {
        txtRowCount.setText("Row Count : " + row);
    }
    //protected abstract void refresh();

    protected JPanel getControllerPanel() {
        FormLayout layout = new FormLayout("fill:p:grow,10dlu,p,10dlu,p,10dlu,p", "");

        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();

        builder.append(filterText);
        //builder.append(cbSensitive);
        builder.append(txtRowCount);
        return builder.getPanel();
    }

    private void newFilter() {

        //If current expression doesn't parse, don't update.
        int[] indices = new int[beanTableModel.getColumnCount()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        try {
            final String filterValue = filterText.getText();

            List<RowFilter<BeanTableModel, Object>> filters
                    = new ArrayList<RowFilter<BeanTableModel, Object>>();

            String[] values = filterValue.split(";");
            for (String value : values) {
                RowFilter<BeanTableModel, Object> rf = null;
                if (cbSensitive.isSelected()) {
                    rf = RowFilter.regexFilter(value.trim(), indices);
                } else {
                    rf = RowFilter.regexFilter("(?i)" + value.trim(), indices);
                }
                filters.add(rf);
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }

        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }

    }

    public int getColIndexByName(String col) {
        if (columnNames == null) {
            throw new NullPointerException("you should fill values before call this method getColIndexByName");
        }
        return columnNames.indexOf(col);
    }

    public void show(Window owner) {
        JFrame frame = new JFrame("Filter");

        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setContentPane(this);
        frame.pack();
        frame.setLocationRelativeTo(owner);
        frame.setVisible(true);
    }

    public List<Object> getRowAt(int row) {
        List<Object> result = new ArrayList<Object>(columnNames.size());

        for (int i = 0; i < columnNames.size(); i++) {
            result.add(table.getModel().getValueAt(row, i));
        }

        return result;
    }

    public JTextField getFilterText() {
        return filterText;
    }

    void allowEdit(boolean selected) {
        table.setEnabled(selected);
        filterText.setEnabled(selected);

    }

    public BeanTableModel getTableModel() {
        return beanTableModel;
    }

    public void deleteSelectedRows() {
        int[] selectedRows = getTable().getSelectedRows();
        Arrays.sort(selectedRows);

        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int row = selectedRows[i];
            int modelRow = getTable().convertRowIndexToModel(row);
            beanTableModel.removeRow(modelRow);
        }
        newFilter();
    }

    public boolean isAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }


}
