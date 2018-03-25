package com.aizong.ishtirak.common;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.aizong.ishtirak.utils.Message;

public class BasicPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 3026529267754719720L;

    protected Message message;

    public BasicPanel() {
	super(new BorderLayout());
	message = ServiceProvider.get().getMessage();
    }

    public Window getOwner() {
	return SwingUtilities.getWindowAncestor(this);
    }

    public void closeWindow() {
	Window window = getOwner();
	if (window != null) {
	    window.dispose();
	}
    }

    protected String message(String code, Object... params) {
	return message.getMessage(code, params);
    }
}

