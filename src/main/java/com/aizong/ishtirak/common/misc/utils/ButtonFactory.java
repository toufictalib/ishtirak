/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc.utils;

import java.awt.Cursor;

import javax.swing.JButton;

/**
 *
 * @author User
 */
public class ButtonFactory {

    public static JButton createBtnSave() {
        return new JButton("حفظ", ImageUtils.getSaveIcon());
    }
    
    public static JButton createBtnDelete() {
        return new JButton("حذف", ImageUtils.getDeleteIcon());
    }
    
    public static JButton createBtnAdd() {
        return new JButton("إضافة", ImageUtils.getAddIcon());
    }
    
    public static JButton createBtnApply() {
        return new JButton("Apply", ImageUtils.getApplyIcon());
    }
    
    public static JButton createBtnCancel() {
        return new JButton("Cancel", ImageUtils.getCancelIcon());
    }
    
    public static JButton createBtnClose() {
        return new JButton("إغلاق", ImageUtils.getCloseIcon());
    }
    
    public static JButton createBtnReset() {
        return new JButton("Reset", ImageUtils.getResetIcon());
    }
    
    public static JButton createBtnInfo() {
        return new JButton("Info", ImageUtils.getInfoIcon());
    }
    
     public static JButton createBtnPrint() {
        return new JButton("طباعة", ImageUtils.getInfoIcon());
    }
     
      public static JButton createBtnCheckup() {
        return new JButton("Checkup", ImageUtils.getCheckupIcon());
    }
      
       public static JButton createBtnSearch() {
        return new JButton("بحث", ImageUtils.getSearchIcon());
    }
       
          public static JButton createBtnRefresh() {
        return new JButton("Refresh", ImageUtils.getRefreshIcon());
    }

    public static JButton createButtonAsIcon() {
        JButton button = new JButton(ImageUtils.getDeleteIcon());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }
    
     public static void makeButtonAsIcon(JButton button) {
    	 button.setText("");
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
