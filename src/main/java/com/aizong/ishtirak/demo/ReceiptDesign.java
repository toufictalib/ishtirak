package com.aizong.ishtirak.demo;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.style.PenBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;

public class ReceiptDesign {

    private static final int VALUE_FONT_SIZE = 13;

    private static final String RECEIPT_NUMBER = "وصل تحصيل";

    private static final String MAINTENANCE_NUMBER = "رقم الصيانة";

    private static final String OLD_COUNTER_VALUE = "عداد قديم";

    private static final String NEW_COUNTER_VALUE = "عداد جديد ";

    private static final String SIGNATURE = "التوقيع";

    private static final String SUBSCRIPTION_TYPE = "مقطوعية";

    private static final String MONTH = "وذلك بدل اشتراك عن شهر";

    private static final String AMOUNT = "مبلغ وقدره";

    private static final String ADDRESS = "عنوانه";

    private static final String MR = "وصلنا من السيد ";

    private static final String COUNTER_ID = "رقم العداد";

    private static final String COUNTER_DIFFERENCE = "فارق العداد";

    ReceiptBean bean = ReceiptBean.create(true);

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

	addLine(horizontalList, MR, bean.getName());
	addLine(horizontalList, ADDRESS, bean.getAddress());
	addLine(horizontalList, AMOUNT, bean.getAmountToPay());
	addLine(horizontalList, MONTH, bean.getDate());

	if (bean.isMonthlySubscription()) {
	    horizontalList.add(cmp.horizontalList().newRow(5).add(cmp.hListCell(subscriptionAmp()).heightFixedOnTop()))
		    .newRow().add(cmp.hListCell(cmp.horizontalList().add(cmp.text(""))).heightFixedOnTop()).newRow()
		    .add(cmp.line().setStyle(stl.style().setBorder(stl.penThin()))).newRow()
		    .add(cmp.text(SIGNATURE).setStyle(stl.style().setFont(stl.fontArialBold())
			    .setPadding(stl.padding().setLeft(100).setTop(5)).setBold(true)));
	} else {
	    horizontalList.add(cmp.horizontalList().newRow(5).add(cmp.hListCell(counter()).heightFixedOnTop())).newRow()
		    .add(cmp.horizontalList(counter2())).newRow()
		    .add(cmp.line().setStyle(stl.style().setBorder(stl.penThin()))).newRow()
		    .add(cmp.text(SIGNATURE).setStyle(stl.style().setFont(stl.fontArialBold())
			    .setPadding(stl.padding().setLeft(100).setTop(5)).setBold(true)));
	}
	return horizontalList;
    }

    public ComponentBuilder<?, ?> counter() {
	HorizontalListBuilder list = cmp.horizontalList();

	addCounerLine(list, COUNTER_DIFFERENCE, String.valueOf(bean.getNewCounter() - bean.getOldCounter()));
	return cmp.verticalList(list);
    }

    public ComponentBuilder<?, ?> counter2() {
	HorizontalListBuilder list = cmp.horizontalList();
	addCounerLine(list, NEW_COUNTER_VALUE, String.valueOf(bean.getNewCounter()), 5);
	list.add(cmp.text("").setFixedColumns(10));
	addCounerLine(list, OLD_COUNTER_VALUE, String.valueOf(bean.getOldCounter()), 6);

	return cmp.verticalList(list);
    }

    public ComponentBuilder<?, ?> subscriptionAmp() {
	HorizontalListBuilder list = cmp.horizontalList();
	addCounerLine(list, SUBSCRIPTION_TYPE, String.valueOf(bean.getSubscriptionType()));
	return cmp.verticalList(list);
    }

    private void addLine(HorizontalListBuilder list, String label, String value) {
	if (value != null) {
	    list.add(
		    cmp.text(value.toString())
			    .setStyle(stl.style(STYLE_RTL)
				    .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
				    .setBottomBorder(stl.penDotted()).setLeftPadding(10).setFontSize(VALUE_FONT_SIZE)
				    .setFontName("Times New Roman").setBold(true)),
		    cmp.text(label).setFixedColumns(12)
			    .setStyle(stl.style(STYLE_RTL).setFontName("Arial").bold().setFontSize(VALUE_FONT_SIZE)))
		    .newRow();
	}
    }

    private void addCounerLine(HorizontalListBuilder list, String label, String value) {
	addCounerLine(list, label, value, 12);
    }

    private void addCounerLine(HorizontalListBuilder list, String label, String value, int fixedCols) {
	if (value != null) {

	    StyleBuilder setTextAlignment = stl.style(STYLE_RTL)
		    .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE).setBold(true);
	    setTextAlignment = border(setTextAlignment);

	    list.add(
		    cmp.horizontalList().add(
			    cmp.text(value.toString())
				    .setStyle(setTextAlignment.setFontSize(VALUE_FONT_SIZE)
					    .setFontName("Times New Roman").setBold(true)),
			    cmp.text(label).setFixedColumns(fixedCols)
				    .setStyle(stl.style(STYLE_RTL).setFontName("Arial").bold().setFontSize(VALUE_FONT_SIZE)))
			    .newRow(5));
	}
    }

    private StyleBuilder border(StyleBuilder builder) {
	PenBuilder penDouble = stl.penDouble();
	return builder.setTopBorder(penDouble).setLeftBorder(penDouble).setRightBorder(penDouble)
		.setBottomBorder(penDouble);
    }

    public ComponentBuilder<?, ?> createTitleComponent(String label) {
	return cmp.horizontalList(
		cmp.verticalList().add(cmp.text(MAINTENANCE_NUMBER).setStyle(Templates.bold12CenteredStyle),
			cmp.text(bean.getMaintenanceNumber()).setStyle(Templates.bold12CenteredStyle)),

		cmp.verticalList().add(

			cmp.text(label).setStyle(stl.style(Templates.boldCenteredStyle).setFontSize(20)),
			cmp.text(RECEIPT_NUMBER).setStyle(Templates.bold12CenteredStyle)),
		cmp.verticalList().add(cmp.text(COUNTER_ID).setStyle(Templates.bold12CenteredStyle),
			cmp.text(bean.getCounterId()).setStyle(Templates.bold12CenteredStyle)));
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