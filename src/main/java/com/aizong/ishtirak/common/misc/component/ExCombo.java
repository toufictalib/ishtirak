package com.aizong.ishtirak.common.misc.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

@SuppressWarnings("rawtypes")
public class ExCombo<T> extends JComboBox {
    private static final long serialVersionUID = -8406426154902695060L;
    private String firstLabel = NONE;
    public static final String NONE = "None";

    public boolean withNone;

    public ExCombo() {
	this(new ArrayList<T>());
    }

    public ExCombo(T[] items) {
	this(false, items);
    }

    public ExCombo(boolean withNone, T[] items) {
	this(withNone, new ArrayList<T>(Arrays.asList(items)));
    }

    public ExCombo(Collection<T> items) {
	this(false, items);
    }

    public ExCombo(boolean withNone, Collection<T> items) {
	this(NONE, withNone, items);
    }
    
    public ExCombo(String firstLabel,boolean withNone, Collection<T> items) {
	this.firstLabel = firstLabel;
	JLabel comboLbl = (JLabel) this.getRenderer();
	if (comboLbl != null) {
	    comboLbl.setHorizontalAlignment(JLabel.RIGHT);
	    init(withNone, items);
	}
    }
    

    @SuppressWarnings("unchecked")
    private void init(boolean withNone, Collection<T> items) {
	this.withNone = withNone;
	List<T> list = new ArrayList<T>(items);

	try {
	    Collections.sort(list, new Comparator<T>() {

		@Override
		public int compare(T o1, T o2) {
		    return o1.toString().compareTo(o2.toString());
		}
	    });
	} catch (Exception e) {
	    e.printStackTrace();
	}

	DefaultComboBoxModel boxModel = new DefaultComboBoxModel(list.toArray());
	if (withNone)
	    boxModel.insertElementAt(firstLabel, 0);
	setModel(boxModel);
	if (boxModel.getSize() > 0)
	    setSelectedIndex(0);
	// ((Component)
	// getRenderer()).applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    @SuppressWarnings("unchecked")
    public T getValue() {
	Object selectedItem = getSelectedItem();
	if (withNone && selectedItem instanceof String && selectedItem.toString().equals(firstLabel))
	    return null;
	return (T) selectedItem;

    }

    public void setValues(T[] items) {
	setValues(false, Arrays.asList(items));
    }

    public void setValues(boolean withNone, Collection<T> items) {
	init(withNone, items);
    }

    public void setValues(String firstLabel, Collection<T> items) {
	this.firstLabel = firstLabel;
	setValues(true, items);

    }

    public void setValues(Collection<T> items) {
	init(false, items);
    }
}
