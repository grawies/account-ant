package model.report;

import java.util.ArrayList;

import model.Book;
import model.Time;

public abstract class AbstractReport implements SectionLister 
{
	public Time fiscalYearStartDate;
	public Time fiscalYearEndDate;
	public Time periodStartDate;
	public Time periodEndDate;
	public String companyName;
	public String title;
	public String subtitle;
	public Time reportCreationDate = new Time();
	public int latestVerNumber; // last verificate of the period (!)
	ArrayList<ReportSection> sections;
	Book book;
	
	public AbstractReport()
	{
		sections = new ArrayList<ReportSection>();
	}

	public void loadSections(Book book)
	{
		this.book = book;
		int[] secIDs = getSectionIDs();
		for ( int i = 0; i < secIDs.length; i++ )
		{
			sections.add(new ReportSection(book, this, secIDs[i]));
		}
	}
}
