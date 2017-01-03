package view.report;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Book;
import model.Time;
import model.report.AbstractReport;
import model.report.HTMLReport;
import model.report.ReportGenerator;
import view.Text;

/*
 * This window takes input for creating a resultsreport as HTML.
 */
@SuppressWarnings("serial")
public class ResultsReportWindow extends CompanyReportWindow
{

	JCheckBox showPeriodColumn;
	JCheckBox showAccumulatedColumn;
	JCheckBox showLastYearColumn;
	JCheckBox showBudgetColumn;
	JCheckBox showPeriodBudgetQuotientColumn;

	String[] columnOptions =
	{ Text.lang.get("showPeriod"), Text.lang.get("showAccumulated"),
			Text.lang.get("showWholeYears") };

	JComboBox<String> lastYearColumnList;
	JComboBox<String> budgetColumnList;

	public ResultsReportWindow( Book b )
	{
		super(b);

		JLabel columnLabel = new JLabel(Text.lang.get("columns"));
		showPeriodColumn = new JCheckBox(Text.lang.get("showPeriodColumn"));
		showAccumulatedColumn = new JCheckBox(Text.lang.get("showAccumulatedColumn"));
		showLastYearColumn = new JCheckBox(Text.lang.get("showLastYearColumn"));
		showBudgetColumn = new JCheckBox(Text.lang.get("showBudgetColumn"));
		showPeriodBudgetQuotientColumn = new JCheckBox(
				Text.lang.get("showPeriodBudgetQuotientColumn"));

		JPanel checkBoxPanel = new JPanel(new GridLayout(3, 2));

		checkBoxPanel.add(columnLabel);
		checkBoxPanel.add(showPeriodColumn);
		checkBoxPanel.add(showAccumulatedColumn);
		checkBoxPanel.add(showLastYearColumn);
		checkBoxPanel.add(showBudgetColumn);
		checkBoxPanel.add(showPeriodBudgetQuotientColumn);
		contentList.add(checkBoxPanel);

		lastYearColumnList = new JComboBox<String>(columnOptions);
		budgetColumnList = new JComboBox<String>(columnOptions);

		JLabel lastYearLabel = new JLabel(Text.lang.get("showLastYear"));
		JLabel budgetLabel = new JLabel(Text.lang.get("showBudget"));

		JPanel comboBoxPanel = new JPanel(new GridLayout(2, 2));
		comboBoxPanel.add(lastYearLabel);
		comboBoxPanel.add(lastYearColumnList);
		comboBoxPanel.add(budgetLabel);
		comboBoxPanel.add(budgetColumnList);
		contentList.add(comboBoxPanel);

		setContent();
		setTitle(Text.lang.get("viewReportResultsTitle"));
		setBounds(300, 200, 360, 460);
	}

	public HTMLReport getReportAsHTMLReport()
	{
		Time start = getDate(startDateField);
		Time end = getDate(endDateField);
		AbstractReport report = ReportGenerator.generateResultsReport(book, start, end);
		return new HTMLReport(report);
	}
}
