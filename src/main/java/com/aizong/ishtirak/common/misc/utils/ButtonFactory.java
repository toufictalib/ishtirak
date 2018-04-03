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

    public static String message(String code) {
	return ServiceProvider.get().getMessage().getMessage(code);
    }
    
    public static JButton createBtnSave() {
	return new JButton(message("save"), ImageUtils.getSaveIcon());
    }

    public static JButton createBtnSaveAndNew() {
	return new JButton(message("save_new"), ImageUtils.getSaveIcon());
    }

    public static JButton createBtnDelete() {
	return new JButton(message("delete"), ImageUtils.getDeleteIcon());
    }

    public static JButton createBtnAdd() {
	return createBtnAdd(message("add"));
    }
    
    public static JButton createBtnAdd(String text) {
	return new JButton(text, ImageUtils.getAddIcon());
    }

    public static JButton createBtnApply() {
	return new JButton(message("apply"), ImageUtils.getApplyIcon());
    }

    public static JButton createBtnCancel() {
	return new JButton(message("cancel"), ImageUtils.getCancelIcon());
    }

    public static JButton createBtnClose() {
	return new JButton(message("close"), ImageUtils.getCloseIcon());
    }

    public static JButton createBtnReset() {
	return new JButton(message("reset"), ImageUtils.getResetIcon());
    }

    public static JButton createBtnInfo() {
	return new JButton(message("info"), ImageUtils.getInfoIcon());
    }

    public static JButton createBtnPrint() {
	return new JButton(message("print"), ImageUtils.getInfoIcon());
    }

    public static JButton createBtnSearch() {
	return new JButton(message("search"), ImageUtils.getSearchIcon());
    }

    public static JButton createBtnRefresh() {
	return new JButton(message("refresh"), ImageUtils.getRefreshIcon());
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

    public static JButton createBtnEdit() {
	return new JButton(message("edit"), ImageUtils.getEditIcon());
    }

    public static JButton createBtnView() {
	return new JButton(message("view"), ImageUtils.getViewIcon());
    }

    public static JButton createBtnLogin() {
	return new JButton(message("login"), ImageUtils.getLoginIcon());
    }
    
    public static JButton createBtnHistory() {
   	return new JButton(message("login"), ImageUtils.getHistoryIcon());
    }
    
    
}
