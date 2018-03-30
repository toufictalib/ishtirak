package com.aizong.ishtirak.gui.table;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ImageHelperCustom;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.form.ContractForm;
import com.aizong.ishtirak.gui.form.CounterHistoryForm;
import com.aizong.ishtirak.gui.form.SubscriberForm;
import com.aizong.ishtirak.gui.table.service.MyTableListener;
import com.aizong.ishtirak.gui.table.service.RefreshTableInterface;
import com.aizong.ishtirak.model.Subscriber;
import com.jgoodies.forms.builder.DefaultFormBuilder;

@SuppressWarnings("serial")
public class SubscriberFilterTable extends CommonFilterTable {

    public SubscriberFilterTable(String title) {
	super(title, new MyTableListener() {

	    @Override
	    public void add(Window owner, RefreshTableInterface refreshTableInterface) {
		WindowUtils.createDialog(owner, "مشترك جديد", new SubscriberForm(Mode.NEW, new SavingCallback() {

		    @Override
		    public void onSuccess(Object o) {
			refreshTableInterface.refreshTable();

		    }
		}));

	    }

	    @Override
	    public void view(Window owner, Long id) {
		Subscriber subscriber = ServiceProvider.get().getSubscriberService().getSubscriberById(id);
		if (subscriber != null) {
		    WindowUtils.createDialog(owner, "عرض المشترك", new SubscriberForm(Mode.VIEW, subscriber, null));
		}
	    }

	    @Override
	    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		Subscriber subscriber = ServiceProvider.get().getSubscriberService().getSubscriberById(id);
		if (subscriber != null) {
		    WindowUtils.createDialog(owner, " تعديل المشترك " + subscriber.getName(),
			    new SubscriberForm(Mode.UPDATE, subscriber, new SavingCallback() {

				@Override
				public void onSuccess(Object o) {
				    refreshTableInterface.refreshTable();

				}
			    }));
		}
	    }

	    @Override
	    public void delete(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		boolean yes = MessageUtils.showConfirmationMessage(owner, "هل تريد حذف هذا القيد؟", "حذف");
		if (yes) {
		    List<Long> ids = new ArrayList<>();
		    ids.add(id);
		    ServiceProvider.get().getSubscriberService().deleteSubscribers(ids);
		    refreshTableInterface.refreshTable();
		}
	    }
	});
    }

    @Override
    public ReportTableModel getReportTableModel() {
	return ServiceProvider.get().getReportServiceImpl().getSubscribers();
    }
    
    @Override
    protected JPanel initUI() {

	table.add(new JPopupMenu("تعديل مشترك"));
	JButton btnAddContract = createAddContractBtn();
	
	JButton btnAddCounterHistory = createAddCounterHistoryBtn();
	
   	String leftToRightSpecs = "fill:p:grow,5dlu,p";
   	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs);
   	builder.setDefaultDialogBorder();
   	
   	builder.appendSeparator(title);

   	
   	JPanel panel = new JPanel();
   	panel.add(btnAdd);
   	panel.add(btnAddContract);
   	panel.add(btnAddCounterHistory);
   	builder.append(txtFE, panel);

   	builder.append(new JScrollPane(table), builder.getColumnCount());

   	builder.append(txtRowCount);

   	return builder.getPanel();
       }

    private JButton createAddCounterHistoryBtn() {
	JButton btnAddCounterHistory = new JButton("أضف معلومات العداد", ImageHelperCustom.get().getImageIcon("maintenance32.png"));
	ButtonFactory.makeButtonAsIcon(btnAddCounterHistory);
	btnAddCounterHistory.addActionListener(e->{
	    int selectedRow = table.getSelectedRow();
	    if(selectedRow>=0) {
		Object valueAt = table.getModel().getValueAt(table.convertRowIndexToModel(selectedRow), 0);
		if(valueAt instanceof Long) {
		    Long id = (Long) valueAt;
		    Subscriber subscriber = ServiceProvider.get().getSubscriberService().getSubscriberById(id);
			if (subscriber == null) {
			    MessageUtils.showWarningMessage(getOwner(), "مشترك ناقص", "الرجاء اختيار مشترك");
			    return;
			}
			 WindowUtils.createDialog(getOwner(), "إدخال عداد", new CounterHistoryForm(subscriber));
		}
	    }
	   
	});
	return btnAddCounterHistory;
    }

    private JButton createAddContractBtn() {
	JButton btnAddContract = new JButton("أضف إشتراك", ImageHelperCustom.get().getImageIcon("menus/green_circle.png"));
	ButtonFactory.makeButtonAsIcon(btnAddContract);
	btnAddContract.addActionListener(e->{
	    int selectedRow = table.getSelectedRow();
	    if(selectedRow>=0) {
		Object valueAt = table.getModel().getValueAt(table.convertRowIndexToModel(selectedRow), 0);
		if(valueAt instanceof Long) {
		    Long id = (Long) valueAt;
		    Subscriber subscriber = ServiceProvider.get().getSubscriberService().getSubscriberById(id);
			if (subscriber == null) {
			    MessageUtils.showWarningMessage(getOwner(), "مشترك ناقص", "الرجاء اختيار مشترك");
			    return;
			}
		    WindowUtils.createDialog(getOwner(), "إشتراك جديد", new ContractForm(subscriber));
		}
	    }
	});
	return btnAddContract;
    }
}
