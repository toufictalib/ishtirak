package com.aizong.ishtirak.common.misc.utils;


import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class StockPublicNotesDocumentFilter extends DocumentFilter {  

 private final int maxLength;

 public StockPublicNotesDocumentFilter (int maxLength) {  
     this.maxLength = maxLength;
 }  

 /** 
  * {@inheritDoc} 
  */  

 public void insertString (DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attr)  
         throws BadLocationException {
     if ((fb.getDocument().getLength() + str.length()) <= this.maxLength)
            super.insertString(fb, offset, str, attr);
        else
            Toolkit.getDefaultToolkit().beep();
 }  

 /** 
  * {@inheritDoc} 
  */  

 public void replace (DocumentFilter.FilterBypass fb, int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
     if ((fb.getDocument().getLength() + str.length()) <= this.maxLength)
            super.replace(fb, offset, length, str, attrs);
        else
            Toolkit.getDefaultToolkit().beep();

 }
}