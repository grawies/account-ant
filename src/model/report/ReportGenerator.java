package model.report;

import model.Book;
import model.Time;
import model.Verificate;
import view.Text;

public class ReportGenerator
{
	public static HTMLReport generateResultsReportHTML(Book book)
	{

		return new HTMLReport("HTML!",
				"<html><body><h1>Results Report</h1><br>No results.</body></html>");

	}

	public static void fillReportHeaderData(Book book, AbstractReport report, Time start, Time end)
	{
		// set company
		report.companyName = book.getCompanyName();
		// set fiscal year period
		report.fiscalYearStartDate = book.getStartDate();
		report.fiscalYearEndDate = book.getEndDate();
		// set report period
		report.periodStartDate = new Time(start);
		report.periodEndDate = new Time(end);
		// set report date
		report.reportCreationDate = new Time();

		int largestVerificateNum = 0;
		// for each verificate
		for ( Verificate v : book.verificates )
		{
			// if in report period and # > largestSoFar then new largestSoFar
			if( v.getID() > largestVerificateNum )
				largestVerificateNum = v.getID();
		}
		// set largest verificate number
		report.latestVerNumber = largestVerificateNum;
		// set reportSubtitle
		// if new Time() within report period, set subtitle preliminary
		if( report.reportCreationDate.compareTo(end) <= 0 )
		{
			report.subtitle = Text.lang.get("resultReportSubTitlePreliminary");
		}
		else
		{
			report.subtitle = "";
		}
	}

	public static ResultsReport generateResultsReport(Book book, Time start, Time end)
	{
		ResultsReport report = new ResultsReport();
		
		fillReportHeaderData(book, report, start, end);
		// for each area on results report
		// calculate the individual account lines
		// calculate the area total sums
		report.loadSections(book);

		// technically the report contains everything, apart from joined
		// sections and other stupid special cases (bah!)

		return report;
	}
	/*
	 * ingående balans: räkenskapsårets ingående balans ingående saldo:
	 * periodens ingående balans
	 */

}
