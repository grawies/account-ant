package view.report;

/*
 * This window takes input for creating reports.
 */

import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Book;
import model.Time;
import view.ClosableJFrame;
import view.Text;
import external.TextPrompt;

@SuppressWarnings("serial")
public abstract class ReportWindow extends ClosableJFrame implements ReportOpener
{

	Book book;
	JTextField startDateField;
	JTextField endDateField;
	Desktop desktop;

	ArrayList<Container> contentList;
	Container mainPanel;

	public ReportWindow( Book b )
	{
		book = b;

		// check support for desktop access (browser, mail, print etc)
		if( Desktop.isDesktopSupported() )
			desktop = Desktop.getDesktop();
		else
			desktop = null;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// setLocationRelativeTo(null);
		this.setVisible(true);
		setResizable(false);

		contentList = new ArrayList<Container>();
		mainPanel = getContentPane();

		// structure:
		// Date: | YYYY-MM-DD | - | YYYY-MM-DD |
		// { Children-specific content }

		startDateField = new JTextField(7);
		endDateField = new JTextField(7);
		TextPrompt startDatePrompt = new TextPrompt("YYYY-MM-DD", startDateField);
		TextPrompt endDatePrompt = new TextPrompt("YYYY-MM-DD", endDateField);
		startDatePrompt.changeAlpha(0.5f);
		endDatePrompt.changeAlpha(0.5f);

		GridBagLayout gb = new GridBagLayout();
		JPanel datePanel = new JPanel(gb);
		datePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		GridBagConstraints c = gb.getConstraints(datePanel);
		c.ipadx = 5;
		c.ipady = 5;
		c.gridy = 0;

		c.gridx = 0;
		datePanel.add(new JLabel(Text.lang.get("period")), c);
		c.gridx = 1;
		datePanel.add(startDateField, c);
		c.gridx = 2;
		datePanel.add(new JLabel(" - "), c);
		c.gridx = 3;
		datePanel.add(endDateField, c);
		contentList.add(datePanel);
	}

	/*
	 * This method adds all the button bar to contentList and then adds all the
	 * components gathered in contentList to the ContentPane.
	 */
	public void setContent()
	{
		GridBagLayout gridbag = new GridBagLayout();
		mainPanel.setLayout(gridbag);
		GridBagConstraints g = gridbag.getConstraints(mainPanel);
		int i = 0;
		g.gridx = 0;
		g.ipady = 20;
		for ( Container c : contentList )
		{
			g.weighty = 1.0;
			g.gridy = i++;
			mainPanel.add(c, g);
		}
		g.weighty = 2.0;
		g.gridy = i++;
		mainPanel.add(MenuButtons(), g);
	}

	JPanel MenuButtons()
	{
		ReportButtonListener RBL = new ReportButtonListener();
		JButton showReportInWindowButton = ShowReportInWindowButton(RBL);
		JButton showReportInBrowserButton = ShowReportInBrowserButton(RBL);
		JButton saveReportAsHTMLButton = SaveReportAsHTMLButton(RBL);
		JButton saveReportAsPDFButton = SaveReportAsPDFButton(RBL);
		JButton sendReportToPrinterButton = SendReportToPrinterButton(RBL);
		JButton exitButton = getCancelButton();

		JPanel buttonPanel = new JPanel(new GridLayout(3, 2));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		buttonPanel.add(showReportInWindowButton);
		buttonPanel.add(showReportInBrowserButton);
		buttonPanel.add(saveReportAsHTMLButton);
		buttonPanel.add(saveReportAsPDFButton);
		buttonPanel.add(sendReportToPrinterButton);
		buttonPanel.add(exitButton);

		return buttonPanel;
	}

	public JButton ShowReportInWindowButton(ReportButtonListener RBL)
	{
		JButton showReportInWindowButton = new JButton(Text.lang.get("showReportInWindow"));
		showReportInWindowButton.setActionCommand("showReportInWindow");
		showReportInWindowButton.addActionListener(RBL);
		return showReportInWindowButton;
	}

	public JButton ShowReportInBrowserButton(ReportButtonListener RBL)
	{
		JButton showReportInBrowserButton = new JButton(Text.lang.get("showReportInBrowser"));
		showReportInBrowserButton.setActionCommand("showReportInBrowser");
		showReportInBrowserButton.addActionListener(RBL);

		// this button is useless if we can't open stuff in the browser
		if( !((desktop != null) && desktop.isSupported(Desktop.Action.BROWSE)) )
		{
			showReportInBrowserButton.setEnabled(false);
		}

		return showReportInBrowserButton;
	}

