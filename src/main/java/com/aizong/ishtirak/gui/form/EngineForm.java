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
import com.aizong.ishtirak.common.misc.DoubleTextField;
import com.aizong.ishtirak.common.misc.ExCombo;
import com.aizong.ishtirak.common.misc.IntergerTextField;
import com.aizong.ishtirak.common.misc.Mode;
import com.aizong.ishtirak.common.misc.ServiceProvider;
import com.aizong.ishtirak.model.Address;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EngineForm extends BasicPanel {

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
	initComponetns();
	initUI();
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
	txtKva.setText(engine.getKva() + "");
	txtDieselPerHour.setText(engine.getDieselConsumption() + "");

	if (engine.getAddress() != null) {
	    comboVillages.setSelectedItem(new Village(engine.getAddress().getVillageId()));

	    txtRegion.setText(engine.getAddress().getRegion());
	    txtAddress.setText(engine.getAddress().getDetailedAddress());
	    txtAddress.setLineWrap(true);

	    txtAddress.setBorder(UIManager.getBorder("TextField.border"));

	}
    }

    private void initComponetns() {
	txtName = new JTextField();
	txtKva = new IntergerTextField();
	txtDieselPerHour = new DoubleTextField();

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
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

		if (engine == null) {
		    engine = new Engine();
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