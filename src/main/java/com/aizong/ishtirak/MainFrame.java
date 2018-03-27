package com.aizong.ishtirak;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.aizong.ishtirak.common.misc.ImageHelperCustom;
import com.jidesoft.swing.JideBorderLayout;
import com.jidesoft.swing.JideButton;

public class MainFrame extends JFrame {

    public MainFrame() {

	buildPanel();
    }

    private void buildPanel() {

	JPanel panel = new JPanel(new JideBorderLayout());

	String[] images = new String[] { "48px-Crystal_Clear_app_ksame.png", "48px-Crystal_Clear_app_kthememgr.png",
		"48px-Crystal_Clear_app_Staroffice.png" };
	JideButton btnSubscriberManagement = new JideButton("إدارة المشتركين",
		ImageHelperCustom.get().getImageIcon("menus/48px-Crystal_Clear_action_bookmark.png"));

	JideButton btnEngineManagement = new JideButton("إدارة المولدات",
		ImageHelperCustom.get().getImageIcon("menus/48px-Crystal_Clear_app_error.png"));

	setTitle("Simple example");

	JPanel panel2 = new JPanel();
	panel2.add(btnSubscriberManagement);
	panel2.add(btnEngineManagement);

	int ctr =1;
	for (String image : images) {
	    panel2.add(new JideButton("Button "+ctr),ImageHelperCustom.get().getImageIcon("menus/"+image));
	}

	panel.add(panel2, JideBorderLayout.CENTER);
	setContentPane(panel);

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	setSize(screenSize);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setVisible(true);

    }

    public static void main(String[] args) {
	new MainFrame();
    }
}
