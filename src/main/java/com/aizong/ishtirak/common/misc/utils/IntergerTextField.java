package com.aizong.ishtirak.common.misc.utils;

import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

public class IntergerTextField extends JTextField {
    /**
     * 
     */
    private static final long serialVersionUID = -8512234769416723831L;

    /*
     * @Override public void processKeyEvent(KeyEvent ev) { if
     * (Character.isDigit(ev.getKeyChar())) { super.processKeyEvent(ev); }
     * ev.consume(); return; }
     */

    public IntergerTextField(Integer value, int columns) {
	super(value != null ? value + "" : "", columns);
	PlainDocument doc = (PlainDocument) getDocument();
	doc.setDocumentFilter(new MyIntFilter());
    }

    public IntergerTextField(Integer value) {
	this(value, 5);
    }

    public IntergerTextField() {
	this((Integer) null);
    }

    public Long getValueAsLong() {
	Long result = null;
	String text = getText();
	text = text == null ? text : text.trim();
	if (text != null && !"".equals(text)) {
	    try {
		return Long.parseLong(text);
	    } catch (NumberFormatException e) {

	    }
	}
	return result;

    }

    public void setValueAsLong(Long value) {
	setText(value == null ? "" : String.valueOf(value));
    }
    
    public void setValue(Integer value) {
	setText(value == null ? "" : String.valueOf(value));
    }

    public Integer getValue() {
	Integer result = null;
	String text = getText();
	text = text == null ? text : text.trim();
	if (text != null && !"".equals(text)) {
	    try {
		return Integer.parseInt(text);
	    } catch (NumberFormatException e) {

	    }
	}
	return result;

    }

}