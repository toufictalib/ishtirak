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
import com.aizong.ishtirak.model.Address;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.MonthlyBundle;
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

	allowEdit(true);

    }

    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("contract.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("contract.form.counter"), txtCounterId);
	builder.append(message("contract.form.active"), cbActive);
	builder.append(message("contract.form.oldBundle"), txtOldCounter);
	builder.append(message("contract.form.bundle"), comboBundles);
	builder.append(message("bundle.form.settelementFees"), txtSettelementFees);
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
	
	Long oldContractId = contract == null ? null : contract.getId();
	
	Contract contract = new Contract();
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

	ServiceProvider.get().getSubscriberService().saveAndDeactivateContact(contract, txtSettelementFees.getValue(), oldContractId);
	closeWindow();

    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

}