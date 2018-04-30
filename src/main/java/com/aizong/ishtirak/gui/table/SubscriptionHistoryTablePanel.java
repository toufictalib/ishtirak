package com.aizong.ishtirak.gui.table;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.SearchPanel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Contract;
import com.jgoodies.forms.builder.DefaultFormBuilder;

@SuppressWarnings("serial")
public class SubscriptionHistoryTablePanel extends ReportTablePanel {

    private Long subscriberId;
    public SubscriptionHistoryTablePanel(String title, Long subscriberId) {
	super(title,null);
	this.subscriberId = subscriberId;
	start();
    }

    @Override
    protected JPanel initUI() {
	
	JButton btnSearch  = ButtonFactory.createBtnSearch();
	
	List<Contract> contracts = ServiceProvider.get().getSubscriberService()
		    .getContractBySubscriberId(subscriberId);
	
	Set<String> values = new HashSet<>();
	 Contract c = null;
	Iterator<Contract> iterator = contracts.iterator();
	while(iterator.hasNext()) {
	   c = iterator.next();
	   if(values.contains(c.getContractUniqueCode())) {
	       iterator.remove();
	   }else {
	       values.add(c.getContractUniqueCode());
	   }
	}
	ExCombo<Contract> combo = new ExCombo<>(contracts);
	combo.setPreferredSize(new Dimension(200,combo.getPreferredSize().height));
	
	DateRange startEndDateOfCurrentMonth = DateUtil.getStartEndDateOfCurrentMonth();
	SearchPanel createDefault = SearchPanel.createDefault(startEndDateOfCurrentMonth.getStartDate(),startEndDateOfCurrentMonth.getEndDate());
	
	String leftToRightSpecs = "fill:p:grow";

	btnSearch.addActionListener(e->{
	    if(combo.getValue()==null) {
		MessageUtils.showWarningMessage(getOwner(), message("contract.noOneSelected"));
		return ;
	    }
	    try {
		ReportTableModel reportTableModel = ServiceProvider.get().getReportServiceImpl().getSubscriptionsHistory(combo.getValue().getId(), createDefault.toSearchBean());
		fillTable(reportTableModel);
	    } catch (Exception e1) {
		e1.printStackTrace();
		MessageUtils.showErrorMessage(getOwner(), e1.getMessage());
	    }
	});
	JPanel panel = controlPanel(btnSearch, combo, createDefault);
	
	
	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, "p,p,fill:p:grow,p");
	builder.setDefaultDialogBorder();
	
	builder.appendSeparator(title);
	
	builder.append(panel, builder.getColumnCount());

	builder.append(new JScrollPane(table), builder.getColumnCount());

	builder.append(txtRowCount, builder.getColumnCount());

	//builder.append("المجموع", txtTotal);

	return builder.getPanel();
    }

    private JPanel controlPanel(JButton btnSearch, ExCombo<Contract> combo, SearchPanel createDefault) {
	JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel.add(createDefault);
	panel.add(combo);
	panel.add(btnSearch);
	return panel;
    };

}
