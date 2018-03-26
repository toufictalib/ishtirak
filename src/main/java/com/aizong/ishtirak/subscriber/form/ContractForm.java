package com.aizong.ishtirak.subscriber.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.bundle.Bundle;
import com.aizong.ishtirak.bundle.Contract;
import com.aizong.ishtirak.common.BasicPanel;
import com.aizong.ishtirak.common.ButtonFactory;
import com.aizong.ishtirak.common.ExCombo;
import com.aizong.ishtirak.common.ServiceProvider;
import com.aizong.ishtirak.engine.Engine;
import com.aizong.ishtirak.subscriber.model.Address;
import com.aizong.ishtirak.subscriber.model.Subscriber;
import com.aizong.ishtirak.subscriber.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ContractForm extends BasicPanel {

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
	initComponetns();
	initUI();
	fillData();
    }


    private void fillData() {
	if (subscriber == null) {
	    return;
	}

    }

    private void initComponetns() {
	
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

    private void initUI() {

	FormLayout layouts = new FormLayout("pref:grow");
	DefaultFormBuilder rowBuilder = new DefaultFormBuilder(layouts, this);
	rowBuilder.setDefaultDialogBorder();

	rowBuilder.append(buildPanel());

    }

    private Component buildPanel() {
	// DefaultFormBuilder builder = new DefaultFormBuilder(new
	// FormLayout("50dlu,10dlu,fill:p:grow", "p,p,p,p"), this);
	String leftToRightSpecs = "right:pref, 4dlu, fill:100dlu:grow";
	FormLayout layout = new FormLayout(OrientationUtils.flipped(leftToRightSpecs), new RowSpec[] {});
	DefaultFormBuilder builder = new DefaultFormBuilder(layout);
	builder.setLeftToRight(false);
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

}