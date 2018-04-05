package com.aizong.ishtirak.demo;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.math.BigDecimal;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.PenBuilder;
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
	STYLE_RTL = stl.style().setTextAlignment(HorizontalTextAlignment.RIGHT, VerticalTextAlignment.MIDDLE)
		.setPadding(7);
	JasperReportBuilder report = report();

	report.setTemplate(Templates.reportTemplate).title(createTitleComponent(bean.getTitle()),
		cmp.horizontalList().add(cmp.verticalGap(25)).add(createCustomerInfo()).newRow());

	cmp.verticalGap(10);

	return report;
    }

    private HorizontalListBuilder createCustomerInfo() {
	HorizontalListBuilder horizontalList = cmp.horizontalList();

	addLine(horizontalList, "وصلنا من السيد ", "توفيق طالب");
	addLine(horizontalList, "عنوانه", "ألسفيرة-البغلة");
	addLine(horizontalList, "مبلغ وقدره", bean.getAmountToPay());
	addLine(horizontalList, "وذلك بدل اشتراك كهرباء عن شهر", bean.getDate());

	horizontalList.add(cmp.horizontalList().newRow(5).add(cmp.hListCell(counter()).heightFixedOnTop())).newRow()
		.add(cmp.hListCell(counter2()).heightFixedOnTop()).newRow()
		.add(cmp.line().setStyle(stl.style().setBorder(stl.penThin()))).newRow()
		.add(cmp.text("التوقيع").setStyle(
			stl.style().setFont(stl.fontArialBold()).setPadding(stl.padding().setLeft(100).setTop(5)).setBold(true)));
	return horizontalList;
    }

    public ComponentBuilder<?, ?> counter() {
	HorizontalListBuilder list = cmp.horizontalList();
	addCounerLine(list, "فارق العداد", "600");
	addCounerLine(list, "رقم العداد", "12050");
	return cmp.verticalList(list);
    }

    public ComponentBuilder<?, ?> counter2() {
	HorizontalListBuilder list = cmp.horizontalList();
	addCounerLine(list, "عداد جديد ", "1800");
	addCounerLine(list, "عداد قديم", "1200");
	return cmp.verticalList(list);
    }

    private void addLine(HorizontalListBuilder list, String label, String value) {
	if (value != null) {
	    list.add(
		    cmp.text(value.toString())
			    .setStyle(stl.style(STYLE_RTL)
				    .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
				    .setBottomBorder(stl.penDotted()).setLeftPadding(10)),
		    cmp.text(label).setFixedColumns(12).setStyle(stl.style(STYLE_RTL).bold().setFontSize(10))).newRow();
	}
    }

    private void addCounerLine(HorizontalListBuilder list, String label, String value) {
	if (value != null) {

	    StyleBuilder setTextAlignment = stl.style(STYLE_RTL).setTextAlignment(HorizontalTextAlignment.CENTER,
		    VerticalTextAlignment.MIDDLE);
	    setTextAlignment = border(setTextAlignment);

	    list.add(cmp.horizontalList()
		    .add(cmp.text(value.toString()).setStyle(setTextAlignment),
			    cmp.text(label).setFixedColumns(12).setStyle(stl.style(STYLE_RTL).bold().setFontSize(10)))
		    .newRow(5));
	}
    }

    private StyleBuilder border(StyleBuilder builder) {
	PenBuilder penDouble = stl.penDouble();
	return builder.setTopBorder(penDouble).setLeftBorder(penDouble).setRightBorder(penDouble)
		.setBottomBorder(penDouble);
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