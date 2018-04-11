package com.aizong.ishtirak.gui.table;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.SearchMonthPanel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.jgoodies.forms.builder.DefaultFormBuilder;

@SuppressWarnings("serial")
public class SubscriberHistoryTablePanel extends ReportTablePanel {

    private SearchMonthPanel searchMonthPanel;
    private JTextField txtCounterId;
    private JCheckBox cbAll;
    private ExCombo<String> comboPaid;

    public SubscriberHistoryTablePanel(String title) {
	super();
	this.title = title;
	start();
    }

    @Override
    protected JPanel initUI() {

	DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth();
	searchMonthPanel = SearchMonthPanel.createDefault(dateRange.getStartDate(), dateRange.getStartDate());

	txtCounterId = new JTextField();
	txtCounterId.setPreferredSize(new Dimension(150, txtCounterId.getPreferredSize().height));
	cbAll = new JCheckBox(message("all"));
	cbAll.setSelected(true);
	cbAll.addActionListener(e -> {
	    txtCounterId.setText("");
	    txtCounterId.setEnabled(!cbAll.isSelected());
	});

	ComponentUtils.fireCheckBox(cbAll);

	JButton btnSearch = ButtonFactory.createBtnSearch();

	comboPaid = new ExCombo<>(message("all"), true,
		Arrays.asList(message("transaction.paid"), message("transaction.unpaid")));
	comboPaid.setPreferredSize(new Dimension(100,comboPaid.getPreferredSize().height));
	

	btnSearch.addActionListener(search());
	DefaultFormBuilder builder = BasicForm.createBuilder("fill:p:grow", "p,p,fill:p:grow,p");
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);
	JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel.add(searchMonthPanel);
	panel.add(new JLabel(message("contract_unique_code")));
	panel.add(txtCounterId);
	panel.add(cbAll);
	panel.add(new JLabel(message("paid")));
	panel.add(comboPaid);
	panel.add(btnSearch);

	builder.append(panel, builder.getColumnCount());
	// builder.append(txtFE, 3);

	JScrollPane scrollPane = new JScrollPane(table);
	// scrollPane.setPreferredSize(ComponentUtils.getDimension(60, 60));
	builder.append(scrollPane, builder.getColumnCount());

	builder.append(txtRowCount, builder.getColumnCount());

	// builder.append("المجموع", txtTotal);

	return builder.getPanel();
    }

    private ActionListener search() {
	return e -> {
	    try {

		String paymentStatus = comboPaid.getValue();
		Boolean paid = null;
		if (paymentStatus != null) {
		    paid = message("transaction.paid").equals(paymentStatus) ? true : false;
		}

		LocalDate fromLocaleDate = LocalDate.of(searchMonthPanel.getSelectedFromYear(),
			searchMonthPanel.getSelectedFromMonth(), 5);
		DateRange fromDateRange = DateUtil.getStartEndDateOfCurrentMonth(fromLocaleDate);

		LocalDate toLocaleDate = LocalDate.of(searchMonthPanel.getSelectedToYear(),
			searchMonthPanel.getSelectedToMonth(), 5);
		DateRange toDateRange = DateUtil.getStartEndDateOfCurrentMonth(toLocaleDate);
		
		
		
		ReportTableModel reportTableModel = ServiceProvider.get().getReportServiceImpl()
			.getContractHistoryPerContractOrALl(
				txtCounterId.getText().trim().isEmpty() ? null : txtCounterId.getText().trim(),
				fromDateRange.getStartDateAsString(), toDateRange.getEndDateAsString(), paid);
		fillTable(reportTableModel);
	    } catch (Exception e1) {
		e1.printStackTrace();
		MessageUtils.showErrorMessage(getOwner(), e1.getMessage());
	    }
	};
    };

    @Override
    public ReportTableModel getReportTableModel() {
	return null;
    }

}
