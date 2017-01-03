package view.report;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import model.Book;
import view.Text;

@SuppressWarnings("serial")
public abstract class CompanyReportWindow extends ReportWindow

{

	JCheckBox specifyAccounts;
	
	public CompanyReportWindow( Book b )
	{
		super(b);
		
		// structure:
		// { Parent-specific content }
		// Specify accounts: [x]
		// { Children-specific content }		
		
		JPanel showAccountsPanel = new JPanel();
		specifyAccounts = new JCheckBox(Text.lang.get("showSpecificAccounts"));
		specifyAccounts.setBorderPainted(false);
		showAccountsPanel.setBorder(BorderFactory.createEmptyBorder(0,10,-15,10));
		showAccountsPanel.add(specifyAccounts);
		contentList.add(showAccountsPanel);
	}
}
