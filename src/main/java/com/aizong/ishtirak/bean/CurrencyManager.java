package com.aizong.ishtirak.bean;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Company;

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

	
	public CurrencyManager() {
	}

	public SupportedCurrency getselectCurrency() {
		if(selectCurrency==null) {
			Company company = ServiceProvider.get().getSubscriberService().getCompany();
			if (company != null) {
				setSelectCurrency(company.getSelectedCurrency());
			}
		}
		return selectCurrency;
	}
	
	public void setSelectCurrency(SupportedCurrency selectCurrency) {
		this.selectCurrency = selectCurrency;
	}

	private Locale getLocale() {
		if (getselectCurrency() == SupportedCurrency.DOLLAR) {
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
	
	public static String formatWithLBP(Double value) {
		DecimalFormat decimalFormat = new DecimalFormat("###,###");
		return decimalFormat.format(value)+" "+SupportedCurrency.LBP.getCode();
	}

	public static String formatArabicNumber(Double amountTopay2) {

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
