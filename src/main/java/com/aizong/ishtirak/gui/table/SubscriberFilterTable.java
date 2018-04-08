package com.aizong.ishtirak.gui.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.bean.SavingCallback;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ImageUtils;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.form.ContractForm;
import com.aizong.ishtirak.gui.form.ContractSwitchingForm;
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
		WindowUtils.createDialog(owner, message("subsriber.form.new"),
			new SubscriberForm(Mode.NEW, new SavingCallback() {

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
		    WindowUtils.createDialog(owner, message("subsriber.form.view"),
			    new SubscriberForm(Mode.VIEW, subscriber, null));
		}
	    }

	    @Override
	    public void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface) {
		Subscriber subscriber = ServiceProvider.get().getSubscriberService().getSubscriberById(id);
		if (subscriber != null) {
		    WindowUtils.createDialog(owner, message("subsriber.form.edit", subscriber.getName()),
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
		/*boolean yes = MessageUtils.showConfirmationMessage(owner, message("deleteRow.confirmation"),
			message("delete"));
		if (yes) {
		    List<Long> ids = new ArrayList<>();
		    ids.add(id);
		    ServiceProvider.get().getSubscriberService().deleteSubscribers(ids);
		    refreshTableInterface.refreshTable();
		}*/
		MessageUtils.showWarningMessage(owner, message("delete.forbidden"));
	    }
	});
    }

    @Override
    public ReportTableModel getReportTableModel() {
	return ServiceProvider.get().getReportServiceImpl().getSubscribers();
    }

    @Override
    protected JPanel initUI() {

	JButton btnAddContract = createAddContractBtn(message("subscription.form.add"));

	JButton btnEditContract = createEditContractBtn(message("subscription.form.edit"), Mode.UPDATE, false);

	JButton btnViewContract = createEditContractBtn(message("subscription.form.view"), Mode.VIEW, false);

	JButton btnSwitchContract = createEditContractBtn(message("subscription.form.switch"), Mode.UPDATE, true);

	JButton btnHistoryontract = createContractHistoryBtn(message("subscription.form.histroy"));

	JButton btnEditCounterHistory = createEditCounterHistoryBtn(message("counter.form.saveAndEdit"), "edit.png",
		Mode.UPDATE);

	JButton btnViewCounterHistory = createEditCounterHistoryBtn(message("counter.form.view"), "view.png",
		Mode.VIEW);

	JButton btnHistoryCounter = showCounterHistory(message("counter.form.histroy"));
	JPanel panel = new JPanel();
	panel.add(btnAddContract);
	panel.add(btnEditContract);
	panel.add(btnSwitchContract);
	panel.add(btnViewContract);
	panel.add(btnHistoryontract);
	panel.add(btnEditCounterHistory);
	panel.add(btnViewCounterHistory);
	panel.add(btnHistoryCounter);

	String leftToRightSpecs = "fill:p:grow,5dlu,p";
	DefaultFormBuilder builder = BasicForm.createBuilder(leftToRightSpecs, "p,p,p,fill:p:grow,p");
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);

	builder.append(panel, builder.getColumnCount());
	builder.append(txtFE, btnAdd);

	builder.append(new JScrollPane(table), builder.getColumnCount());

	builder.append(txtRowCount);

	return builder.getPanel();
    }

    private JButton createAddContractBtn(String title) {
	JButton btnAddContract = ButtonFactory.createBtnAdd(title);
	btnAddContract.setToolTipText(btnAddContract.getText());
	btnAddContract.addActionListener(e -> {

	    Optional<Long> selectedRowId = getSelectedRowId();
	    if (selectedRowId.isPresent()) {
		Long id = selectedRowId.get();
		Subscriber subscriber = ServiceProvider.get().getSubscriberService().getSubscriberById(id);
		if (subscriber == null) {
		    MessageUtils.showWarningMessage(getOwner(), message("subscriber.noOneSelected"));
		    return;
		}
		WindowUtils.createDialog(getOwner(), title, new ContractForm(subscriber.getId()));
	    }

	});
	return btnAddContract;
    }

    private JButton createEditContractBtn(String title, Mode mode, boolean switchSubscription) {
	JButton btnAddContract = new JButton(title, switchSubscription ? ImageUtils.getSwapIcon() : mode.getImage());
	btnAddContract.setToolTipText(btnAddContract.getText());
	btnAddContract.addActionListener(e -> {
	    Optional<Long> selectedRowId = getSelectedRowId();
	    if (selectedRowId.isPresent()) {
		Long id = selectedRowId.get();
		List<Contract> contracts = null;
		if (switchSubscription) {
		    contracts = ServiceProvider.get().getSubscriberService().getActiveContractBySubscriberId(id);
		} else {
		    contracts = ServiceProvider.get().getSubscriberService().getActiveContractBySubscriberId(id);
		}

		if (contracts == null || contracts.isEmpty()) {
		    MessageUtils.showInfoMessage(getOwner(), message("subscriber.noSubscription"));
		    return;
		}
		if (contracts.size() > 1) {

		    JPanel panel = new JPanel(new BorderLayout());
		    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
				    panelBotton
					    .add(switchSubscription ? new ContractSwitchingForm(combo.getValue(), mode)
						    : new ContractForm(combo.getValue(), mode));
				}
				panelBotton.setPreferredSize(null);
				Window owner = SwingUtilities.getWindowAncestor(panelBotton);
				if (owner != null) {
				    WindowUtils.applyRtl(owner);
				    owner.pack();
				}
			    }

			}
		    });

		    WindowUtils.createDialog(getOwner(), title, panel);

		} else {
		    WindowUtils.createDialog(getOwner(), title,
			    switchSubscription ? new ContractSwitchingForm(contracts.get(0), mode)
				    : new ContractForm(contracts.get(0), mode));
		}
	    }
	});
	return btnAddContract;
    }

    private JButton createEditCounterHistoryBtn(String title, String image, Mode mode) {
	JButton btnAddCounterHistory = new JButton(title, mode.getImage());
	btnAddCounterHistory.setToolTipText(btnAddCounterHistory.getText());
	btnAddCounterHistory.addActionListener(e -> {
	    Optional<Long> selectedRowId = getSelectedRowId();
	    if (selectedRowId.isPresent()) {
		Long id = selectedRowId.get();
		Subscriber subscriber = ServiceProvider.get().getSubscriberService().getSubscriberById(id);
		if (subscriber == null) {
		    MessageUtils.showWarningMessage(getOwner(), message("subscriber.noOneSelected"));
		    return;
		}
		JDialog createDialog = null;
		try {
		    createDialog = WindowUtils.createDialog(getOwner(), title,
			    new CounterHistoryForm(subscriber.getId(), mode));
		} catch (Exception e1) {
		    MessageUtils.showInfoMessage(getOwner(), message("counterHistory.noContract"));
		    if (createDialog != null) {
			createDialog.dispose();
		    }
		}
	    }

	});
	return btnAddCounterHistory;
    }

    interface Apply {
	void action(String title, Subscriber subscriber);
    }

    private JButton createContractHistoryBtn(String title) {
	return applyAction(title, ImageUtils.getHistoryIcon(), (title1, subscriber) -> WindowUtils
		.createDialog(getOwner(), title, new SubscriptionHistoryTablePanel(title, subscriber.getId())));
    }

    private JButton showCounterHistory(String title) {
	return applyAction(title, ImageUtils.getHistoryIcon(), (title1, subscriber) -> WindowUtils
		.createDialog(getOwner(), title1, new CounterHistoryTablePanel(title1, subscriber.getId())));

    }

    private JButton applyAction(String title, ImageIcon icon, Apply apply) {
	JButton btnAddCounterHistory = new JButton(title, icon);
	btnAddCounterHistory.setToolTipText(btnAddCounterHistory.getText());
	btnAddCounterHistory.addActionListener(e -> {
	    Optional<Long> selectedRowId = getSelectedRowId();
	    if (selectedRowId.isPresent()) {
		Long id = selectedRowId.get();
		Subscriber subscriber = ServiceProvider.get().getSubscriberService().getSubscriberById(id);
		if (subscriber == null) {
		    MessageUtils.showWarningMessage(getOwner(), message("subscriber.noOneSelected"));
		    return;
		}
		apply.action(title, subscriber);
	    }
	});
	return btnAddCounterHistory;
    }

    @Override
    protected String getAddTooltip() {
	return message(message("subscriber.form.add"));
    }
}
