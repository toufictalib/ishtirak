package com.aizong.ishtirak.utils;

import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class DefaultFormBuilderUtils {
	
	private DefaultFormBuilderUtils() {
		
	}
	
	public static DefaultFormBuilder createRightDefaultFormBuilder(String endCodedColumnsSpecs, JPanel panel,boolean right) {
		// /fill:200dlu:grow,10dlu,p
		
		StringBuilder buffer = new StringBuilder();
		if (right)
		{
			String[] values = endCodedColumnsSpecs.split(",");

			if (values.length > 0) for (int i = values.length - 1; i >= 0; i-- )
				buffer.append(values[i] + ",");

			if (buffer.length() > 0) endCodedColumnsSpecs = buffer.substring(0, buffer.length() - 1);
		}
		
		FormLayout formLayout = new FormLayout(endCodedColumnsSpecs);
		DefaultFormBuilder builder = null;
		if (panel != null)
		{
			builder = new DefaultFormBuilder(formLayout, panel);
		}
		else
		{
			builder = new DefaultFormBuilder(formLayout);
		}
		
		builder.setDebugToolTipsEnabled(true);
		builder.setDefaultDialogBorder();
		builder.setLeftToRight(!right);
		return builder;
	}
}
