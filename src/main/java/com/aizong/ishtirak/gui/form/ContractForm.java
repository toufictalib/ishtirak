package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.IntergerTextField;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Address;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.MonthlyBundle;
import com.aizong.ishtirak.model.SubscriptionBundle;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class ContractForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected JTextField txtCounterId;
    protected JCheckBox cbActive;

    protected ExCombo<Engine> comboEngines;
    protected ExCombo<Bundle> comboBundles;
    protected ExCombo<Village> comboVillages;
    protected JTextField txtRegion;
    protected JTextArea txtAddress;
    protected IntergerTextField txtSettelementFees;

    protected Long subscriberId;
    protected Contract contract;

    protected Mode mode = Mode.NEW;

    public ContractForm(Long subscriberId) {
	this.subscriberId = subscriberId;
	initializePanel();
	fillData();
    }

    public ContractForm(Contract contract, Mode mode) {
	this.contract = contract;
	this.subscriberId = contract.getSubscriberId();
	this.mode = mode;
	initializePanel();
	fillData();
    }

    protected void fillData() {
	if (contract == null) {
	    return;
	}

	txtCounterId.setText(contract.getCounterId());
	cbActive.setSelected(contract.isActive());

	Bundle bundle = ServiceProvider.get().getSubscriberService().getBundleById(contract.getBundleId());
	comboBundles.setSelectedItem(bundle);
	comboEngines.setSelectedItem(new Engine(contract.getEngineId()));

	if (contract.getAddress() != null) {
	    comboVillages.setSelectedItem(new Village(contract.getAddress().getVillageId()));
	    txtRegion.setText(contract.getAddress().getRegion());
	    txtAddress.setText(contract.getAddress().getDetailedAddress());

	}
    }

    @Override
    protected void initComponents() {

	txtCounterId = new JTextField();
	txtSettelementFees = new IntergerTextField();
	
	cbActive = new JCheckBox();
	cbActive.setSelected(true);

	comboEngines = new ExCombo<>(ServiceProvider.get().getSubscriberService().getEngines());
	comboBundles = new ExCombo<>(ServiceProvider.get().getSubscriberService().getAllBundles());
	comboBundles.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    enableTxtCounter(comboBundles.getValue() instanceof SubscriptionBundle);
		    txtSettelementFees.setValue(comboBundles.getValue().getSettlementFees());
		}

	    }
	});
	comboVillages = new ExCombo<>(ServiceProvider.get().getSubscriberService().getVillages());
	txtRegion = new JTextField();
	txtAddress = new JTextArea(2, 1);
	txtAddress.setLineWrap(true);

	txtAddress.setBorder(UIManager.getBorder("TextField.border"));

	enableTxtCounter(comboBundles.getValue() instanceof SubscriptionBundle);

	if (mode == Mode.UPDATE) {
	    allowEdit(false);
	}
	
	ComponentUtils.fireCombobBoxItemListener(comboBundles);
    }

    protected void allowEdit(boolean allow) {
	comboBundles.setEnabled(allow);
	txtCounterId.setEditable(allow);
	cbActive.setEnabled(allow);
    }

    private void enableTxtCounter(boolean isCounterSubscription) {
	txtCounterId.setEnabled(isCounterSubscription);
    }

    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("contract.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("contract.form.counter"), txtCounterId);
	builder.append(message("contract.form.active"), cbActive);
	builder.append(message("contract.form.bundle"), comboBundles);
	if(mode==Mode.NEW) {
	    builder.append(message("bundle.form.settelementFees"), txtSettelementFees);
	}
	builder.append(message("contract.form.engine"), comboEngines);
	builder.appendSeparator(message("subsriber.form.address"));
	builder.append(message("subsriber.form.village"), comboVillages);
	builder.append(message("subsriber.form.region"), txtRegion);
	builder.append(message("subsriber.form.address"), txtAddress);
	builder.appendSeparator();

	JButton btnSave = btnSave();
	JButton btnClose = btnClose();

	if (mode == Mode.VIEW) {
	    builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose), builder.getColumnCount());
	} else {
	    builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder.getColumnCount());
	}

	return builder.getPanel();
    }

    @Override
    protected void save() {
	Optional<List<String>> validateInputs = validateInputs();
	if (validateInputs.isPresent()) {
	    MessageUtils.showWarningMessage(getOwner(), String.join("\n", validateInputs.get()));
	    return;
	}

	contract = contract == null ? new Contract() : contract;
	contract.setActive(cbActive.isSelected());

	Address address = new Address();
	address.setVillageId(comboVillages.getValue().getId());
	address.setRegion(txtRegion.getText());
	address.setDetailedAddress(txtAddress.getText());

	contract.setAddress(address);

	contract.setBundleId(comboBundles.getValue().getId());
	contract.setCounterId((comboBundles.getValue() instanceof MonthlyBundle) ? comboBundles.getValue().getName()
		: txtCounterId.getText());
	contract.setEngineId(comboEngines.getValue().getId());
	contract.setSubscriberId(subscriberId);

	ServiceProvider.get().getSubscriberService().saveContract(contract, txtSettelementFees.getValue());
	closeWindow();

    }

    @Override
    protected Optional<List<String>> validateInputs() {
	List<String> errors = new ArrayList<>();

	if (comboBundles.getValue() == null) {
	    errors.add(errorPerfix("contract.form.bundle"));
	}

	if ((comboBundles.getValue() instanceof SubscriptionBundle) && txtCounterId.getText().isEmpty()) {
	    errors.add(errorPerfix("contract.form.counter"));
	}

	if (comboEngines.getValue() == null) {
	    errors.add(errorPerfix("contract.form.engine"));
	}

	if (comboVillages.getValue() == null) {
	    errors.add(errorPerfix("subsriber.form.village"));
	}
	if (txtRegion.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.region"));
	}

	if (txtAddress.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.address"));
	}
	return errors.isEmpty() ? Optional.empty() : Optional.of(errors);

    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

}