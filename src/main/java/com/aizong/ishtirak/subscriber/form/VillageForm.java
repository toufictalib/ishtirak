package com.aizong.ishtirak.subscriber.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.aizong.ishtirak.common.BasicPanel;
import com.aizong.ishtirak.common.ButtonFactory;
import com.aizong.ishtirak.common.ServiceProvider;
import com.aizong.ishtirak.subscriber.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class VillageForm extends BasicPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField txtName;

    public VillageForm() {
	super();
	initComponetns();
	initUI();
    }

    private void initComponetns() {
	txtName = new JTextField();
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
	builder.appendSeparator("القرية");
	builder.setDefaultDialogBorder();
	builder.append("الإسم*", txtName);
	builder.appendSeparator();
	
	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		Village village = new Village();
		village.setName(txtName.getText());
		
		ServiceProvider.get().getSubscriberService().saveVillage(village);
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
	builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose,btnSave), builder.getColumnCount());
	return builder.getPanel();
    }


}