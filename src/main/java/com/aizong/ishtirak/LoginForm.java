package com.aizong.ishtirak;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.form.BasicPanel;
import com.aizong.ishtirak.common.misc.utils.ImageUtils;
import com.aizong.ishtirak.common.misc.utils.Message;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.common.misc.utils.WindowUtils;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
public class LoginForm extends BasicForm {

    @Autowired
    Message message;
    
    private JTextField txtUserName;
    private JPasswordField txtPassword;

    

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
	    
	    boolean login = false;
	    try {
		login = ServiceProvider.get().getSubscriberService().login(txtUserName.getText(),
			    txtPassword.getPassword());
	    }catch (Exception e1) {
		MessageUtils.showErrorMessage(LoginForm.this,"خطأ في اسم المستخدم أو كلمة السر");
		return;
	    }
	    
	    if (login) {
		closeWindow();
		MainFrame mainFrame = new MainFrame();
		mainFrame.setMessage(message);
		mainFrame.onStart();
	    } else {
		MessageUtils.showErrorMessage(getOwner(), message("login.badCredentials"));
	    }
	});

	JButton btnClose = new JButton(message("close"), ImageUtils.getCloseIcon());
	btnClose.addActionListener(e -> closeWindow());

	builder.appendSeparator();
	builder.append(message("login.form.userName"), txtUserName);
	txtUserName.requestFocus();
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

    void onStart() {
	BasicPanel.message=  message;
	EventQueue.invokeLater(() -> {

	    LoginForm.this.startGui();
	   JDialog createDialog = WindowUtils.createDialog(null, message.getMessage("login.form.title"), LoginForm.this);
	   createDialog.addWindowListener( new WindowAdapter() {
	       public void windowOpened( WindowEvent e ){
	           txtUserName.requestFocus();
	       }
	   }); 
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
	});
	
	
    }


    public static Locale getCurrentLocale() {
	return Application.getCurrentLocale();
    }
    
    public static boolean isRtl() {
	return true;
    }
    
    public static void main(String[] args) {
	// SpringApplication.run(LoginForm.class, args);
	/*
	 * new SpringApplicationBuilder(LoginForm.class) .headless(false)
	 * .web(false) .run(args);
	 */
	ConfigurableApplicationContext context = new SpringApplicationBuilder(LoginForm.class).headless(false).run(args);
	LoginForm appFrame = context.getBean(LoginForm.class);
    }

}
