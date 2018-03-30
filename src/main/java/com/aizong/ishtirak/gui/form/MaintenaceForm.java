package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.bean.MaintenanceType;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DoubleTextField;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.DieselLog;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.MaintenaceLog;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class MaintenaceForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected JTextField txtDesc;
    protected DoubleTextField txtAmount;
    protected ExCombo<SearchResult> comboMaintenaceTypes;
    protected ExCombo<Engine> comboEngines;
    protected JTextArea txtNote;
    private DoubleTextField txtDieselQuantity;

    private DefaultFormBuilder builder;

    boolean diesel;

    public MaintenaceForm() {
	super();
	initializePanel();
    }

    @Override
    protected void initComponents() {
	txtDesc = new JTextField();
	txtAmount = new DoubleTextField();
	List<SearchResult> values = new ArrayList<>();
	for (MaintenanceType type : MaintenanceType.values()) {

	    SearchResult result = new SearchResult();
	    result.type = type;
	    result.label = enumMessage(type.name(), MaintenanceType.class);

	    values.add(result);
	}
	comboMaintenaceTypes = new ExCombo<>(values);
	comboMaintenaceTypes.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent arg0) {
		if (arg0.getStateChange() == ItemEvent.SELECTED) {
		    diesel = false;
		    SearchResult value = comboMaintenaceTypes.getValue();
		    if (value != null) {
			switch (value.type) {
			case DIESEL:
			    diesel = true;
			    redrawPanel();
			    break;

			default:
			    diesel = false;
			    redrawPanel();
			    break;
			}
		    }
		}

	    }
	});
	comboEngines = new ExCombo<>(true, ServiceProvider.get().getSubscriberService().getEngines());

	txtNote = new JTextArea();
	txtNote.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	txtNote.setBorder(UIManager.getBorder("TextField.border"));
	txtNote.setLineWrap(true);
	txtNote.setRows(4);

	txtDieselQuantity = new DoubleTextField();
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.setDefaultDialogBorder();
	builder.appendSeparator(message("maintenance.form.seperator"));
	builder.append(message("maintenance.form.maintenaceType"), comboMaintenaceTypes);
	builder.append(message("maintenance.form.name"), txtDesc);
	if (diesel) {
	    builder.append(message("maintenance.form.dieselAmount"), txtDieselQuantity);
	}
	builder.append(message("maintenance.form.amount"), txtAmount);
	builder.append(message("maintenance.form.engines"), comboEngines);

	JScrollPane scrollPane = new JScrollPane(txtNote);
	scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	builder.append(message("maintenance.form.note"), scrollPane);
	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(save());
	JButton btnClose = ButtonFactory.createBtnClose();
	btnClose.addActionListener(e -> closeWindow());
	builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder.getColumnCount());
	return builder.getPanel();
    }

    private ActionListener save() {
	return new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

		MaintenaceLog maintenaceLog = new MaintenaceLog();
		maintenaceLog.setDesc(txtDesc.getText());
		maintenaceLog.setAmount(txtAmount.getValue());
		maintenaceLog.setMaintenanceType(comboMaintenaceTypes.getValue().type);
		maintenaceLog.setEngineId(comboEngines.getValue() != null ? comboEngines.getValue().getId() : null);
		maintenaceLog.setNote(txtNote.getText().trim());

		if (diesel) {
		    DieselLog dieselLog = new DieselLog();
		    dieselLog.setAmount(txtAmount.getValue());
		    dieselLog.setDescription(txtDesc.getText());
		    dieselLog.setDieselAmount(txtDieselQuantity.getValue());
		    dieselLog.setMaintenanceLog(maintenaceLog.getId());

		    ServiceProvider.get().getSubscriberService().saveDieselLog(maintenaceLog, dieselLog);

		} else {

		    ServiceProvider.get().getSubscriberService().saveMaintenanceLog(maintenaceLog);
		}
		closeWindow();
	    }
	};
    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

    static class SearchResult {
	MaintenanceType type;
	String label;

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