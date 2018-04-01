package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.IntergerTextField;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.CounterHistory;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class CounterHistoryForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private IntergerTextField txtConsumption;

    private ExCombo<Contract> comboContact;

    private Mode mode;

    private List<Contract> contractBySubscriberId;
    
    public CounterHistoryForm(Long subscriberId, Mode mode) throws Exception{
	super();
	this.mode = mode;
	
	contractBySubscriberId = ServiceProvider.get().getSubscriberService()
		.getCounterContractBySubscriberId(subscriberId);
	
	if(contractBySubscriberId==null || contractBySubscriberId.isEmpty()) {
	    throw new Exception(message("counterHistory.noContract"));
	}
	
	initializePanel();
	
    }

    @Override
    protected void initComponents() {

	txtConsumption = new IntergerTextField();
	
	comboContact = new ExCombo<>(contractBySubscriberId);
	comboContact.setEnabled(contractBySubscriberId.size() > 1);
	comboContact.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    if (comboContact.getValue() != null) {
			CounterHistory counterHistroy = ServiceProvider.get().getSubscriberService()
				.getCounterHistoryByContractId(comboContact.getValue().getId());
			System.out.println("Conter Histroy"+counterHistroy);
			    txtConsumption.setValueAsLong(counterHistroy!=null ? counterHistroy.getConsumption() : null);

		    }
		}

	    }
	});
	
	ComponentUtils.fireCombobBoxItemListener(comboContact);
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

		Long consumption = txtConsumption.getValueAsLong();
		if(consumption==null) {
		    MessageUtils.showWarningMessage(getOwner(), message("value.missing", message("counterHistory.form.current")));
		    return;
		}
		
		CounterHistory history = new CounterHistory();
		history.setContractId(comboContact.getValue().getId());
		history.setConsumption(consumption);

		try {
		    ServiceProvider.get().getSubscriberService().saveCounterHistory(history);
		} catch (Exception e1) {
		    MessageUtils.showWarningMessage(getOwner(), e1.getMessage());
		}
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

	if (mode == Mode.VIEW) {
	    builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose), builder.getColumnCount());
	} else {
	    builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder.getColumnCount());
	}
	return builder.getPanel();
    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

}