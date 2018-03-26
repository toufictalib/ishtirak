package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicPanel;
import com.aizong.ishtirak.common.form.OrientationUtils;
import com.aizong.ishtirak.common.misc.ButtonFactory;
import com.aizong.ishtirak.common.misc.ExCombo;
import com.aizong.ishtirak.common.misc.Mode;
import com.aizong.ishtirak.common.misc.ServiceProvider;
import com.aizong.ishtirak.model.Information;
import com.aizong.ishtirak.model.Subscriber;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SubscriberForm extends BasicPanel {

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
	initComponetns();
	initUI();
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

    private void initComponetns() {
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

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

		if (subscriber == null) {
		    subscriber = new Subscriber();
		}

		subscriber.setName(txtName.getText());
		subscriber.setLastName(txtLastName.getText());
		subscriber.setFatherName(txtFatherName.getText());
		subscriber.setIdentifier(txtIdentifier.getText());

		Information information = new Information();
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

}