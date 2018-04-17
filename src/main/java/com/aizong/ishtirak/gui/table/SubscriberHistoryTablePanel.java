package com.aizong.ishtirak.gui.table;

import static net.sf.dynamicreports.report.builder.DynamicReports.concatenatedReport;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.aizong.ishtirak.MainFrame;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.component.ExCombo;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.ImageUtils;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.MySwingWorker;
import com.aizong.ishtirak.common.misc.utils.ProgressAction;
import com.aizong.ishtirak.common.misc.utils.SearchMonthPanel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.demo.ReceiptBean;
import com.aizong.ishtirak.demo.ReceiptDesign;
import com.aizong.ishtirak.gui.form.TransactionForm;
import com.aizong.ishtirak.gui.table.service.Response;
import com.aizong.ishtirak.model.Company;
import com.aizong.ishtirak.model.Transaction;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;

@SuppressWarnings("serial")
public class SubscriberHistoryTablePanel extends ReportTablePanel {

    private SearchMonthPanel searchMonthPanel;
    private JTextField txtCounterId;
    private JCheckBox cbAll;
    private ExCombo<String> comboPaid;
    private JButton btnSearch;

    public SubscriberHistoryTablePanel(String title) {
	super();
	this.title = title;
	start();
    }

    @Override
    protected int getTotalTargetedColumn() {
        return 3;
    }
    
    @Override
    protected JPanel initUI() {

	DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth();
	searchMonthPanel = SearchMonthPanel.createDefault(dateRange.getStartDate(), dateRange.getStartDate());

	txtCounterId = new JTextField();
	txtCounterId.setPreferredSize(new Dimension(150, txtCounterId.getPreferredSize().height));
	cbAll = new JCheckBox(message("all"));
	cbAll.setSelected(true);
	cbAll.addActionListener(e -> {
	    txtCounterId.setText("");
	    txtCounterId.setEnabled(!cbAll.isSelected());
	});

	ComponentUtils.fireCheckBox(cbAll);

	btnSearch = ButtonFactory.createBtnSearch();

	comboPaid = new ExCombo<>(message("all"), true,
		Arrays.asList(message("transaction.paid"), message("transaction.unpaid")));
	comboPaid.setPreferredSize(new Dimension(100, comboPaid.getPreferredSize().height));

	btnSearch.addActionListener(search());

	JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel.add(searchMonthPanel);
	panel.add(new JLabel(message("contract_unique_code")));
	panel.add(txtCounterId);
	panel.add(cbAll);
	panel.add(new JLabel(message("paid")));
	panel.add(comboPaid);
	panel.add(btnSearch);

	JButton btnEdit = ButtonFactory.createBtnEdit();
	btnEdit.addActionListener(editTransaction(btnEdit));

	JButton btnDelete = ButtonFactory.createBtnDelete();
	btnDelete.addActionListener(deleteTransaction());
	

	JButton btnPaymentStatus = ButtonFactory.button(message("payment.change"), ImageUtils.getSwitchPayment());
	btnPaymentStatus.addActionListener(paymentAction(btnSearch, btnPaymentStatus));

	JButton btnPrint = ButtonFactory.createBtnPrint();
	btnPrint.setText(message("receipt.print"));
	btnPrint.addActionListener(printReceipts(false));
	
	JButton btnPrintAll = ButtonFactory.createBtnPrint();
	btnPrintAll.setText(message("receipt.printAll"));
	btnPrintAll.addActionListener(printReceipts(true));
	
	JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	panel2.add(btnEdit);
	panel2.add(btnDelete);
	panel2.add(btnPaymentStatus);
	panel2.add(btnPrint);
	panel2.add(btnPrintAll);

	DefaultFormBuilder builder = BasicForm.createBuilder("fill:p:grow", "p,p,p,p,fill:p:grow,p,p,p");
	builder.setDefaultDialogBorder();

	builder.appendSeparator(title);
	
	builder.append(panel, builder.getColumnCount());
	builder.appendSeparator();
	builder.append(panel2, builder.getColumnCount());
	// builder.append(txtFE, 3);

	JScrollPane scrollPane = new JScrollPane(table);
	// scrollPane.setPreferredSize(ComponentUtils.getDimension(60, 60));
	builder.append(scrollPane, builder.getColumnCount());

	builder.append(txtRowCount, builder.getColumnCount());

	// builder.append("المجموع", txtTotal);

	return builder.getPanel();
    }

