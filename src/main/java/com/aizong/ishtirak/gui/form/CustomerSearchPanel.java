package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.aizong.ishtirak.bean.Enums.SearchCustomerType;
import com.aizong.ishtirak.bean.SearchCustomerCriteria;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.model.Subscriber;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class CustomerSearchPanel extends BasicForm {

    private static final long serialVersionUID = 1L;

    private JTextField txtName;
    private JButton btnSearch;
    private ExCombo<SearchResult> comboSearchType;

    private JList<Subscriber> jList;

    public CustomerSearchPanel() {
	initializePanel();
    }

    @Override
    protected void initComponents() {

	txtName = new JTextField();
	btnSearch = ButtonFactory.createBtnSearch();
	btnSearch.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		List<Subscriber> searchSubscribers = onSearch(txtName.getText(),
			comboSearchType.getValue().customerType);
		if (searchSubscribers != null) {
		    DefaultListModel<Subscriber> model = new DefaultListModel<>();
		    searchSubscribers.forEach(r -> {
			model.addElement(r);
		    });
		    jList.setModel(model);
		}
	    }

	});

	List<SearchResult> values = new ArrayList<>();
	for (SearchCustomerType customerType : SearchCustomerType.values()) {
	    SearchResult result = new SearchResult();
	    result.customerType = customerType;
	    result.label = enumMessage(customerType.name(), SearchCustomerType.class);

	    values.add(result);
	}
	comboSearchType = new ExCombo<>(values);

	jList = new JList<>();
	jList.setVisibleRowCount(5);

    }

    static class SearchResult {
	SearchCustomerType customerType;
	String label;

	@Override
	public String toString() {
	    return label;
	}
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("customerSearch.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(txtName);
	builder.append(comboSearchType);
	builder.appendSeparator();

	JButton btnClose = ButtonFactory.createBtnClose();
	btnClose.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		closeWindow();

	    }
	});

	builder.append(ButtonBarFactory.buildRightAlignedBar(btnSearch), builder.getColumnCount());

	// splitPane.add(builder.getPanel());
	builder.appendSeparator();

	JButton btn = new JButton("إنشاء إشتراك");
	btn.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		Subscriber subscriber = jList.getSelectedValue();
		if (subscriber == null) {
		    MessageUtils.showWarningMessage(getOwner(), "مشترك ناقص", "الرجاء اختيار مشترك");
		    return;
		}
		onCreate(subscriber);

	    }
	});
	builder.appendSeparator("إختر مشتركين");

	JScrollPane pane = new JScrollPane(jList);
	pane.setPreferredSize(new Dimension(150, 300));
	builder.append(jList);
	builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btn), builder.getColumnCount());
	return builder.getPanel();
    }

    public void onCreate(Subscriber subscriber) {
	WindowUtils.createDialog(CustomerSearchPanel.this.getOwner(), "إشتراك جديد", new ContractForm(subscriber));
    }

    public List<Subscriber> onSearch(String text, SearchCustomerType customerType) {
	SearchCustomerCriteria criteria = new SearchCustomerCriteria(text);
	criteria.setCustomerType(customerType);

	return ServiceProvider.get().getSubscriberService().searchSubscribers(criteria);

    }

    @Override
    protected String getLayoutSpecs() {
	return "fill:150dlu:grow";
    }
}
