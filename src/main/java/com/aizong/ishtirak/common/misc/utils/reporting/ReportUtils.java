package com.aizong.ishtirak.common.misc.utils.reporting;

import java.awt.Window;
import java.awt.print.PrinterException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aizong.ishtirak.common.misc.utils.Message;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;

public class ReportUtils {
    public static void writeToExcel(JTable table, String file, String title) throws FileNotFoundException, IOException {
	file = file.trim();
	if (!file.contains(".xls")) {
	    String[] split = file.split(".");
	    file = split[0] + ".xls";
	}

	XSSFWorkbook wb = new XSSFWorkbook();
	Sheet sheet = wb.createSheet(title); // WorkSheet
	Row row = sheet.createRow(2); // Row created at line 3
	TableModel model = table.getModel(); // Table model

	Row headerRow = sheet.createRow(0); // Create row at line 0
	for (int headings = 0; headings < model.getColumnCount(); headings++) { // For
										// each
	    headerRow.createCell(headings).setCellValue(model.getColumnName(headings));// Write
										       // column
										       // name
	}

	for (int rows = 0; rows < model.getRowCount(); rows++) { // For each
								 // table row
	    for (int cols = 0; cols < table.getColumnCount(); cols++) { // For
									// table
		Object valueAt = model.getValueAt(rows, cols);
		if (valueAt != null) {
		    row.createCell(cols).setCellValue(valueAt.toString()); // Write
		}
		// value
	    }

	    // Set the row to the next one in the sequence
	    row = sheet.createRow((rows + 3));
	}
	wb.write(new FileOutputStream(file));// Save the file
    }

    public static void printTable(JTable table, Window owner) {
	printTable(table, owner, message("title"), message("print.footer"), true, true, true);
    }

    public static void printTable(JTable table, Window owner, String headerTxt, String footerTxt, boolean fitWidth,
	    boolean showPrintDialog, boolean interactive) {
	/* Fetch printing properties from the GUI components */

	MessageFormat header = null;

	/* if we should print a header */
	if (headerTxt != null) {
	    /* create a MessageFormat around the header text */
	    header = new MessageFormat(headerTxt);
	}

	MessageFormat footer = null;

	/* if we should print a footer */
	if (footerTxt != null) {
	    /* create a MessageFormat around the footer text */
	    footer = new MessageFormat(footerTxt);
	}

	/* determine the print mode */
	JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;

	try {
	    /* print the table */
	    PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
	    attr.add(MediaSizeName.ISO_A4);
	    attr.add(OrientationRequested.LANDSCAPE);
	    boolean complete = table.print(mode, header, footer, showPrintDialog, attr, interactive, null);

	    /* if printing completes */

	    MessageUtils.showInfoMessage(owner, message("printing.title.result"),
		    message(complete ? "printing.done" : "printing.cancelled"));
	} catch (PrinterException pe) {
	    MessageUtils.showErrorMessage(owner, message("printing.failure", pe.getMessage()));
	}
    }

    private static String message(String key, Object... params) {
	Message message = ServiceProvider.get().getMessage();
	return message.getMessage(key, params);
    }
}
