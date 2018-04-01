package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Subscriber;
import com.aizong.ishtirak.model.SubscriberInformation;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class SubscriberForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtName;
    private JTextField txtFatherName;
    private JTextField txtLastName;
    private JTextField txtIdentifier;
    private ExCombo<Village> comboVillages;
    private JTextField txtRegion;
    private JTextArea txtAddress;
    private JTextField txtLandLine;
    private JTextField txtPhone1;
    private JTextField txtPhone2;
    private JTextField txtEmail;
    private SavingCallback callback;
    private Mode mode;
    private Subscriber subscriber;

    public SubscriberForm(Mode mode) {
	this.mode = mode;
	initializePanel();
    }

    public SubscriberForm(Mode mode, SavingCallback callback) {
	this(mode);
	this.callback = callback;
    }

    public SubscriberForm(Mode mode, Subscriber subscriber, SavingCallback callback) {
	this(mode);
	this.subscriber = subscriber;
	this.callback = callback;
	fillData();
    }

    private void fillData() {
	if (subscriber == null) {
	    return;
	}

	txtName.setText(subscriber.getName());
	txtFatherName.setText(subscriber.getFatherName());
	txtLastName.setText(subscriber.getLastName());
	txtIdentifier.setText(subscriber.getIdentifier());

	if (subscriber.getInformation() != null) {
	    comboVillages.setSelectedItem(new Village(subscriber.getInformation().getVillageId()));

	    txtRegion.setText(subscriber.getInformation().getRegion());
	    txtAddress.setText(subscriber.getInformation().getDetailedAddress());
	    txtAddress.setLineWrap(true);

	    txtAddress.setBorder(UIManager.getBorder("TextField.border"));

	    txtLandLine.setText(subscriber.getInformation().getLandLine());
	    txtPhone1.setText(subscriber.getInformation().getMainPhone());
	    txtPhone2.setText(subscriber.getInformation().getAlternativePhone());
	    txtEmail.setText(subscriber.getInformation().getEmail());
	}
    }

    @Override
    protected void initComponents() {
	txtName = new JTextField();
	txtFatherName = new JTextField();
	txtLastName = new JTextField();
	txtIdentifier = new JTextField();

	comboVillages = new ExCombo<>(ServiceProvider.get().getSubscriberService().getVillages());
	txtRegion = new JTextField();
	txtAddress = new JTextArea(2, 1);
	txtAddress.setLineWrap(true);

	txtAddress.setBorder(UIManager.getBorder("TextField.border"));

	txtLandLine = new JTextField();
	txtPhone1 = new JTextField();
	txtPhone2 = new JTextField();
	txtEmail = new JTextField();
    }

    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("subsriber.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("subsriber.form.name"), txtName);
	builder.append(message("subsriber.form.fatherName"), txtFatherName);
	builder.append(message("subsriber.form.lastName"), txtLastName);
	builder.append(message("subsriber.form.identifier"), txtIdentifier);
	builder.appendSeparator(message("subsriber.form.address"));
	builder.append(message("subsriber.form.village"), comboVillages);
	builder.append(message("subsriber.form.region"), txtRegion);
	builder.append(message("subsriber.form.address"), txtAddress);
	builder.append(message("subsriber.form.landLine"), txtLandLine);
	builder.append(message("subsriber.form.mainPhone"), txtPhone1);
	builder.append(message("subsriber.form.alternativePhone"), txtPhone2);
	builder.append(message("subsriber.form.email"), txtEmail);
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
    protected Optional<List<String>> validateInputs() {
	List<String> errors = new ArrayList<>();
	if (txtName.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.name"));
	}
	if (txtFatherName.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.lastName"));
	}
	if (txtPhone1.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.mainPhone"));
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
	return "right:pref, 4dlu, fill:125dlu:grow";
    }

    @Override
    protected void save() {
	if (subscriber == null) {
	    subscriber = new Subscriber();
	}

	subscriber.setName(txtName.getText());
	subscriber.setLastName(txtLastName.getText());
	subscriber.setFatherName(txtFatherName.getText());
	subscriber.setIdentifier(txtIdentifier.getText());

	SubscriberInformation information = new SubscriberInformation();
	if (subscriber != null && subscriber.getInformation() != null) {
	    information.setId(subscriber.getInformation().getId());
	}
	information.setVillageId(comboVillages.getValue().getId());
	information.setRegion(txtRegion.getText());
	information.setDetailedAddress(txtAddress.getText());
	information.setLandLine(txtLandLine.getText());
	information.setMainPhone(txtPhone1.getText());
	information.setAlternativePhone(txtPhone2.getText());
	information.setEmail(txtEmail.getText());

	subscriber.setInformation(information);

	ServiceProvider.get().getSubscriberService().saveSubscriber(subscriber);
	closeWindow();
	if (callback != null) {
	    callback.onSuccess(subscriber);
	}
	
    }

}