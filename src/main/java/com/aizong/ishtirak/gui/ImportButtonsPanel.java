package com.aizong.ishtirak.gui;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.MonthYearCombo;
import com.aizong.ishtirak.common.misc.utils.ProgressBar;
import com.aizong.ishtirak.common.misc.utils.ProgressBar.ProgressBarListener;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.reporting.ReportUtils;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

@SuppressWarnings("serial")
public class ImportButtonsPanel extends BasicForm {

    private final static java.util.logging.Logger LOG = java.util.logging.Logger
	    .getLogger(ImportButtonsPanel.class.getSimpleName());;

    public ImportButtonsPanel() {
	super();
	initializePanel();
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder = new DefaultFormBuilder(new FormLayout(getLayoutSpecs()));
	builder.setDefaultDialogBorder();
	JideButton btnMonthlyReports = button(message("import.counterValues"), "import.png");
	btnMonthlyReports.addActionListener(e -> importData(true, message("export.counterValues")));

	JideButton btnMonthlyExpenses = button(message("import.payement"), "import.png");
	btnMonthlyExpenses.addActionListener(e -> importData(false, message("export.payement")));

	JideButton btnIshtirakReport = button(message("export.counterValues"), "export.png");
	btnIshtirakReport.addActionListener(e -> {
	    ProgressBar.execute(export(btnIshtirakReport, true), ImportButtonsPanel.this);
	});

	JideButton btnIshtirakReportWithoutReceipts = button(message("export.payement"), "export.png");
	btnIshtirakReportWithoutReceipts.addActionListener(e -> {
	    ProgressBar.execute(export(btnIshtirakReportWithoutReceipts, false), ImportButtonsPanel.this);

	});

	builder.append(btnMonthlyReports);
	builder.append(btnMonthlyExpenses);
	// builder.append(btnCounterHistory);
	builder.append(btnIshtirakReport);
	builder.append(btnIshtirakReportWithoutReceipts);
	return builder.getPanel();
    }

    private void importData(boolean importCounter, String defaultFile) {

	JFileChooser fc = new JFileChooser();
	fc.setSelectedFile(new File(defaultFile + ".xls"));
	fc.setFileFilter(new FileNameExtensionFilter("Excel Only", "xls"));
	int returnVal = fc.showSaveDialog(ImportButtonsPanel.this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();

	    MonthYearCombo monthYearCombo = new MonthYearCombo();
	    Object[] options = { message("import.counter.button"), message("import.counter.close") };
	    int answer = JOptionPane.showOptionDialog(null, monthYearCombo, message("import.title"),
		    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

	    if (answer == JOptionPane.YES_OPTION) {
		try {

		    String message = "";
		    LocalDate now = LocalDate.of(monthYearCombo.getYear(), monthYearCombo.getMonth(),
			    6);

		    FileInputStream excelFile = new FileInputStream(file);
		    Workbook workbook = new XSSFWorkbook(excelFile);
		    Sheet datatypeSheet = workbook.getSheetAt(0);
		    Iterator<Row> rowIterator = datatypeSheet.iterator();

		    int ctr = 0;

		    if (importCounter) {
			Map<String, Long> values = new HashMap<>();
			while (rowIterator.hasNext()) {

			    if (ctr == 0) {
				ctr++;
				continue;
			    }
			    Row currentRow = rowIterator.next();

			    String uniqueCode = currentRow.getCell(0).getStringCellValue();
			    try {
				if (currentRow.getCell(3) != null) {
				    double amount = currentRow.getCell(3).getNumericCellValue();
				    if (amount >= 0) {
					values.put(uniqueCode, new Double(amount).longValue());
				    }
				}
			    } catch (Exception e) {
				//e.printStackTrace();

			    }

			}
			ServiceProvider.get().getSubscriberService().updateCounters(values, now);
			message = "import.counter.done";
		    } else {
			Map<String, Boolean> values = new HashMap<>();
			while (rowIterator.hasNext()) {

			    if (ctr == 0) {
				ctr++;
				continue;
			    }
			    Row currentRow = rowIterator.next();

			    String uniqueCode = currentRow.getCell(0).getStringCellValue();
			    try {

				values.put(uniqueCode, currentRow.getCell(2).getBooleanCellValue());
			    } catch (Exception e) {
				// TODO: handle exception
			    }

			}
			ServiceProvider.get().getSubscriberService().updatePaid(values, now);
			message = "import.paid.done";
		    }
		    MessageUtils.showInfoMessage(ImportButtonsPanel.this, message(message));
		} catch (Exception e1) {
		    e1.printStackTrace();
		    LOG.warning(e1.getMessage());
		    MessageUtils.showErrorMessage(ImportButtonsPanel.this, message("import.counter.failure"));
		}
	    }

	}

    }

    private ProgressBarListener<ReportTableModel> export(JideButton btn, boolean counterInput) {
	return new ProgressBarListener<ReportTableModel>() {

	    @Override
	    public ReportTableModel onBackground() {
		return ServiceProvider.get().getReportServiceImpl().getExportedFiles(counterInput);
	    }

	    @SuppressWarnings("rawtypes")
	    @Override
	    public void onDone(ReportTableModel reportTableModel) {
		JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(new File(getFileName(btn)));
		int returnVal = fc.showSaveDialog(ImportButtonsPanel.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();

		    try {

			List<String> header = null;
			if(counterInput) {
			    header =    Arrays.asList(message("contract_unique_code"), message("fullName"),
					message("counter.previous"),message("counter.current"));
			}else {
			    header = Arrays.asList(message("contract_unique_code"), message("fullName"),message("paid"));
			}

			Vector<Vector> rows = new Vector<>();
			for (Object[] row : reportTableModel.getRows()) {
			    rows.add(new Vector<>(Arrays.asList(row)));
			}

			DefaultTableModel model = new DefaultTableModel(rows, new Vector<>(header));
			ReportUtils.writeToExcel(model, file.getPath(), FilenameUtils.removeExtension(file.getName()));
			MessageUtils.showInfoMessage(ImportButtonsPanel.this.getOwner(),
				message("export.done", btn.getText()));

		    } catch (IOException e) {
			e.printStackTrace();
		    }

		}
	    }

	};
    }

    private String getFileName(JideButton btn) {
	return btn.getActionCommand() + ".xls";
    }

    private JideButton button(String text, String imagePath) {
	return (JideButton) MainFrame.button(text, imagePath);
    }

    @Override
    protected String getLayoutSpecs() {
	return "p,30dlu,p";
    }

}
