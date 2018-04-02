package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DoubleTextField;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.OutExpensesLog;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class OutExpensesForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected JTextField txtDesc;
    protected DoubleTextField txtAmount;
    protected JTextArea txtNote;
    private ExCombo<Employee> comboEmployees;

    private SavingCallback callback;
    private Mode mode = Mode.NEW;
    private OutExpensesLog expensesLog;

    public OutExpensesForm() {
	super();
	initializePanel();
    }

    public OutExpensesForm(Mode mode, OutExpensesLog expensesLog, SavingCallback callback) {
	this.mode = mode;
	this.callback = callback;
	this.expensesLog = expensesLog;
	initializePanel();
	fillData();
    }

    @Override
    protected void initComponents() {
	txtDesc = new JTextField();
	txtAmount = new DoubleTextField();

	txtNote = new JTextArea();
	txtNote.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	txtNote.setBorder(UIManager.getBorder("TextField.border"));
	txtNote.setLineWrap(true);
	txtNote.setRows(4);

    }

    private void reset() {
	txtDesc.setText("");
	txtAmount.setText("");

	if (comboEmployees.getItemCount() > 0) {
	    comboEmployees.setSelectedIndex(0);
	}

    }

    private void fillData() {
	if (expensesLog == null) {
	    return;
	}

	txtDesc.setText(expensesLog.getDesc());
	txtAmount.setText(String.valueOf(expensesLog.getAmount()));
	txtNote.setText(expensesLog.getNote());
	if (expensesLog.getEmployeeId() != null) {
	    if (comboEmployees == null) {
		comboEmployees = new ExCombo<>(ServiceProvider.get().getSubscriberService().getActiveEmployees());
	    }
	    comboEmployees.setSelectedItem(new Employee(expensesLog.getEmployeeId()));
	}
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.setDefaultDialogBorder();
	builder.appendSeparator(message("maintenance.form.seperator"));
	builder.append(message("maintenance.form.name"), txtDesc);

	if (comboEmployees == null) {
	    comboEmployees = new ExCombo<>(ServiceProvider.get().getSubscriberService().getActiveEmployees());
	}
	builder.append(message("maintenance.form.employee"), comboEmployees);
	builder.append(message("maintenance.form.amount"), txtAmount);

	JScrollPane scrollPane = new JScrollPane(txtNote);
	scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	builder.append(message("maintenance.form.note"), scrollPane);
	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(e -> save(false));
	JButton btnSaveAndNew = ButtonFactory.createBtnSaveAndNew();
	btnSaveAndNew.addActionListener(e -> save(true));
	btnSaveAndNew.setActionCommand("saveAndNew");

	JButton btnClose = ButtonFactory.createBtnClose();
	btnClose.addActionListener(e -> closeWindow());

	if (mode == Mode.VIEW) {
	    builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose), builder.getColumnCount());
	} else {
	    builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSaveAndNew, btnSave),
		    builder.getColumnCount());
	}

	return builder.getPanel();
    }

    private void save(boolean saveAndNew) {
	Optional<List<String>> validateInputs = validateInputs();
	if (validateInputs.isPresent()) {
	    MessageUtils.showWarningMessage(getOwner(), String.join("\n", validateInputs.get()));
	    return;
	}

	OutExpensesLog maintenaceLog = expensesLog == null ? new OutExpensesLog() : expensesLog;
	maintenaceLog.setDesc(txtDesc.getText());
	maintenaceLog.setNote(txtNote.getText().trim());
	maintenaceLog.setAmount(txtAmount.getValue());
	maintenaceLog.setEmployeeId(comboEmployees.getValue().getId());

	ServiceProvider.get().getSubscriberService().saveOutExpenses(maintenaceLog);

	if (!saveAndNew) {
	    closeWindow();

	} else {
	    reset();
	}
	if (callback != null) {
	    callback.onSuccess(maintenaceLog);
	}

    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

    @Override
    protected Optional<List<String>> validateInputs() {
	List<String> errors = new ArrayList<>();

	if (txtDesc.getText().isEmpty()) {
	    errors.add(errorPerfix("maintenance.form.name"));
	}

	if (comboEmployees.getValue() == null) {
	    errors.add(errorPerfix("maintenance.form.dieselAmount"));
	}
	if (txtAmount.getValue() == null) {
	    errors.add(errorPerfix("maintenance.form.amount"));
	}

	return errors.isEmpty() ? Optional.empty() : Optional.of(errors);
    }

}