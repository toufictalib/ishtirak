package com.aizong.ishtirak.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;

@SuppressWarnings("rawtypes")
public class ExList<T> extends JList {
	private static final long serialVersionUID = -8406426154902695060L;
	public static final String NONE = "None";

	public boolean withNone;

        public ExList()
        {
            this(new ArrayList<T>());
            setValueIsAdjusting(true);
        }
        
	public ExList(T[] items) {
		this(false, items);
	}

	public ExList(boolean withNone, T[] items) {
		List<T> list = new ArrayList<T>(Arrays.asList(items));
		init(withNone, list);
	}

	public ExList(Collection<T> items) {
		this(false, items);
	}

	public ExList(boolean withNone, Collection<T> items) {
		init(withNone, items);
	}

	@SuppressWarnings({
	"unchecked"
	})
	private void init(boolean withNone, Collection<T> items) {
		this.withNone = withNone;
		List<T> list = new ArrayList<T>(items);
		
                try
                {
                Collections.sort(list,new Comparator<T>() {

                    @Override
                    public int compare(T o1, T o2) {
                        return o1.toString().compareTo(o2.toString());
                    }
                });
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

		DefaultComboBoxModel boxModel = new DefaultComboBoxModel(list.toArray());
		if (withNone)
			boxModel.insertElementAt(NONE, 0);
		setModel(boxModel);
		 //((Component) getRenderer()).applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	}

	@SuppressWarnings("unchecked")
	public T getValue() {
		Object selectedItem = getSelectedValue();
		if (withNone && selectedItem instanceof String
				&& selectedItem.toString().equals(NONE))
			return null;
		return (T) selectedItem;

	}
        
		public void setValues(T[] items)
        {
          setValues(Arrays.asList(items));
        }
        
        @SuppressWarnings({
		"unchecked"
		})

        public void setValues(List<T> items)
        {
        	  DefaultComboBoxModel boxModel = new DefaultComboBoxModel(items.toArray());
      		if (withNone)
      			boxModel.insertElementAt(NONE, 0);
      		setModel(boxModel);
        }
}
