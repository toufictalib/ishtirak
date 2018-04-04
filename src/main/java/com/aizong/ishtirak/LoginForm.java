package com.aizong.ishtirak;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.form.BasicPanel;
import com.aizong.ishtirak.common.misc.utils.ImageUtils;
import com.aizong.ishtirak.common.misc.utils.Message;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

@SuppressWarnings("serial")
@SpringBootApplication
public class LoginForm extends BasicForm {

    private JTextField txtUserName;
    private JPasswordField txtPassword;

    @Autowired
    Message message;

    public LoginForm() {
	super();

    }

    public void startGui() {
	initializePanel();
    }

    @Override
    protected void initComponents() {
	txtUserName = new JTextField();
	txtPassword = new JPasswordField();

    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {

	JButton btnLogin = new JButton(message("login"), ImageUtils.getLoginIcon());
	btnLogin.addActionListener(e -> {

	    Optional<List<String>> validateInputs = validateInputs();
	    if (validateInputs.isPresent()) {
		MessageUtils.showWarningMessage(getOwner(), String.join("\n", validateInputs.get()));
		return;
	    }

	    boolean login = ServiceProvider.get().getSubscriberService().login(txtUserName.getText(),
		    txtPassword.getPassword());
	    if (login) {
		closeWindow();
		new MainFrame();
	    } else {
		MessageUtils.showErrorMessage(getOwner(), message("login.badCredentials"));
	    }
	});

	JButton btnClose = new JButton(message("close"), ImageUtils.getCloseIcon());
	btnClose.addActionListener(e -> closeWindow());

	builder.appendSeparator();
	builder.append(message("login.form.userName"), txtUserName);
	builder.append(message("login.form.password"), txtPassword);
	builder.appendSeparator();
	builder.append(ButtonBarFactory.buildRightAlignedBar(btnClose, btnLogin), builder.getColumnCount());
	return builder.getPanel();
    }

    @Override
    protected String getLayoutSpecs() {
	return "p,10dlu,fill:120dlu:grow";
    }

    @Override
    protected Optional<List<String>> validateInputs() {

	List<String> errors = new ArrayList<>();
	if (txtUserName.getText().isEmpty()) {
	    errors.add(errorPerfix("login.form.userName"));
	}

	if (txtPassword.getPassword().length == 0) {
	    errors.add(errorPerfix("login.form.password"));
	}

	return errors.isEmpty() ? Optional.empty() : Optional.of(errors);
    }

    @PostConstruct
    void onStart() {
	BasicPanel.message = message;
	EventQueue.invokeLater(() -> {
	    /*
	     * LoginForm.this.startGui(); WindowUtils.createDialog(null,
	     * message.getMessage("login.form.title"), LoginForm.this);
	     */
	    try {
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		    if ("Nimbus".equals(info.getName())) {
			UIManager.setLookAndFeel(info.getClassName());

			

			Enumeration<Object> keys = UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
			    Object key = keys.nextElement();
			    Object value = UIManager.get(key);
			    if (value instanceof Font) {
				UIManager.put(key, new Font("Leitura News", Font.PLAIN, 15));
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
	    new MainFrame();
	});
    }

    public MessageSource messageSource() {
	ResourceBundleMessageSource source = new ResourceBundleMessageSource();
	source.setBasenames("i18n/messages", "i18n/enum", "i18n/cols", "i18n/form");
	source.setDefaultEncoding("UTF-8");
	return source;
    }

    @Bean
    public Message createMessage() {
	return new Message(new Locale("ar", "LB"), messageSource());
    }

    public static void main(String[] args) {
	// SpringApplication.run(LoginForm.class, args);
	/*
	 * new SpringApplicationBuilder(LoginForm.class) .headless(false)
	 * .web(false) .run(args);
	 */
	SpringApplication.run(LoginForm.class, args);
    }

}
