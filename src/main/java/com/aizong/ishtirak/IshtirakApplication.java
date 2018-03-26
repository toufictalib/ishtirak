package com.aizong.ishtirak;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.aizong.ishtirak.common.misc.Message;
import com.aizong.ishtirak.common.misc.Mode;
import com.aizong.ishtirak.common.misc.ServiceProvider;
import com.aizong.ishtirak.common.misc.WindowUtils;
import com.aizong.ishtirak.gui.form.ContractorSearchPanel;
import com.aizong.ishtirak.gui.form.CustomerSearchPanel;
import com.aizong.ishtirak.gui.form.SubscriberForm;
import com.aizong.ishtirak.gui.form.VillageForm;
import com.aizong.ishtirak.gui.table.BundleFilterTable;
import com.aizong.ishtirak.gui.table.EngineFitlerTable;
import com.aizong.ishtirak.gui.table.MonthlyBundleFilterTable;
import com.aizong.ishtirak.gui.table.SubscriberFilterTable;
import com.aizong.ishtirak.gui.table.SubscriptionBundleFilterTable;

@SpringBootApplication
public class IshtirakApplication extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 6873896406409121740L;

    @PostConstruct
    private void init() {

    }
    
    public IshtirakApplication() {
	 try {
	     for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		           UIManager.setLookAndFeel(info.getClassName());
		           /*  UIManager.put("nimbusBase", Color.BLUE);
		            UIManager.put("nimbusBlueGrey", Color.GREEN);
		            UIManager.put("control", Color.RED); */
		               Enumeration keys = UIManager.getDefaults().keys();  
		               while (keys.hasMoreElements() ) {  
		                   Object key = keys.nextElement();  
		                   Object value = UIManager.get( key );  
		                   if ( value instanceof Font ) {  
		                       UIManager.put( key, new Font(Font.DIALOG,  Font.BOLD, 15) );  
		                   }  
		               }  
		            break;
		        }
		    }
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
		| UnsupportedLookAndFeelException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	 
	JPanel panel = new JPanel();

	JButton btn = new JButton("Add");
	btn.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		WindowUtils.createDialog(null, "مشترك جديد", new SubscriberForm(Mode.NEW));
	    }
	});

	JButton btnVillage = new JButton("Add Village");
	btnVillage.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		WindowUtils.createDialog(null, "قرية جديدة", new VillageForm());
	    }
	});

	JButton btnShow = new JButton("Show Report");
	btnShow.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {

		// WindowUtils.createDialog(null, "قرية
		// جديدة",reportFilterTableFrame );
		SubscriberFilterTable subscriberFilterTable = new SubscriberFilterTable("المشتركون");
		WindowUtils.createDialog(IshtirakApplication.this, "المشتركون", subscriberFilterTable);
	    }
	});
	
	JButton btnEngine = new JButton("Show Engine");
	btnEngine.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {

		// WindowUtils.createDialog(null, "قرية
		// جديدة",reportFilterTableFrame );
		EngineFitlerTable subscriberFilterTable = new EngineFitlerTable("المولدات");
		WindowUtils.createDialog(IshtirakApplication.this, "المولدات", subscriberFilterTable);
	    }
	});
	
	JButton btnBundle = new JButton("Show Bundle");
	btnBundle.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {

		// WindowUtils.createDialog(null, "قرية
		// جديدة",reportFilterTableFrame );
		BundleFilterTable subscriberFilterTable = new MonthlyBundleFilterTable("نوع الإشتراك");
		WindowUtils.createDialog(IshtirakApplication.this, "نوع الإشتراك", subscriberFilterTable);
	    }
	});
	
	JButton btnBundleSub = new JButton("Show Sub Bundle");
	btnBundleSub.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {

		// WindowUtils.createDialog(null, "قرية
		// جديدة",reportFilterTableFrame );
		BundleFilterTable subscriberFilterTable = new SubscriptionBundleFilterTable("نوع الإشتراك");
		WindowUtils.createDialog(IshtirakApplication.this, "بحث المشتركين", subscriberFilterTable);
	    }
	});
	
	JButton btnSearchCustomer = new JButton("Show Customer");
	btnSearchCustomer.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		WindowUtils.createDialog(IshtirakApplication.this, "بحث المشتركين", new CustomerSearchPanel());
	    }
	});
	
	JButton btnShowCounterHistory = new JButton("Show Counter History");
	btnShowCounterHistory.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		WindowUtils.createDialog(IshtirakApplication.this, "العداد", new ContractorSearchPanel());
	    }
	});
	
	JButton btnGenerateReceipts = new JButton("Generate Receipts");
	btnGenerateReceipts.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		ServiceProvider.get().getSubscriberService().generateReceipts();
	    }
	});
	

	JTextArea textArea = new JTextArea();

	textArea.setText("Toufic Talib");
	panel.add(textArea);
	panel.add(btn);
	panel.add(btnVillage);
	panel.add(btnShow);
	panel.add(btnEngine);
	panel.add(btnBundle);
	panel.add(btnBundleSub);
	panel.add(btnSearchCustomer);
	panel.add(btnShowCounterHistory);
	panel.add(btnGenerateReceipts);
	setTitle("Simple example");
	setContentPane(panel);
	setSize(300, 200);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setVisible(true);
    }

    @Bean
    public MessageSource messageSource() {
	ResourceBundleMessageSource source = new ResourceBundleMessageSource();
	source.setBasenames("i18n/messages", "i18n/enum", "i18n/enum_sql");
	source.setDefaultEncoding("UTF-8");
	return source;
    }

    @Bean
    public Message createMessage() {
	return new Message(new Locale("ar", "LB"));
    }

    public static void main(String[] args) {
	SpringApplication.run(IshtirakApplication.class, args);
	
	// new Example().setVisible(true);
    }

}