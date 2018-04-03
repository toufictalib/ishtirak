package com.aizong.ishtirak.demo;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class ReceiptDesign {
    private InvoiceData data = new InvoiceData();
    private AggregationSubtotalBuilder<BigDecimal> totalSum;

    ReceiptBean bean = ReceiptBean.create();

    public JasperReportBuilder build() throws DRException {
	JasperReportBuilder report = report();

	HorizontalListBuilder text = cmp.horizontalList().add(cmp.text("title1"))
		.setBackgroundComponent(cmp.rectangle());
	report.setTemplate(Templates.reportTemplate).title(createTitleComponent(bean.getTitle()),
		cmp.verticalList().setStyle(stl.style(10)).setGap(5).add(
			
			cmp.vListCell(createCustomerComponent("المنطقة", bean.getVillage())).heightFixed(),
			cmp.vListCell(createCounterComponent("معلومات العداد", bean.getFullName())).heightFixed()),
		cmp.verticalGap(10));

	return report;
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
	    list.add(cmp.text(value.toString()),
		    cmp.text(label).setFixedColumns(8).setStyle(Templates.columnTitleStyle)).newRow();
	}
    }

    public static ComponentBuilder<?, ?> createTitleComponent(String label) {
	return cmp.verticalList().add(

		cmp.text(label).setStyle(Templates.bold18CenteredStyle),
		cmp.text("وصل تحصيل").setStyle(Templates.bold12CenteredStyle));
    }

    public static void main(String[] args) {
	ReceiptDesign design = new ReceiptDesign();
	try {
	    JasperReportBuilder report = design.build();
	    report.setPageFormat(PageType.A6);
	    report.show();
	} catch (DRException e) {
	    e.printStackTrace();
	}
    }
}