    private ActionListener printReceipts(boolean all) {
	return l -> {

	    
	    Optional<Long> transactionId = getSelectedRowId(false);
	    
	    List<Long> selectedTransactions = new ArrayList<>();
	    if (!all) {
		if(transactionId.isPresent()) {
		    selectedTransactions.add(transactionId.get());
		}else {
		    MessageUtils.showWarningMessage(SubscriberHistoryTablePanel.this, message("table.row.select.missing"));
			return;
		}
	    }


		JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(new File("ايصالات.pdf"));
		fc.setFileFilter(new FileNameExtensionFilter("pdf Only", "pdf"));
		int returnVal = fc.showSaveDialog(SubscriberHistoryTablePanel.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    MySwingWorker.execute(new ProgressAction<List<ReceiptBean>>() {

			@Override
			public List<ReceiptBean> action() {

			    DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth();
			    return ServiceProvider.get().getSubscriberService().getReceipts(
				    selectedTransactions.isEmpty() ? null : selectedTransactions, dateRange.getStartDateAsString(),
				    dateRange.getEndDateAsString());
			}

			@Override
			public void success(List<ReceiptBean> receipts) {
			    
			    if(!all && receipts.isEmpty()) {
				MessageUtils.showWarningMessage(SubscriberHistoryTablePanel.this, message("receipts.empty"));
				return;
			    }
			    
			    List<JasperReportBuilder> list = new ArrayList<>();
			    ReceiptDesign design = new ReceiptDesign();
			    for (ReceiptBean receiptBean : receipts) {

				JasperReportBuilder report;
				try {
				    Company company = ServiceProvider.get().getCompany();
				    report = design.build(receiptBean, company.getName(),
					    company.getMaintenanceNumber());
				    report.setPageFormat(PageType.A6, PageOrientation.LANDSCAPE);
				    if (report != null) {
					list.add(report);
				    }
				} catch (DRException e) {
				    e.printStackTrace();
				}

			    }

			    try {
				//setPageFormat(PageType.A6, PageOrientation.LANDSCAPE);
				JasperConcatenatedReportBuilder concatenate = concatenatedReport().concatenate(list.toArray(new JasperReportBuilder[0]));
				concatenate.toPdf(Exporters.pdfExporter(file));
				MessageUtils.showInfoMessage(SubscriberHistoryTablePanel.this, message("receipts.export.success"));
			    } catch (DRException e) {
				e.printStackTrace();
			    }
			}

			@Override
			public void failure(Exception e) {

			}
		    });
		}
	    
	};
    }

    private ActionListener paymentAction(JButton btnSearch, JButton btnPaymentStatus) {
	return e -> {

	    Optional<Long> selectedRowId = getSelectedRowId();
	    if (selectedRowId.isPresent()) {

		JRadioButton rdpayement = new JRadioButton(message("payment.btnPayment"));
		rdpayement.setSelected(true);

		JRadioButton rdNotPayment = new JRadioButton(message("payment.btnNoPayment"));
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdpayement);
		buttonGroup.add(rdNotPayment);

		JButton btnClose = ButtonFactory.createBtnClose();
		
		JButton btnSave = ButtonFactory.createBtnSave();
		btnSave.addActionListener(k -> {
		    MySwingWorker.execute(new ProgressAction<Void>() {

			@Override
			public Void action() {
			    List<Long> transactionIds = Arrays.asList(selectedRowId.get());
			    ServiceProvider.get().getSubscriberService().updatePayment(transactionIds,
				    rdpayement.isSelected());
			    return null;
			}

			@Override
			public void success(Void t) {
			    btnClose.doClick();
			    btnSearch.doClick();

			}

			@Override
			public void failure(Exception e) {
			    MessageUtils.showErrorMessage(SubscriberHistoryTablePanel.this, message("payment.failure"));

			}
		    });
		});

		

		DefaultFormBuilder builder2 = BasicForm.createBuilder("p,10dlu,fill:p:grow", "p,p,p,p");
		builder2.setDefaultDialogBorder();
		builder2.appendSeparator(btnPaymentStatus.getText());
		builder2.append(rdpayement);
		builder2.append(rdNotPayment);
		builder2.appendSeparator();
		builder2.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnSave), builder2.getColumnCount());

