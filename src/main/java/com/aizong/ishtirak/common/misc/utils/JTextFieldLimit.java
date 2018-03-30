/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author User
 */
public class JTextFieldLimit extends PlainDocument {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6358727719745692070L;
	private int limit;

    public JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

   public JTextFieldLimit(int limit, boolean upper) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
