/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc.component;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 *
 * @author Talaco1
 */
public class HintTextField extends JTextField implements FocusListener {
    
    private String hint = null;
    private boolean showingHint;
    
    public HintTextField() {
        super("");
        this.showingHint = true;
        super.addFocusListener(this);
    }
    
    public String getHint() {
        return hint;
    }
    
  @Override
  public void focusGained(FocusEvent e) {
    if(this.getText().isEmpty()) {
      super.setText("");
      showingHint = false;
    }
    updateColor();
  }
  @Override
  public void focusLost(FocusEvent e) {
    if(this.getText().isEmpty()) {
      super.setText(hint);
      showingHint = true;
    }
    updateColor();
  }
    public void setHint(String value) {
        String oldValue = hint;
        hint = value;
        if(getText().equals("") || getText().equals(oldValue)) {
            setText(hint);
            showingHint = true;
        }
        updateColor();
    }

    @Override
    public void setText(String t) {
        super.setText(t); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setText(String t,boolean showingHint)
    {
        super.setText(t);
        this.showingHint = showingHint;
    }
    
    
    
    @Override
    public String getText() {
      return showingHint ? "" : super.getText();
    }

    private void updateColor() {
        if(showingHint) {
            setForeground(Color.GRAY);
        }
        else {
            setForeground(Color.BLACK);
        }
    }
}
