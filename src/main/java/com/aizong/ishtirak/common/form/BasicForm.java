package com.aizong.ishtirak.common.form;

import java.awt.Component;

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

    protected abstract Component buildPanel(DefaultFormBuilder builder);

    protected abstract String getLayoutSpecs();
}
