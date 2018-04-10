/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author User
 */
public class ComponentUtils {

    public static void fireCheckBox(JCheckBox checkBox) {
        ActionListener[] actionListeners = checkBox.getActionListeners();
        for (ActionListener actionListener : actionListeners) {
            actionListener.actionPerformed(new ActionEvent(checkBox, -1, ""));
        }
    }

    public static void fireCombobBox(JComboBox comboBox) {
        for (PopupMenuListener listener : comboBox.getPopupMenuListeners()) {
            listener.popupMenuWillBecomeInvisible(new PopupMenuEvent(comboBox));
        }
    }
    
    public static void fireCombobBoxItemListener(JComboBox comboBox) {
        for (ItemListener listener : comboBox.getItemListeners()) {
            listener.itemStateChanged(new ItemEvent(comboBox, -1, null, ItemEvent.SELECTED));
        }
    }
    
    public static Dimension getDimension(double widthPercentage,double heightPercentage)
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int width = (int) (screenSize.width*widthPercentage/100);
        int height = (int) (screenSize.height*heightPercentage/100);
        return new Dimension(width, height);
    }
    
    public static JTextArea createTextArea() {
	JTextArea txtArea = new JTextArea(2, 1);
	txtArea.setLineWrap(true);

	txtArea.setBorder(UIManager.getBorder("TextField.border"));
	return txtArea;
    }
    
    public static void addTitleBorder(JComponent component,String title)
    {
    	component.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), title));;
    }
    
    public static JXDatePicker createDatePicker() {
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setFormats("yyyy-MM-dd");
        return datePicker;
    }
    
    public static Border emptyBorder(int padding) {
	return BorderFactory.createEmptyBorder(padding, padding, padding, padding);
    }
    
    public static JScrollPane createScrollPane(Component component) {
	JScrollPane scrollPane = new JScrollPane(component);
	scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	return scrollPane;
    }
}
