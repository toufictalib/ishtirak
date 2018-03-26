/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc;

import java.awt.Button;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 *
 * @author c.simon
 */
public class InputUtils {
    public static String askPassword() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter a password:");
        JPasswordField pass = new JPasswordField(10);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "The title",
                                 JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                                 null, options, pass);//last argument means selected by default example : options[0]=ok: is selected by default. But I chose pass selected by default
////        this.getRootPane().setDefaultButton(options[0]);
//        JButton b = new JButton();
//        panel.getRootPane().setDefaultButton(b);// bas ana bade 7ot l ok ba7al l b
        if(option == 0) // pressing OK button
        {
            char[] password = pass.getPassword();
            return new String(password);
        }
        return null;
    }
}
