package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DoubleTextField;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.IntergerTextField;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Address;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class EngineForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtName;
    private IntergerTextField txtKva;
    private DoubleTextField txtDieselPerHour;
    private ExCombo<Village> comboVillages;
    private JTextField txtRegion;
    private JTextArea txtAddress;
    private SavingCallback callback;
    private Mode mode;
    private Engine engine;

    public EngineForm(Mode mode) {
	this.mode = mode;
	initializePanel();
    }

    public EngineForm(Mode mode, SavingCallback callback) {
	this(mode);
	this.callback = callback;
    }

    public EngineForm(Mode mode, Engine engine, SavingCallback callback) {
	this(mode);
	this.engine = engine;
	this.callback = callback;
	fillData();
    }

    private void fillData() {
	if (engine == null) {
	    return;
	}

	txtName.setText(engine.getName());
	txtKva.setValue(engine.getKva());
	txtDieselPerHour.setValue(engine.getDieselConsumption());

	if (engine.getAddress() != null) {
	    comboVillages.setSelectedItem(new Village(engine.getAddress().getVillageId()));

	    txtRegion.setText(engine.getAddress().getRegion());
	    txtAddress.setText(engine.getAddress().getDetailedAddress());

	}
    }

    @Override
    protected void initComponents() {
	txtName = new JTextField();
	txtKva = new IntergerTextField();
	txtDieselPerHour = new DoubleTextField();

	comboVillages = new ExCombo<>(ServiceProvider.get().getSubscriberService().getVillages());
	txtRegion = new JTextField();
	txtAddress = ComponentUtils.createTextArea();

    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("engine.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("engine.form.name"), txtName);
	builder.append(message("engine.form.kva"), txtKva);
	builder.append(message("engine.form.dieselConsumption"), txtDieselPerHour);
	builder.appendSeparator(message("subsriber.form.address"));
	builder.append(message("subsriber.form.village"), comboVillages);
	builder.append(message("subsriber.form.region"), txtRegion);
	builder.append(message("subsriber.form.address"), txtAddress);
	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(e->btnSaveAction());
	JButton btnClose = ButtonFactory.createBtnClose();
	btnClose.addActionListener(e->closeWindow());

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

    private void btnSaveAction() {
	if (engine == null) {
	    engine = new Engine();
	}

	Optional<List<String>> validateInputs = validateInputs();
	if (validateInputs.isPresent()) {
	    MessageUtils.showWarningMessage(getOwner(), String.join("\n", validateInputs.get()));
	    return;
	}
	
	engine.setName(txtName.getText());
	engine.setKva(txtKva.getValue());
	engine.setDieselConsumption(txtDieselPerHour.getValue());

	Address address = new Address();
	address.setVillageId(comboVillages.getValue().getId());
	address.setRegion(txtRegion.getText());
	address.setDetailedAddress(txtAddress.getText());

	engine.setAddress(address);

	ServiceProvider.get().getSubscriberService().saveEngine(engine);
	closeWindow();
	if (callback != null) {
	    callback.onSuccess(engine);
	}
    }

    private Optional<List<String>> validateInputs() {
	List<String> errors = new ArrayList<>();
	if (txtName.getText().isEmpty()) {
	    errors.add(error("value.missing", "engine.form.name"));
	}
	if (comboVillages.getValue() == null) {
	    errors.add(error("value.missing", "subsriber.form.village"));
	}
	if (txtRegion.getText().isEmpty()) {
	    errors.add(error("value.missing", "subsriber.form.region"));
	}

	if (txtAddress.getText().isEmpty()) {
	    errors.add(error("value.missing", "subsriber.form.address"));
	}
	return errors.isEmpty() ? Optional.empty() : Optional.of(errors);

    }

}