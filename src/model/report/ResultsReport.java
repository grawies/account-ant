package model.report;

import view.Text;

public class ResultsReport extends AbstractReport
{
	public int[] sectionIDs =
	{ 3, 5, 6, 7, 8 };

	public ResultsReport()
	{
		super();
		title = Text.lang.get("viewReportResultsTitle");
	}

	public int[] getSectionIDs()
	{
		return sectionIDs;
	}
}