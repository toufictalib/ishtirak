package com.aizong.ishtirak.gui.form;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.AlphanumComparator;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.ImageUtils;
import com.aizong.ishtirak.common.misc.utils.IntergerTextField;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.Mode;
import com.aizong.ishtirak.common.misc.utils.MonthYearCombo;
import com.aizong.ishtirak.common.misc.utils.ProgressBar;
import com.aizong.ishtirak.common.misc.utils.ProgressBar.ProgressBarListener;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.demo.ReceiptBean;
import com.aizong.ishtirak.gui.table.SubscriberHistoryTablePanel;
import com.aizong.ishtirak.model.Address;
import com.aizong.ishtirak.model.Bundle;
import com.aizong.ishtirak.model.Contract;
import com.aizong.ishtirak.model.Engine;
import com.aizong.ishtirak.model.SubscriptionBundle;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.report.exception.DRException;

public class ContractForm extends BasicForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected JTextField txtCounterId;
    protected JCheckBox cbActive;

    protected ExCombo<Engine> comboEngines;
    protected ExCombo<Bundle> comboBundles;
    protected ExCombo<Village> comboVillages;
    protected JTextField txtRegion;
    protected JTextArea txtAddress;
    protected IntergerTextField txtSettelementFees;

    protected Long subscriberId;
    protected Contract contract;

    protected Mode mode = Mode.NEW;

    protected Map<Long, Set<String>> counterPerEngine = new HashMap<>();

    protected List<String> allKeys = new ArrayList<>();

    public ContractForm(Long subscriberId) {
	this.subscriberId = subscriberId;
	init();
    }

    public ContractForm(Contract contract, Mode mode) {
	this.contract = contract;
	this.subscriberId = contract.getSubscriberId();
	this.mode = mode;
	init();
    }

    private void init() {
	if (mode != Mode.VIEW || mode != Mode.DELETE) {
	    counterPerEngine = ServiceProvider.get().getSubscriberService().getContractUniqueCodesByEngine();
	    counterPerEngine.entrySet().forEach(e -> allKeys.addAll(e.getValue()));
	    Collections.sort(allKeys, new AlphanumComparator());
	} else {
	    allKeys = new ArrayList<>();
	}

	initializePanel();
	fillData();
    }

    protected void fillData() {

	if (contract == null) {
	    return;
	}

	txtCounterId.setText(contract.getContractUniqueCode());
	cbActive.setSelected(contract.isActive());

	Bundle bundle = ServiceProvider.get().getSubscriberService().getBundleById(contract.getBundleId());
	comboBundles.setSelectedItem(bundle);
	comboEngines.setSelectedItem(new Engine(contract.getEngineId()));

	if (contract.getAddress() != null) {
	    comboVillages.setSelectedItem(new Village(contract.getAddress().getVillageId()));
	    txtRegion.setText(contract.getAddress().getRegion());
	    txtAddress.setText(contract.getAddress().getDetailedAddress());

	}
    }

    @Override
    protected void initComponents() {

	txtCounterId = new JTextField();
	txtSettelementFees = new IntergerTextField();

	cbActive = new JCheckBox();
	cbActive.setSelected(true);

	comboEngines = new ExCombo<>(ServiceProvider.get().getSubscriberService().getEngines());
	comboEngines.addItemListener(e -> {
	    if (e.getStateChange() == ItemEvent.SELECTED) {
		if (comboEngines.getValue() != null) {

		    if (mode == Mode.VIEW) {
			txtCounterId.setText(contract.getContractUniqueCode());
		    } else {
			Set<String> set = counterPerEngine.get(comboEngines.getValue().getId());
			if (set != null && !set.isEmpty()) {
			    List<String> list = new ArrayList<>(set);
			    Collections.sort(list, new AlphanumComparator());
			    txtCounterId.setText(list.get(list.size() - 1));
			}
		    }

		}
	    }
	});
	comboBundles = new ExCombo<>(ServiceProvider.get().getSubscriberService().getAllBundles());
	comboBundles.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    txtSettelementFees.setValue(comboBundles.getValue().getSettlementFees());
		}

	    }
	});
	comboVillages = new ExCombo<>(ServiceProvider.get().getSubscriberService().getVillages());
	txtRegion = new JTextField();
	txtAddress = new JTextArea(2, 1);
	txtAddress.setLineWrap(true);

	txtAddress.setBorder(UIManager.getBorder("TextField.border"));

	if (mode == Mode.UPDATE) {
	    allowEdit(false);
	}

	ComponentUtils.fireCombobBoxItemListener(comboBundles);
	ComponentUtils.fireCombobBoxItemListener(comboEngines);
    }

    protected void allowEdit(boolean allow) {
	comboBundles.setEnabled(allow);
    }

    protected Component buildPanel(DefaultFormBuilder builder) {
	builder.appendSeparator(message("contract.form.seperator"));

	if (mode == Mode.VIEW) {
	    
	    txtCounterId.setEditable(false);
	    
	    JButton btnCreateReceipt = ButtonFactory.button(message("contract.form.createReceipt"),
		    ImageUtils.getAddIcon());
	    btnCreateReceipt.addActionListener(createReceipt());

	    JButton btnPrintReceipt = ButtonFactory.button(message("contract.form.createReceiptAndPrint"),
		    ImageUtils.getPrintIcon());
	    btnPrintReceipt.addActionListener(e -> {

		MonthYearCombo monthYearCombo = new MonthYearCombo();
		Object[] options = { message("contract.print.button"), message("import.counter.close") };
		int answer = JOptionPane.showOptionDialog(null, monthYearCombo, message("import.title"),
			JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (answer == JOptionPane.YES_OPTION) {
		    LocalDate now = LocalDate.of(monthYearCombo.getYear(), monthYearCombo.getMonth(),
			    DateUtil.START_MONTH);

		    JFileChooser fc = new JFileChooser();
		    fc.setSelectedFile(new File(contract.getContractUniqueCode() + "_ايصال.pdf"));
		    fc.setFileFilter(new FileNameExtensionFilter("pdf Only", "pdf"));
		    int returnVal = fc.showSaveDialog(ContractForm.this);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			ProgressBar.execute(new ProgressBarListener<JasperConcatenatedReportBuilder>() {

			    @Override
			    public JasperConcatenatedReportBuilder onBackground() throws Exception {
				DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth(now);
				List<ReceiptBean> receipts = ServiceProvider.get().getSubscriberService()
					.getContractReceipt(contract.getId(), dateRange.getStartDateAsString(),
						dateRange.getEndDateAsString());
				return receipts.isEmpty() ? null
					: SubscriberHistoryTablePanel.getReportBuilder(receipts);
			    }

			    @Override
			    public void onDone(JasperConcatenatedReportBuilder jasperConcatenatedReportBuilder) {
				if (jasperConcatenatedReportBuilder == null) {
				    MessageUtils.showWarningMessage(ContractForm.this, message("contract.print.empty"));
				    return;
				}

				try {
				    // setPageFormat(PageType.A6,
				    // PageOrientation.LANDSCAPE);
				    jasperConcatenatedReportBuilder.toPdf(Exporters.pdfExporter(file));
				    MessageUtils.showInfoMessage(ContractForm.this, message("receipts.export.success"));
				} catch (DRException e) {
				    e.printStackTrace();
				}

			    }
			}, ContractForm.this);
		    }
		}
	    });
	    JButton btnAddSettelmentFees = ButtonFactory.button(message("contract.form.addTransaction"),
		    ImageUtils.getAddIcon());

	    btnAddSettelmentFees.addActionListener(e -> {
		MainFrame.openWindow(ContractForm.this.getOwner(), btnAddSettelmentFees.getText(),
			new TransactionForm(contract.getId(), TransactionType.SETTELMENT_FEES));
	    });
	    builder.append(
		    ButtonBarFactory.buildRightAlignedBar(btnAddSettelmentFees, btnPrintReceipt, btnCreateReceipt),
		    builder.getColumnCount());
	}

	builder.setDefaultDialogBorder();
	builder.append(message("contract.form.counter"), txtCounterId);
	builder.append(message("contract.form.active"), cbActive);
	builder.append(message("contract.form.bundle"), comboBundles);
	if (mode == Mode.NEW) {
	    builder.append(message("bundle.form.settelementFees"), txtSettelementFees);
	}
	builder.append(message("contract.form.engine"), comboEngines);
	builder.appendSeparator(message("subsriber.form.address"));
	builder.append(message("subsriber.form.village"), comboVillages);
	builder.append(message("subsriber.form.region"), txtRegion);
	builder.append(message("subsriber.form.address"), txtAddress);
	builder.appendSeparator();

	JButton btnSave = btnSave();
	JButton btnClose = btnClose();

	if (mode == Mode.VIEW) {
	    builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose), builder.getColumnCount());
	} else if (mode == Mode.DELETE) {
	    JButton btnDelete = new JButton(message("subscription.form.delete"), ImageUtils.getDeleteIcon());
	    btnDelete.addActionListener(deleteSubscription());
	    builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnDelete), builder.getColumnCount());
	} else {
	    builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder.getColumnCount());
	}

	return builder.getPanel();
    }

    private ActionListener createReceipt() {
	return e -> {

	    MonthYearCombo monthYearCombo = new MonthYearCombo();
	    Object[] options = { message("import.counter.button"), message("import.counter.close") };
	    int answer = JOptionPane.showOptionDialog(null, monthYearCombo, message("import.title"),
		    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

	    if (answer == JOptionPane.YES_OPTION) {
		LocalDate now = LocalDate.of(monthYearCombo.getYear(), monthYearCombo.getMonth(), DateUtil.START_MONTH);
		try {
		    ServiceProvider.get().getSubscriberService().generateSelectedContractReceipt(now, contract.getId());
		    MessageUtils.showInfoMessage(ContractForm.this, message("receipts.export.success"));
		} catch (Exception e1) {
		    MessageUtils.showWarningMessage(ContractForm.this, e1.getMessage());
		}
	    }
	};
    }

    private ActionListener deleteSubscription() {
	return e -> {
	    boolean yes = MessageUtils.showConfirmationMessage(ContractForm.this,
		    message("subscription.delete", contract.getContractUniqueCode()));
	    if (yes) {

		ProgressBar.execute(new ProgressBarListener<Void>() {

		    @Override
		    public Void onBackground() throws Exception {
			ServiceProvider.get().getSubscriberService().deleteContracts(Arrays.asList(contract.getId()));
			return null;
		    }

		    @Override
		    public void onDone(Void response) {
			closeWindow();
			MessageUtils.showInfoMessage(ContractForm.this, message("subscription.success"));

		    }
		}, ContractForm.this);
	    }
	};
    }

    @Override
    protected void save() {
	Optional<List<String>> validateInputs = validateInputs();
	if (validateInputs.isPresent()) {
	    MessageUtils.showWarningMessage(getOwner(), String.join("\n", validateInputs.get()));
	    return;
	}

	Integer reatctivateSubscriptionFees = null;

	if (mode == Mode.UPDATE && contract != null && !contract.isActive() && cbActive.isSelected()) {
	    String input = MessageUtils.showInputDialog(ContractForm.this, message("subscription.reactivate.fees"));
	    try {
		if (input == null || input.isEmpty()) {

		} else {
		    reatctivateSubscriptionFees = Integer.parseInt(input);
		}
	    } catch (Exception e) {
		MessageUtils.showWarningMessage(ContractForm.this, message("input.integer"));
		return;
	    }
	}

	contract = contract == null ? new Contract() : contract;
	contract.setActive(cbActive.isSelected());

	Address address = new Address();
	address.setVillageId(comboVillages.getValue().getId());
	address.setRegion(txtRegion.getText());
	address.setDetailedAddress(txtAddress.getText());

	contract.setAddress(address);

	contract.setBundleId(comboBundles.getValue().getId());
	contract.setContractUniqueCode(txtCounterId.getText());
	contract.setEngineId(comboEngines.getValue().getId());
	contract.setSubscriberId(subscriberId);

	boolean createEmptyCounterHistory = false;
	if (comboBundles.getValue() instanceof SubscriptionBundle && contract.getId() == null) {
	    createEmptyCounterHistory = true;
	}

	ServiceProvider.get().getSubscriberService().saveContract(contract, txtSettelementFees.getValue(),
		createEmptyCounterHistory, reatctivateSubscriptionFees);
	closeWindow();

    }

    @Override
    protected Optional<List<String>> validateInputs() {
	List<String> errors = new ArrayList<>();

	if (comboBundles.getValue() == null) {
	    errors.add(errorPerfix("contract.form.bundle"));
	}

	if (txtCounterId.getText().isEmpty()) {
	    errors.add(errorPerfix("contract.form.counter"));
	}

	if (comboEngines.getValue() == null) {
	    errors.add(errorPerfix("contract.form.engine"));
	}

	if (comboVillages.getValue() == null) {
	    errors.add(errorPerfix("subsriber.form.village"));
	}

	if (!txtCounterId.getText().isEmpty() && comboEngines.getValue() != null) {

	    boolean checkCounter = true;
	    if (mode == Mode.UPDATE && contract.getContractUniqueCode().equals(txtCounterId.getText())) {
		checkCounter = false;
	    }

	    if (checkCounter) {
		if (allKeys != null && new HashSet<>(allKeys).contains(txtCounterId.getText())) {
		    errors.add(message("contractUniqueCode.redundant"));
		}
	    }

	}

	return errors.isEmpty() ? Optional.empty() : Optional.of(errors);

    }

    @Override
    protected String getLayoutSpecs() {
	return "right:pref, 4dlu, fill:100dlu:grow";
    }

}