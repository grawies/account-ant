package view.report;

import view.Text;
import model.Book;
import model.report.HTMLReport;
import model.report.ReportGenerator;

/*
 * This window takes input for creating a balancereport as HTML.
 */
@SuppressWarnings("serial")
public class BalanceReportWindow extends CompanyReportWindow
{

	public BalanceReportWindow( Book b )
	{
		super(b);
		
		setContent();
		
		setTitle(Text.lang.get("viewReportBalanceTitle"));
		setBounds(300,200,300,250);
	}

	@Override
	public HTMLReport getReportAsHTMLReport()
	{
		// TODO Auto-generated method stub
		return ReportGenerator.generateResultsReportHTML(book);
	}

}
