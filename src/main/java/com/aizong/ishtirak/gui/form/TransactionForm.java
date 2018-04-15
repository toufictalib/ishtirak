package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DoubleTextField;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.VoidProgressAction;
import com.aizong.ishtirak.common.misc.utils.VoidSwingWorker;
import com.aizong.ishtirak.gui.table.service.Response;
import com.aizong.ishtirak.model.Transaction;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class TransactionForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtCode;
    private DoubleTextField txtAmount;

    private Transaction transaction;

   private  Response response;
    public TransactionForm(Transaction transaction, Response response) {
	this.transaction = transaction;
	this.response =  response;
	if(transaction==null) {
	    throw new IllegalArgumentException("Transaction must not be null");
	}
	initializePanel();
    }
    

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("transaction.title"));
	builder.setDefaultDialogBorder();
	builder.append(message("codeId"), txtCode);
	builder.append(message("transaction.amount"), txtAmount);
	builder.appendSeparator();

	JButton btnSave = btnSave();
	JButton btnClose = btnClose();

	builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder.getColumnCount());
	return builder.getPanel();
    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

    @Override
    protected void save() {
	VoidSwingWorker.execute(new VoidProgressAction() {
	    
	    @Override
	    public void success() {
		
		closeWindow();
		MessageUtils.showInfoMessage(TransactionForm.this.getOwner(), message("modification.success", message("transaction.amount")));
		response.success(null);
	    }
	    
	    @Override
	   public void apply() {
		
		transaction.setAmount(txtAmount.getValue());
		ServiceProvider.get().getSubscriberService().updateTransaction(transaction);
	    }
	});
    };

    @Override
    protected Optional<List<String>> validateInputs() {
	List<String> errors = new ArrayList<String>();

	if (txtAmount.getValue() == null) {
	    errors.add(message("transaction.amount"));
	}

	return errors.isEmpty() ? Optional.empty() : Optional.of(errors);
    }

    @Override
    protected void initComponents() {
	txtCode  = new JTextField();
	txtCode.setEditable(false);
	txtAmount = new DoubleTextField();
	
	
	txtCode.setText(String.valueOf(transaction.getId()));
	txtAmount.setValue(transaction.getAmount());
    }

}