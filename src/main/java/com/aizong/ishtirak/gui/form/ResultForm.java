package com.aizong.ishtirak.gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.DoubleAdder;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
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
import com.aizong.ishtirak.common.misc.utils.MySwingWorker;
import com.aizong.ishtirak.common.misc.utils.PrintUtils;
import com.aizong.ishtirak.common.misc.utils.ProgressAction;
import com.aizong.ishtirak.common.misc.utils.SearchMonthPanel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.jgoodies.forms.builder.DefaultFormBuilder;

public class ResultForm extends BasicForm implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private SearchMonthPanel searchMonthPanel;

    private JButton btnSearch;

    private String title;

    private JPanel panel;

    private Map<String, List<Tuple<String, Double>>> result;

    private Double profit = null;

    public ResultForm(String title) {

	this.title = title;

	initializePanel();
    }

    @Override
    protected void initComponents() {
	btnSearch = ButtonFactory.createBtnSearch();
	btnSearch.addActionListener(this);

	DateRange dataRange = DateUtil.getStartEndDateOfCurrentMonth();
	// because we need to show only previous month that effevely between 5
	// perv month
	// and 5 current month
	searchMonthPanel = SearchMonthPanel.createDefault(dataRange.getStartDate(), dataRange.getStartDate());

    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.setDefaultDialogBorder();

	panel = new JPanel(new BorderLayout(10, 10));

	DefaultFormBuilder subBuilder = BasicForm.createBuilder("fill:p:grow", "p,p");

	JButton btnPrint = ButtonFactory.createBtnPrint();
	btnPrint.addActionListener(print());

	JButton btnExportExcel = ButtonFactory.createBtnExportExcel();
	btnExportExcel.addActionListener(exportAction());

	JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel2.add(searchMonthPanel);
	panel2.add(btnSearch);
	panel2.add(btnPrint);
	panel2.add(btnExportExcel);

	subBuilder.append(panel2);
	subBuilder.append(panel);

	return subBuilder.getPanel();
    }

    private ActionListener exportAction() {
	return e -> {

	    JFileChooser fc = new JFileChooser();
	    fc.setSelectedFile(new File(title + ".xls"));
	    int returnVal = fc.showSaveDialog(ResultForm.this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		File file = fc.getSelectedFile();
		try {
		     writeToExcel(file.getPath(), title);
		    MessageUtils.showInfoMessage(ResultForm.this, message("file.exported.success"));
		} catch (Exception e1) {
		    e1.printStackTrace();
		    MessageUtils.showErrorMessage(ResultForm.this, message("file.exported.error"));
		}
	    }
	};
    }

    private JPanel createTopPanel(Double profit) {

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

	DefaultFormBuilder builder = BasicForm.createBuilder("p,20dlu,fill:140dlu:grow");

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

    private PieDataset createDataset(List<Tuple<String, Double>> list) {
	DefaultPieDataset dataset = new DefaultPieDataset();
	list.forEach(e -> {
	    dataset.setValue(enumMessage(e.getKey(), TransactionType.class), e.getValue());
	});
	return dataset;
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

    private PieDataset createExpensesDataset(List<Tuple<String, Double>> list) {
	DefaultPieDataset dataset = new DefaultPieDataset();
	list.forEach(e -> {
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

    @SuppressWarnings("unused")
    private JPanel getChartPanel(boolean expenses, List<Tuple<String, Double>> list) {
	ChartPanel chartPanel = new ChartPanel(
		createChart(expenses ? createExpensesDataset(list) : createDataset(list)));
	chartPanel.setPreferredSize(new Dimension(250, 250));
	return chartPanel;
    }

    @Override
    protected String getLayoutSpecs() {
	return "fill:p:grow";
    }

    public void writeToExcel(String file, String title) throws FileNotFoundException, IOException {
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
	cell.setCellValue("نتيجة المدخول والنفقات لشهر " + getMonthesLabel());
	formatter.close();
	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));


	 Cell createCell = row.createCell(0);
	 createCell.setCellValue(message("income"));
	 createCell.setCellStyle(style(wb, font));
	 
	 sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 1));
	 
	 int i=3;
	 Row rowValues = sheet.createRow(i++);
	
	DoubleAdder total = new DoubleAdder();
	
	for (Tuple<String, Double> e : getIncomeResult()) {
	    Cell cellz = rowValues.createCell(0);
	    cellz.setCellValue(enumMessage(e.getKey(), TransactionType.class));
	    
	    cellz = rowValues.createCell(1);
	    
	    currencyFormat(wb, cellz);
	    cellz.setCellValue(e.getValue());
	    
	    rowValues = sheet.createRow(i++);
	    total.add(e.getValue()==null ? 0d:e.getValue());
	}
	
	Cell createCell2 = row.createCell(3);
	createCell2.setCellValue(message("expenses"));
	createCell2.setCellStyle(style(wb, font));
	
	sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 3, 4));
	
	DoubleAdder total2 = new DoubleAdder();
	
	i=3;
	rowValues = sheet.getRow(i++);
	for (Tuple<String, Double> e : getExpensesResult()) {
	    Cell cellz = rowValues.createCell(3);
	    cellz.setCellValue(enumMessage(e.getKey(), ExpensesType.class));
	    
	    cellz = rowValues.createCell(4);
	    currencyFormat(wb, cellz);
	    cellz.setCellValue(e.getValue());
	    
	    rowValues = sheet.getRow(i);
	    if(rowValues==null) {
		rowValues = sheet.createRow(i);
	    }
	    i++;
	    total2.add(e.getValue()==null ? 0d:e.getValue());
	}
	
	int totalRow = Math.max(getIncomeResult().size(), getExpensesResult().size())+3;
	Row createRow = sheet.createRow(totalRow);
	
	final Cell incomeLabelCell = createRow.createCell(0);
	incomeLabelCell.setCellStyle(style(wb, font));
	incomeLabelCell.setCellValue(message("total"));
	final Cell incomeTotal = createRow.createCell(1);
	currencyFormat(wb, incomeTotal);
	incomeTotal.setCellValue(total.doubleValue());
	final Cell expensesLabelCell = createRow.createCell(3);
	expensesLabelCell.setCellStyle(style(wb, font));
	expensesLabelCell.setCellValue(message("total"));
	final Cell expensesTotal = createRow.createCell(4);
	currencyFormat(wb, expensesTotal);
	expensesTotal.setCellValue(total2.doubleValue());
	
	sheet.addMergedRegion(new CellRangeAddress(totalRow+2, totalRow+2, 0, 4));
	final Cell resultCell1 = sheet.createRow(totalRow+2).createCell(0);
	resultCell1.setCellStyle(style(wb, font));
	resultCell1.setCellValue(message(profit>=0 ? "profit" : "loss"));
	sheet.addMergedRegion(new CellRangeAddress(totalRow+3, totalRow+3, 0, 4));
	final Cell resultCell2 = sheet.createRow(totalRow+3).createCell(0);
	
	XSSFDataFormat cf = wb.createDataFormat();
	XSSFCellStyle currencyCellStyle = wb.createCellStyle();
	currencyCellStyle.setDataFormat(cf.getFormat("#,##0"));
	
	resultCell2.setCellStyle(style(wb, font));
	resultCell2.getCellStyle().setDataFormat(cf.getFormat("#,##0"));
	resultCell2.setCellValue(profit);
	
        
	wb.write(new FileOutputStream(file));// Save the file
    }

    private void currencyFormat(XSSFWorkbook wb, Cell cell) {
	XSSFDataFormat cf = wb.createDataFormat();
	XSSFCellStyle currencyCellStyle = wb.createCellStyle();
	currencyCellStyle.setDataFormat(cf.getFormat("#,##0"));
	cell.setCellStyle(currencyCellStyle);
    }

    private String getMonthesLabel() {
	if (searchMonthPanel.getSelectedToMonth() != searchMonthPanel.getSelectedFromMonth()) {
	    return searchMonthPanel.getFromMonthLabel() + "-" + searchMonthPanel.getToMonthLabel();
	}
	return searchMonthPanel.getFromMonthLabel();
    }

    private void c(XSSFWorkbook wb) {

	XSSFFont defaultFont = wb.createFont();
	defaultFont.setFontHeightInPoints((short) 10);
	defaultFont.setFontName("Arial");
	defaultFont.setColor(IndexedColors.LIGHT_GREEN.getIndex());
	defaultFont.setBold(false);
	defaultFont.setItalic(false);

    }

    private CellStyle style(Workbook wb, XSSFFont font) {
	CellStyle style = wb.createCellStyle();
	style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setFont(font);
	font.setBold(true);
	font.setFontHeight(12);
	return style;
    }

    private List<Tuple<String, Double>> getIncomeResult() {
	return result.get(Constant.INCOME) == null ? new ArrayList<>() : result.get(Constant.INCOME);
    }

    private List<Tuple<String, Double>> getExpensesResult() {
	return result.get(Constant.EXPENSES) == null ? new ArrayList<>() : result.get(Constant.EXPENSES);
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
    
    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == btnSearch) {
	    profit = null;
	    MySwingWorker.execute(new ProgressAction<Map<String, List<Tuple<String, Double>>>>() {

		@Override
		public Map<String, List<Tuple<String, Double>>> action() {
		    LocalDate fromLocaleDate = LocalDate.of(searchMonthPanel.getSelectedFromYear(),
			    searchMonthPanel.getSelectedFromMonth(), 6);
		    DateRange fromDateRange = DateUtil.getStartEndDateOfCurrentMonth(fromLocaleDate);

		    LocalDate toLocaleDate = LocalDate.of(searchMonthPanel.getSelectedToYear(),
			    searchMonthPanel.getSelectedToMonth(), 6);
		    DateRange toDateRange = DateUtil.getStartEndDateOfCurrentMonth(toLocaleDate);

		    return ServiceProvider.get().getSubscriberService().getResult(fromDateRange.getStartDateAsString(),
			    toDateRange.getEndDateAsString());
		}

		@Override
		public void success(Map<String, List<Tuple<String, Double>>> results) {

		    result = results;
		    panel.removeAll();
		    JPanel incomePanel = createIncomeExpenses(message("income"), getIncomeResult(),
			    TransactionType.class, false);

		    JPanel expensesPanel = createIncomeExpenses(message("expenses"), getExpensesResult(),
			    ExpensesType.class, true);

		    JPanel panels = new JPanel(new BorderLayout());
		    panels.add(expensesPanel, BorderLayout.WEST);
		    panels.add(incomePanel, BorderLayout.EAST);

		    panel.add(createTopPanel(getProfit()), BorderLayout.PAGE_START);

		    panel.add(panels, BorderLayout.CENTER);

		    profit = getProfit();

		    panel.revalidate();
		    panel.repaint();

		    if (getOwner() != null) {
			WindowUtils.applyRtl(getOwner());
			getOwner().pack();
		    }
		}

		

		@Override
		public void failure(Exception e) {

		}
	    });
	}

    }

}