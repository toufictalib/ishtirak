package com.aizong.ishtirak.bean;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyManager {

	public enum SupportedCurrency {

		LBP("ل.ل."), DOLLAR("$");

		private String code;

		SupportedCurrency(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

	}

	private SupportedCurrency selectCurrency;

	public CurrencyManager(SupportedCurrency selectCurrency) {
		this.selectCurrency = selectCurrency;
	}

	public SupportedCurrency getselectCurrency() {
		return selectCurrency;
	}
	
	public void setSelectCurrency(SupportedCurrency selectCurrency) {
		this.selectCurrency = selectCurrency;
	}

	private Locale getLocale() {
		if (selectCurrency == SupportedCurrency.DOLLAR) {
			return new Locale("en", "US");
		}
		return new Locale("ar", "LB");
	}

	public String formatCurrency(Double value) {
		Double currencyAmount = new Double(value);
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(getLocale());
		return currencyFormatter.format(currencyAmount);
	}

	public static String format(Double value) {
		DecimalFormat decimalFormat = new DecimalFormat("###,###");
		return decimalFormat.format(value);
	}

	public static String formatCurrencyLbp(Double amountTopay2) {

		DecimalFormat formatter = (DecimalFormat) NumberFormat
				.getInstance(new Locale.Builder().setLanguageTag("ar-LB-u-nu-arab").build());
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

		symbols.setGroupingSeparator(',');
		formatter.setDecimalFormatSymbols(symbols);

		return formatter.format(amountTopay2);
	}

	public Double getTheRightValue(Double amount) {
		if(getselectCurrency()==SupportedCurrency.DOLLAR) {
			return amount;
		}
		
		Double a = Math.floor(amount / 1000) * 1000;
		if ((amount - a) >= 500) {
			return a + 1000;
		}
		return a;
	}

}
