package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.aizong.ishtirak.bean.MaintenanceType;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.ButtonFactory;
import com.aizong.ishtirak.common.misc.DoubleTextField;
import com.aizong.ishtirak.common.misc.ExCombo;
import com.aizong.ishtirak.common.misc.ServiceProvider;
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
	comboEngines = new ExCombo<>(true, ServiceProvider.get().getSubscriberService().getEngines());

	txtNote = new JTextArea();
	txtNote.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	txtNote.setBorder(UIManager.getBorder("TextField.border"));
	txtNote.setLineWrap(true);
	txtNote.setRows(4);
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("maintenance.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("maintenance.form.name"), txtDesc);
	builder.append(message("maintenance.form.amount"), txtAmount);
	builder.append(message("maintenance.form.maintenaceType"), comboMaintenaceTypes);
	builder.append(message("maintenance.form.engines"), comboEngines);
	
	JScrollPane scrollPane = new JScrollPane(txtNote);
	scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	builder.append(message("maintenance.form.note"), scrollPane);
	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		MaintenaceLog maintenaceLog = new MaintenaceLog();
		maintenaceLog.setDesc(txtDesc.getText());
		maintenaceLog.setAmount(txtAmount.getValue());
		maintenaceLog.setMaintenanceType(comboMaintenaceTypes.getValue().type);
		maintenaceLog.setEngineId(comboEngines.getValue() != null ? comboEngines.getValue().getId() : null);
		maintenaceLog.setNote(txtNote.getText().trim());
		
		ServiceProvider.get().getSubscriberService().saveMaintenanceLog(maintenaceLog);
		closeWindow();
	    }
	});
	JButton btnClose = ButtonFactory.createBtnClose();
	btnClose.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		
		closeWindow();

	    }
	});
	builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder.getColumnCount());
	return builder.getPanel();
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