package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DoubleTextField;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.IntergerTextField;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Employee;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.ExpensesLog;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class ExpensesForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected JTextField txtDesc;
    protected DoubleTextField txtAmount;
    protected ExCombo<SearchResult> comboMaintenaceTypes;
    protected ExCombo<Engine> comboEngines;
    protected JTextArea txtNote;
    private IntergerTextField txtDieselQuantity;
    private ExCombo<Employee> comboEmployees;

    private SavingCallback callback;
    private Mode mode = Mode.NEW;
    private ExpensesLog expensesLog;

    private ExpensesType[] maintenanceTypeValues = ExpensesType.values();

    public ExpensesForm() {
	super();
	initializePanel();
    }

    public ExpensesForm(Mode mode, ExpensesLog expensesLog, SavingCallback callback) {
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
	List<SearchResult> values = new ArrayList<>();
	for (ExpensesType type : maintenanceTypeValues) {
	    values.add(new SearchResult(type, enumMessage(type.name(), ExpensesType.class)));
	}

	comboMaintenaceTypes = new ExCombo<>(values);
	comboMaintenaceTypes.addItemListener(createComboDieselListener());
	comboEngines = new ExCombo<>(true, ServiceProvider.get().getSubscriberService().getEngines());

	txtNote = new JTextArea();
	txtNote.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	txtNote.setBorder(UIManager.getBorder("TextField.border"));
	txtNote.setLineWrap(true);
	txtNote.setRows(4);

	txtDieselQuantity = new IntergerTextField();
    }

    private boolean isDiesel() {
	return comboMaintenaceTypes.getValue() != null && comboMaintenaceTypes.getValue().type == ExpensesType.DIESEL;
    }
    
    private boolean isOil() {
	return comboMaintenaceTypes.getValue() != null && comboMaintenaceTypes.getValue().type == ExpensesType.FILTER_OIL_CHANGING;
    }

    private boolean isEmployee() {
	return comboMaintenaceTypes.getValue() != null && comboMaintenaceTypes.getValue().type == ExpensesType.EMPLOYEE;
    }

    private void reset() {
	txtDesc.setText("");
	txtAmount.setText("");
	if (comboMaintenaceTypes.getItemCount() > 0) {
	    comboMaintenaceTypes.setSelectedIndex(0);
	}

	if (comboEngines.getItemCount() > 0) {
	    comboEngines.setSelectedIndex(0);
	}
	txtNote.setText("");
	txtDieselQuantity.setText("");

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
	comboMaintenaceTypes.setSelectedItem(new SearchResult(expensesLog.getMaintenanceType(), null));
	if (expensesLog.getEngineId() != null) {
	    comboEngines.setSelectedItem(new Engine(expensesLog.getEngineId()));
	}
	txtNote.setText(expensesLog.getNote());

	if (expensesLog.getDieselConsupmtion() != null) {
	    txtDieselQuantity.setText(String.valueOf(expensesLog.getDieselConsupmtion()));
	}
	
	if (isOil()) {
	    txtDieselQuantity.setValue(txtDieselQuantity.getValue());
	}
	
	if (expensesLog.getEmployeeId() != null) {
	    if (comboEmployees == null) {
		comboEmployees = new ExCombo<>(ServiceProvider.get().getSubscriberService().getActiveEmployees());
		comboEmployees.addItemListener(new ItemListener() {

		    @Override
		    public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
			    if (comboEmployees.getValue() != null) {
				txtAmount.setValue(Double.valueOf(comboEmployees.getValue().getSalary()));
			    }
			}

		    }
		});
	    }
	    comboEmployees.setSelectedItem(new Employee(expensesLog.getEmployeeId()));
	}
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.setDefaultDialogBorder();
	builder.appendSeparator(message("maintenance.form.seperator"));
	builder.append(message("maintenance.form.maintenaceType"), comboMaintenaceTypes);
	builder.append(message("maintenance.form.name"), txtDesc);
	if (isDiesel()) {
	    builder.append(message("maintenance.form.dieselAmount"), txtDieselQuantity);
	}
	
	if(isOil()) {
	    builder.append(message("maintenance.form.oil"), txtDieselQuantity);
	}
	

	if (isEmployee()) {
	    if (comboEmployees == null) {
		comboEmployees = new ExCombo<>(ServiceProvider.get().getSubscriberService().getActiveEmployees());
		comboEmployees.addItemListener(new ItemListener() {

		    @Override
		    public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
			    if (comboEmployees.getValue() != null) {
				txtAmount.setValue(Double.valueOf(comboEmployees.getValue().getSalary()));
			    }
			}

		    }
		});
	    }
	    builder.append(message("maintenance.form.employee"), comboEmployees);
	    if (comboEmployees.getValue() != null) {
		txtAmount.setText(String.valueOf(comboEmployees.getValue().getSalary()));
	    }
	    builder.append(message("maintenance.form.salary"), txtAmount);
	} else {
	    builder.append(message("maintenance.form.amount"), txtAmount);
	    builder.append(message("maintenance.form.engines"), comboEngines);
	}

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

	ExpensesLog maintenaceLog = expensesLog == null ? new ExpensesLog() : expensesLog;
	maintenaceLog.setDesc(txtDesc.getText());
	maintenaceLog.setMaintenanceType(comboMaintenaceTypes.getValue().type);
	maintenaceLog.setEngineId(comboEngines.getValue() != null ? comboEngines.getValue().getId() : null);
	maintenaceLog.setNote(txtNote.getText().trim());
	maintenaceLog.setAmount(txtAmount.getValue());

	if (isEmployee()) {
	    maintenaceLog.setEmployeeId(comboEmployees.getValue().getId());
	    maintenaceLog.setDesc(message("employee.payment", comboEmployees.getValue().getName()+" "+comboEmployees.getValue().getLastName()));
	    maintenaceLog.setEngineId(null);
	}

	maintenaceLog.setDieselConsupmtion(isDiesel() ? txtDieselQuantity.getValue() : null);
	 
	maintenaceLog.setOilConsumption(isOil() ? txtDieselQuantity.getValue() : null);

	ServiceProvider.get().getSubscriberService().saveMaintenanceLog(maintenaceLog);

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

	/*if (txtDesc.getText().isEmpty()) {
	    errors.add(errorPerfix("maintenance.form.name"));
	}*/

	if (isDiesel() && txtDieselQuantity.getValue() == null) {
		errors.add(errorPerfix("maintenance.form.dieselAmount"));
	}
	
	if (isOil() && txtDieselQuantity.getValue() == null) {
		errors.add(errorPerfix("maintenance.form.oil"));
	}

	if (isEmployee()) {
	    if (comboEmployees.getValue() == null) {
		errors.add(errorPerfix("maintenance.form.dieselAmount"));
	    }

	    if (txtAmount.getValue() == null) {
		errors.add(errorPerfix("maintenance.form.salary"));
	    }
	} else {
	    if (txtAmount.getValue() == null) {
		errors.add(errorPerfix("maintenance.form.amount"));
	    }
	    if (mustHasEngineValue() && comboEngines.getValue() == null) {
		errors.add(errorPerfix("maintenance.form.engines"));
	    }
	}

	return errors.isEmpty() ? Optional.empty() : Optional.of(errors);
    }

    private boolean mustHasEngineValue() {
	SearchResult value = comboMaintenaceTypes.getValue();
	switch (value.type) {
	case CHANGING_FIXING_ENGINE_PARTS:
	case DIESEL:
	case FILTER_OIL_CHANGING:
	    return true;
	default:
	    break;
	}
	return false;
    }

    private ItemListener createComboDieselListener() {
	return new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent arg0) {
		if (arg0.getStateChange() == ItemEvent.SELECTED) {
		    SearchResult value = comboMaintenaceTypes.getValue();
		    if (value != null) {
			txtAmount.setValue(null);
			switch (value.type) {
			case DIESEL:
			    redrawPanel();
			    break;
			case EMPLOYEE:
			    redrawPanel();
			    break;

			default:
			    redrawPanel();
			    break;
			}
		    }
		}

	    }
	};
    }

    static class SearchResult {
	final ExpensesType type;
	final String label;

	public SearchResult(ExpensesType type, String label) {
	    super();
	    this.type = type;
	    this.label = label;
	}

	@Override
	public String toString() {
	    return label;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((type == null) ? 0 : type.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    SearchResult other = (SearchResult) obj;
	    if (type != other.type)
		return false;
	    return true;
	}

    }

}