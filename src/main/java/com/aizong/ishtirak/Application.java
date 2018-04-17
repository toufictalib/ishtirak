package com.aizong.ishtirak;

import java.util.Locale;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.aizong.ishtirak.common.misc.utils.Message;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
	ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class).headless(false)
		.web(false).run(args);
	LoginForm appFrame = context.getBean(LoginForm.class);
	appFrame.startGui();
	appFrame.onStart();
    }

    @Bean
    public Message createMessage() {
	return new Message(getCurrentLocale(), messageSource());
    }

    public static Locale getCurrentLocale() {
	return new Locale("ar", "LB");
    }

    public MessageSource messageSource() {
	ResourceBundleMessageSource source = new ResourceBundleMessageSource();
	source.setBasenames("i18n/messages", "i18n/enum", "i18n/cols", "i18n/form");
	source.setDefaultEncoding("UTF-8");
	return source;
    }
}