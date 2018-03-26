package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.ButtonFactory;
import com.aizong.ishtirak.common.misc.DoubleTextField;
import com.aizong.ishtirak.common.misc.Mode;
import com.aizong.ishtirak.common.misc.ServiceProvider;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.MonthlyBundle;
import com.aizong.ishtirak.model.SubscriptionBundle;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class BundleForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtName;
    private DoubleTextField txtSettelementFees;
    private DoubleTextField txtFees;
    private DoubleTextField txtCostPerKb;
    private DoubleTextField txtSubscriptionFees;
    private SavingCallback callback;
    private Mode mode;
    private Bundle bundle;
    private boolean monthly;

    public BundleForm(Mode mode, boolean monthly) {
	this.mode = mode;
	this.monthly = monthly;
	initializePanel();
    }

    public BundleForm(Mode mode, SavingCallback callback, boolean monthly) {
	this(mode, monthly);
	this.callback = callback;
    }

    public BundleForm(Mode mode, Bundle bundle, SavingCallback callback, boolean monthly) {
	this(mode, monthly);
	this.bundle = bundle;
	this.callback = callback;
	fillData();
    }

    private void fillData() {
	if (bundle == null) {
	    return;
	}

	if (bundle instanceof MonthlyBundle) {
	    MonthlyBundle monthlyBundle = (MonthlyBundle) bundle;
	    txtName.setText(monthlyBundle.getName());
	    txtSettelementFees.setText(monthlyBundle.getSettlementFees() + "");
	    txtFees.setText(monthlyBundle.getFees() + "");
	} else {
	    SubscriptionBundle subscriptionBundle = (SubscriptionBundle) bundle;
	    txtName.setText(subscriptionBundle.getName());
	    txtSettelementFees.setText(subscriptionBundle.getSettlementFees() + "");
	    txtCostPerKb.setText(subscriptionBundle.getCostPerKb() + "");
	    txtSubscriptionFees.setText(subscriptionBundle.getSubscriptionFees() + "");
	}

    }

    @Override
    protected void initComponents() {
	txtName = new JTextField();
	txtSettelementFees = new DoubleTextField();
	txtFees = new DoubleTextField();
	txtCostPerKb = new DoubleTextField();
	txtSubscriptionFees = new DoubleTextField();

    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {

	if (monthly) {
	    builder.appendSeparator(message("bundle.form.monthly.seperator"));
	    builder.setDefaultDialogBorder();
	    builder.append(message("bundle.form.name"), txtName);
	    builder.append(message("bundle.form.settelementFees"), txtSettelementFees);
	    builder.append(message("bundle.form.monthly.value"), txtFees);
	} else {
	    builder.appendSeparator(message("bundle.form.subscription.seperator"));
	    builder.setDefaultDialogBorder();
	    builder.append(message("bundle.form.name"), txtName);
	    builder.append(message("bundle.form.settelementFees"), txtSettelementFees);
	    builder.append(message("bundle.form.subscription.costPerKb"), txtCostPerKb);
	    builder.append(message("bundle.form.subscription.subscriptionFees"), txtSubscriptionFees);
	}

	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

		if (monthly) {
		    MonthlyBundle monthlyBundle = bundle == null ? new MonthlyBundle() : (MonthlyBundle) bundle;
		    monthlyBundle.setName(txtName.getText());
		    monthlyBundle.setSettlementFees(txtSettelementFees.getValue());
		    monthlyBundle.setFees(txtFees.getValue());
		    ServiceProvider.get().getSubscriberService().saveBundle(monthlyBundle);
		} else {
		    SubscriptionBundle subscriptionBundle = bundle == null ? new SubscriptionBundle()
			    : (SubscriptionBundle) bundle;
		    subscriptionBundle.setName(txtName.getText());
		    subscriptionBundle.setSettlementFees(txtSettelementFees.getValue());
		    subscriptionBundle.setCostPerKb(txtCostPerKb.getValue());
		    subscriptionBundle.setSubscriptionFees(txtSubscriptionFees.getValue());
		    ServiceProvider.get().getSubscriberService().saveBundle(subscriptionBundle);
		}

		closeWindow();
		if (callback != null) {
		    callback.onSuccess(bundle);
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

}