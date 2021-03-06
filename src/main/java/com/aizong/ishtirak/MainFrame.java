package com.aizong.ishtirak;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import com.aizong.ishtirak.bean.LogBean;
import com.aizong.ishtirak.bean.ReportTableModel;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.form.BasicPanel;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.ImageHelperCustom;
import com.aizong.ishtirak.common.misc.utils.ImageUtils;
import com.aizong.ishtirak.common.misc.utils.Message;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.MonthYearCombo;
import com.aizong.ishtirak.common.misc.utils.ProgressBar;
import com.aizong.ishtirak.common.misc.utils.ProgressBar.ProgressBarListener;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.aizong.ishtirak.gui.DatabaseUtils;
import com.aizong.ishtirak.gui.ImportButtonsPanel;
import com.aizong.ishtirak.gui.SummaryTablePanel;
import com.aizong.ishtirak.gui.form.CompanyForm;
import com.aizong.ishtirak.gui.form.ExpensesForm;
import com.aizong.ishtirak.gui.form.ExpensesReportButtonsPanel;
import com.aizong.ishtirak.gui.form.GeneralReportButtonsPanel;
import com.aizong.ishtirak.gui.form.ReportButtonsPanel;
import com.aizong.ishtirak.gui.form.ResultForm;
import com.aizong.ishtirak.gui.form.SortingSubscribersPanel;
import com.aizong.ishtirak.gui.table.CommonFilterTable;
import com.aizong.ishtirak.gui.table.EmployeeFilterTable;
import com.aizong.ishtirak.gui.table.EmployeeTypeFilterTable;
import com.aizong.ishtirak.gui.table.EngineFitlerTable;
import com.aizong.ishtirak.gui.table.ExpensesFitlerTable;
import com.aizong.ishtirak.gui.table.MonthlyBundleFilterTable;
import com.aizong.ishtirak.gui.table.OutExpensesFitlerTable;
import com.aizong.ishtirak.gui.table.ReportTablePanel;
import com.aizong.ishtirak.gui.table.SubscriberFilterTable;
import com.aizong.ishtirak.gui.table.SubscriberHistoryTablePanel;
import com.aizong.ishtirak.gui.table.SubscriptionBundleFilterTable;
import com.aizong.ishtirak.gui.table.VillageFilterTable;
import com.aizong.ishtirak.model.Village;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

@SuppressWarnings("serial")

public class MainFrame extends JFrame {

    Message message;
    public MainFrame(Message message) {
	this.message =  message;
    }
    
    public MainFrame() {
	
    }

    public void setMessage(Message message) {
        this.message = message;
    }

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

	//Section 1
	JButton btnSubscriberManagement = button("إدارة المشتركين", "48px_customer.png");
	btnSubscriberManagement.addActionListener(manageSubscribers());
	
	JButton btnSubscription = button("ادارة الإشتراكات", "subreport.png");
	btnSubscription.addActionListener(e->{
	    openFullWindow(MainFrame.this, e.getActionCommand(),new SubscriberHistoryTablePanel(e.getActionCommand()));
	});

	JButton btnEngineManagement = button("إدارة المولدات", "engine.png");
	btnEngineManagement.addActionListener(e -> {
	    JPanel innerPanel = new EngineFitlerTable(e.getActionCommand());
	    openWindow(e.getActionCommand(), innerPanel);
	});
	
	JButton btnReceipts = button("إنشاء كل الايصالات", "receipts.png");
	btnReceipts.addActionListener(generateReceipts());
	
	JButton btnImport = button("إدخال المعلومات", "import.png");
	btnImport.addActionListener(e->{
	    openWindow(btnImport.getActionCommand(), new ImportButtonsPanel());
	});
	
	JButton btnSortSubscribers = button("رتب المشتركين", "sort.png");
	btnSortSubscribers.addActionListener(sortSubscribers(btnSortSubscribers));
	
	
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

