package com.aizong.ishtirak.common.misc.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    static public String formatCurrency(Locale currentLocale, Double value) {

	Double currencyAmount = new Double(value);
	NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);

	return currencyFormatter.format(currencyAmount);
    }
}
