package com.aizong.ishtirak.gui.table.service;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import net.coderazzi.filters.examples.ActionHandler;
import net.coderazzi.filters.examples.menu.MenuAgeOddComparator;
import net.coderazzi.filters.examples.menu.MenuAlphaChoicesOrder;
import net.coderazzi.filters.examples.menu.MenuAutoChoices;
import net.coderazzi.filters.examples.menu.MenuAutoCompletion;
import net.coderazzi.filters.examples.menu.MenuColumnRemove;
import net.coderazzi.filters.examples.menu.MenuCountryFlagRenderer;
import net.coderazzi.filters.examples.menu.MenuCountrySpecialSorter;
import net.coderazzi.filters.examples.menu.MenuEditable;
import net.coderazzi.filters.examples.menu.MenuEnabled;
import net.coderazzi.filters.examples.menu.MenuHtmlCountry;
import net.coderazzi.filters.examples.menu.MenuIgnoreCase;
import net.coderazzi.filters.examples.menu.MenuInstantFiltering;
import net.coderazzi.filters.examples.menu.MenuInverseChoicesOrder;
import net.coderazzi.filters.examples.menu.MenuMaleCustomChoices;
import net.coderazzi.filters.examples.menu.MenuMaxHistory;
import net.coderazzi.filters.examples.menu.MenuReset;
import net.coderazzi.filters.examples.menu.MenuUIEnabled;
import net.coderazzi.filters.examples.utils.AgeCustomChoice;
import net.coderazzi.filters.examples.utils.AgeOddComparator;
import net.coderazzi.filters.examples.utils.Ages60sCustomChoice;
import net.coderazzi.filters.examples.utils.CenteredRenderer;
import net.coderazzi.filters.examples.utils.DateRenderer;
import net.coderazzi.filters.examples.utils.FlagRenderer;
import net.coderazzi.filters.examples.utils.InverseComparator;
import net.coderazzi.filters.examples.utils.MaleRenderer;
import net.coderazzi.filters.examples.utils.TestTableModel;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.CustomChoice;
import net.coderazzi.filters.gui.FilterSettings;
import net.coderazzi.filters.gui.IFilterEditor;
import net.coderazzi.filters.gui.IFilterHeaderObserver;
import net.coderazzi.filters.gui.TableFilterHeader;


/** Main example showing the {@link TableFilterHeader} functionality. */
public class TableFilterExample extends JFrame implements ActionHandler {

    private static final long serialVersionUID = 382439526043424294L;

    private static final int DEFAULT_MODEL_ROWS = 1000;
    private static final boolean START_LARGE_MODEL = false;

    private TestTableModel tableModel;
    private JTable table;
    private TableFilterHeader filterHeader;
    private JCheckBoxMenuItem allEnabled;
    JMenu filtersMenu;

    public TableFilterExample(int modelRows) {
        super("Table Filter Example");
        if (START_LARGE_MODEL){
        	TestTableModel.setLargeModel(true);
        }
        JPanel tablePanel = createGui(modelRows);
        getContentPane().add(tablePanel);
        filterHeader.setTable(table);
//        filterHeader.setInstantFiltering(false);
    }

    private JPanel createGui(int modelRows) {
        tableModel = TestTableModel.createTestTableModel(modelRows);
        table = new JTable(tableModel);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        filterHeader = new TableFilterHeader();
        filterHeader.addHeaderObserver(new IFilterHeaderObserver() {

                @Override public void tableFilterUpdated(
                        TableFilterHeader header,
                        IFilterEditor     editor,
                        TableColumn       tableColumn) {
                    // no need to react
                }

                @Override public void tableFilterEditorExcluded(
                        TableFilterHeader header,
                        IFilterEditor     editor,
                        TableColumn       tableColumn) {
                  
                }

                @Override public void tableFilterEditorCreated(
                        TableFilterHeader header,
                        IFilterEditor     editor,
                        TableColumn       tableColumn) {
                    handleNewColumn(editor, tableColumn);
                }
            });

        return tablePanel;
    }


    /** Method to handle the information associated to a (new) filter editor. */
    void handleNewColumn(IFilterEditor editor, TableColumn tc) {
        String name = (String) tc.getHeaderValue();
        boolean countryColumn = name.equalsIgnoreCase(TestTableModel.COUNTRY);
        boolean maleColumn = name.equalsIgnoreCase(TestTableModel.MALE);
        boolean ageColumn = name.equalsIgnoreCase(TestTableModel.AGE);

        if (countryColumn) {
            tc.setCellRenderer(new FlagRenderer());
            editor.setEditable(false);
        } else if (name.equalsIgnoreCase(TestTableModel.NOTE)) {
        	editor.setEditable(false);
        } else if (ageColumn) {
            tc.setCellRenderer(new CenteredRenderer());
            editor.setCustomChoices(AgeCustomChoice.getCustomChoices());
        } else if (name.equalsIgnoreCase(TestTableModel.DATE)) {
            tc.setCellRenderer(new DateRenderer(
                    filterHeader.getParserModel().getFormat(Date.class)));

            Set<CustomChoice> choices = new HashSet<CustomChoice>();
            choices.add(new Ages60sCustomChoice());
            CustomChoice december = CustomChoice.create(
            		Pattern.compile("\\d+/12/\\d+"));
            december.setPrecedence(CustomChoice.DEFAULT_PRECEDENCE + 1);
            december.setRepresentation("from December");
            choices.add(december);
            editor.setCustomChoices(choices);
        } else if (maleColumn) {
            tc.setCellRenderer(new MaleRenderer(this));
        }

    }

    /** {@link ActionHandler} interface. */
    @Override public TestTableModel getTableModel() {
        return tableModel;
    }

    /** {@link ActionHandler} interface. */
    @Override public void initTableModel(int rows) {
        this.tableModel = TestTableModel.createTestTableModel(rows);
        table.setModel(tableModel);
    }

    /** {@link ActionHandler} interface. */
    @Override public JMenu getFilterMenu() {
        return filtersMenu;
    }

    /** {@link ActionHandler} interface. */
    @Override public JTable getTable() {
        return table;
    }

    /** {@link ActionHandler} interface. */
    @Override public JFrame getJFrame() {
        return this;
    }

    /** {@link ActionHandler} interface. */
    @Override public TableFilterHeader getFilterHeader() {
        return filterHeader;
    }

    /** {@link ActionHandler} interface. */
    @Override public void updateEnabledFlag() {
        allEnabled.setSelected(filterHeader.isEnabled());
    }


    /** {@link ActionHandler} interface. */
    @Override public void updateFiltersInfo() {
    	int n = table.getColumnCount();
        while (n-- > 0) {
            updateFilterInfo(filterHeader.getFilterEditor(n));
        }
    }

    /** {@link ActionHandler} interface. */
    @Override public void updateFilterInfo(IFilterEditor editor) {}

    public final static void init(int modelRows) {
        TableFilterExample frame = new TableFilterExample(modelRows);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        if (START_LARGE_MODEL){
        	frame.setSize(1200, frame.getSize().height);
        }
        frame.setVisible(true);
    }

    public final static void main(String args[]) {
        FilterSettings.autoChoices = AutoChoices.ENABLED;
        init(DEFAULT_MODEL_ROWS);
    }

    @Override
    public void recreate() {
	// TODO Auto-generated method stub
	
    }

}