		JDialog openWindow = MainFrame.openWindow(SubscriberHistoryTablePanel.this.getOwner(),
			btnPaymentStatus.getText(), builder2.getPanel());
		btnClose.addActionListener(s -> openWindow.dispose());

	    }

	};
    }

    private ActionListener deleteTransaction() {
	return e -> {

	    boolean yes = MessageUtils.showConfirmationMessage(SubscriberHistoryTablePanel.this.getOwner(),
		    message("deleteRow.confirmation"), message("delete"));
	    if (yes) {
		Optional<Long> selectedRowId = getSelectedRowId();
		if (selectedRowId.isPresent()) {
		    MySwingWorker.execute(new ProgressAction<Void>() {

			@Override
			public Void action() {
			    ServiceProvider.get().getSubscriberService()
				    .deleteTransactions(Arrays.asList(selectedRowId.get()));
			    return null;
			}

			@Override
			public void success(Void t) {
			    MessageUtils.showInfoMessage(SubscriberHistoryTablePanel.this, message("delete.success"));
			    btnSearch.doClick();
			}

			@Override
			public void failure(Exception e) {
			    MessageUtils.showErrorMessage(SubscriberHistoryTablePanel.this, message("delete.failure"));

			}
		    });
		}
	    }

	};
    }

    private ActionListener editTransaction(JButton btnEdit) {
	return e -> {
	    Optional<Long> transactionId = getSelectedRowId();
	    if (transactionId.isPresent()) {
		MySwingWorker.execute(new ProgressAction<Transaction>() {

		    @Override
		    public Transaction action() {
			return ServiceProvider.get().getSubscriberService().getTransactionById(transactionId.get());
		    }

		    @Override
		    public void success(Transaction transaction) {
			if(transaction!=null) {
			MainFrame.openWindow(SubscriberHistoryTablePanel.this.getOwner(), btnEdit.getText(),
				new TransactionForm(transaction, reSearch()));
			}

		    }

		    private Response reSearch() {
			return new Response() {
			    
			    @Override
			    public void success(Object o) {
				btnSearch.doClick();
				
			    }
			    
			    @Override
			    public void failure() {
				// TODO Auto-generated method stub
				
			    }
			};
		    }

		    @Override
		    public void failure(Exception e) {

		    }
		});
	    }
	};
    }

    private ActionListener search() {
	return e -> {
	    try {

		String paymentStatus = comboPaid.getValue();
		Boolean paid = null;
		if (paymentStatus != null) {
		    paid = message("transaction.paid").equals(paymentStatus) ? true : false;
		}

		LocalDate fromLocaleDate = LocalDate.of(searchMonthPanel.getSelectedFromYear(),
			searchMonthPanel.getSelectedFromMonth(), 5);
		DateRange fromDateRange = DateUtil.getStartEndDateOfCurrentMonth(fromLocaleDate);

		LocalDate toLocaleDate = LocalDate.of(searchMonthPanel.getSelectedToYear(),
			searchMonthPanel.getSelectedToMonth(), 5);
		DateRange toDateRange = DateUtil.getStartEndDateOfCurrentMonth(toLocaleDate);

		List<String> uniqueContractIds = null;
		if (!cbAll.isSelected()) {
		    if (txtCounterId.getText().isEmpty()) {
			MessageUtils.showWarningMessage(SubscriberHistoryTablePanel.this, message("counter.empty"));
			return;
		    }

		    uniqueContractIds = new ArrayList<>();
		    for (String s : txtCounterId.getText().split(",")) {
			uniqueContractIds.add(s.trim());
		    }
		}

		ReportTableModel reportTableModel = ServiceProvider.get().getReportServiceImpl()
			.getContractHistoryPerContractOrALl(uniqueContractIds, fromDateRange.getStartDateAsString(),
				toDateRange.getEndDateAsString(), paid);
		fillTable(reportTableModel);
	    } catch (Exception e1) {
		e1.printStackTrace();
		MessageUtils.showErrorMessage(getOwner(), e1.getMessage());
	    }
	};
    };

    @Override
    public ReportTableModel getReportTableModel() {
	return null;
    }

    public void warnNoSelectedRow() {
	MessageUtils.showInfoMessage(getOwner(), message("table.row.select.missing"));
    }

    protected Optional<Long> getSelectedRowId() {
	return getSelectedRowId(true);
    }
    protected Optional<Long> getSelectedRowId(boolean warn) {
	int selectedRow = table.getSelectedRow();
	if (selectedRow >= 0) {
	    Object valueAt = table.getModel().getValueAt(table.convertRowIndexToModel(selectedRow), 0);
	    if (valueAt instanceof Long) {
		return Optional.of((Long) valueAt);
	    }
	} else {
	    if (warn) {
		warnNoSelectedRow();
	    }
	}
	return Optional.empty();
    }

}
