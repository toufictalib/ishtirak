package com.aizong.ishtirak.common.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;

import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public abstract class BasicForm extends BasicPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -2580512542483414135L;

    public BasicForm() {
	super();
    }

    
    protected void initializePanel() {
	initComponents();
	initUI();
    }
    
    protected abstract void initComponents();

    private void initUI() {

	FormLayout layouts = new FormLayout("pref:grow");
	DefaultFormBuilder rowBuilder = new DefaultFormBuilder(layouts, this);
	rowBuilder.setDefaultDialogBorder();

	// create builder
	DefaultFormBuilder builder = createBuilder(getLayoutSpecs());

	rowBuilder.append(buildPanel(builder));

    }
    
    protected void redrawPanel() {
	removeAll();
	initUI();
	revalidate();
	repaint();
	
	if(getOwner()!=null) {
	    WindowUtils.applyRtl(getOwner());
	    getOwner().pack();
	}
    }
    
    //new FormDebugPanel()
    public static DefaultFormBuilder createBuilder(String leftToRightSpecs) {
	FormLayout layout = new FormLayout(OrientationUtils.flipped(leftToRightSpecs), new RowSpec[] {});
	DefaultFormBuilder builder = new DefaultFormBuilder(layout);
	builder.setLeftToRight(false);
	return builder;
    }
    
    protected JButton btnSave() {
	JButton btnSave = ButtonFactory.createBtnSave();
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		Optional<List<String>> validateInputs = validateInputs();
		if (validateInputs.isPresent()) {
		    MessageUtils.showWarningMessage(getOwner(), String.join("\n", validateInputs.get()));
		    return;
		}

		save();

	    }
	});
	return btnSave;
    }
    
    protected JButton btnClose() {
	JButton btnClose = ButtonFactory.createBtnClose();
	btnClose.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		closeWindow();

	    }
	});
	return btnClose;
    }
    
    protected void save() {
	
    }
    
    protected Optional<List<String>> validateInputs(){
	return Optional.empty();
    }
    
    protected abstract Component buildPanel(DefaultFormBuilder builder);

    protected abstract String getLayoutSpecs();
}
