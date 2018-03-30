package com.aizong.ishtirak.common.form;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.aizong.ishtirak.common.misc.utils.Message;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;

public abstract class BasicPanel extends JPanel {
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
	if(message!=null) {
	    return message.getMessage(code, params);
	}
	return code;
    }
    
    protected String enumMessage(String code, Class<?> enumClazz,Object... params) {
	if(message!=null) {
	    return message.getEnumLabel(code, enumClazz);
	}
	return code;
    }
}

