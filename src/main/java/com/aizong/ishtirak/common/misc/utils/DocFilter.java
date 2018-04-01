package com.aizong.ishtirak.common.misc.utils;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class DocFilter {
    public static void main(String[] args) {
	JTextField textField = new JTextField(10);

	JPanel panel = new JPanel();
	panel.add(textField);

	PlainDocument doc = (PlainDocument) textField.getDocument();
	doc.setDocumentFilter(new MyIntFilter());

	JOptionPane.showMessageDialog(null, panel);
    }
}

class MyIntFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
	    throws BadLocationException {

	Document doc = fb.getDocument();
	StringBuilder sb = new StringBuilder();
	sb.append(doc.getText(0, doc.getLength()));
	sb.insert(offset, string);

	if (test(sb.toString())) {
	    super.insertString(fb, offset, string, attr);
	} else {
	    // warn the user and don't allow the insert
	}
    }

    private boolean test(String text) {
	try {
	    if (!"".equals(text)) {
		Integer.parseInt(text);
	    }
	    return true;
	} catch (NumberFormatException e) {
	    return false;
	}
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
	    throws BadLocationException {

	Document doc = fb.getDocument();
	StringBuilder sb = new StringBuilder();
	sb.append(doc.getText(0, doc.getLength()));
	sb.replace(offset, offset + length, text);
	if (test(sb.toString())) {
	    super.replace(fb, offset, length, text, attrs);
	} else {
	    // warn the user and don't allow the insert
	}

    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
	Document doc = fb.getDocument();
	StringBuilder sb = new StringBuilder();
	sb.append(doc.getText(0, doc.getLength()));
	sb.delete(offset, offset + length);

	super.remove(fb, offset, length);

    }
}