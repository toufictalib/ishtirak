package com.aizong.ishtirak.common.misc.utils;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MonthYearCombo extends JPanel {

    private JComboBox<String> datePickerFrom;

    private JComboBox<Integer> datePickerYearFrom;

    private static Vector<Integer> YEARS = new Vector<>();
    static {
	Calendar instance = Calendar.getInstance();
	for (int j = 2001; j <= instance.get(Calendar.YEAR); j++) {
	    YEARS.add(j);
	}
    }
    
    public  MonthYearCombo() {
	super(new FlowLayout(FlowLayout.RIGHT));
	datePickerFrom = monthCombo();
	datePickerYearFrom = yearsCombo();
	add(datePickerYearFrom);
	add(datePickerFrom);
	add(new JLabel("شهر :"));
	LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	datePickerFrom.setSelectedIndex(DateUtil.getEffectiveMonth()-1);
	datePickerYearFrom.setSelectedItem(localDate.getYear());
    }
    
    private JComboBox<Integer> yearsCombo() {
  	JComboBox<Integer> jComboBox = new JComboBox<Integer>(YEARS);
  	jComboBox.setPreferredSize(new Dimension(100, jComboBox.getPreferredSize().height));
  	return jComboBox;
      }

    private JComboBox<String> monthCombo() {
	JComboBox<String> jComboBox = new JComboBox<>(
		ServiceProvider.get().getMessage().getMessage("monthes").split(","));
	jComboBox.setPreferredSize(new Dimension(135, jComboBox.getPreferredSize().height));
	return jComboBox;
    }
    
    public Integer getMonth() {
  	return getString(datePickerFrom);
      }

      public Integer getYear() {
  	return get(datePickerYearFrom);
      }

      private Integer get(JComboBox<Integer> combo) {
  	return (Integer) combo.getSelectedItem();
      }

      private Integer getString(JComboBox<String> combo) {
  	return combo.getSelectedIndex() + 1;
      }
}
