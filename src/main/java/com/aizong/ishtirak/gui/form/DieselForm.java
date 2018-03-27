package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import com.aizong.ishtirak.bean.MaintenanceType;
import com.aizong.ishtirak.common.misc.ButtonFactory;
import com.aizong.ishtirak.common.misc.DoubleTextField;
import com.aizong.ishtirak.common.misc.ServiceProvider;
import com.aizong.ishtirak.model.DieselLog;
import com.aizong.ishtirak.model.MaintenaceLog;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class DieselForm extends MaintenaceForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DoubleTextField txtDieselQuantity;

    public DieselForm() {
	super();
	initializePanel();
    }

    @Override
    protected void initComponents() {
	super.initComponents();
	txtDieselQuantity = new DoubleTextField();
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {

	// disable combo maintenace type and set default value of combo to
	// Diesel
	comboMaintenaceTypes.setEnabled(false);
	SearchResult searchResult = new SearchResult();
	searchResult.type = MaintenanceType.DIESEL;
	comboMaintenaceTypes.setSelectedItem(searchResult);

	builder.appendSeparator(message("maintenance.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("maintenance.form.name"), txtDesc);
	builder.append(message("maintenance.form.dieselAmount"), txtDieselQuantity);
	builder.append(message("maintenance.form.amount"), txtAmount);
	builder.append(message("maintenance.form.maintenaceType"), comboMaintenaceTypes);
	builder.append(message("maintenance.form.engines"), comboEngines);

	JScrollPane scrollPane = new JScrollPane(txtNote);
	scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	builder.append(message("maintenance.form.note"), scrollPane);
	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		MaintenaceLog maintenaceLog = new MaintenaceLog();
		maintenaceLog.setDesc(txtDesc.getText());
		maintenaceLog.setAmount(txtAmount.getValue());
		maintenaceLog.setMaintenanceType(comboMaintenaceTypes.getValue().type);
		maintenaceLog.setEngineId(comboEngines.getValue() != null ? comboEngines.getValue().getId() : null);
		maintenaceLog.setNote(txtNote.getText().trim());

		DieselLog dieselLog = new DieselLog();
		dieselLog.setAmount(txtAmount.getValue());
		dieselLog.setDescription(txtDesc.getText());
		dieselLog.setDieselAmount(txtDieselQuantity.getValue());
		dieselLog.setMaintenanceLog(maintenaceLog.getId());
		
		ServiceProvider.get().getSubscriberService().saveDieselLog(maintenaceLog, dieselLog);
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