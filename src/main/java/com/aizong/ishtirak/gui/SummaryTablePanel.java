package com.aizong.ishtirak.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.aizong.ishtirak.bean.ExpensesType;
import com.aizong.ishtirak.bean.SummaryBean;
import com.aizong.ishtirak.common.form.BasicForm;
import com.aizong.ishtirak.common.misc.component.DateRange;
import com.aizong.ishtirak.common.misc.component.HeaderRenderer;
import com.aizong.ishtirak.common.misc.utils.ButtonFactory;
import com.aizong.ishtirak.common.misc.utils.ComponentUtils;
import com.aizong.ishtirak.common.misc.utils.DateCellRenderer;
import com.aizong.ishtirak.common.misc.utils.DateUtil;
import com.aizong.ishtirak.common.misc.utils.MySwingWorker;
import com.aizong.ishtirak.common.misc.utils.ProgressAction;
import com.aizong.ishtirak.common.misc.utils.SearchMonthPanel;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;
import com.aizong.ishtirak.model.Engine;
import com.jgoodies.forms.builder.DefaultFormBuilder;

@SuppressWarnings("serial")
public class SummaryTablePanel extends BasicForm implements ActionListener {

    private SummaryBean summaryBean;

    private SearchMonthPanel searchMonthPanel;

    private JButton btnSearch;

    private JTable tableExpenses;
    private JTable tableConsumption;
    private JTable tableIncome;

    public SummaryTablePanel(String title) {
	super();
	initializePanel();
    }

    protected void init() {
    }

    @Override
    protected void initComponents() {
	btnSearch = ButtonFactory.createBtnSearch();
	btnSearch.addActionListener(this);

	DateRange dataRange = DateUtil.getStartEndDateOfCurrentMonth();
	// because we need to show only previous month that effevely between 5
	// perv month
	// and 5 current month
	searchMonthPanel = SearchMonthPanel.createDefault(dataRange.getStartDate(), dataRange.getStartDate());

	tableExpenses = createTable();
	
	tableConsumption = new JTable();
	tableIncome = new JTable();
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
	table.setRowHeight(50);
	table.setFillsViewportHeight(true);
	table.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	table.setDefaultRenderer(Date.class, new DateCellRenderer());
	return table;
	
    }

    @Override
    protected Component buildPanel(DefaultFormBuilder builder) {

	builder = createBuilder(getLayoutSpecs(), "p,p,p,p");
	JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	controlPanel.add(searchMonthPanel);
	controlPanel.add(btnSearch);

	// starting build panel
	builder.appendSeparator();
	builder.append(controlPanel);
	builder.appendSeparator();

	builder.append(ComponentUtils.createScrollPane(tableExpenses));

	return builder.getPanel();
    }

    @Override
    protected String getLayoutSpecs() {
	return "fill:p:grow";
    }

    private void fillTableExpenses(List<Engine> engines, List<Object[]> rows) {

	Map<String, Map<String, Double>> map = new HashMap<>();
	for (Object[] row : rows) {

	    String engine = row[0]==null ? message("noEngine") : row[0].toString();
	    String expensesType = enumMessage(row[1].toString(),ExpensesType.class);
	    Double amount = (Double) row[2];
	    Map<String, Double> values = map.get(engine);
	    if (values == null) {
		values = new HashMap<>();
		map.put(engine, values);
	    }

	    values.put(expensesType, amount);
	}
	
	tableExpenses.setModel(new MyTableModel(map));

    }

    private static class MyTableModel extends AbstractTableModel {

	Map<String, Map<String, Double>> map;

	List<String> cols = new ArrayList<>();
	public MyTableModel(Map<String, Map<String, Double>> map) {
	    super();
	    cols.add(message("expenses"));
	    cols.addAll(map.keySet());
	    cols.add(message("total"));
	    this.map = map;
	}

	@Override
	public int getColumnCount() {
	    return cols.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
	    if(columnIndex!=0) {
		return Double.class;
	    }
	    return super.getColumnClass(columnIndex);
	}
	@Override
	public String getColumnName(int column) {
	    return cols.get(column);
	}

	@Override
	public int getRowCount() {
	    return ExpensesType.values().length + 1;
	}

	@Override
	public Object getValueAt(int row, int col) {
	    if (col == 0) {
		if(row<getRowCount()-1) {
		    
		    return enumMessage(ExpensesType.values()[row].toString(),ExpensesType.class);
		}
		return message("total");
	    }
	    
	    if(col == cols.size()-1) {
		
		Double total = null;
		for(int i=1;i<cols.size()-1;i++) {
		    if(getValueAt(row, i) instanceof Double) {
			if(total==null) {
			    total =0d;
			}
			total+=(Double)getValueAt(row, i);
		    }
		}
		return total;
	    }
	    
	    if(row == getRowCount()-1) {
		Double total = null;
		for(int i=0;i<getRowCount()-1;i++) {
		    if(getValueAt(i, col) instanceof Double) {
			if(total==null) {
			   total = 0d;
			}
			 total+=(Double)getValueAt(i, col);
		    }
		}
		return total;
	    }
	    Map<String, Double> map2 = map.get(getColumnName(col));
	    Double double1 = map2.get(getValueAt(row, 0));
	    return double1;
	}

    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == btnSearch) {
	    MySwingWorker.execute(new ProgressAction<SummaryBean>() {

		@Override
		public SummaryBean action() {
		    DateRange dateRange = DateUtil.getStartEndDateOfCurrentMonth();
		    return ServiceProvider.get().getReportServiceImpl()
			    .getSummaryResult(dateRange.getStartDateAsString(), dateRange.getEndDateAsString());
		}

		@Override
		public void success(SummaryBean t) {
		    fillTableExpenses(t.getEngines(), t.getExpenses());
		}

		@Override
		public void failure(Exception e) {

		}
	    });
	}

    }

    private static class Keys {
	private String engineName;
	private String expenseType;
	private Double value;

	public Keys(String engineName, String expenseType, Double value) {
	    super();
	    this.engineName = engineName;
	    this.expenseType = expenseType;
	    this.value = value;
	}

	public String getEngineName() {
	    return engineName;
	}

	public void setEngineName(String engineName) {
	    this.engineName = engineName;
	}

	public String getExpenseType() {
	    return expenseType;
	}

	public void setExpenseType(String expenseType) {
	    this.expenseType = expenseType;
	}

	public Double getValue() {
	    return value;
	}

	public void setValue(Double value) {
	    this.value = value;
	}

    }
};