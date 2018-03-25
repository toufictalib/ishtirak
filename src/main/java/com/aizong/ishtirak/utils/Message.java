package com.aizong.ishtirak.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class Message {

	@Autowired
	private MessageSource message;

	private Locale locale;

	public Message(@Autowired final Locale locale) {
		this.locale = locale;
	}

	public String getMessage(String code, Object... params) {
		return message.getMessage(code, params, locale);
	}

	public String getEnumLabel(String code, Class<?> enumClazz) {
		return message.getMessage(enumClazz.getSimpleName().toLowerCase() + "." + code.toLowerCase(), null, locale);
	}

}
