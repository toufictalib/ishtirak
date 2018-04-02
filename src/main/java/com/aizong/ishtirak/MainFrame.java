package com.aizong.ishtirak;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;

import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.ImageHelperCustom;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.demo.ExampleRibbonFrame;
import com.aizong.ishtirak.gui.form.ExpensesForm;
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
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSwingUtilities;

@SuppressWarnings("serial")
public class MainFrame extends JRibbonFrame {

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

	JPanel buttomPanel = new JPanel();
	buttomPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

	buttomPanel.setPreferredSize(ComponentUtils.getDimension(70, 100));
	buttomPanel.setOpaque(true);
	buttomPanel.setBackground(Color.CYAN);

	JideButton btnSubscriberManagement = button("إدارة المشتركين", "48px_customer.png");
	btnSubscriberManagement.addActionListener(e -> {
	    SubscriberFilterTable subscriberFilterTable = new SubscriberFilterTable(e.getActionCommand());
	    subscriberFilterTable.setPreferredSize(ComponentUtils.getDimension(90, 90));
	    openWindow(e.getActionCommand(), subscriberFilterTable);
	});

	JideButton btnEngineManagement = button("إدارة المولدات", "48px-Crystal_Clear_app_error.png");
	btnEngineManagement.addActionListener(e -> {
	    JPanel innerPanel = new EngineFitlerTable(e.getActionCommand());
	    innerPanel.setPreferredSize(ComponentUtils.getDimension(90, 90));
	    openWindow(e.getActionCommand(), innerPanel);
	});
	JideButton btnVillage = button("إدارة القرى", "48px-Crystal_Clear_app_ksame.png");
	btnVillage.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new VillageFilterTable(e.getActionCommand()));
	});

	JideButton btnSubscriptionBundle = button("إضافة إشتراك عداد", "48px-Crystal_Clear_app_kthememgr.png");
	btnSubscriptionBundle.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new SubscriptionBundleFilterTable(e.getActionCommand()));
	});

	JideButton btnMonthlyBundle = button("إضافة إشتراك مقطوعية", "48px-Crystal_Clear_app_kthememgr.png");
	btnMonthlyBundle.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new MonthlyBundleFilterTable(e.getActionCommand()));
	});

	JideButton btnEmployee = button("إدارة الموظفين", "48px-Crystal_Clear_app_ksame.png");
	btnEmployee.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new EmployeeFilterTable(e.getActionCommand()));
	});

	JideButton btnEmployeeJob = button("إدارة الوظائف", "48px-Crystal_Clear_app_ksame.png");
	btnEmployeeJob.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new EmployeeTypeFilterTable(e.getActionCommand()));
	});

	JideButton btnExpenses = button("إضافة  مصاريف", "48px-Crystal_Clear_app_kthememgr.png");
	btnExpenses.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ExpensesForm());
	});

	JideButton btnExpensesManagement = button("إدارة  مصاريف", "48px-Crystal_Clear_app_kthememgr.png");
	btnExpensesManagement.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ExpensesFitlerTable(e.getActionCommand()));
	});

	JideButton btnReceipts = button("إنشاء كل الايصالات", "48px_customer.png");
	btnReceipts.addActionListener(e -> {
	    boolean yes = MessageUtils.showConfirmationMessage(MainFrame.this, "هل تريد إصدار كل الايصالات لهذا الشهر",
		    "اصدار ايصالات");
	    if (yes) {
		ServiceProvider.get().getSubscriberService().generateReceipts();
		MessageUtils.showInfoMessage(MainFrame.this, "نجاح", "تم اصدار الايصالات بنجاح");
	    }
	});

	JideButton btnMonthlyReports = button("تقارير شهرية", "48px-Crystal_Clear_app_kthememgr.png");
	btnMonthlyReports.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ReportButtonsPanel());
	});

	JideButton btnReports = button("تقارير", "48px-Crystal_Clear_app_kthememgr.png");
	btnReports.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ReportButtonsPanel());
	});
	
	JideButton btnOutOfExpenses = button("مسحوبات المدير والموظفين", "48px-Crystal_Clear_app_kthememgr.png");
	btnOutOfExpenses.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new OutExpensesFitlerTable(btnOutOfExpenses.getText()));
	});

	setTitle("Simple example");

	DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("p,15dlu,p"));
	builder.setDefaultDialogBorder();
	builder.append(btnSubscriberManagement);
	builder.append(btnEngineManagement);
	builder.append(btnVillage);
	builder.append(btnSubscriptionBundle);
	builder.append(btnMonthlyBundle);
	builder.append(btnEmployee);
	builder.append(btnEmployeeJob);
	builder.append(btnExpenses);
	builder.append(btnExpensesManagement);
	builder.append(btnReceipts);
	builder.append(btnMonthlyReports);
	builder.append(btnReports);
	builder.append(btnOutOfExpenses);

	try {
	    ExampleRibbonFrame.createApplicationRibbon(getRibbon());

	} catch (Exception e) {
	    e.printStackTrace();
	}
	add(JideSwingUtilities.createCenterPanel(builder.getPanel()));
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	setSize(screenSize);
	// setJMenuBar(createMenus());
	setLocationRelativeTo(null);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	WindowUtils.applyRtl(this);
	setVisible(true);

    }

    @SuppressWarnings("unused")
    private JMenuBar createMenus() {
	JMenuBar menuBar;
	JMenu menu, submenu;
	JMenuItem menuItem;
	JRadioButtonMenuItem rbMenuItem;
	JCheckBoxMenuItem cbMenuItem;

	// Create the menu bar.
	menuBar = new JMenuBar();

	// Build the first menu.
	menu = new JMenu("A Menu");
	menu.setMnemonic(KeyEvent.VK_A);
	menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
	menuBar.add(menu);

	// a group of JMenuItems
	menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
	// menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
	menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
	menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
	menu.add(menuItem);

	ImageIcon icon = ImageHelperCustom.get().getImageIcon("print.png");
	menuItem = new JMenuItem("Both text and icon", icon);
	menuItem.setMnemonic(KeyEvent.VK_B);
	menu.add(menuItem);

	menuItem = new JMenuItem(icon);
	menuItem.setMnemonic(KeyEvent.VK_D);
	menu.add(menuItem);

	// a group of radio button menu items
	menu.addSeparator();
	ButtonGroup group = new ButtonGroup();

	rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
	rbMenuItem.setSelected(true);
	rbMenuItem.setMnemonic(KeyEvent.VK_R);
	group.add(rbMenuItem);
	menu.add(rbMenuItem);

	rbMenuItem = new JRadioButtonMenuItem("Another one");
	rbMenuItem.setMnemonic(KeyEvent.VK_O);
	group.add(rbMenuItem);
	menu.add(rbMenuItem);

	// a group of check box menu items
	menu.addSeparator();
	cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
	cbMenuItem.setMnemonic(KeyEvent.VK_C);
	menu.add(cbMenuItem);

	cbMenuItem = new JCheckBoxMenuItem("Another one");
	cbMenuItem.setMnemonic(KeyEvent.VK_H);
	menu.add(cbMenuItem);

	// a submenu
	menu.addSeparator();
	submenu = new JMenu("A submenu");
	submenu.setMnemonic(KeyEvent.VK_S);

	menuItem = new JMenuItem("An item in the submenu");
	menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
	submenu.add(menuItem);

	menuItem = new JMenuItem("Another item");
	submenu.add(menuItem);
	menu.add(submenu);

	// Build second menu in the menu bar.
	menu = new JMenu("Another Menu");
	menu.setMnemonic(KeyEvent.VK_N);
	menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");

	JMenuItem menu2Item = new JMenuItem("Menu 2");
	menu.add(menu2Item);
	menuBar.add(menu);

	return menuBar;

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