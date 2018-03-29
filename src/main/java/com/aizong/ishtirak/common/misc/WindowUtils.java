/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

/**
 *
 * @author User
 */
public class WindowUtils {

    private static final KeyStroke escapeStroke
            = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    public static final String dispatchWindowClosingActionMapKey
            = "com.spodding.tackline.dispatch:WINDOW_CLOSING";

    public static void installEscapeCloseOperation(final Window dialog) {
        Action dispatchClosing = new AbstractAction() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -3745952682763164967L;

			public void actionPerformed(ActionEvent event) {
                dialog.dispatchEvent(new WindowEvent(
                        dialog, WindowEvent.WINDOW_CLOSING
                ));
            }
        };
        
        
        JRootPane root = null;
		if (dialog instanceof JDialog)
		{

			root = ( (JDialog) dialog ).getRootPane();
		}
		else if(dialog instanceof JFrame)
		{
			root = ( (JFrame) dialog ).getRootPane();
		}
		else throw new UnsupportedOperationException("installEscapeCloseOperation::please pass dialog or frame as parameter or add new type to conditions statement");
		
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                escapeStroke, dispatchWindowClosingActionMapKey
        );
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing
        );
    }
    

    
    public static JDialog createDialog(Window owner, String title, JPanel jp) {
        JDialog frame = new JDialog(owner);
        WindowUtils.installEscapeCloseOperation(frame);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(jp);
        frame.pack();

        frame.setMinimumSize(frame.getPreferredSize());
        frame.setLocationRelativeTo(owner);
        frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        applyRtl(frame);
        
        frame.setVisible(true);
        return frame;
    }



    public static void applyRtl(Window frame) {
	Component[] component = frame.getComponents();
        for(int i=0; i<component.length; i++){
            component[i].applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            Component[] cp = ((Container) component[i]).getComponents();
            for(int j=0; j<cp.length; j++){
                try{
                    cp[j].applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                }catch(Exception e){
                    continue;

                }
            }
        }
    }
    
    public static JFrame createFrame(String title, JPanel jp) {
        JFrame frame = new JFrame(title);
        WindowUtils.installEscapeCloseOperation(frame);
        frame.setTitle(title);
        frame.setIconImage(ImageUtils.getSoftwareIcon().getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(jp);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setVisible(true);
        return frame;
    }
    
    
    public static JDialog createDialogWithOkAndCancel(Window owner, String title, JPanel jp,WindowWithButtonsActionListener listener) {
        JDialog frame = new JDialog(owner);
        WindowUtils.installEscapeCloseOperation(frame);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JButton btnOk = ButtonFactory.createBtnApply();
        JButton btnClose = ButtonFactory.createBtnClose();

        @SuppressWarnings("unused")
		ActionListener actionListener = e->
        {
        	if(e.getSource()==btnOk)
        	{
        		listener.btnOkAction();
        	}
        	else 
        	{
        		frame.dispose();
        	}
        };
        
        DefaultFormBuilder builder = DefaultFormBuilderUtils.createRightDefaultFormBuilder("p",null, false);
        builder.append(jp);
        builder.appendSeparator();
        
        builder.append(ButtonBarFactory.buildRightAlignedBar(btnOk,btnClose), builder.getColumnCount());
        frame.setContentPane(builder.getPanel());
        frame.pack();

        frame.setMinimumSize(frame.getPreferredSize());
        frame.setLocationRelativeTo(owner);
        frame.setVisible(true);
        return frame;
    }
    
    public static void createModalDialogWithOkAndCancel(Window owner, String title, JPanel jp,WindowWithButtonsActionListener listener) {
        int choice= JOptionPane.showConfirmDialog(owner, jp,"Project Selection",JOptionPane.OK_CANCEL_OPTION);
        if(choice==JOptionPane.OK_OPTION)
        {
        	listener.btnOkAction();
        }
    }
    
    public interface WindowWithButtonsActionListener
    {
    	public void btnOkAction();
    }
         
}
