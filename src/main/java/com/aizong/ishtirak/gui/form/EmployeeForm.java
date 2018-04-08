package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.IntergerTextField;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.EmployeeInformation;
import com.aizong.ishtirak.model.EmployeeType;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class EmployeeForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtName;
    private JTextField txtFatherName;
    private JTextField txtLastName;
    private JTextField txtIdentifier;
    private IntergerTextField txtSalary;
    private JCheckBox cbActive;
    private ExCombo<EmployeeType> comboEmployeeTypes;
    private ExCombo<Village> comboVillages;
    private JTextField txtRegion;
    private JTextArea txtAddress;
    private JTextField txtLandLine;
    private JTextField txtPhone1;
    private JTextField txtPhone2;
    private JTextField txtEmail;
    private SavingCallback callback;
    private Mode mode;
    private Employee employee;

    public EmployeeForm(Mode mode) {
	this.mode = mode;
	initializePanel();
    }

    public EmployeeForm(Mode mode, SavingCallback callback) {
	this(mode);
	this.callback = callback;
    }

    public EmployeeForm(Mode mode, Employee employee, SavingCallback callback) {
	this(mode);
	this.employee = employee;
	this.callback = callback;
	fillData();
    }

    private void fillData() {
	if (employee == null) {
	    return;
	}

	txtName.setText(employee.getName());
	txtFatherName.setText(employee.getFatherName());
	txtLastName.setText(employee.getLastName());
	txtIdentifier.setText(employee.getIdentifier());

	txtSalary.setText(String.valueOf(employee.getSalary()));
	cbActive.setSelected(employee.isActive());
	comboEmployeeTypes.setSelectedItem(new EmployeeType(employee.getEmployeeTypeId().getId()));
	
	if (employee.getInformation() != null) {
	    comboVillages.setSelectedItem(new Village(employee.getInformation().getVillageId()));

	    txtRegion.setText(employee.getInformation().getRegion());
	    txtAddress.setText(employee.getInformation().getDetailedAddress());
	    txtAddress.setLineWrap(true);

	    txtAddress.setBorder(UIManager.getBorder("TextField.border"));
	    txtAddress.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

	    txtLandLine.setText(employee.getInformation().getLandLine());
	    txtPhone1.setText(employee.getInformation().getMainPhone());
	    txtPhone2.setText(employee.getInformation().getAlternativePhone());
	    txtEmail.setText(employee.getInformation().getEmail());
	}
    }

    @Override
    protected void initComponents() {
	txtName = new JTextField();
	txtFatherName = new JTextField();
	txtLastName = new JTextField();
	txtIdentifier = new JTextField();
	
	txtSalary=  new IntergerTextField();
	cbActive = new JCheckBox();
	cbActive.setSelected(true);
	comboEmployeeTypes = new ExCombo<>(ServiceProvider.get().getSubscriberService().getEmployeeTypes());

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

    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("subsriber.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("subsriber.form.name"), txtName);
	builder.append(message("subsriber.form.fatherName"), txtFatherName);
	builder.append(message("subsriber.form.lastName"), txtLastName);
	builder.append(message("subsriber.form.identifier"), txtIdentifier);
	builder.append(message("employee.form.salary"),txtSalary);
	builder.append(message("employee.form.active"),cbActive);
	builder.append(message("employee.form.employeeType"),comboEmployeeTypes);
	/*builder.appendSeparator(message("subsriber.form.address"));
	builder.append(message("subsriber.form.village"), comboVillages);
	builder.append(message("subsriber.form.region"), txtRegion);
	builder.append(message("subsriber.form.address"), txtAddress);
	builder.append(message("subsriber.form.landLine"), txtLandLine);
	builder.append(message("subsriber.form.mainPhone"), txtPhone1);
	builder.append(message("subsriber.form.alternativePhone"), txtPhone2);
	builder.append(message("subsriber.form.email"), txtEmail);*/
	builder.appendSeparator();

	JButton btnSave = btnSave();
	
	JButton btnClose = btnClose();

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
    protected Optional<List<String>> validateInputs() {
	List<String> errors = new ArrayList<>();
	
	if (txtName.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.name"));
	}
	if (txtLastName.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.lastName"));
	}
	
	if (txtSalary.getValue()==null) {
	    errors.add(errorPerfix("employee.form.salary"));
	}
	
	if (comboEmployeeTypes.getValue() ==null) {
	    errors.add(errorPerfix("employee.form.employeeType"));
	}
	
	/*if (comboVillages.getValue() == null) {
	    errors.add(errorPerfix("subsriber.form.village"));
	}
	if (txtRegion.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.region"));
	}

	if (txtAddress.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.address"));
	}
	if (txtPhone1.getText().isEmpty()) {
	    errors.add(errorPerfix("subsriber.form.mainPhone"));
	}*/
	return errors.isEmpty() ? Optional.empty() : Optional.of(errors);

    }

    @Override
    protected void save() {
	if (employee == null) {
	    employee = new Employee();
	}

	employee.setName(txtName.getText());
	employee.setLastName(txtLastName.getText());
	employee.setFatherName(txtFatherName.getText());
	employee.setIdentifier(txtIdentifier.getText());
	employee.setSalary(txtSalary.getValue());
	employee.setActive(cbActive.isSelected());
	employee.setEmployeeTypeId(comboEmployeeTypes.getValue());

	EmployeeInformation information = new EmployeeInformation();
	if (employee != null && employee.getInformation() != null) {
	    information.setId(employee.getInformation().getId());
	}
	information.setVillageId(comboVillages.getValue()!=null ? comboVillages.getValue().getId() : null);
	information.setRegion(txtRegion.getText());
	information.setDetailedAddress(txtAddress.getText());
	information.setLandLine(txtLandLine.getText());
	information.setMainPhone(txtPhone1.getText());
	information.setAlternativePhone(txtPhone2.getText());
	information.setEmail(txtEmail.getText());

	employee.setInformation(information);

	ServiceProvider.get().getSubscriberService().saveEmployee(employee);
	closeWindow();
	if (callback != null) {
	    callback.onSuccess(employee);
	}

    }
}