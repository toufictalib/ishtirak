/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc.utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class MessageUtils {

    public static void showErrorMessage(Component owner, String message) {
        JOptionPane.showMessageDialog(owner, message, "Error", JOptionPane.ERROR_MESSAGE);

    }

    public static void showWarningMessage(Component owner, String title, String message) {
        JOptionPane.showMessageDialog(owner, message, title, JOptionPane.WARNING_MESSAGE);

    }
    
    public static void showWarningMessage(Component owner,  String message) {
        JOptionPane.showMessageDialog(owner, message, "Warning", JOptionPane.WARNING_MESSAGE);

    }

    public static void showInfoMessage(Component owner, String title, String message) {
        JOptionPane.showMessageDialog(owner, message, title, JOptionPane.INFORMATION_MESSAGE);

    }

    public static void showInfoMessage(Component owner, String message) {
        showInfoMessage(owner, "Info", message);

    }
    
    public static boolean showConfirmationMessage(Component owner,String question,String title)
    {
        int answer =  JOptionPane.showConfirmDialog(owner, question, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        return answer == JOptionPane.YES_OPTION;
    }
}
