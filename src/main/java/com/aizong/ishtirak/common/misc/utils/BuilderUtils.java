package com.aizong.ishtirak.common.misc.utils;

import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class BuilderUtils
{

	public static DefaultFormBuilder build(JPanel owner,String specs)
	{
		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(specs),owner);
		return builder;
	}
}
