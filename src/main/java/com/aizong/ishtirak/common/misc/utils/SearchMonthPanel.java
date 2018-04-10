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

public class SearchMonthPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 5251638566168674323L;

    private JComboBox<String> datePickerFrom;

    private JComboBox<Integer> datePickerYearFrom;

    private JComboBox<String> datePickerTo;

    private JComboBox<Integer> datePickerYearTo;

    private static Vector<Integer> YEARS = new Vector<>();
    static {
	Calendar instance = Calendar.getInstance();
	for (int j = 2001; j <= instance.get(Calendar.YEAR); j++) {
	    YEARS.add(j);
	}
    }

    private int fromMonth;
    private int fromYear;

    private int toMonth;
    private int toYear;

    public static SearchMonthPanel create() {
	return new SearchMonthPanel();
    }

    public static SearchMonthPanel createDefault() {
	return new SearchMonthPanel().addDatePickerFrom().addDatePickerTo();
    }

    public static SearchMonthPanel createDefault(Date start, Date end) {
	return new SearchMonthPanel(start, end).addDatePickerFrom().addDatePickerTo();
    }

    public SearchMonthPanel() {
	this(null, null);
    }

    public SearchMonthPanel(Date start, Date end) {
	super(new FlowLayout(FlowLayout.LEADING));

	if (end == null) {
	    end = new Date();
	}

	if (start == null) {
	    start = end == null ? new Date() : end;
	}

	Date date = start;
	LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	fromMonth = localDate.getMonthValue();
	fromYear = localDate.getYear();

	localDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	toMonth = localDate.getMonthValue();
	toYear = localDate.getYear();
    }

    public SearchMonthPanel addDatePickerFrom() {
	datePickerFrom = monthCombo();
	datePickerYearFrom = new JComboBox<>(YEARS);
	add(new JLabel("من تاريخ :"));
	add(datePickerFrom);
	add(datePickerYearFrom);
	datePickerFrom.setSelectedIndex(fromMonth-1);
	datePickerYearFrom.setSelectedItem(fromYear);
	return this;
    }

    private JComboBox<String> monthCombo() {
	return new JComboBox<>(ServiceProvider.get().getMessage().getMessage("monthes").split(","));
    }

    public SearchMonthPanel addDatePickerTo() {
	datePickerTo = monthCombo();
	datePickerYearTo = yearsCombo();
	add(new JLabel("إلى تاريخ :"));
	add(datePickerTo);
	add(datePickerYearTo);
	datePickerTo.setSelectedIndex(toMonth-1);
	datePickerYearTo.setSelectedItem(toYear);
	return this;
    }

    private JComboBox<Integer> yearsCombo() {
	 JComboBox<Integer> jComboBox = new JComboBox<Integer>(YEARS);
	 jComboBox.setPreferredSize(new Dimension(100,jComboBox.getPreferredSize().height));
	 return jComboBox;
    }

    public int getFromMonth() {
	return fromMonth;
    }

    public int getFromYear() {
	return fromYear;
    }

    public int getToMonth() {
	return toMonth;
    }

    public int getToYear() {
	return toYear;
    }

}
