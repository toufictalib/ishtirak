package com.aizong.ishtirak.bean;

public class LogBean {

    private LogTypes logTypes;
    private String text;

    public LogBean(LogTypes logTypes, String text) {
	super();
	this.logTypes = logTypes;
	this.text = text;
    }

    public LogTypes getLogTypes() {
	return logTypes;
    }

    public void setLogTypes(LogTypes logTypes) {
	this.logTypes = logTypes;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public static LogBean createWarning(String text) {
	return new LogBean(LogTypes.WARNING, text);
    }
}
