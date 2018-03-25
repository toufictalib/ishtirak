package com.aizong.ishtirak.common;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DateCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -4652643526136617958L;
    public static final SimpleDateFormat sdf = new SimpleDateFormat(DateCellRenderer.DATE_FORMAT);
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int viewRow, int column) {
        Component c = super.getTableCellRendererComponent(table, value == null ? null : sdf.format((Date) value), isSelected, hasFocus, viewRow, column);
        return c;
    }

}
