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

	public double getNetBalance()
	{
		double netBalance = 0;
		for ( ReportSection section : sections )
		{
			double sign = -1;
			if( section.sectionDigit == 3 )
			{
				sign = +1;
			}
			netBalance += sign * section.sumLine.period;
		}
		return netBalance;
	}
}