	public JButton SaveReportAsHTMLButton(ReportButtonListener RBL)
	{
		JButton saveReportAsHTMLButton = new JButton(Text.lang.get("saveReportAsHTML"));
		saveReportAsHTMLButton.setActionCommand("saveReportAsHTML");
		saveReportAsHTMLButton.addActionListener(RBL);

		// TODO check HTML support

		return saveReportAsHTMLButton;
	}

	public JButton SaveReportAsPDFButton(ReportButtonListener RBL)
	{
		JButton saveReportAsPDFButton = new JButton(Text.lang.get("saveReportAsPDF"));
		saveReportAsPDFButton.setActionCommand("saveReportAsPDF");
		saveReportAsPDFButton.addActionListener(RBL);

		// TODO check PDF support

		return saveReportAsPDFButton;
	}

	public JButton SendReportToPrinterButton(ReportButtonListener RBL)
	{
		JButton sendReportToPrinterButton = new JButton(Text.lang.get("sendReportToPrinter"));
		sendReportToPrinterButton.setActionCommand("sendReportToPrinter");
		sendReportToPrinterButton.addActionListener(RBL);

		// this button is useless if we can't send stuff to print
		if( !((desktop != null) && desktop.isSupported(Desktop.Action.PRINT)) )
		{
			sendReportToPrinterButton.setEnabled(false);
		}

		return sendReportToPrinterButton;
	}

	class ReportButtonListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			String s = e.getActionCommand();
			if( s.equals("showReportInWindow") )
			{
				showReportInWindow();
			}
			else if( s.equals("showReportInBrowser") )
			{
				showReportInBrowser();
			}
			else if( s.equals("saveReportAsHTML") )
			{
				saveReportAsHTML();
			}
			else if( s.equals("saveReportAsPDF") )
			{
				saveReportAsPDF();
			}
			else if( s.equals("sendReportToPrinter") )
			{
				sendReportToPrinter();
			}
		}

	}

	public Time getStartDate()
	{
		return getDate(startDateField);
	}

	public Time getEndDate()
	{
		return getDate(endDateField);
	}

	public Time getDate(JTextField field)
	{
		String text = field.getText();
		try
		{
			if( !text.contains("-") )
			{
				throw new ParseException("wrong format", 0);
			}
			// Parse string to date-relatable integers
			int year = Integer.parseInt(text.substring(0, text.indexOf("-")));
			text = text.substring(text.indexOf("-") + 1, text.length());
			int month = Integer.parseInt(text.substring(0, text.indexOf("-")));
			text = text.substring(text.indexOf("-") + 1, text.length());
			int day = Integer.parseInt(text);

			Time date = new Time(year, month, day);

			// Check the date is reasonable and valid
			if( 1900 > year || year > 3000 )
				throw new ParseException(year + " is not a reasonable year", 0);

			date.validate();

			// Ensure the period is within the fiscal year
			Time start = book.getStartDate();
			Time end = book.getEndDate();
			if( date.compareTo(start) < 0 || date.compareTo(end) > 0 )
				throw new ParseException(date + " is not a date of the fiscal year " + start + ":"
						+ end, 0);
			
			return date;

		} catch ( ParseException p )
		{
			p.printStackTrace();
			System.out.println(p.getMessage());
			JOptionPane.showMessageDialog(this, text + Text.lang.get("warningWrongFormatDate")
					+ Text.lang.get("warningWrongFormatDateFiscal"),
					Text.lang.get("warningWrongFormatDateTitle"), JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}

	public void showReportInWindow()
	{
		new DisplayHTMLWindow(getReportAsHTMLReport());
	}

	public void showReportInBrowser()
	{
		// when this method is called, we are guaranteed that browsing is
		// supported (in theory)
		// TODO support this
		
		try
		{
			// save file as garble.html to the system's temp dir
			String tmpdir = System.getProperty("java.io.tmpdir");
			String filename = "accbook_report_" + getStartDate() + "_" + getEndDate() + System.currentTimeMillis() + ".html";
			String filepath = tmpdir + System.getProperty("file.separator") + filename;
			File htmlfile = new File(filepath);
			PrintWriter writer = new PrintWriter(htmlfile,"UTF-8");
			writer.write(getReportAsHTMLReport().getHTMLFull());
			writer.close();
			// created an URI from the file path
			URI uri = htmlfile.toURI();
			// browse! :)
			desktop.browse(uri);
		} catch ( Exception e )
		{
			e.printStackTrace();
			System.out.println("An error occurred while attempting to browse the HTML report.");
		}
		
	}

	public void saveReportAsHTML()
	{
		// TODO Auto-generated method stub
		// FileChooser!
	}

	public void saveReportAsPDF()
	{
		// TODO Auto-generated method stub
		// itext?
		// FileChooser!
	}

	public void sendReportToPrinter()
	{
		// TODO Auto-generated method stub

	}

}
