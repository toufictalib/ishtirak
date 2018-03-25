package com.aizong.ishtirak;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.aizong.ishtirak.common.Mode;
import com.aizong.ishtirak.common.WindowUtils;
import com.aizong.ishtirak.engine.EngineFitlerTable;
import com.aizong.ishtirak.subscriber.form.SubscriberForm;
import com.aizong.ishtirak.subscriber.form.VillageForm;
import com.aizong.ishtirak.table.SubscriberFilterTable;
import com.aizong.ishtirak.utils.Message;

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
	JPanel jPanel = new JPanel();

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

	JTextArea textArea = new JTextArea();

	textArea.setText("Toufic Talib");
	jPanel.add(textArea);
	jPanel.add(btn);
	jPanel.add(btnVillage);
	jPanel.add(btnShow);
	jPanel.add(btnEngine);
	setTitle("Simple example");
	setContentPane(jPanel);
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