/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author User
 */
public class ReportFilterTableFrame extends FilterTableFrame 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4382501662729174873L;

	
	
	
	private JButton btnSearch;
	
	private ControllerListener controllerListener;
	
	private JPanel controlPanel;
	
	private SearchPanel searchPanel;
	
	private JPanel leftControlPanel;
	
	
	/**
	 * use this to add custom control panel to left and default search panel to right
	 * @param controlPanel
	 * @param controllerListener
	 */
	public void addControlPanel(JPanel leftControlPanel,ControllerListener controllerListener)
	{
		
		
		addCustomControlPanel(leftControlPanel, controllerListener, SearchPanel.createDefault());
	}
	
	public void addCustomControlPanel(JPanel leftControlPanel,ControllerListener controllerListener,SearchPanel searchPanel)
	{
		this.leftControlPanel = leftControlPanel;
		this.controllerListener = controllerListener;
		this.searchPanel = searchPanel;
	}
	
	

	public ReportFilterTableFrame( ) throws HeadlessException
	{

	}
	

	@Override
	public void lazyInitalize( )
	{
		super.lazyInitalize();
	}

	
	public void setTableDimension(Dimension tableDimension)
	{
		reBuildPanel();
	}

	@Override
	public void initComponents( )
	{
		super.initComponents();
		btnSearch = ButtonFactory.createBtnSearch();
		btnSearch.addActionListener(e->{
			
			
			controllerListener.search(searchPanel.toSearchBean());
		});
	}
	
	
	
	@Override
	public void init( )
	{
		
		
		setAllowUpdate(false);
		
		 JScrollPane jScrollPane = new JScrollPane(table);
	        if (tableDimension != null) {
	            jScrollPane.setPreferredSize(tableDimension);
	        }
	        
	        JPanel panel = new JPanel(new BorderLayout());
	        
	        JPanel myPanel = new JPanel();
			myPanel.add(searchPanel);
			myPanel.add(btnSearch);
			
			
	        this.controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        
	        controlPanel.add(leftControlPanel);
	        controlPanel.add(myPanel);
			//controlPanel.add(myPanel,BorderLayout.PAGE_END);
			
			panel.add(controlPanel,BorderLayout.PAGE_START);
	        panel.add(getControllerPanel(),BorderLayout.CENTER);
	        
	        add(panel, BorderLayout.PAGE_START);
	        add(jScrollPane, BorderLayout.CENTER);
		//tableFooter = new TableFooter(table);
		//add(tableFooter.getPanel(), BorderLayout.CENTER);

	}

	@SuppressWarnings("rawtypes")
	public void fillValues(BeanTableModel beanTableModel)
	{

		super.fillValues(beanTableModel);

		//tableFooter.init(table);

	}

	public interface ControllerListener
	{
		public void search(SearchBean searchBean);
	}

}
