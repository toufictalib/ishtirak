package com.aizong.ishtirak.gui.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
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
import com.aizong.ishtirak.model.Contract;
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
	
	JButton btnEditContract = createEditContractBtn("تعديل إشتراك", Mode.UPDATE);
	
	JButton btnViewContract = createEditContractBtn("معاينة إشتراك",Mode.VIEW);
	
	JButton btnEditCounterHistory = createEditCounterHistoryBtn("حفظ وتعديل عداد",Mode.UPDATE);
	
	JButton btnViewCounterHistory = createEditCounterHistoryBtn("معاينة عداد",Mode.VIEW);
	
   	String leftToRightSpecs = "fill:p:grow,5dlu,p";
   	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs);
   	builder.setDefaultDialogBorder();
   	
   	builder.appendSeparator(title);

   	
   	JPanel panel = new JPanel();
   	panel.add(btnAdd);
   	panel.add(btnEditContract);
   	panel.add(btnViewContract);
   	panel.add(btnAddContract);
   	panel.add(btnEditCounterHistory);
   	panel.add(btnViewCounterHistory);
   	
   	builder.append(panel,builder.getColumnCount());
   	builder.append(txtFE, builder.getColumnCount());

   	builder.append(new JScrollPane(table), builder.getColumnCount());

   	builder.append(txtRowCount);

   	return builder.getPanel();
       }

    private JButton createEditCounterHistoryBtn(String title, Mode mode) {
	JButton btnAddCounterHistory = new JButton(title, ImageHelperCustom.get().getImageIcon("maintenance32.png"));
	btnAddCounterHistory.setToolTipText(btnAddCounterHistory.getText());
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
				JDialog createDialog = null;
				try {
				    createDialog = WindowUtils.createDialog(getOwner(),  title, new CounterHistoryForm(subscriber.getId(), mode));
				} catch (Exception e1) {
				   MessageUtils.showInfoMessage(getOwner(), message("counterHistory.noContract"));
				  if(createDialog!=null) {
				      createDialog.dispose();
				  }
		    }
			}
		    }else {
			warnNoSelectedRow();
		    }
	});
	return btnAddCounterHistory;
    }

    private JButton createAddContractBtn() {
	JButton btnAddContract = new JButton("أضف إشتراك", ImageHelperCustom.get().getImageIcon("menus/green_circle.png"));
	btnAddContract.setToolTipText(btnAddContract.getText());
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
		    WindowUtils.createDialog(getOwner(), "إشتراك جديد", new ContractForm(subscriber.getId()));
		}
	    }else {
		warnNoSelectedRow();
	    }
	});
	return btnAddContract;
    }
    
    private JButton createEditContractBtn(String title, Mode mode) {
   	JButton btnAddContract = new JButton(title, ImageHelperCustom.get().getImageIcon("red_circle.png"));
   	btnAddContract.setToolTipText(btnAddContract.getText());
   	btnAddContract.addActionListener(e->{
   	    int selectedRow = table.getSelectedRow();
   	    if(selectedRow>=0) {
   		Object valueAt = table.getModel().getValueAt(table.convertRowIndexToModel(selectedRow), 0);
   		if(valueAt instanceof Long) {
   		    Long id = (Long) valueAt;
   		    List<Contract> contracts = ServiceProvider.get().getSubscriberService().getContractBySubscriberId(id);
   			if (contracts == null || contracts.isEmpty()) {
   			    MessageUtils.showWarningMessage(getOwner(), "مشترك ناقص", "لا يوجد اشتراكات لهذا المشترك");
   			    return;
   			}
		    if (contracts.size() > 1) {

			JPanel panel = new JPanel(new BorderLayout());
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			JPanel panelBotton = new JPanel();
			panelBotton.setPreferredSize(new Dimension(400, 350));
			ExCombo<Contract> combo = new ExCombo<>(true, contracts);
			panel.add(combo, BorderLayout.PAGE_START);
			panel.add(panelBotton, BorderLayout.CENTER);
			combo.addItemListener(new ItemListener() {

			    @Override
			    public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {

				    panelBotton.removeAll();
				    if (combo.getValue() != null) {
					panelBotton.add(new ContractForm(combo.getValue(), mode));
				    }
				    panelBotton.revalidate();
				    panelBotton.repaint();

				    Window owner = SwingUtilities.getWindowAncestor(panel);
				    if (owner != null) {
					WindowUtils.applyRtl(owner);
					getOwner().pack();
				    }
				}

			    }
			});

			WindowUtils.createDialog(getOwner(), "تعديل اشتراك", panel);

		    }else {
   		    WindowUtils.createDialog(getOwner(), "تعديل اشتراك", new ContractForm(contracts.get(0), mode));
   			}
   		}
   	    }else {
   		warnNoSelectedRow();
   	    }
   	});
   	return btnAddContract;
       }
}
