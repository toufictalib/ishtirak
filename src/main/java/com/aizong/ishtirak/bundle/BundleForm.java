package com.aizong.ishtirak.bundle;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.aizong.ishtirak.common.BasicPanel;
import com.aizong.ishtirak.common.ButtonFactory;
import com.aizong.ishtirak.common.DoubleTextField;
import com.aizong.ishtirak.common.Mode;
import com.aizong.ishtirak.common.ServiceProvider;
import com.aizong.ishtirak.subscriber.form.OrientationUtils;
import com.aizong.ishtirak.subscriber.form.SavingCallback;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class BundleForm extends BasicPanel {

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
	initComponetns();
	initUI();
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
	
	if(bundle instanceof MonthlyBundle) {
	    MonthlyBundle monthlyBundle = (MonthlyBundle) bundle;
	    txtName.setText(monthlyBundle.getName());
	    txtSettelementFees.setText(monthlyBundle.getSettlementFees()+"");
	    txtFees.setText(monthlyBundle.getFees()+"");
	}else {
	    SubscriptionBundle subscriptionBundle  = (SubscriptionBundle) bundle;
	    txtName.setText(subscriptionBundle.getName());
	    txtSettelementFees.setText(subscriptionBundle.getSettlementFees()+"");
	    txtCostPerKb.setText(subscriptionBundle.getCostPerKb()+"");
	    txtSubscriptionFees.setText(subscriptionBundle.getSubscriptionFees()+"");
	}

    }

    private void initComponetns() {
	txtName = new JTextField();
	txtSettelementFees = new DoubleTextField();
	txtFees = new DoubleTextField();
	txtCostPerKb = new DoubleTextField();
	txtSubscriptionFees = new DoubleTextField();
	
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

}