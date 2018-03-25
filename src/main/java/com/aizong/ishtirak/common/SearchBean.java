package com.aizong.ishtirak.common;

import java.io.Serializable;

public class SearchBean implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5518895082680547577L;

	private String fromDate = "-1";

	private String endDate = "-1";

	private int startCount = 0;

	private int endCount = 1000;

	private Searchable holder;

	public static SearchBean edit( )
	{
		return new SearchBean();
	}

	public SearchBean setFromDate(String fromDate)
	{
		if ( !fromDate.equals("-1"))
		{
			this.fromDate = fromDate + " 00:00:00";
		}
		return this;
	}

	public SearchBean setEndDate(String endDate)
	{
		if ( !endDate.equals("-1"))
		{
			this.endDate = endDate + " 23:59:59";
		}
		return this;
	}

	public SearchBean setStartCount(int startCount)
	{
		this.startCount = startCount;
		return this;
	}

	public SearchBean setEndCount(int endCount)
	{
		this.endCount = endCount;
		return this;
	}

	public SearchBean setHolder(Searchable holder)
	{
		this.holder = holder;
		return this;
	}

	public String getFromDate( )
	{
		return fromDate;
	}

	public String getEndDate( )
	{
		return endDate;
	}

	public int getStartCount( )
	{
		return startCount;
	}

	public int getEndCount( )
	{
		return endCount;
	}

	public Searchable getHolder( )
	{
		return holder;
	}
	
	

}
