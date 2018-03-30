package com.aizong.ishtirak.gui.form;

import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Address;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;

public class AddressForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1169384636970868332L;
    private ExCombo<Village> comboVillages;
    private JTextField txtRegion;
    private JTextArea txtAddress;

    private Address address;

    public AddressForm(Address address) {
	super();
	this.address = address;
	initializePanel();
	fillData();
    }

    private void fillData() {
	if (address == null) {
	    return;
	}
	comboVillages.setSelectedItem(new Village(address.getVillageId()));

	txtRegion.setText(address.getRegion());
	txtAddress.setText(address.getDetailedAddress());

    }

    @Override
    protected void initComponents() {
	comboVillages = new ExCombo<>(ServiceProvider.get().getSubscriberService().getVillages());
	txtRegion = new JTextField();
	txtAddress = new JTextArea(2, 1);
	txtAddress.setLineWrap(true);

	txtAddress.setBorder(UIManager.getBorder("TextField.border"));

    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.setDefaultDialogBorder();
	builder.appendSeparator(message("subsriber.form.address"));
	builder.append(message("subsriber.form.village"), comboVillages);
	builder.append(message("subsriber.form.region"), txtRegion);
	builder.append(message("subsriber.form.address"), txtAddress);
	return builder.getPanel();
    }

    public Address getAddress() {
	Address address = new Address();
	address.setVillageId(comboVillages.getValue().getId());
	address.setRegion(txtRegion.getText());
	address.setDetailedAddress(txtAddress.getText());
	return address;
    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

}
