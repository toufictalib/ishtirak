/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author User
 */
@SuppressWarnings("serial")
public abstract class JpanelTemplate extends JPanel {

    public boolean alreadyInit = false;
    
    public JpanelTemplate() {
        super(new BorderLayout());
    }

    public void lazyInitalize() {
        if (!alreadyInit) {
            initComponents();
            init();
            alreadyInit = true;
        }
    }
    
    public void refreshPanel()
    {
        lazyInitalize();
    }
    
     public void reBuildPanel()
    {
        removeAll();
        alreadyInit = false;
        lazyInitalize();
    }
    
    

    public abstract void init();

    public abstract void initComponents();
    
    public Window getOwner()
    {
        return SwingUtilities.getWindowAncestor(this);
    }
    
	public void closeWindow( )
	{
		Window window = getOwner();
		if (window != null)
		{
			window.dispose();
		}
	}
}
