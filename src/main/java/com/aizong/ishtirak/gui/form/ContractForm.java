package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Address;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.Subscriber;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class ContractForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtCounterId;
    private JCheckBox cbActive;

    private ExCombo<Engine> comboEngines;
    private ExCombo<Bundle> comboBundles;
    private ExCombo<Village> comboVillages;
    private JTextField txtRegion;
    private JTextArea txtAddress;

    private Subscriber subscriber;

    public ContractForm(Subscriber subscriber) {
	this.subscriber = subscriber;
	initializePanel();
	fillData();
    }

    private void fillData() {
	if (subscriber == null) {
	    return;
	}

    }

    @Override
    protected void initComponents() {

	txtCounterId = new JTextField();
	cbActive = new JCheckBox();
	cbActive.setSelected(true);

	comboEngines = new ExCombo<>(ServiceProvider.get().getSubscriberService().getEngines());
	comboBundles = new ExCombo<>(ServiceProvider.get().getSubscriberService().getAllBundles());
	comboVillages = new ExCombo<>(ServiceProvider.get().getSubscriberService().getVillages());
	txtRegion = new JTextField();
	txtAddress = new JTextArea(2, 1);
	txtAddress.setLineWrap(true);

	txtAddress.setBorder(UIManager.getBorder("TextField.border"));

    }

    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("contract.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("contract.form.counter"), txtCounterId);
	builder.append(message("contract.form.active"), cbActive);
	builder.append(message("contract.form.bundle"), comboBundles);
	builder.append(message("contract.form.engine"), comboEngines);
	builder.appendSeparator(message("subsriber.form.address"));
	builder.append(message("subsriber.form.village"), comboVillages);
	builder.append(message("subsriber.form.region"), txtRegion);
	builder.append(message("subsriber.form.address"), txtAddress);
	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

		Contract contract = new Contract();
		contract.setActive(cbActive.isSelected());

		Address address = new Address();
		address.setVillageId(comboVillages.getValue().getId());
		address.setRegion(txtRegion.getText());
		address.setDetailedAddress(txtAddress.getText());

		contract.setAddress(address);

		contract.setBundleId(comboBundles.getValue().getId());
		contract.setCounterId(txtCounterId.getText());
		contract.setEngineId(comboEngines.getValue().getId());
		contract.setSubscriberId(subscriber.getId());

		ServiceProvider.get().getSubscriberService().saveContract(contract);
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