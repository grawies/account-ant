package view.report;

import model.report.HTMLReport;

public interface ReportOpener
{
	void showReportInWindow();
	void showReportInBrowser();
	void saveReportAsHTML();
	void saveReportAsPDF();
	void sendReportToPrinter();
	HTMLReport getReportAsHTMLReport();
}
