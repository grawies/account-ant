package model.report;

import java.util.ArrayList;

import model.Book;

public class ReportSection
{
	int sectionDigit;
	ArrayList<ReportSubSection> subSections;
	ReportLine sumLine;
	AbstractReport report;
	Book book;
	
	public ReportSection(Book book, AbstractReport report, int sectionDigit) {
		this.book = book;
		this.report = report;
		this.sectionDigit = sectionDigit;
		subSections = new ArrayList<ReportSubSection>();
		sumLine = new ReportLine();
		
		for (int subSectionDigit=0; subSectionDigit<10; subSectionDigit++) {
			int subSectionID = 10*sectionDigit + subSectionDigit;
			subSections.add(new ReportSubSection(book, report, subSectionID));
		}
		sumLine.sumAllSubSections(subSections, sectionDigit);
	}
	
	
}
