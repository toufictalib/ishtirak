package com.aizong.ishtirak.common;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class DoubleTextField extends JTextField implements FocusListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8512234769416723831L;

	public DoubleTextField(Long value)
	{
		super(value != null ? value+"" : "");
	}
	
	public DoubleTextField()
	{
		this(null);
		addFocusListener(this);
	}


	public Double getValue()
	{
		return Double.parseDouble(getText());
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		select(0, getText().length());
		
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		select(0, 0);
		 
		
	}
}