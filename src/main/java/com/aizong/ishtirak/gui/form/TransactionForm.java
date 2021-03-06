package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DoubleTextField;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.MonthYearCombo;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
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
    private MonthYearCombo monthYearCombo;

    private Long contractId;
    private TransactionType transactionType;

    public TransactionForm(Long contractId, TransactionType transactionType) {
	this.contractId = contractId;
	this.transactionType = transactionType;
	initializePanel();
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("transaction.title"));
	builder.setDefaultDialogBorder();
	builder.append(message("codeId"), txtCode);
	builder.append(message("transaction.amount"), txtAmount);
	builder.append(message("subscriptionMonth"), monthYearCombo);
	builder.appendSeparator();

	JButton btnSave = btnSave();
	JButton btnClose = btnClose();

	builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder.getColumnCount());
	return builder.getPanel();
    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:p:grow";
    }

    @Override
    protected void save() {
	Transaction transaction = new Transaction();
	transaction.setAmount(txtAmount.getValue());
	transaction.setContractId(contractId);
	LocalDate now = LocalDate.of(monthYearCombo.getYear(), monthYearCombo.getMonth(), DateUtil.START_MONTH);
	transaction.setInsertDate(DateUtil.fromLocalDate(now));
	transaction.setTransactionType(transactionType);
	try {
	    ServiceProvider.get().getSubscriberService().saveTransaction(now, transaction);
	    MessageUtils.showInfoMessage(TransactionForm.this, message("transaction.input.success"));
	    closeWindow();
	} catch (Exception e) {
	    MessageUtils.showErrorMessage(TransactionForm.this, e.getMessage());
	}
    };

    @Override
    protected Optional<List<String>> validateInputs() {
	List<String> errors = new ArrayList<String>();

	if (txtAmount.getValue() == null) {
	    errors.add(errorPerfix("transaction.amount"));
	}

	return errors.isEmpty() ? Optional.empty() : Optional.of(errors);
    }

    @Override
    protected void initComponents() {
	txtCode = new JTextField();
	txtCode.setEditable(false);
	txtAmount = new DoubleTextField();
	monthYearCombo = new MonthYearCombo(FlowLayout.LEFT);

    }

}