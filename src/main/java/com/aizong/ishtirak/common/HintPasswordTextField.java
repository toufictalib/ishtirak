/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.*;
import java.io.Serializable;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Talaco1
 */
public class HintPasswordTextField extends JPasswordField implements FocusListener {

    private String hint = null;
    private boolean showingHint;

    public HintPasswordTextField() {
        super("");
        try {
            this.showingHint = true;
            super.addFocusListener(this);
        } catch (Exception e) {
        }
    }

    public String getHint() {
        return hint;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText("");
            showingHint = false;
        }
        updateColor();
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText(hint);
            showingHint = true;
        }
        updateColor();
    }

    public void setHint(String value) {
        String oldValue = hint;
        hint = value;
        if (getText().equals("") || getText().equals(oldValue)) {
            setText(hint);
            showingHint = true;
        }
        updateColor();
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }

    private void updateColor() {
        if (showingHint) {
            setForeground(Color.GRAY);
        } else {
            setForeground(Color.BLACK);
        }
    }
}
