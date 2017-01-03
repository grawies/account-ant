package model.report;

import java.util.ArrayList;
import java.util.Comparator;

import model.Account;
import model.AccountBudget;
import model.Book;
import model.Time;

public class ReportSubSection
{
	ArrayList<ReportLine> lines;
	ReportLine sumLine;
	public ReportSubSection(Book book, AbstractReport report, int section) {
		// section is number # or ##, indicating all accounts #XXX or ##XX.
		
		Time start = report.periodStartDate;
		Time end = report.periodEndDate;
		
		lines = new ArrayList<ReportLine>();
		sumLine = new ReportLine();
		
		for (Account acc : book.accountPlan.values()) {
			int accID = acc.getID();
			if (accID/1000 == section || accID/100 == section) {
//				System.out.println("Found acc: " + accID);
				ReportLine line = new ReportLine();
				line.accID = accID;
				AccountBudget accBudget = book.getBudget().getAccountBudget(accID);
				line.period = acc.getOutBalance(start, end);
				line.accumulated = acc.getOutBalance(book.getStartDate(), end);
				// TODO lastYearX values to be added
				line.budgetPeriod = accBudget.getPeriodBalance(book, start, end);
				line.budgetAccumulated = accBudget.getPeriodBalance(book, book.getStartDate(), end);
				line.budgetFiscalYear = accBudget.getTotalBalance();
				// TODO don't divide by zero, handle it gracefully
				line.setQuotient();
				
				// TODO correct signs gracefully
				if (section/10 == 3) {
					line.period *= -1;
					line.accumulated *= -1;
					line.budgetPeriod *= -1;
					line.budgetAccumulated *= -1;
					line.budgetFiscalYear *= -1;
					
				}
				
				lines.add(line);
			}
		}
//		lines.sort(new ReportLineComparator());

		sumLine.accID = section;
		for (ReportLine line : lines) {
			sumLine.period += line.period;
			sumLine.accumulated += line.accumulated;
			sumLine.lastYearPeriod += line.lastYearPeriod;
			sumLine.lastYearAccumulated += line.lastYearAccumulated;
			sumLine.lastYearFiscalYear += line.lastYearFiscalYear;
			sumLine.budgetPeriod += line.budgetPeriod;
			sumLine.budgetAccumulated += line.budgetAccumulated;
			sumLine.budgetFiscalYear += line.budgetFiscalYear;
		}
		// TODO don't divide by zero, handle it gracefully

		sumLine.setQuotient();
	}
}

class ReportLine {
	int accID;
	double period, accumulated, periodBudgetperiodQuotient;
	double lastYearPeriod, lastYearAccumulated, lastYearFiscalYear;
	double budgetPeriod, budgetAccumulated, budgetFiscalYear;
	public ReportLine() {
		accID = 0;
		period = 0;
		accumulated = 0;
		periodBudgetperiodQuotient = 1;
		lastYearPeriod = 0;
		lastYearAccumulated = 0;
		lastYearFiscalYear = 0;
		budgetPeriod = 0;
		budgetAccumulated = 0;
		budgetFiscalYear = 0;
	}
	public void sumAllLines(ArrayList<ReportLine> lines, int section) {
		accID = section;
		for (ReportLine line : lines) {
			period += line.period;
			accumulated += line.accumulated;
			lastYearPeriod += line.lastYearPeriod;
			lastYearAccumulated += line.lastYearAccumulated;
			lastYearFiscalYear += line.lastYearFiscalYear;
			budgetPeriod += line.budgetPeriod;
			budgetAccumulated += line.budgetAccumulated;
			budgetFiscalYear += line.budgetFiscalYear;
		}
		// TODO don't divide by zero, handle it gracefully

		setQuotient();
	}

	public void sumAllSubSections(ArrayList<ReportSubSection> sections, int section) {
		accID = section;
		for (ReportSubSection subSection : sections) {
			ReportLine line = subSection.sumLine;
			period += line.period;
			accumulated += line.accumulated;
			lastYearPeriod += line.lastYearPeriod;
			lastYearAccumulated += line.lastYearAccumulated;
			lastYearFiscalYear += line.lastYearFiscalYear;
			budgetPeriod += line.budgetPeriod;
			budgetAccumulated += line.budgetAccumulated;
			budgetFiscalYear += line.budgetFiscalYear;
		}
		// TODO don't divide by zero, handle it gracefully

		setQuotient();
	}
	
	public void setQuotient() {
		if (budgetPeriod != 0.0)
			periodBudgetperiodQuotient = period/budgetPeriod;
		else
			periodBudgetperiodQuotient = 0;
	}
}

class ReportLineComparator implements Comparator<ReportLine> {
	public ReportLineComparator() {
		super();
	}
	public int compare(ReportLine r1, ReportLine r2)
	{
		return r1.accID-r2.accID;
	}
}
