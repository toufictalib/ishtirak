package com.aizong.ishtirak.gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.DoubleAdder;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.aizong.ishtirak.LoginForm;
import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.bean.Tuple;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.Constant;
import com.aizong.ishtirak.common.misc.utils.CurrencyUtils;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.PrintUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.jgoodies.forms.builder.DefaultFormBuilder;

public class ResultForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Map<String, List<Tuple<String, Double>>> result;

    private String title;

    DateRange startEndDateOfCurrentMonth;

    public ResultForm(String title) {

	this.title = title;
	startEndDateOfCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	result = ServiceProvider.get().getSubscriberService().getResult(
		startEndDateOfCurrentMonth.getStartDateAsString(), startEndDateOfCurrentMonth.getEndDateAsString());

	initializePanel();
    }

    @Override
    protected void initComponents() {
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.setDefaultDialogBorder();

	JPanel panel = new JPanel(new BorderLayout(10, 10));

	JPanel incomePanel = createIncomeExpenses(message("income"), getIncomeResult(), TransactionType.class, false);

	JPanel expensesPanel = createIncomeExpenses(message("expenses"), getExpensesResult(), ExpensesType.class, true);

	JPanel panels = new JPanel(new BorderLayout());
	panels.add(expensesPanel, BorderLayout.WEST);
	panels.add(incomePanel, BorderLayout.EAST);

	panel.add(createTopPanel(), BorderLayout.PAGE_START);

	panel.add(panels, BorderLayout.CENTER);

	DefaultFormBuilder subBuilder = BasicForm.createBuilder("fill:p:grow", "p,p");
	builder.append(panel);

	subBuilder.append(panel);
	JButton btnPrint = ButtonFactory.createBtnPrint();
	btnPrint.addActionListener(print());

	JButton btnExportExcel = ButtonFactory.createBtnExportExcel();
	btnExportExcel.addActionListener(e -> {
	    DefaultTableModel model = table();

	    JFileChooser fc = new JFileChooser();
	    fc.setSelectedFile(new File(title + ".xls"));
	    int returnVal = fc.showSaveDialog(ResultForm.this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		File file = fc.getSelectedFile();
		try {
		    writeToExcel(model, file.getPath(), title);
		    MessageUtils.showInfoMessage(ResultForm.this, message("file.exported.success"));
		} catch (Exception e1) {
		    e1.printStackTrace();
		    MessageUtils.showErrorMessage(ResultForm.this, message("file.exported.error"));
		}
	    }

	});
	JPanel panel2 = new JPanel();
	panel2.add(btnPrint);
	panel2.add(btnExportExcel);

	subBuilder.append(panel2);
	subBuilder.append(panel);

	return subBuilder.getPanel();
    }

    private DefaultTableModel table() {
	Double profit = getProfit();

	Vector<Object[]> rows = new Vector<>();
	addRow(rows, message("income"), "");

	DoubleAdder total = new DoubleAdder();
	getIncomeResult().forEach(e -> {
	    addRow(rows, enumMessage(e.getKey(), TransactionType.class), e.getValue());
	    total.add(e.getValue());
	}

	);
	addRow(rows, message("total"), total.sum());
	addRow(rows, "", "");
	addRow(rows, message("expenses"), "");
	total.reset();
	getExpensesResult().forEach(e -> {
	    addRow(rows, enumMessage(e.getKey(), ExpensesType.class), e.getValue());
	    total.add(e.getValue());
	});
	addRow(rows, message("total"), total.sum());
	addRow(rows, "", "");
	addRow(rows, message(profit >= 0 ? "profit" : "loss"), profit);
	Vector<Vector<Object>> data = new Vector<>();
	for (Object[] row : rows) {
	    data.add(new Vector<>(Arrays.asList(row)));

	}

	DefaultTableModel model = new DefaultTableModel(data, new Vector<>(Arrays.asList("", "")));
	return model;

    }

    private void addRow(Vector<Object[]> rows, Object label, Object value) {
	rows.add(new Object[] { label, value });
    }

    private JPanel createTopPanel() {
	Double profit = getProfit();

	int padding = 10;

	boolean profitability = profit >= 0;

	JLabel lbl = new JLabel(toBold(message(profitability ? "profit" : "loss")), SwingConstants.CENTER);
	lbl.setBorder(ComponentUtils.emptyBorder(padding));
	lbl.setFont(new Font("Tahoma", Font.PLAIN, 24));
	lbl.setForeground(Color.DARK_GRAY);

	JLabel lblAmount = new JLabel(CurrencyUtils.formatCurrency(LoginForm.getCurrentLocale(), profit),
		SwingConstants.CENTER);
	lblAmount.setBorder(ComponentUtils.emptyBorder(padding));
	lblAmount.setFont(new Font("Tahoma", Font.PLAIN, 20));
	lblAmount.setForeground(profitability ? Color.decode("#32CD32") : Color.red);

	DefaultFormBuilder builder = BasicForm.createBuilder("fill:p:grow");
	builder.append(lbl);
	builder.append(lblAmount);

	JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
	panelTop.setBorder(BorderFactory.createLineBorder(Color.black));
	panelTop.add(builder.getPanel());
	return panelTop;
    }

    private JPanel createIncomeExpenses(String title, List<Tuple<String, Double>> result, Class<?> enumClass,
	    boolean expenses) {

	DefaultFormBuilder builder = BasicForm.createBuilder("p,20dlu,fill:150dlu:grow");

	Double amount = 0d;

	builder.appendSeparator(toBold(title));
	for (Tuple<String, Double> tuple : result) {
	    addLine(builder, enumMessage(tuple.getKey(), enumClass), tuple.getValue());
	    amount += tuple.getValue();
	}

	JPanel panel = new JPanel(new BorderLayout());
	JPanel panel2 = builder.getPanel();
	// panel2.add(getChartPanel(expenses));
	int padding = 15;
	panel2.setBorder(new CompoundBorder(BorderFactory.createEtchedBorder(),
		BorderFactory.createEmptyBorder(padding, padding, padding, padding)));
	panel.add(panel2, BorderLayout.CENTER);
	panel.add(createTotalPanel(amount), BorderLayout.SOUTH);
	return panel;
    }

    private JPanel createTotalPanel(Double amount) {

	JLabel txtField = new JLabel(CurrencyUtils.formatCurrency(LoginForm.getCurrentLocale(), amount) + "");
	txtField.setFont(new Font("Tahoma", Font.PLAIN, 24));
	txtField.setForeground(Color.DARK_GRAY);

	JLabel jLabel = new JLabel(toBold(message("total") + " : "));
	jLabel.setFont(new Font("Arial", Font.PLAIN, 22));
	jLabel.setForeground(Color.BLUE);
	JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel1.add(jLabel);
	panel1.add(txtField);

	return panel1;
    }

    private PieDataset createDataset() {
	DefaultPieDataset dataset = new DefaultPieDataset();
	getIncomeResult().forEach(e -> {
	    dataset.setValue(enumMessage(e.getKey(), TransactionType.class), e.getValue());
	});
	return dataset;
    }

    private Double getProfit() {
	Double income = 0d;
	Double expenses = 0d;

	List<Tuple<String, Double>> values = getIncomeResult();
	if (values != null) {
	    income = values.stream().mapToDouble(e -> e.getValue()).sum();
	}

	values = getExpensesResult();
	if (values != null) {
	    expenses = values.stream().mapToDouble(e -> e.getValue()).sum();
	}

	return income - expenses;

    }

    private String toBold(String text) {
	return "<html><b>" + text + "</b></html>";
    }

    private void addLine(DefaultFormBuilder builder, String label, Double value) {

	JTextField txtField = new JTextField(CurrencyUtils.formatCurrency(LoginForm.getCurrentLocale(), value) + "");
	txtField.setEditable(false);
	builder.append(label, txtField);
    }

    @SuppressWarnings("unused")
    private void addTotal(DefaultFormBuilder builder, String label, Double value) {
	JLabel txtField = new JLabel(CurrencyUtils.formatCurrency(LoginForm.getCurrentLocale(), value) + "");
	txtField.setFont(new Font("Tahoma", Font.PLAIN, 24));
	txtField.setForeground(Color.DARK_GRAY);
	builder.append(label, txtField);
    }

    private List<Tuple<String, Double>> getIncomeResult() {
	return result.get(Constant.INCOME) == null ? new ArrayList<>() : result.get(Constant.INCOME);
    }

    private List<Tuple<String, Double>> getExpensesResult() {
	return result.get(Constant.EXPENSES) == null ? new ArrayList<>() : result.get(Constant.EXPENSES);
    }

    private ActionListener print() {
	return e -> {
	    try {
		boolean printed = PrintUtils.printLandscapeComponent(ResultForm.this);
		if (printed) {
		    MessageUtils.showInfoMessage(getOwner(), message("printing.done"));
		}
	    } catch (PrinterException e1) {
		MessageUtils.showErrorMessage(getOwner(), message("printing.failure", e1.getMessage()));
	    }
	};
    }

    private PieDataset createExpensesDataset() {
	DefaultPieDataset dataset = new DefaultPieDataset();
	getExpensesResult().forEach(e -> {
	    dataset.setValue(enumMessage(e.getKey(), ExpensesType.class), e.getValue());
	});
	return dataset;
    }

    private JFreeChart createChart(PieDataset dataset) {
	JFreeChart chart = ChartFactory.createPieChart(message("income"), // chart
		// title
		dataset, // data
		true, // include legend
		true, LoginForm.getCurrentLocale());
	PiePlot plot = (PiePlot) chart.getPlot();
	plot.setSimpleLabels(true);

	PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{0}: {1} ({2})",
		NumberFormat.getCurrencyInstance(LoginForm.getCurrentLocale()), new DecimalFormat("0%"));
	plot.setLabelGenerator(gen);
	return chart;
    }

    private JPanel getChartPanel(boolean expenses) {
	ChartPanel chartPanel = new ChartPanel(createChart(expenses ? createExpensesDataset() : createDataset()));
	chartPanel.setPreferredSize(new Dimension(250, 250));
	return chartPanel;
    }

    @Override
    protected String getLayoutSpecs() {
	return "fill:p:grow";
    }

    public void writeToExcel(TableModel model, String file, String title) throws FileNotFoundException, IOException {
	file = file.trim();
	if (!file.contains(".xls")) {
	    String[] split = file.split(".");
	    file = split[0] + ".xls";
	}

	XSSFWorkbook wb = new XSSFWorkbook();

	XSSFFont font = wb.createFont();
	font.setFontHeightInPoints((short) 10);
	font.setFontName("Arial");
	font.setColor(IndexedColors.WHITE.getIndex());
	font.setBold(true);
	font.setItalic(false);

	Sheet sheet = wb.createSheet(title);
	sheet.setRightToLeft(LoginForm.isRtl());// WorkSheet
	Row row = sheet.createRow(2); // Row created at line 3

	c(wb);

	Row rowHeader = sheet.createRow(0);
	Cell cell = rowHeader.createCell(0);
	cell.setCellStyle(style(wb, font));

	Formatter formatter = new Formatter(System.out, LoginForm.getCurrentLocale());
	cell.setCellValue("نتيجة المدخول والنفقات لشهر " + DateUtil.getMonthName(DateUtil.getCurrentMonth()));
	formatter.close();
	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

	for (int rows = 0; rows < model.getRowCount(); rows++) { // For each
								 // table row
	    for (int cols = 0; cols < model.getColumnCount(); cols++) { // For
									// table
		Object valueAt = model.getValueAt(rows, cols);
		if (valueAt != null) {
		    Cell createCell = row.createCell(cols);
		    createCell.setCellValue(valueAt.toString());
		    if (rows == 0) {
			createCell.setCellStyle(style(wb, font));
		    } else {

			row.createCell(cols).setCellValue(valueAt.toString()); // Write
		    }
		}

		// value
	    }

	    // Set the row to the next one in the sequence
	    row = sheet.createRow((rows + 3));
	}
	wb.write(new FileOutputStream(file));// Save the file
    }

    private void c(XSSFWorkbook wb) {

	XSSFFont defaultFont = wb.createFont();
	defaultFont.setFontHeightInPoints((short) 10);
	defaultFont.setFontName("Arial");
	defaultFont.setColor(IndexedColors.BLACK.getIndex());
	defaultFont.setBold(false);
	defaultFont.setItalic(false);

    }

    private CellStyle style(Workbook wb, XSSFFont font) {
	CellStyle style = wb.createCellStyle();
	style.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setFont(font);
	return style;
    }

}