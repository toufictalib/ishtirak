package com.aizong.ishtirak;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.ImageHelperCustom;
import com.aizong.ishtirak.common.misc.utils.ImageUtils;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.form.ExpensesForm;
import com.aizong.ishtirak.gui.form.GeneralReportButtonsPanel;
import com.aizong.ishtirak.gui.form.ReportButtonsPanel;
import com.aizong.ishtirak.gui.table.EmployeeFilterTable;
import com.aizong.ishtirak.gui.table.EmployeeTypeFilterTable;
import com.aizong.ishtirak.gui.table.EngineFitlerTable;
import com.aizong.ishtirak.gui.table.ExpensesFitlerTable;
import com.aizong.ishtirak.gui.table.MonthlyBundleFilterTable;
import com.aizong.ishtirak.gui.table.OutExpensesFitlerTable;
import com.aizong.ishtirak.gui.table.SubscriberFilterTable;
import com.aizong.ishtirak.gui.table.SubscriptionBundleFilterTable;
import com.aizong.ishtirak.gui.table.VillageFilterTable;
import com.aizong.ishtirak.model.Contract;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    public MainFrame() {

	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		startGui();
	    }
	});

    }

    private void startGui() {
	buildPanel();
    }

    private void buildPanel() {

	JideButton btnSubscriberManagement = button("إدارة المشتركين", "48px_customer.png");
	btnSubscriberManagement.addActionListener(e -> {
	    SubscriberFilterTable subscriberFilterTable = new SubscriberFilterTable(e.getActionCommand());
	    subscriberFilterTable.setPreferredSize(ComponentUtils.getDimension(90, 90));
	    openWindow(e.getActionCommand(), subscriberFilterTable);
	});

	JideButton btnEngineManagement = button("إدارة المولدات", "engine.png");
	btnEngineManagement.addActionListener(e -> {
	    JPanel innerPanel = new EngineFitlerTable(e.getActionCommand());
	    innerPanel.setPreferredSize(ComponentUtils.getDimension(90, 90));
	    openWindow(e.getActionCommand(), innerPanel);
	});
	JideButton btnVillage = button("إدارة القرى", "village.png");
	btnVillage.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new VillageFilterTable(e.getActionCommand()));
	});

	JideButton btnSubscriptionBundle = button("إضافة إشتراك عداد", "add.png");
	btnSubscriptionBundle.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new SubscriptionBundleFilterTable(e.getActionCommand()));
	});

	JideButton btnMonthlyBundle = button("إضافة إشتراك مقطوعية", "add.png");
	btnMonthlyBundle.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new MonthlyBundleFilterTable(e.getActionCommand()));
	});

	JideButton btnEmployee = button("إدارة الموظفين", "employee.png");
	btnEmployee.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new EmployeeFilterTable(e.getActionCommand()));
	});

	JideButton btnEmployeeJob = button("إدارة الوظائف", "employees.png");
	btnEmployeeJob.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new EmployeeTypeFilterTable(e.getActionCommand()));
	});

	JideButton btnExpenses = button("إضافة  مصاريف", "add.png");
	btnExpenses.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ExpensesForm());
	});

	JideButton btnExpensesManagement = button("إدارة  مصاريف", "expenses.png");
	btnExpensesManagement.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ExpensesFitlerTable(e.getActionCommand()));
	});

	JideButton btnReceipts = button("إنشاء كل الايصالات", "receipts.png");
	btnReceipts.addActionListener(e -> {
	    boolean yes = MessageUtils.showConfirmationMessage(MainFrame.this, "هل تريد إصدار كل الايصالات لهذا الشهر",
		    "اصدار ايصالات");
	    if (yes) {
		List<Contract> generateReceipts = ServiceProvider.get().getSubscriberService().generateReceipts();
		if (!generateReceipts.isEmpty()) {
		    DefaultFormBuilder builder = BasicForm.createBuilder("fill:p:grow");
		    builder.appendSeparator("");

		} else {
		    MessageUtils.showInfoMessage(MainFrame.this, "نجاح", "تم اصدار الايصالات بنجاح");
		}

	    }
	});

	JideButton btnMonthlyReports = button("تقارير شهرية", "reports.png");
	btnMonthlyReports.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ReportButtonsPanel());
	});

	JideButton btnReports = button("تقارير", "reports.png");
	btnReports.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new GeneralReportButtonsPanel());
	});

	JideButton btnOutOfExpenses = button("مسحوبات المدير والموظفين", "reports.png");
	btnOutOfExpenses.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new OutExpensesFitlerTable(btnOutOfExpenses.getText()));
	});

	setTitle(message("tite"));

	JPanel ishtirakMenu = createMenuPanel(message("subscritpions"));
	JPanel expensesMenu = createMenuPanel(message("expenses"));
	JPanel miscMenu = createMenuPanel(message("misc"));
	JPanel reportsMenu = createMenuPanel(message("reports"));
	ishtirakMenu.add(btnSubscriberManagement);
	ishtirakMenu.add(btnSubscriptionBundle);
	ishtirakMenu.add(btnMonthlyBundle);
	ishtirakMenu.add(btnReceipts);
	expensesMenu.add(btnEmployee);
	expensesMenu.add(btnEmployeeJob);
	expensesMenu.add(btnExpensesManagement);
	expensesMenu.add(btnExpenses);
	reportsMenu.add(btnMonthlyReports);
	reportsMenu.add(btnReports);
	reportsMenu.add(btnOutOfExpenses);
	miscMenu.add(btnEngineManagement);
	miscMenu.add(btnVillage);

	JPanel topPanel = createTopPanel();

	DefaultFormBuilder mainBuilder = new DefaultFormBuilder(new FormLayout("fill:p:grow", "100dlu,p"),
		new FormDebugPanel());
	mainBuilder.append(topPanel);

	JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,15,0));
	panel.add(ishtirakMenu);
	panel.add(expensesMenu);
	panel.add(reportsMenu);
	panel.add(miscMenu);
	mainBuilder.append(panel);

	setContentPane(mainBuilder.getPanel());
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	setSize(screenSize);
	setLocationRelativeTo(null);
	setIconImage(ImageUtils.getFrameIcon().getImage());
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	WindowUtils.applyRtl(this);
	setVisible(true);

    }

    private JPanel createMenuPanel(String title) {
	 JPanel jPanel = new JPanel(new GridLayout(4, 1, 15, 15));
	 jPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black), title));
	 return jPanel;
    }

    private JPanel createTopPanel() {

	JLabel lblTitle = new JLabel(message("title"),
		ImageHelperCustom.get().getImageIcon("logo_talaco.jpg"), SwingConstants.CENTER);
	lblTitle.setFont(new Font("Serif", Font.BOLD, 50));

	lblTitle.setVerticalTextPosition(JLabel.TOP);
	lblTitle.setHorizontalTextPosition(JLabel.CENTER);

	int gap = 15;
	lblTitle.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
	JPanel topPanel = new JPanel();
	topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	topPanel.add(lblTitle);
	return topPanel;
    }

    private String message(String text) {
	return ServiceProvider.get().getMessage().getMessage(text);
    }

    @SuppressWarnings("unused")
    private void addToButtonPanel(JPanel buttomPanel, JPanel guestPanel) {
	buttomPanel.removeAll();
	guestPanel.setPreferredSize(new Dimension(buttomPanel.getWidth(), buttomPanel.getHeight()));
	buttomPanel.add(guestPanel);
	buttomPanel.repaint();
	buttomPanel.revalidate();
    }

    private void openWindow(String text, JPanel component) {
	WindowUtils.createDialog(MainFrame.this, text, component);
    }

    @SuppressWarnings("unused")
    private ImageIcon icon(String imagePath) {
	return ImageHelperCustom.get().getImageIcon("menus/" + imagePath);
    }

    private JideButton button(String text, String imagePath) {
	JideButton btnEngineManagement = new JideButton(text,
		ImageHelperCustom.get().getImageIcon("menus/" + imagePath));
	return btnEngineManagement;
    }
}