package com.aizong.ishtirak.common.form;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class MyProgressBar extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MyProgressBar() {
	JTextArea msgLabel;
	JProgressBar progressBar;
	final int MAXIMUM = 100;
	JPanel panel;

	progressBar = new JProgressBar(0, MAXIMUM);
	progressBar.setIndeterminate(true);
	msgLabel = new JTextArea("deneme");
	msgLabel.setEditable(false);

	panel = new JPanel(new BorderLayout(5, 5));
	panel.add(msgLabel, BorderLayout.PAGE_START);
	panel.add(progressBar, BorderLayout.CENTER);
	panel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));

	getContentPane().add(panel);
	setResizable(false);
	pack();
	setSize(500, getHeight());
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	setAlwaysOnTop(false);
	setVisible(true);
	msgLabel.setBackground(panel.getBackground());
    }
}
