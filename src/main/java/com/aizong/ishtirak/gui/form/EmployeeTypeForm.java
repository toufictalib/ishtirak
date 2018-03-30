package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.EmployeeType;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class EmployeeTypeForm extends BasicForm implements RefreshTableInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtName;

    private SavingCallback callback;
    private Mode mode;
    private EmployeeType oldEmployeeType;

    public EmployeeTypeForm(Mode mode) {
	this.mode = mode;
	initializePanel();
    }

    public EmployeeTypeForm(Mode mode, SavingCallback callback) {
	this(mode);
	this.callback = callback;
    }

    public EmployeeTypeForm(Mode mode, EmployeeType employeeType, SavingCallback callback) {
	this(mode);
	this.oldEmployeeType = employeeType;
	this.callback = callback;
	fillData();
    }

    private void fillData() {
	if (oldEmployeeType != null) {
	    txtName.setText(oldEmployeeType.getName());
	}
    }

    @Override
    protected void initComponents() {
	txtName = new JTextField();
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("employeeType.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("employeeType.form.name"), txtName);
	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		EmployeeType employeeType = oldEmployeeType == null ? new EmployeeType() : oldEmployeeType;
		employeeType.setName(txtName.getText());

		ServiceProvider.get().getSubscriberService().saveEmployeeType(employeeType);
		closeWindow();
		if (callback != null) {
		    callback.onSuccess(employeeType);
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