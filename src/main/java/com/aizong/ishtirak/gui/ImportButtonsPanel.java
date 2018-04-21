package com.aizong.ishtirak.gui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.math.NumberUtils;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.MonthYearCombo;
import com.aizong.ishtirak.common.misc.utils.ProgressBar;
import com.aizong.ishtirak.common.misc.utils.ProgressBar.ProgressBarListener;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

@SuppressWarnings("serial")
public class ImportButtonsPanel extends BasicForm {

    private static final String COMMA = ",";

    private final static java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(ImportButtonsPanel.class.getSimpleName());;
    
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
	btnMonthlyReports.addActionListener(e ->importData(true,message("export.counterValues")));

	JideButton btnMonthlyExpenses = button(message("import.payement"), "import.png");
	btnMonthlyExpenses.addActionListener(e -> importData(false,message("export.payement")));

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

    @SuppressWarnings("deprecation")
    private void importData(boolean importCounter, String defaultFile) {

	JFileChooser fc = new JFileChooser();
	fc.setSelectedFile(new File(defaultFile + ".csv"));
	fc.setFileFilter(new FileNameExtensionFilter("Csv Only", "csv"));
	int returnVal = fc.showSaveDialog(ImportButtonsPanel.this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();

	    MonthYearCombo monthYearCombo = new MonthYearCombo();
	    Object[] options = { message("import.counter.button"),message("import.counter.close") };
	    int answer = JOptionPane.showOptionDialog(null, monthYearCombo, message("import.title"),
	        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, 
	        options, options[0]);

	    
	    if (answer == JOptionPane.YES_OPTION) {
		List<String> readAllLines;
		try {
		    readAllLines = Files.readAllLines(Paths.get(file.getPath()));
		    readAllLines = readAllLines.size() > 0 ? readAllLines.subList(1, readAllLines.size())
			    : readAllLines;

		    String message = "";
		    if (importCounter) {

			Map<String, Long> values = new HashMap<>();
			for (String line : readAllLines) {
			    String[] split = line.split(COMMA);
			    if (split.length == 3) {
				String uniqueCode = split[0];
				String amount = split[2];

				if (NumberUtils.isNumber(amount)) {
				    values.put(uniqueCode, Long.valueOf(amount));
				}
			    }
			}
			
			LocalDate now = LocalDate.of(monthYearCombo.getYear(), monthYearCombo.getMonth(), LocalDate.now().getDayOfMonth());
			ServiceProvider.get().getSubscriberService().updateCounters(values,
				now);
			message = "import.counter.done";
		    } else {
			Map<String, Boolean> values = new HashMap<>();
			for (String line : readAllLines) {
			    String[] split = line.split(COMMA);
			    if (split.length == 3) {
				String uniqueCode = split[0];
				String amount = split[2];

				if (amount != null) {
				    values.put(uniqueCode, Boolean.valueOf(amount));
				}
			    }
			}
			DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth();
			ServiceProvider.get().getSubscriberService().updatePaid(values,
				dateRange.getStartDateAsString(), dateRange.getEndDateAsString());
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

	    @Override
	    public void onDone(ReportTableModel reportTableModel) {
		JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(new File(getFileName(btn)));
		int returnVal = fc.showSaveDialog(ImportButtonsPanel.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();

		    try {
			
			List<String> data = new ArrayList<>();
			
			List<String> header = Arrays.asList(message("contract_unique_code"),message("fullName"),message(counterInput ? "counter.current" : "paid"));
			data.add(String.join(COMMA, header));
			for (Object[] row : reportTableModel.getRows()) {
			    List<String> rows = new ArrayList<>();
			    for (Object o : row) {
				rows.add(o != null ? o.toString() : "");
			    }
			    data.add(String.join(COMMA, rows));
			}
			Files.write(Paths.get(file.getPath()), data);
			MessageUtils.showInfoMessage(ImportButtonsPanel.this.getOwner(), message("export.done", btn.getText()));
			
			
		    } catch (IOException e) {
			e.printStackTrace();
		    }

		}
	    }

	};
    }
    
    private String getFileName(JideButton btn) {
	return btn.getActionCommand() + ".csv";
    }
    
    private JideButton button(String text, String imagePath) {
	return (JideButton) MainFrame.button(text, imagePath);
    }

    @Override
    protected String getLayoutSpecs() {
	return "p,30dlu,p";
    }

}
