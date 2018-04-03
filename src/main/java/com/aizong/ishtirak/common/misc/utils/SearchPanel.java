package com.aizong.ishtirak.common.misc.utils;

import java.awt.FlowLayout;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXDatePicker;

import com.aizong.ishtirak.bean.SearchBean;

public class SearchPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 5251638566168674323L;

    private JXDatePicker datePickerFrom;

    private JXDatePicker datePickerTo;

    private IntergerTextField txtStartCount;

    private IntergerTextField txtEndCount;

    private Date start;
    private Date end;

    public static SearchPanel create() {
	return new SearchPanel();
    }

    public static SearchPanel createDefault() {
	return new SearchPanel().addDatePickerFrom().addDatePickerTo();
    }
    
    public static SearchPanel createDefault(Date start, Date end) {
	return new SearchPanel(start, end).addDatePickerFrom().addDatePickerTo();
    }

    public SearchPanel() {
	super(new FlowLayout(FlowLayout.LEADING));
    }

    public SearchPanel(Date start, Date end) {
	super(new FlowLayout(FlowLayout.LEADING));
	this.start = start;
	this.end = end;
    }

    public SearchPanel addDatePickerFrom() {
	datePickerFrom = ComponentUtils.createDatePicker();
	add(new JLabel("من تاريخ :"));
	add(datePickerFrom);
	if (start != null) {
	    datePickerFrom.setDate(start);
	}
	return this;
    }

    public SearchPanel addDatePickerTo() {
	datePickerTo = ComponentUtils.createDatePicker();
	add(new JLabel("إلى تاريخ :"));
	add(datePickerTo);
	if (end != null) {
	    datePickerTo.setDate(end);
	}
	return this;
    }

    public SearchPanel addStartCount() {
	txtStartCount = new IntergerTextField(0);
	add(new JLabel("Start Count:"));
	add(txtStartCount);

	return this;
    }

    public SearchPanel addEndCount() {
	txtEndCount = new IntergerTextField(1000);
	add(new JLabel("Max Lines :"));
	add(txtEndCount);

	return this;
    }

    public Date getFromDate() {
	if (datePickerFrom.getDate() == null)
	    return null;
	return datePickerFrom.getDate();
    }

    public Date getEndDate() {
	if (datePickerTo.getDate() == null)
	    return null;
	return datePickerTo.getDate();
    }

    /*public int getStartCount() {
	return txtStartCount.getValue() == null ? 0 : txtStartCount.getValue();
    }

    public int getEndCount() {
	return txtEndCount.getValue() == null ? 1000 : txtEndCount.getValue();
    }*/

    public SearchBean toSearchBean() {
	return SearchBean.edit().setFromDate(getFromDate() == null ? "-1" : DateUtil.formatShortSqlDate(getFromDate()))
		.setEndDate(getEndDate() == null ? "-1" : DateUtil.formatShortSqlDate(getEndDate()))
		;
    }

}
