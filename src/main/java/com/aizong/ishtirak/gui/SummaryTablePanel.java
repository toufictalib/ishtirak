package com.aizong.ishtirak.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.SummaryBean;
import com.aizong.ishtirak.bean.TransactionType;
import com.aizong.ishtirak.common.form.BasicPanel;
import com.aizong.ishtirak.common.form.OrientationUtils;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.component.HeaderRenderer;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.DateCellRenderer;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.MySwingWorker;
import com.aizong.ishtirak.common.misc.utils.PrintUtils;
import com.aizong.ishtirak.common.misc.utils.ProgressAction;
import com.aizong.ishtirak.common.misc.utils.SearchMonthPanel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.gui.table.tablemodel.ConsumptionTableModel;
import com.aizong.ishtirak.gui.table.tablemodel.ExpensesTableModel;
import com.aizong.ishtirak.gui.table.tablemodel.IncomeTableModel;
import com.aizong.ishtirak.gui.table.tablemodel.ResultTableModel;
import com.aizong.ishtirak.gui.table.tablemodel.SubscriptionFeesTableModel;
import com.aizong.ishtirak.model.Engine;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class SummaryTablePanel extends BasicPanel implements ActionListener {

    private SearchMonthPanel searchMonthPanel;

    private JButton btnSearch;

    private JTable tableExpenses;
    private JTable tableConsumption;
    private JTable tableIncome;
    private JTable tableResult;
    private JTable tableSubscriptionFees;
    private JButton btnPrint;

    public SummaryTablePanel(String title) {
	super();
	initComponents();
	buildPanel();
    }

    protected void init() {
    }

    protected void initComponents() {
	btnSearch = ButtonFactory.createBtnSearch();
	btnSearch.addActionListener(this);

	DateRange dataRange = DateUtil.getStartEndDateOfCurrentMonth();
	// because we need to show only previous month that effevely between 5
	// perv month
	// and 5 current month
	searchMonthPanel = SearchMonthPanel.createDefault(dataRange.getStartDate(), dataRange.getStartDate());

	tableExpenses = createTable();

	tableConsumption = createTable();
	tableIncome = createTable();
	tableResult = createTable();
	tableSubscriptionFees = createTable();

	btnPrint = ButtonFactory.createBtnPrint();
	btnPrint.addActionListener(this);
    }

    private JTable createTable() {
	JTable table = new JTable() {
	    @Override
	    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		Component comp = super.prepareRenderer(renderer, row, col);
		if (comp instanceof JLabel) {
		    ((JLabel) comp).setHorizontalAlignment(JLabel.RIGHT);
		}
		return comp;
	    }
	};
	JTableHeader header = table.getTableHeader();
	header.setDefaultRenderer(new HeaderRenderer(table));
	table.setRowHeight(20);
	table.setFillsViewportHeight(true);
	table.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	table.setDefaultRenderer(Date.class, new DateCellRenderer());
	return table;

    }

    protected void buildPanel() {
	
	JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	controlPanel.add(searchMonthPanel);
	controlPanel.add(btnSearch);
	controlPanel.add(btnPrint);
	
	
	FormLayout layout = new FormLayout(OrientationUtils.flipped(getLayoutSpecs()), RowSpec.decodeSpecs("p,90dlu,p,100dlu,p,30dlu,p,40dlu,p,30dlu"));
	DefaultFormBuilder builder = new DefaultFormBuilder(layout);
	builder.setDefaultDialogBorder();
 	builder.setLeftToRight(false);
	// starting build panel
	builder.appendSeparator(message("income"));
	builder.append(ComponentUtils.createScrollPane(tableIncome));
	
	builder.appendSeparator(message("expenses"));
	builder.append(ComponentUtils.createScrollPane(tableExpenses));
	
	builder.appendSeparator(message("result"));
	builder.append(ComponentUtils.createScrollPane(tableResult));
	
	builder.appendSeparator(message("diesel_result"));
	builder.append(ComponentUtils.createScrollPane(tableConsumption));
	
	builder.appendSeparator(message("subscription_fees"));
	builder.append(ComponentUtils.createScrollPane(tableSubscriptionFees));

	JPanel panel2 = builder.getPanel();
	panel2.setPreferredSize(ComponentUtils.getDimension(97, 90));
	JScrollPane scrollPane = new JScrollPane();
	scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	scrollPane.setViewportView(panel2);
	scrollPane.setPreferredSize(new Dimension(ComponentUtils.getDimension(100, 90)));
	
	
	add(controlPanel,BorderLayout.NORTH);
	add(scrollPane,BorderLayout.CENTER);
    }

    protected String getLayoutSpecs() {
	return "fill:p:grow";
    }

    private Map<String, Map<String, Double>> getExpensesValues(List<Object[]> rows) {
	Map<String, Map<String, Double>> map = new HashMap<>();
	for (Object[] row : rows) {

	    String engine = row[0] == null ? message("noEngine") : row[0].toString();
	    String expensesType = enumMessage(row[1].toString(), ExpensesType.class);
	    Double amount = (Double) row[2];
	    Map<String, Double> values = map.get(engine);
	    if (values == null) {
		values = new HashMap<>();
		map.put(engine, values);
	    }

	    values.put(expensesType, amount);
	}
	return map;
    }

    private Map<String, Map<String, Double>>  getConsupmtions(List<Object[]> rows) {

	Map<String, Map<String, Double>> map = new HashMap<>();
	for (Object[] row : rows) {

	    String engine = row[0].toString();
	    String expensesType = enumMessage(row[1].toString(), ExpensesType.class);
	    if (row[2] instanceof Number) {
		Number amount = (Number) row[2];
		Map<String, Double> values = map.get(engine);
		if (values == null) {
		    values = new HashMap<>();
		    map.put(engine, values);
		}

		values.put(expensesType, amount.doubleValue());
	    } else {
		System.out.println(row[2] + " is not a double");
	    }
	}

	return map;

    }

    private Map<String, Map<String, Double>> getIncomeValues(List<Object[]> rows) {
	Map<String, Map<String, Double>> map = new HashMap<>();
	for (Object[] row : rows) {

	    String engine = row[0] == null ? message("noEngine") : row[0].toString();
	    String expensesType = enumMessage(row[1].toString(), TransactionType.class);
	    Double amount = (Double) row[2];
	    Map<String, Double> values = map.get(engine);
	    if (values == null) {
		values = new HashMap<>();
		map.put(engine, values);
	    }

	    values.put(expensesType, amount);
	}

	//map.put(message("noEngine"), new HashMap<String, Double>());
	return map;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == btnSearch) {
	    MySwingWorker.execute(new ProgressAction<SummaryBean>() {

		@Override
		public SummaryBean action() {

		    LocalDate fromLocaleDate = LocalDate.of(searchMonthPanel.getSelectedFromYear(),
			    searchMonthPanel.getSelectedFromMonth(), 6);
		    DateRange fromDateRange = DateUtil.getStartEndDateOfCurrentMonth(fromLocaleDate);

		    LocalDate toLocaleDate = LocalDate.of(searchMonthPanel.getSelectedToYear(),
			    searchMonthPanel.getSelectedToMonth(), 6);
		    DateRange toDateRange = DateUtil.getStartEndDateOfCurrentMonth(toLocaleDate);

		    return ServiceProvider.get().getReportServiceImpl()
			    .getSummaryResult(fromDateRange.getStartDateAsString(), toDateRange.getEndDateAsString());
		}

		@Override
		public void success(SummaryBean t) {
		    Map<String, Map<String, Double>> expensesValues = getExpensesValues(t.getExpenses());
		    Map<String, Map<String, Double>> incomeValues = getIncomeValues(t.getIncome());
		    Map<String, Map<String, Double>> consumptions = getConsupmtions(t.getConsumptions());
		    Map<String, Double> incomeValuesPaid = getIncomeValuesPaid(t.getIncomePaid());
		    Map<String, Map<String, Double>> subscriptionFees = getIncomeValues(t.getSubscriptionFees());
		    
			// add all engine even there no data for them
		    for (Engine engine : t.getEngines()) {
			if(incomeValues.get(engine.getName())==null) {
			    incomeValues.put(engine.getName(), new HashMap<>());
			}
			
			if(expensesValues.get(engine.getName())==null) {
			    expensesValues.put(engine.getName(), new HashMap<>());
			}
			
			if(consumptions.get(engine.getName())==null) {
			    consumptions.put(engine.getName(), new HashMap<>());
			}
			
			if(incomeValuesPaid.get(engine.getName())==null) {
							incomeValuesPaid.put(engine.getName(), -1d);
			}
			
			if(subscriptionFees.get(engine.getName())==null) {
				subscriptionFees.put(engine.getName(), new HashMap<>());
			}
			}
			
		    
		    tableExpenses.setModel(new ExpensesTableModel(expensesValues));
		    tableConsumption.setModel(new ConsumptionTableModel(consumptions));
			tableIncome.setModel(new IncomeTableModel(incomeValues, incomeValuesPaid));
			tableSubscriptionFees.setModel(new SubscriptionFeesTableModel(subscriptionFees));
			fillResult(incomeValues, expensesValues);
		}

		private Map<String, Double> getIncomeValuesPaid(List<Object[]> rows) {
			Map<String, Double> incomeValuesPaid = new HashMap<>();
		    
		    for (Object[] row : rows) {
						if (row[0] != null) {
							incomeValuesPaid.put(row[0].toString(), (Double) row[1]);
						}
			}
			return incomeValuesPaid;
		}

		private void fillResult(Map<String, Map<String, Double>> incomeValues,
			Map<String, Map<String, Double>> expensesValues) {

		    Map<String, Map<String, Double>> resutl = new HashMap<>();
		    for (Entry<String, Map<String, Double>> entry : expensesValues.entrySet()) {
			Map<String, Double> map = resutl.get(entry.getKey());
			if (map == null) {
			    map = new HashMap<>();
			    resutl.put(entry.getKey(), map);
			}

			Double expensesTotal = 0d;
			for (Double d : entry.getValue().values()) {
			    expensesTotal += d;
			}
			Map<String, Double> map2 = incomeValues.get(entry.getKey());
			Double incomeTotal = 0d;
			if (map2 != null) {

			    for (Double d : map2.values()) {
				incomeTotal += d;
			    }
			}
			map.put(message("result"), incomeTotal - expensesTotal);

		    }
		    tableResult.setModel(new ResultTableModel(resutl));
		}

		@Override
		public void failure(Exception e) {

		}
	    });
	} else if (btnPrint == e.getSource()) {
	    try {
		boolean printed = PrintUtils.printLandscapeComponent(SummaryTablePanel.this);
		if (printed) {
		    MessageUtils.showInfoMessage(getOwner(), message("printing.done"));
		}
	    } catch (PrinterException e1) {
		MessageUtils.showErrorMessage(getOwner(), message("printing.failure", e1.getMessage()));
	    }
	}

    }

}