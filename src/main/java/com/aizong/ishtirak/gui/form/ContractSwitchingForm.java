package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.Contract;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class ContractSwitchingForm extends ContractForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtOldCounter;

    public ContractSwitchingForm(Contract contract, Mode mode) {
	super(contract, mode);
    }

    protected void fillData() {
	super.fillData();
	if (contract == null) {
	    return;
	}

	Bundle bundle = comboBundles.getValue();
	if (bundle != null) {
	    txtOldCounter.setText(bundle.getName());
	    comboBundles.removeItem(bundle);
	    if (comboBundles.getItemCount() > 0) {
		comboBundles.setSelectedIndex(0);
	    }
	    ComponentUtils.fireCombobBoxItemListener(comboBundles);
	}
    }

    @Override
    protected void initComponents() {
	super.initComponents();
	txtOldCounter = new JTextField();
	txtOldCounter.setEditable(false);
	txtCounterId.setEditable(false);
	comboEngines.setEnabled(false);
	allowEdit(true);
    }

    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("contract.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("contract.form.counter"), txtCounterId);
	builder.append(message("contract.form.oldBundle"), txtOldCounter);
	builder.append(message("contract.form.bundle"), comboBundles);
	builder.append(message("bundle.form.settelementFees"), txtSettelementFees);
	builder.append(message("contract.form.engine"), comboEngines);
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
	
	Long oldContractId = contract == null ? null : contract.getId();
	

	Contract newContract = new Contract();
	newContract.setActive(contract.isActive());
	newContract.setContractUniqueCode(contract.getContractUniqueCode());
	newContract.setAddress(contract.getAddress());
	newContract.setBundleId(comboBundles.getValue().getId());
	newContract.setEngineId(contract.getEngineId());
	newContract.setSubscriberId(subscriberId);

	ServiceProvider.get().getSubscriberService().saveAndDeactivateContact(newContract, txtSettelementFees.getValue(), oldContractId);
	closeWindow();

    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

}