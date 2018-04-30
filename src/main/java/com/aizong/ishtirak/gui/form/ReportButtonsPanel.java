package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.ProgressBar;
import com.aizong.ishtirak.common.misc.utils.ProgressBar.ProgressBarListener;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.gui.table.ReportTablePanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

@SuppressWarnings("serial")
public class ReportButtonsPanel extends BasicForm {

    public ReportButtonsPanel() {
	super();
	initializePanel();
    }

    @Override
    protected void initComponents() {

    }

    public abstract class ShowMyReport {

	abstract ReportTableModel getReportTableModel();

	 int getTotalTargetedColumn() {
	     return -1;
	 }
	
	public ShowMyReport() {
	}

	public void start(String title) {

	    ProgressBar.execute(new ProgressBarListener<ReportTableModel>() {

		@Override
		public ReportTableModel onBackground() throws Exception {
		    return getReportTableModel();
		}

		@Override
		public void onDone(ReportTableModel reportTableModel) {
		    openWindow(title, new ReportTablePanel(title, reportTableModel) {

			@Override
			protected int getTotalTargetedColumn() {
			    return  ShowMyReport.this.getTotalTargetedColumn();
			}
		    });

		}
	    }, ReportButtonsPanel.this.getOwner());
	}
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder = new DefaultFormBuilder(new FormLayout(getLayoutSpecs()));
	builder.setDefaultDialogBorder();
	JideButton btnMonthlyReports = button(message("reports.subscirption.contract"), "subreport.png");
	btnMonthlyReports.addActionListener(e -> {

	    new ShowMyReport() {

		@Override
		ReportTableModel getReportTableModel() {
		    return ServiceProvider.get().getReportServiceImpl().getSubscriptionsIncomeReport();
		}

		@Override
		int getTotalTargetedColumn() {
		   return 5;
		}
	    }.start(e.getActionCommand());

	});

	JideButton btnMonthlyExpenses = button(message("reports.subscirption.expenses"), "subreport.png");
	btnMonthlyExpenses.addActionListener(e -> {

	    new ShowMyReport() {

		@Override
		ReportTableModel getReportTableModel() {
		    DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth();
		    return ServiceProvider.get().getReportServiceImpl().getExpenses(dateRange.getStartDateAsString(),
			    dateRange.getEndDateAsString());
		}

		@Override
		int getTotalTargetedColumn() {
		    return 2;
		}
	    }.start(e.getActionCommand());;

	});

	JideButton btnIshtirakReport = button(message("reports.subscirption.activeIshtirak"), "subreport.png");
	btnIshtirakReport.addActionListener(e -> {
	    new ShowMyReport() {

		@Override
		ReportTableModel getReportTableModel() {
		    return ServiceProvider.get().getReportServiceImpl().getActiveIshtirakInfo(new ArrayList<>());
		}
	    }.start(e.getActionCommand());;

	});

	JideButton btnIshtirakReportWithoutReceipts = button(
		message("reports.subscirption.activeIshtirakWithoutReceipts"), "subreport.png");
	btnIshtirakReportWithoutReceipts.addActionListener(e -> {

	    new ShowMyReport() {

		@Override
		ReportTableModel getReportTableModel() {
		    return ServiceProvider.get().getReportServiceImpl().getActiveIshtirakWithoutReceipts();
		}
	    }.start(e.getActionCommand());;

	});

	builder.append(btnMonthlyReports);
	builder.append(btnMonthlyExpenses);
	builder.append(btnIshtirakReport);
	builder.append(btnIshtirakReportWithoutReceipts);
	return builder.getPanel();
    }

    private void openWindow(String text, JPanel component) {
	MainFrame.openWindow(SwingUtilities.getWindowAncestor(ReportButtonsPanel.this), text, component);
    }

    private JideButton button(String text, String imagePath) {
	return (JideButton) MainFrame.button(text, imagePath);
    }

    @Override
    protected String getLayoutSpecs() {
	return "p,30dlu,p";
    }

}
