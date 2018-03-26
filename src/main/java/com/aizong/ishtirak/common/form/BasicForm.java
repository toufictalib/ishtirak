package com.aizong.ishtirak.common.form;

import java.awt.Component;

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
	initComponetns();
	initUI();
    }
    
    protected abstract void initComponetns();
    
    private void initUI() {

   	FormLayout layouts = new FormLayout("pref:grow");
   	DefaultFormBuilder rowBuilder = new DefaultFormBuilder(layouts, this);
   	rowBuilder.setDefaultDialogBorder();

   	//create builder
   	String leftToRightSpecs = getLayoutSpecs();
	FormLayout layout = new FormLayout(OrientationUtils.flipped(leftToRightSpecs), new RowSpec[] {});
	DefaultFormBuilder builder = new DefaultFormBuilder(layout);
	builder.setLeftToRight(false);
	
   	rowBuilder.append(buildPanel(builder));

       }
    
    protected abstract Component buildPanel(DefaultFormBuilder builder);
    
    protected abstract String getLayoutSpecs();
}
