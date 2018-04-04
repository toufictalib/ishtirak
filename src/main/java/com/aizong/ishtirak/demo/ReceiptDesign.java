package com.aizong.ishtirak.demo;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.math.BigDecimal;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class ReceiptDesign {
    private InvoiceData data = new InvoiceData();
    private AggregationSubtotalBuilder<BigDecimal> totalSum;

    ReceiptBean bean = ReceiptBean.create();

    StyleBuilder STYLE_RTL;

    public JasperReportBuilder build() throws DRException {
	STYLE_RTL = stl.style().setTextAlignment(HorizontalTextAlignment.RIGHT, VerticalTextAlignment.MIDDLE);
	JasperReportBuilder report = report();

	HorizontalListBuilder text = cmp.horizontalList().add(cmp.text("title1"))
		.setBackgroundComponent(cmp.rectangle());
	report.setTemplate(Templates.reportTemplate).title(createTitleComponent(bean.getTitle()),
		cmp.horizontalList().add(rtlText("توفيق طالب"), rtlText("وصلنا من السيد ")).newRow()
			.add(rtlText("ألسفيرة-البغلة"), rtlText("عنوانه")).newRow()
			.add(rtlText(bean.getAmountToPay()), rtlText("مبلغ وقدره")).newRow()
			.add(rtlText(bean.getDate()), rtlText("وذلك بدل اشتراك كهرباء عن شهر")),
		cmp.verticalGap(10));

	return report;
    }

    private TextFieldBuilder<String> rtlText(String text) {
	return cmp.text(text).setStyle(STYLE_RTL);
    }

    private ComponentBuilder<?, ?> createCustomerComponent(String label, String text) {
	HorizontalListBuilder list = cmp.horizontalList()
		.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
	addCustomerAttribute(list, "اسم المشترك", bean.getFullName());
	addCustomerAttribute(list, "العنوان", bean.getAddress());
	addCustomerAttribute(list, "المبلغ المستحق", bean.getAmountToPay());
	addCustomerAttribute(list, "شهر", bean.getDate());
	return cmp.verticalList(
		cmp.text(label).setStyle(
			stl.style().setTextAlignment(HorizontalTextAlignment.RIGHT, VerticalTextAlignment.MIDDLE)),
		list);
    }

    private ComponentBuilder<?, ?> createCounterComponent(String label, String text) {
	HorizontalListBuilder list = cmp.horizontalList()
		.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
	addCustomerAttribute(list, "رقم العداد", bean.getCounterId().toString());
	addCustomerAttribute(list, "عداد قديم", bean.getOldCounter());
	addCustomerAttribute(list, "عداد جديد", bean.getNewCounter());
	addCustomerAttribute(list, "فارق العداد", bean.getNewCounter() - bean.getOldCounter());
	return cmp.verticalList(
		cmp.text(label).setStyle(
			stl.style().setTextAlignment(HorizontalTextAlignment.RIGHT, VerticalTextAlignment.MIDDLE)),
		list);
    }

    private void addCustomerAttribute(HorizontalListBuilder list, String label, Object value) {
	if (value != null) {
	    list.add(
		    cmp.text(value.toString()).setStyle(
			    stl.style().setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)),
		    cmp.text(label).setFixedColumns(8).setStyle(Templates.columnTitleStyle)).newRow();
	}
    }

    public static ComponentBuilder<?, ?> createTitleComponent(String label) {
	return cmp.horizontalList(
		cmp.verticalList().add(cmp.text("رقم الصيانة").setStyle(Templates.bold12CenteredStyle),
			cmp.text("76 619869").setStyle(Templates.bold12CenteredStyle)),

		cmp.verticalList().add(

			cmp.text(label).setStyle(Templates.bold22CenteredStyle),
			cmp.text("وصل تحصيل").setStyle(Templates.bold12CenteredStyle)),
		cmp.verticalList().add(cmp.text("رقم الصيانة").setStyle(Templates.bold12CenteredStyle),
			cmp.text("76 619869").setStyle(Templates.bold12CenteredStyle)));
    }

    public static void main(String[] args) {
	ReceiptDesign design = new ReceiptDesign();
	try {
	    JasperReportBuilder report = design.build();
	    report.setPageFormat(PageType.A6, PageOrientation.LANDSCAPE);
	    report.show();
	} catch (DRException e) {
	    e.printStackTrace();
	}
    }
}