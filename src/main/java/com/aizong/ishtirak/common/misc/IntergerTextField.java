package com.aizong.ishtirak.common.misc;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class IntergerTextField extends JTextField implements FocusListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8512234769416723831L;

	
	public IntergerTextField(Document doc, String text, int columns)
	{
		super(doc, text, columns);
		// TODO Auto-generated constructor stub
	}


	public IntergerTextField(String text, int columns)
	{
		super(text, columns);
		// TODO Auto-generated constructor stub
	}

	public IntergerTextField(String text)
	{
		super(text);
		// TODO Auto-generated constructor stub
	}

	public IntergerTextField(Integer value,int columns)
	{
		this(value != null ? value+"" : "", columns);
	}
	
	public IntergerTextField(Integer value)
	{
		this(value, 5);
	}
	
	public IntergerTextField()
	{
		this((String)null);
		addFocusListener(this);
	}


	public Integer getValue()
	{
		return Integer.parseInt(getText());
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