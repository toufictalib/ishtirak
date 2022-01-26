package com.aizong.ishtirak.gui.table;

import java.awt.FlowLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.MonthYearCombo;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.jgoodies.forms.builder.DefaultFormBuilder;

@SuppressWarnings("serial")
public class CounterConsumptionSumTablePanel extends ReportTablePanel {

	public CounterConsumptionSumTablePanel(String title) {
		super(title, null);
		start();
	}

	/*
	 * @Override protected int getTotalTargetedColumn() { return 2;
	 * 
	 * }
	 */

	@Override
	protected JPanel initUI() {

		JButton btnSearch = ButtonFactory.createBtnSearch();

		MonthYearCombo monthYearCombo = new MonthYearCombo(FlowLayout.RIGHT);

		String leftToRightSpecs = "fill:p:grow";

		btnSearch.addActionListener(e -> {
			try {

				LocalDate now = LocalDate.of(monthYearCombo.getYear(), monthYearCombo.getMonth(), 6);
				DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth(now);
				ReportTableModel reportTableModel = ServiceProvider.get().getReportServiceImpl()
						.getCounterConsumptionSumByEngine(dateRange.getStartDateAsString(),
								dateRange.getEndDateAsString());

				fillTable(reportTableModel);
			} catch (Exception e1) {
				e1.printStackTrace();
				MessageUtils.showErrorMessage(getOwner(), e1.getMessage());
			}
		});

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(monthYearCombo);
		panel.add(btnSearch);

		DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, "p,p,fill:240dlu:grow,p,p");
		builder.setDefaultDialogBorder();

		builder.appendSeparator(title);

		builder.append(panel, builder.getColumnCount());

		builder.append(new JScrollPane(table), builder.getColumnCount());

		builder.append(txtRowCount, builder.getColumnCount());

		builder.append(txtTotal, builder.getColumnCount());

		return builder.getPanel();
	};

	@Override
	protected int getTotalTargetedColumn() {
		return 1;
	}

}