	JButton btnMonthlyReports = button("تقارير شهرية", "reports.png");
	btnMonthlyReports.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ReportButtonsPanel());
	});

	JButton btnReports = button("تقارير أخرى", "reports.png");
	btnReports.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new GeneralReportButtonsPanel());
	});

	JButton btnCompany = button(message("mainFrame.company"), "company.png");
	btnCompany.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new CompanyForm());
	});
	
	JButton btnResult = button("نتيجة المدخول والنفقات", "result.png");
	btnResult.addActionListener(e -> {
	    JPanel resultPanel = new ResultForm(btnResult.getActionCommand());
	    resultPanel.setPreferredSize(ComponentUtils.getDimension(90, 80));
	    WindowUtils.createDialog(MainFrame.this, e.getActionCommand(),resultPanel );
	});
	
	JButton btnExpensesReport = button("تقارير المصاريف", "reports.png");
	btnExpensesReport.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new ExpensesReportButtonsPanel());
	});
	
	JButton btnSummary = button(message("reports.subscirption.incomeExpenses"), "subreport.png");
	btnSummary.addActionListener(e -> {
	    
	    final SummaryTablePanel panel = new SummaryTablePanel(btnMonthlyReports.getActionCommand());
	    WindowUtils.createDialog(MainFrame.this, e.getActionCommand(),
		    panel);

	});
	
	JButton btnOutOfExpenses = button("مسحوبات المدير والشركاء", "reports.png");
	btnOutOfExpenses.addActionListener(e -> {
	    openWindow(e.getActionCommand(), new OutExpensesFitlerTable(btnOutOfExpenses.getText()));
	});
	
	setTitle(ServiceProvider.get().getCompany().getName());

	JPanel ishtirakMenu = createMenuPanel(message("subscritpions"));
	JPanel expensesMenu = createMenuPanel(message("expenses"));
	JPanel miscMenu = createMenuPanel(message("misc"));
	
	JPanel reportsMenu = createMenuPanel(message("reports"));
	ishtirakMenu.add(btnSubscriberManagement);
	ishtirakMenu.add(btnSubscription);
	ishtirakMenu.add(btnReceipts);
	ishtirakMenu.add(btnImport);
	ishtirakMenu.add(btnSortSubscribers);
	
	expensesMenu.add(btnEmployee);
	expensesMenu.add(btnEmployeeJob);
	expensesMenu.add(btnExpensesManagement);
	expensesMenu.add(btnExpenses);
	expensesMenu.add(btnOutOfExpenses);
	reportsMenu.add(btnMonthlyReports);
	reportsMenu.add(btnReports);
	reportsMenu.add(btnExpensesReport);
	reportsMenu.add(btnSummary);
	reportsMenu.add(btnResult);
	miscMenu.add(btnEngineManagement);
	miscMenu.add(btnMonthlyBundle);
	miscMenu.add(btnSubscriptionBundle);
	miscMenu.add(btnVillage);
	miscMenu.add(btnCompany);

	DefaultFormBuilder mainBuilder = new DefaultFormBuilder(new FormLayout("fill:p:grow", "p,p,80dlu,p"));
	
	JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	
	JButton btnClose = MainFrame.button(message("close"), "close.png");
	btnClose.setPreferredSize(new Dimension(200,btnClose.getPreferredSize().height));
	btnClose.setHorizontalAlignment(SwingConstants.CENTER);
	btnClose.addActionListener(e->{
	    MainFrame.this.dispose();
	});
	
	JButton btnExportDatabase = MainFrame.button(message("export.db.message"), "backup.png");
	btnExportDatabase.setPreferredSize(new Dimension(200,btnExportDatabase.getPreferredSize().height));
	btnExportDatabase.setHorizontalAlignment(SwingConstants.CENTER);
	btnExportDatabase.addActionListener(e->{
	    LocalDateTime dateTime = LocalDateTime.now();
	    String format = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	    new DatabaseUtils().export(null, format);
	});
	
	closePanel.add(btnClose);
	closePanel.add(btnExportDatabase);
	
	mainBuilder.append(closePanel,mainBuilder.getColumnCount());
	mainBuilder.appendSeparator();
	mainBuilder.append(createTopPanel());

	JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
	panel.setBorder(ComponentUtils.emptyBorder(15));
	panel.add(ishtirakMenu);
	panel.add(expensesMenu);
	panel.add(reportsMenu);
	panel.add(miscMenu);
	mainBuilder.append(panel);

	setContentPane(mainBuilder.getPanel());
	setExtendedState(JFrame.MAXIMIZED_BOTH);
	setLocationRelativeTo(null);
	setIconImage(ImageUtils.getFrameIcon().getImage());
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	WindowUtils.applyRtl(this);
	setVisible(true);

    }

    private ActionListener sortSubscribers(JButton btnSortSubscribers) {
	return e->{
	    ProgressBar.execute(new ProgressBarListener<List<Village>>() {

		    @Override
		    public List<Village> onBackground() throws Exception {
			return ServiceProvider.get().getSubscriberService().getVillages();
		    }

		    @Override
		    public void onDone(List<Village> villages) {
			openWindow(btnSortSubscribers.getActionCommand(), new SortingSubscribersPanel(villages));
			
		    }
		}, MainFrame.this);
	    
	    
	};
    }

    private ActionListener manageSubscribers() {
	return e -> {
	    ProgressBar.execute(new ProgressBarListener<ReportTableModel>() {

		    @Override
		    public ReportTableModel onBackground() throws Exception {
			return ServiceProvider.get().getReportServiceImpl().getSubscribers();
		    }

		    @Override
		    public void onDone(ReportTableModel response) {
			 SubscriberFilterTable subscriberFilterTable = new SubscriberFilterTable(e.getActionCommand(),response);
			    openWindow(e.getActionCommand(), subscriberFilterTable);
		    }}
		    ,MainFrame.this);
	   
	};
    }

    private ActionListener generateReceipts() {
	return e -> {
	    
	    MonthYearCombo monthYearCombo = new MonthYearCombo();
	    Object[] options = { message("إصدار كل الايصالات"), message("import.counter.close") };
	    int answer = JOptionPane.showOptionDialog(null, monthYearCombo, message("import.title"),
		    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

	    if (answer == JOptionPane.YES_OPTION) {
			LocalDate now = LocalDate.of(monthYearCombo.getYear(), monthYearCombo.getMonth(),
			    DateUtil.START_MONTH);
			
			List<LogBean> generateReceipts = ServiceProvider.get().getSubscriberService().generateReceipts(now);
			if (!generateReceipts.isEmpty()) {
			    DefaultFormBuilder builder = BasicForm.createBuilder("fill:p:grow");
			    builder.appendSeparator("");
			    MessageUtils.showInfoMessage(MainFrame.this, "نجاح", "تم اصدار الايصالات بنجاح");
			} else {
			    MessageUtils.showInfoMessage(MainFrame.this, "نجاح", "تم اصدار الايصالات بنجاح");
			}

	    }
	};
    }

    private JPanel createMenuPanel(String title) {
	JPanel jPanel = new JPanel(new GridLayout(5, 1, 15, 15));

	jPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black), title));
	jPanel.setPreferredSize(new Dimension(275, 400));
	return jPanel;
    }

    private JPanel createTopPanel() {

	int gap = 15;
	
	JLabel lblTitle = new JLabel(ServiceProvider.get().getCompany().getName(), ImageHelperCustom.get().getImageIcon("logo.png"),
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
	
	topPanel.add(lblTitle, BorderLayout.CENTER);
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
    
    public static JDialog openWindow(Window owner, String text, JPanel component) {
	if (component instanceof CommonFilterTable || component instanceof ReportTablePanel) {
	    component.setPreferredSize(ComponentUtils.getDimension(90, 93));
	}
	JDialog createDialog = WindowUtils.createDialog(owner, text, component);

	if (!((component instanceof CommonFilterTable) || component instanceof ReportTablePanel) && component instanceof BasicPanel) {
	    createDialog.setResizable(false);
	}
	
	return createDialog;
	
    }

    public static JDialog openWindowAsFrame(Window owner, String text, JPanel component) {
	component.setPreferredSize(ComponentUtils.getDimension(90, 70));
	JDialog createDialog = WindowUtils.createDialog(owner, text, component);
	return createDialog;
    }
    
    public static JDialog openFullWindow(Window owner, String text, JPanel component) {
   	component.setPreferredSize(ComponentUtils.getDimension(100, 95));
   	JDialog createDialog = WindowUtils.createDialog(owner, text, component);
   	return createDialog;
       }
    
    @SuppressWarnings("unused")
    private ImageIcon icon(String imagePath) {
	return ImageHelperCustom.get().getImageIcon("menus/" + imagePath);
    }

    public static JButton button(String text, String imagePath) {
	return button(text, ImageHelperCustom.get().getImageIcon("menus/" + imagePath));
    }
    
    public static JButton button(String text, ImageIcon imageIcon) {
	JideButton btn = new JideButton(text, imageIcon);
	btn.setHorizontalAlignment(SwingConstants.RIGHT);
	btn.setButtonStyle(JideButton.TOOLBOX_STYLE);
	btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btn.setFont(LoginForm.getJideCustomFont());
	return btn;
    }
    
   
}