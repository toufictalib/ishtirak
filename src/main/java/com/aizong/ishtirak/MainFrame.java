package com.aizong.ishtirak;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import org.springframework.stereotype.Component;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.form.BasicPanel;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.ImageHelperCustom;
import com.aizong.ishtirak.common.misc.utils.ImageUtils;
import com.aizong.ishtirak.common.misc.utils.Message;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.ImportButtonsPanel;
import com.aizong.ishtirak.gui.form.CompanyForm;
import com.aizong.ishtirak.gui.form.ExpensesForm;
import com.aizong.ishtirak.gui.form.GeneralReportButtonsPanel;
import com.aizong.ishtirak.gui.form.ReportButtonsPanel;
import com.aizong.ishtirak.gui.form.ResultForm;
import com.aizong.ishtirak.gui.table.CommonFilterTable;
import com.aizong.ishtirak.gui.table.EmployeeFilterTable;
import com.aizong.ishtirak.gui.table.EmployeeTypeFilterTable;
import com.aizong.ishtirak.gui.table.EngineFitlerTable;
import com.aizong.ishtirak.gui.table.ExpensesFitlerTable;
import com.aizong.ishtirak.gui.table.MonthlyBundleFilterTable;
import com.aizong.ishtirak.gui.table.OutExpensesFitlerTable;
import com.aizong.ishtirak.gui.table.ReportTablePanel;
import com.aizong.ishtirak.gui.table.SubscriberFilterTable;
import com.aizong.ishtirak.gui.table.SubscriptionBundleFilterTable;
import com.aizong.ishtirak.gui.table.VillageFilterTable;
import com.aizong.ishtirak.model.Contract;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

@SuppressWarnings("serial")

@Component
public class MainFrame extends JFrame {

    Message message;
    public MainFrame(Message message) {
	this.message =  message;
    }

