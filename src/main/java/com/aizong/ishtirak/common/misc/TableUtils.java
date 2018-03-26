/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author User
 */
public class TableUtils {

    public static void moveToBottom(JTable table) {
        table.scrollRectToVisible(table.getCellRect(table.getRowCount()+1, 0, true));
        table.getSelectionModel().clearSelection();
        table.getSelectionModel().setSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
    }
    
    public static void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
    
    public static void resizeColumnWidth(JTable table, int column) {
        final TableColumnModel columnModel = table.getColumnModel();
        int width = 50; // Min width
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component comp = table.prepareRenderer(renderer, row, column);
            width = Math.max(comp.getPreferredSize().width + 1, width);
        }
        columnModel.getColumn(column).setPreferredWidth(width);
    }

    /**
     * 
     * @param table
     * @param anAction
     * @param aCommand
     * @param aKeyStroke
      * @param aCondition  one of WHEN_IN_FOCUSED_WINDOW, WHEN_FOCUSED,
     *        WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
     */
    public static void registerKeyboardAction(JTable table,final ActionListener anAction, String aCommand, KeyStroke aKeyStroke, int aCondition) {

        InputMap inputMap = table.getInputMap(aCondition);

        if (inputMap != null) {
            ActionMap actionMap = table.getActionMap();
            inputMap.put(aKeyStroke, aCommand);
            if (actionMap != null) {
                actionMap.put(aCommand, new AbstractAction() {

                    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
                    public void actionPerformed(ActionEvent e) {
                        anAction.actionPerformed(e);
                    }
                });
            }
        }
    }
    
    public static int[] convertSelectedRowsToModel(JTable table) {
        int [] rows = table.getSelectedRows();
        final int[] modelRows = new int[rows.length];
        for (int i = 0; i < rows.length; i++) {
            modelRows[i] = table.convertRowIndexToModel(rows[i]);
        }

        return modelRows;
    }

     public static int[] convertSelectedColsToModel(JTable table) {
        return convertSelectedColsToModel(table, table.getSelectedColumns());
    }
     
    public static int[] convertSelectedColsToModel(JTable table, int[] cols) {
        final int[] modelCols = new int[cols.length];
        for (int i = 0; i < cols.length; i++) {
            modelCols[i] = table.convertColumnIndexToModel(cols[i]);
        }

        return modelCols;
    }
}
