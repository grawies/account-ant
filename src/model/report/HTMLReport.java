package model.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import view.Text;
import model.Book;
import model.Resources;

public class HTMLReport
{
	String title;
	String htmlhead;
	String htmlbody;
	private String[] html_size =
	{ "<h1>", "<h2>", "<h3>", "<h4>", "<h5>", "<h6>", };
	private String[] html_size_end =
	{ "</h1>", "</h2>", "</h3>", "</h4>", "</h5>", "</h6>", };
	
	private Book book;
	private boolean period=true, accumulated=true, periodBudgetperiodQuotient, lastYearPeriod,
			lastYearAccumulated, lastYearFiscalYear, budgetPeriod=true, budgetAccumulated,
			budgetFiscalYear;

	/*
	 * Creates a dummy HTML report
	 */
	public HTMLReport( String title, String html )
	{
		this.title = title;
		this.htmlhead ="";
		this.htmlbody = html;
	}

	public HTMLReport( AbstractReport report )
	{
		book = report.book;
		
		title = report.title;

		// ============ HTML HEAD ============
		
		htmlhead = "<head>";
		try {
			Scanner sc = new Scanner(new File("htmlreport-head.html"));
			String s="";
			while (sc.hasNextLine()) {
				s += sc.nextLine() + "\n";
			}
			sc.close();
			htmlhead += s;
		} catch (Exception e) {
			System.out.println("Could not parse or find the html header file.");
			e.printStackTrace();
		}
		htmlhead += "</head>";
		
		
		// ============ HTML BODY ============
		
		htmlbody = "<body>";
		
		htmlbody += "<h1>" + report.title + "</h1><br>\n";
		htmlbody += "<h2>" + report.subtitle + "</h2><br>\n";
		
		htmlbody += "<table>";
		htmlbody += firstTableLineHtml();
		
		for (ReportSection section : report.sections ) {
			for (ReportSubSection subSec : section.subSections ) {
				for (ReportLine line : subSec.lines) {
					htmlbody += "<tr>" + toHtml(line) + "</tr>\n";
				}
				//htmlbody += "<tr>" + toHtml(subSec.sumLine) + "</tr>\n";
			}
			htmlbody += "<tr style=\"border-bottom:1px solid black\">" + "<h2>" + toHtml(section.sumLine) + "</h2>" + "</tr>\n";
		}
		htmlbody += "</table>";
		htmlbody += "</body>";
	}

	public String firstTableLineHtml() {
		String s = "";
		s += "<tr class=\"columnHeader\">";
		
		ArrayList<String> values = new ArrayList<String>();
		
		values.add(Text.lang.get("resultReportColumnTitleAccountID"));
		values.add(Text.lang.get("resultReportColumnTitleAccountName"));
		
		if (period) {
			values.add(Text.lang.get("resultReportColumnTitlePeriod"));
		}
		if (accumulated) {
			values.add(Text.lang.get("resultReportColumnTitleAccumulated"));
		}
		if (lastYearPeriod) {
			values.add(Text.lang.get("resultReportColumnTitleLastYearPeriod"));
		}
		if (lastYearAccumulated) {
			values.add(Text.lang.get("resultReportColumnTitleLastYearAccumulated"));
		}
		if (lastYearFiscalYear) {
			values.add(Text.lang.get("resultReportColumnTitleLastYearFiscalYear"));
		}
		if (budgetPeriod) {
			values.add(Text.lang.get("resultReportColumnTitleBudgetPeriod"));
		}
		if (budgetAccumulated) {
			values.add(Text.lang.get("resultReportColumnTitleBudgetAccumulated"));
		}
		if (budgetFiscalYear) {
			values.add(Text.lang.get("resultReportColumnTitleBudgetFiscalYear"));
		}
		if (periodBudgetperiodQuotient) {
			values.add(Text.lang.get("resultReportColumnTitlePeriodBudgetQuotient"));
		}
		for (String column : values) {
			s += "\n<td>" + column + "</td>\n";
		}
		s += "</tr>";
		return s;
	}
	
	public String toHtml(ReportLine line) // if accID is a real account and not just a sectionID
	{
		String s = "";
		s += "<td>" + line.accID + "</td>";
		if (book.accountPlan.containsKey(line.accID))
			s += "<td class=\"accname\">" + book.accountPlan.get(line.accID).getName() + "</td>"; 
		ArrayList<Double> values = new ArrayList<Double>();
		if (period) {
			values.add(line.period);
		}
		if (accumulated) {
			values.add(line.accumulated);
		}
		if (lastYearPeriod) {
			values.add(line.lastYearPeriod);
		}
		if (lastYearAccumulated) {
			values.add(line.lastYearAccumulated);
		}
		if (lastYearFiscalYear) {
			values.add(line.lastYearFiscalYear);
		}
		if (budgetPeriod) {
			values.add(line.budgetPeriod);
		}
		if (budgetAccumulated) {
			values.add(line.budgetAccumulated);
		}
		if (budgetFiscalYear) {
			values.add(line.budgetFiscalYear);
		}
		if (periodBudgetperiodQuotient) {
			values.add(line.periodBudgetperiodQuotient);
		}
		for (double d : values) {
			s += "\n<td>" + Resources.decformat.format(d) + "</td>\n";
		}
		return s;
	}

	public String getTitle()
	{
		return title;
	}

	public String getHTMLBody()
	{
		return htmlbody;
	}

	public String getHTMLFull()
	{
		String fullhtml = "<html>" + htmlhead + "\n" + htmlbody + "</html>";
		//System.out.println(fullhtml);
		return fullhtml;
	}
}