    @PostConstruct
    public void onStart() {
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

	JButton btnSubscriberManagement = button("إدارة المشتركين", "48px_customer.png");
	btnSubscriberManagement.addActionListener(e -> {
	    SubscriberFilterTable subscriberFilterTable = new SubscriberFilterTable(e.getActionCommand());
	    openWindow(e.getActionCommand(), subscriberFilterTable);
	});

	JButton btnEngineManagement = button("إدارة المولدات", "engine.png");
	btnEngineManagement.addActionListener(e -> {
	    JPanel innerPanel = new EngineFitlerTable(e.getActionCommand());
	    openWindow(e.getActionCommand(), innerPanel);
	});
	JButton btnVillage = button("إدارة القرى", "village.png");
	btnVillage.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new VillageFilterTable(e.getActionCommand()));
	});

	JButton btnSubscriptionBundle = button("إضافة إشتراك عداد", "add.png");
	btnSubscriptionBundle.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new SubscriptionBundleFilterTable(e.getActionCommand()));
	});

	JButton btnMonthlyBundle = button("إضافة إشتراك مقطوعية", "add.png");
	btnMonthlyBundle.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new MonthlyBundleFilterTable(e.getActionCommand()));
	});

	JButton btnEmployee = button("إدارة الموظفين", "employee.png");
	btnEmployee.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new EmployeeFilterTable(e.getActionCommand()));
	});

	JButton btnEmployeeJob = button("إدارة الوظائف", "employees.png");
	btnEmployeeJob.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new EmployeeTypeFilterTable(e.getActionCommand()));
	});

	JButton btnExpenses = button("إضافة  مصاريف", "add.png");
	btnExpenses.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ExpensesForm());
	});

	JButton btnExpensesManagement = button("إدارة  مصاريف", "expenses.png");
	btnExpensesManagement.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ExpensesFitlerTable(e.getActionCommand()));
	});

	JButton btnReceipts = button("إنشاء كل الايصالات", "receipts.png");
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
	
	JButton btnImport = button("إدخال المعلومات", "import.png");
	btnImport.addActionListener(e->{
	    openWindow(btnImport.getActionCommand(), new ImportButtonsPanel());
	});

	JButton btnMonthlyReports = button("تقارير شهرية", "reports.png");
	btnMonthlyReports.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ReportButtonsPanel());
	});

	JButton btnReports = button("تقارير", "reports.png");
	btnReports.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new GeneralReportButtonsPanel());
	});

	JButton btnOutOfExpenses = button("مسحوبات المدير والموظفين", "reports.png");
	btnOutOfExpenses.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new OutExpensesFitlerTable(btnOutOfExpenses.getText()));
	});

	JButton btnCompany = button(message("mainFrame.company"), "company.png");
	btnCompany.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new CompanyForm());
	});
	
	JButton btnResult = button("نتيجة المدخول والنفقات", "result.png");
	btnResult.addActionListener(e -> {
	    JPanel resultPanel = new ResultForm(btnResult.getActionCommand());
	    resultPanel.setPreferredSize(ComponentUtils.getDimension(90, 80));
	    WindowUtils.createDialog(MainFrame.this.getOwner(), e.getActionCommand(),resultPanel );
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
	ishtirakMenu.add(btnImport);
	expensesMenu.add(btnEmployee);
	expensesMenu.add(btnEmployeeJob);
	expensesMenu.add(btnExpensesManagement);
	expensesMenu.add(btnExpenses);
	reportsMenu.add(btnMonthlyReports);
	reportsMenu.add(btnReports);
	reportsMenu.add(btnOutOfExpenses);
	reportsMenu.add(btnResult);
	miscMenu.add(btnEngineManagement);
	miscMenu.add(btnVillage);
	miscMenu.add(btnCompany);

	JPanel topPanel = createTopPanel();

	DefaultFormBuilder mainBuilder = new DefaultFormBuilder(new FormLayout("fill:p:grow", "130dlu,p"));
	mainBuilder.append(topPanel);

	JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
	panel.setBorder(ComponentUtils.emptyBorder(15));
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
	JPanel jPanel = new JPanel(new GridLayout(5, 1, 15, 15));

	jPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black), title));
	jPanel.setPreferredSize(new Dimension(250, 400));
	return jPanel;
    }

    private JPanel createTopPanel() {

	int gap = 15;
	
	JLabel lblTitle = new JLabel(message("title"), ImageHelperCustom.get().getImageIcon("logo.png"),
		SwingConstants.CENTER);
	lblTitle.setFont(new Font("Serif", Font.BOLD, 50));
	lblTitle.setVerticalTextPosition(JLabel.TOP);
	lblTitle.setHorizontalTextPosition(JLabel.CENTER);
	lblTitle.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
	
	JLabel lbtPhone = new JLabel(ServiceProvider.get().getCompany().getMainMobilePhone(), SwingConstants.CENTER);
	lbtPhone.setFont(new Font("Serif", Font.BOLD, 25));
	lbtPhone.setVerticalTextPosition(JLabel.TOP);
	lbtPhone.setHorizontalTextPosition(JLabel.CENTER);
	gap = 15;
	

	JPanel topPanel = new JPanel(new BorderLayout());
	topPanel.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black),BorderFactory.createEmptyBorder()));
	
	topPanel.add(lblTitle, BorderLayout.PAGE_START);
	topPanel.add(lbtPhone, BorderLayout.PAGE_END);
	return topPanel;
    }

    private String message(String text) {
	return message.getMessage(text);
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
	openWindow(MainFrame.this, text, component);
    }
    
    public static void openWindow(Window owner, String text, JPanel component) {
	if (component instanceof CommonFilterTable || component instanceof ReportTablePanel) {
	    component.setPreferredSize(ComponentUtils.getDimension(90, 80));
	}
	JDialog createDialog = WindowUtils.createDialog(owner, text, component);

	if (!((component instanceof CommonFilterTable) || component instanceof ReportTablePanel) && component instanceof BasicPanel) {
	    createDialog.setResizable(false);
	}
    }

    public static JDialog openWindowAsFrame(Window owner, String text, JPanel component) {
	component.setPreferredSize(ComponentUtils.getDimension(90, 80));
	JDialog createDialog = WindowUtils.createDialog(owner, text, component);
	return createDialog;
    }
    
    public static JDialog openFullWindow(Window owner, String text, JPanel component) {
   	component.setPreferredSize(ComponentUtils.getDimension(100, 100));
   	JDialog createDialog = WindowUtils.createDialog(owner, text, component);
   	return createDialog;
       }
    
    @SuppressWarnings("unused")
    private ImageIcon icon(String imagePath) {
	return ImageHelperCustom.get().getImageIcon("menus/" + imagePath);
    }

    public static JButton button(String text, String imagePath) {
	JideButton btn = new JideButton(text, ImageHelperCustom.get().getImageIcon("menus/" + imagePath));
	btn.setHorizontalAlignment(SwingConstants.RIGHT);
	btn.setButtonStyle(JideButton.TOOLBOX_STYLE);
	btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	return btn;
    }
}