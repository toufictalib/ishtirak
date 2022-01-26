package com.aizong.ishtirak;

import java.util.Locale;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.aizong.ishtirak.bean.CurrencyManager;
import com.aizong.ishtirak.bean.CurrencyManager.SupportedCurrency;
import com.aizong.ishtirak.common.misc.utils.Message;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = {  CassandraAutoConfiguration.class,CassandraDataAutoConfiguration.class,
	CassandraReactiveDataAutoConfiguration.class, CassandraReactiveRepositoriesAutoConfiguration.class,
	CassandraRepositoriesAutoConfiguration.class, CouchbaseAutoConfiguration.class, CouchbaseDataAutoConfiguration.class,
	 CouchbaseReactiveDataAutoConfiguration.class,  CouchbaseReactiveRepositoriesAutoConfiguration.class,
	 CouchbaseRepositoriesAutoConfiguration.class,
	 ActiveMQAutoConfiguration.class,JmxAutoConfiguration.class
	 })
@ConditionalOnNotWebApplication
//credentials >> manager/manager
//The contract_unique_code column need alawys to be modified into contract and counter_history tables
public class Application {
    @SuppressWarnings("deprecation")
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
    
    @Bean
    public CurrencyManager currencyManager() {
    	return new CurrencyManager(SupportedCurrency.LBP);
    }

    public static Locale getCurrentLocale() {
	return new Locale("ar", "LB");
    }

    public MessageSource messageSource() {
	ResourceBundleMessageSource source = new ResourceBundleMessageSource();
	source.setBasenames("i18n/messages", "i18n/enum", "i18n/cols", "i18n/form","application");
	source.setDefaultEncoding("UTF-8");
	return source;
    }
}