package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.ButtonFactory;
import com.aizong.ishtirak.common.misc.Mode;
import com.aizong.ishtirak.common.misc.ServiceProvider;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class VillageForm extends BasicForm implements RefreshTableInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtName;

    private SavingCallback callback;
    private Mode mode;
    private Village oldVillage;

    public VillageForm(Mode mode) {
	this.mode = mode;
	initializePanel();
    }

    public VillageForm(Mode mode, SavingCallback callback) {
	this(mode);
	this.callback = callback;
    }

    public VillageForm(Mode mode, Village village, SavingCallback callback) {
	this(mode);
	this.oldVillage = village;
	this.callback = callback;
	fillData();
    }

    private void fillData() {

	if (oldVillage != null) {
	    txtName.setText(oldVillage.getName());
	}
    }

    @Override
    protected void initComponents() {
	txtName = new JTextField();
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator("القرية");
	builder.setDefaultDialogBorder();
	builder.append("الإسم*", txtName);
	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		Village village = oldVillage == null ? new Village() : oldVillage;
		village.setName(txtName.getText());

		ServiceProvider.get().getSubscriberService().saveVillage(village);
		closeWindow();
		if (callback != null) {
		    callback.onSuccess(village);
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

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

    @Override
    public void refreshTable() {
	fillData();

    }

}