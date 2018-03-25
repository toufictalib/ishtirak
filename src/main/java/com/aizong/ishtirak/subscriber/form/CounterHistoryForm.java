package com.aizong.ishtirak.subscriber.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import com.aizong.ishtirak.CounterHistory;
import com.aizong.ishtirak.bundle.Bundle;
import com.aizong.ishtirak.bundle.Contract;
import com.aizong.ishtirak.common.BasicPanel;
import com.aizong.ishtirak.common.ButtonFactory;
import com.aizong.ishtirak.common.ServiceProvider;
import com.aizong.ishtirak.input.IntergerTextField;
import com.aizong.ishtirak.subscriber.model.Subscriber;
import com.aizong.ishtirak.utils.ExCombo;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CounterHistoryForm extends BasicPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private IntergerTextField txtConsumption;
    
    private ExCombo<Contract> comboContact;
    
    private Subscriber subscriber;

    public CounterHistoryForm(Subscriber subscriber) {
	this.subscriber = subscriber;
	initComponetns();
	initUI();
	fillData();
    }


    private void fillData() {
	if (subscriber == null) {
	    return;
	}

    }

    Map<Long, String> map = new HashMap<>();
    @SuppressWarnings("unchecked")
    private void initComponetns() {
	
	txtConsumption = new IntergerTextField();
	
	List<Contract> contractBySubscriberId = ServiceProvider.get().getSubscriberService().getContractBySubscriberId(subscriber.getId());
	for(Contract contract:contractBySubscriberId) {
	    Bundle bundle = ServiceProvider.get().getSubscriberService().getBundleById(contract.getBundleId());
	    if(bundle!=null) {
		map.put(contract.getId(), bundle.getName()+":"+contract.getCounterId());
	    }
	}
	comboContact = new ExCombo<>(contractBySubscriberId);
	comboContact.setRenderer(new DefaultListCellRenderer() {
	    /**
	     * 
	     */
	    private static final long serialVersionUID = -2015108228646251293L;

	    @Override
	    public Component getListCellRendererComponent(JList<?> arg0, Object arg1, int arg2, boolean arg3,
	            boolean arg4) {
	        // TODO Auto-generated method stub
		Component component =  super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
		if(component instanceof JLabel) {
		    JLabel lbl = (JLabel) component;
		    Contract contract = (Contract)arg1;
		    if(contract!=null) {
			  String string = map.get(contract.getId());
			    if(string!=null) {
				lbl.setText(string);
			    }
		    }
		  
		}
		return component;
	    }
	});
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
	builder.appendSeparator(message("counterHistory.form.seperator"));
	builder.setDefaultDialogBorder();
	builder.append(message("counterHistory.form.subscription"),comboContact);
	builder.append(message("counterHistory.form.current"), txtConsumption);
	
	builder.appendSeparator();

	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		
		CounterHistory history = new CounterHistory();
		history.setContractId(comboContact.getValue().getId());
		history.setConsumption(Long.valueOf(txtConsumption.getValue()));
		
		ServiceProvider.get().getSubscriberService().saveConsumptionHistory(history);
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

}