package com.aizong.ishtirak.gui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.math.NumberUtils;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.MySwingWorker;
import com.aizong.ishtirak.common.misc.utils.ProgressAction;
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

    @SuppressWarnings("deprecation")
    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder = new DefaultFormBuilder(new FormLayout(getLayoutSpecs()));
	builder.setDefaultDialogBorder();
	JideButton btnMonthlyReports = button(message("import.counterValues"), "import.png");
	btnMonthlyReports.addActionListener(e -> {
	    importData(true);
	});

	JideButton btnMonthlyExpenses = button(message("import.payement"), "import.png");
	btnMonthlyExpenses.addActionListener(e -> importData(false));

	JideButton btnIshtirakReport = button(message("export.counterValues"), "export.png");
	btnIshtirakReport.addActionListener(e -> {

	    MySwingWorker.execute(export(btnIshtirakReport, true));
	});

	JideButton btnIshtirakReportWithoutReceipts = button(message("export.payement"), "export.png");
	btnIshtirakReportWithoutReceipts.addActionListener(e -> {
	    MySwingWorker.execute(export(btnIshtirakReportWithoutReceipts, false));

	});

	builder.append(btnMonthlyReports);
	builder.append(btnMonthlyExpenses);
	// builder.append(btnCounterHistory);
	builder.append(btnIshtirakReport);
	builder.append(btnIshtirakReportWithoutReceipts);
	return builder.getPanel();
    }

    @SuppressWarnings("deprecation")
private void importData(boolean importCounter) {

	JFileChooser fc = new JFileChooser();
	fc.setFileFilter(new FileNameExtensionFilter("Csv Only", "csv"));
	int returnVal = fc.showSaveDialog(ImportButtonsPanel.this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	    List<String> readAllLines;
	    try {
		readAllLines = Files.readAllLines(Paths.get(file.getPath()));
		readAllLines = readAllLines.size() > 0 ? readAllLines.subList(1, readAllLines.size()) : readAllLines;
		
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
		    DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth();
		    ServiceProvider.get().getSubscriberService().updateCounters(values,
			    dateRange.getStartDateAsString(), dateRange.getEndDateAsString());
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
		    ServiceProvider.get().getSubscriberService().updatePaid(values, dateRange.getStartDateAsString(),
			    dateRange.getEndDateAsString());
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

    private ProgressAction<ReportTableModel> export(JideButton btn, boolean counterInput) {
	return new ProgressAction<ReportTableModel>() {

	    @Override
	    public ReportTableModel action() {
		return ServiceProvider.get().getReportServiceImpl().getExportedFiles(counterInput);
	    }

	    @Override
	    public void success(ReportTableModel reportTableModel) {
		JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(new File(btn.getActionCommand() + ".csv"));
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

	    @Override
	    public void failure(Exception e) {

	    }
	};
    }

    private JideButton button(String text, String imagePath) {
	return (JideButton) MainFrame.button(text, imagePath);
    }

    @Override
    protected String getLayoutSpecs() {
	return "p,30dlu,p";
    }

}
