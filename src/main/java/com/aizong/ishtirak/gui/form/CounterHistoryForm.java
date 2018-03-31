package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.IntergerTextField;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.CounterHistory;
import com.aizong.ishtirak.model.Subscriber;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class CounterHistoryForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private IntergerTextField txtConsumption;

    private ExCombo<Contract> comboContact;

    private Subscriber subscriber;

    public CounterHistoryForm(Subscriber subscriber) {
	this.subscriber = subscriber;
	initializePanel();
	fillData();
    }

    private void fillData() {
	if (subscriber == null) {
	    return;
	}

    }

    Map<Long, String> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    protected void initComponents() {

	txtConsumption = new IntergerTextField();

	List<Contract> contractBySubscriberId = ServiceProvider.get().getSubscriberService()
		.getCounterContractBySubscriberId(subscriber.getId());
	for (Contract contract : contractBySubscriberId) {
	    Bundle bundle = ServiceProvider.get().getSubscriberService().getBundleById(contract.getBundleId());
	    if (bundle != null) {
		map.put(contract.getId(), bundle.getName() + ":" + contract.getCounterId());
	    }
	}
	comboContact = new ExCombo<>(contractBySubscriberId);
	comboContact.setRenderer(new DefaultListCellRenderer() {
	    /**
	     * 
	     */
	    private static final long serialVersionUID = -2015108228646251293L;

	    @Override
	    public Component getListCellRendererComponent(JList<?> arg0, Object arg1, int arg2, boolean arg3,
		    boolean arg4) {
		// TODO Auto-generated method stub
		Component component = super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
		if (component instanceof JLabel) {
		    JLabel lbl = (JLabel) component;
		    Contract contract = (Contract) arg1;
		    if (contract != null) {
			String string = map.get(contract.getId());
			if (string != null) {
			    lbl.setText(string);
			}
		    }

		}
		return component;
	    }
	});
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("counterHistory.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("counterHistory.form.subscription"), comboContact);
	builder.append(message("counterHistory.form.current"), txtConsumption);

	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

		CounterHistory history = new CounterHistory();
		history.setContractId(comboContact.getValue().getId());
		history.setConsumption(Long.valueOf(txtConsumption.getValue()));

		ServiceProvider.get().getSubscriberService().saveConsumptionHistory(history);
		closeWindow();
	    }
	});
	JButton btnClose = ButtonFactory.createBtnClose();
	btnClose.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		closeWindow();

	    }
	});
	builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder.getColumnCount());
	return builder.getPanel();
    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

}