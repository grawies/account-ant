package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import model.Account;
import model.Book;
import model.Resources;
import model.Time;
import model.Transaction;
import model.TransactionType;
import model.Verificate;
import model.report.ReportGenerator;
import model.report.ResultsReport;

@SuppressWarnings("serial")
public class VerificateWindow extends UniqueJFrame
{
	static boolean windowOpen = false;
	public boolean isWindowOpen() {
		return windowOpen;
	}
	public void setWindowOpenness(boolean open) {
		windowOpen = open;
	}
	
	Book book;
	PlaceboVerificate placverif;
	JTextField verifDate;
	JTextField verifDescr;
	boolean newVerificate;

	public VerificateWindow( Book b, Verificate v, boolean newVerificate )
	{
		super();
		book = b;
		this.newVerificate = false;

		if (newVerificate || v == null)
		{
			tryCreatingNewUniqueWindow();
			// new Verificate
			System.out.println("Creating window for new verificate");
			if (v == null) {
				int id = book.getNextVerificateID();
				v = new Verificate(id, "");
			}
			this.newVerificate = true;
		}
		placverif = new PlaceboVerificate(b, v, this.newVerificate);

		setTitle(Text.viewVerificateNum + placverif.id);

		JLabel headerLabel = new JLabel(Text.viewVerificateNum + placverif.id + ":",
				SwingConstants.RIGHT);
		verifDate = new JTextField(placverif.date, 15);

		JLabel descrLabel = new JLabel(Text.viewVerificateDescr + ":", SwingConstants.RIGHT);
		verifDescr = new JTextField(placverif.descr, 40);

		if( !placverif.newVerificate )
		{
			verifDate.setEditable(false);
			verifDescr.setEditable(false);
		}
		JPanel headerPanel = new JPanel(new GridLayout(2, 2));
		headerPanel.add(headerLabel);
		headerPanel.add(verifDate);
		headerPanel.add(descrLabel);
		headerPanel.add(verifDescr);

		SpreadSheetJTable ssjt = makeSpreadSheetJTable();
		JScrollPane scrollpane = new JScrollPane(ssjt, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		GridBagLayout gb = new GridBagLayout();
		JPanel buttonBar = new JPanel(gb);
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 10;
		c.ipady = 10;
		gb.setConstraints(buttonBar, c);
		buttonBar.setPreferredSize(new Dimension(200, 50));
		JButton saveButton = new JButton(Text.viewVerificateSave);
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				saveVerificate();
			}
		});
		JButton cancelButton = getCancelButton();

		buttonBar.add(saveButton);
		buttonBar.add(cancelButton);

		JPanel p = new JPanel(new GridLayout(3, 1));
		p.add(headerPanel);// , "North");
		p.add(scrollpane);// , "Center");
		p.add(buttonBar);// , "South");

		add(p);

		setLocationRelativeTo(null);
		setBounds(300, 200, 600, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public SpreadSheetJTable makeSpreadSheetJTable()
	{
		SpreadSheetColumnType[] colTypes =
		{ SpreadSheetColumnType.ACCOUNTNUMBER, SpreadSheetColumnType.ACCOUNTNAME,
				SpreadSheetColumnType.DOUBLE, SpreadSheetColumnType.DOUBLE,
				SpreadSheetColumnType.PLAINTEXT, SpreadSheetColumnType.DATE };
		boolean signatureEnabled = placverif.newVerificate;
		boolean[] editCols =
		{ true, false, true, true, !signatureEnabled, false };
		String[] columnNames =
		{ Text.viewVerificateAccountNum, Text.viewVerificateAccountName, Text.viewVerificateDebet,
				Text.viewVerificateCredit, Text.viewVerificateChangeSignature,
				Text.viewVerificateChangeDate };
		return new SpreadSheetJTable(placverif.table, columnNames, book, colTypes, editCols,
				placverif.firstEditLength, true);
	}

	public Time parseVerificateDate()
	{
		try
		{
			String dateText = verifDate.getText();
			return new Time(dateText);
		} catch ( Exception e )
		{
			JOptionPane.showMessageDialog(this, Text.warningWrongFormatDate,
					Text.warningVerificateUnbalanced, JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}

	public Transaction parseTransaction(int rowIndex)
	{
		// returns null if all fields are empty
		// returns Transaction.BrokenTransaction if row is non-empty but an
		// invalid Transaction
		// returns a complete Transaction if row is valid Transaction
		boolean accEmpty = false;
		boolean valuesEmpty = false;
		boolean signEmpty = false;
		String accNumText = (String) placverif.getValueAt(rowIndex,
				placverif.getColumnIndex(Text.viewVerificateAccountNum));
		int accNum = placverif.parseInt(accNumText);
		Account acc;
		if( accNum == -1 )
		{
			accEmpty = true;
			acc = null;
		}
		else if( !book.accountPlan.containsKey(accNum) )
		{
			System.out.println("error: invalid account name");
			return Transaction.BrokenTransaction;
		}
		else
		{
			// else account number is OK
			acc = book.accountPlan.get(accNum);
		}

		String debetText = (String) placverif.getValueAt(rowIndex,
				placverif.getColumnIndex(Text.viewVerificateDebet));
		String creditText = (String) placverif.getValueAt(rowIndex,
				placverif.getColumnIndex(Text.viewVerificateCredit));
		String valueText = "";
		double value = 0.0;
		TransactionType type;
		// find type & value
		boolean debetEmpty = "".equals(debetText);
		boolean creditEmpty = "".equals(creditText);
		if( debetEmpty && creditEmpty )
		{
			valuesEmpty = true;
			value = 0.0;
			type = null;
		}
		else
		{
			if( debetEmpty || creditEmpty )
			{
				if( debetEmpty )
				{
					type = TransactionType.CREDIT;
					valueText = creditText;
				}
				else
				{
					type = TransactionType.DEBET;
					valueText = debetText;
				}
			}
			else
			{
				// else both debet & credit are non-empty
				System.out.println("error: both debet & credit non-empty");
				System.out.println("DEBET: " + debetText);
				System.out.println("CREDIT: " + creditText);
				return Transaction.BrokenTransaction;
			}
		}
		if( !(debetEmpty && creditEmpty) )
		{
			try
			{
				if( ".".equals(valueText) )
				{
					value = 0.0;
				}
				else
				{
					value = (double) (Resources.decformat.parse(valueText).doubleValue());
				}
			} catch ( ParseException e )
			{
				// e.printStackTrace();
				System.out.println("error: format of value: " + valueText);
				return Transaction.BrokenTransaction;
			}
		}

		Time date;

		// if transaction is from an unedited verificate, don't check the
		// signature
		if( newVerificate )
		{
			if( accEmpty && valuesEmpty )
			{
				return null;
			}
			else if( accEmpty || valuesEmpty )
			{
				return Transaction.BrokenTransaction;
			}
			else
			{
				date = null;
				return new Transaction(acc, value, type, "", date);
			}
		}
		// else new transaction, we get the signature from the table and the
		// date is today
		String signature = (String) placverif.getValueAt(rowIndex,
				placverif.getColumnIndex(Text.viewVerificateChangeSignature));
		if( "".equals(signature) )
		{
			signEmpty = true;
			// if all empty, it's a null row
			if( accEmpty && valuesEmpty )
				return null;
			// else not all filled
			System.out.println("error: some empty fields code " + (accEmpty ? "0" : "1")
					+ (valuesEmpty ? "0" : "1") + (signEmpty ? "0" : "1"));
			return Transaction.BrokenTransaction;
		}

		date = new Time();

		return new Transaction(acc, value, type, signature, date);
	}

	public void saveVerificate()
	{
		// try parsing window data as verificate

		String descrText = verifDescr.getText();
		Time date = parseVerificateDate();// verifDate.getText());
		if( date == null )
			return;
		Verificate verificate = new Verificate(placverif.id, descrText, date);

		// try parsing transaction data
		int startIndex = 0;
		// if this is an old verificate, we only parse newer lines - these
		// transactions are then added to relevant accounts and verificates
		// older lines are already accounted for in the accounts
		if( !newVerificate )
			startIndex = placverif.firstEditLength;
		for ( int i = startIndex; i < Resources.verificateMaxLen - 1; i++ )
		{
			Transaction transaction = parseTransaction(i);
			if( transaction == null )
			{
				// empty row
				System.out.println("skipping empty transaction row");
			}
			else if( transaction.equals(Transaction.BrokenTransaction) )
			{
				// unclear/badly formatted transaction on line i, break
				JOptionPane.showMessageDialog(this, Text.warningAmbiguousTransactionText + (i + 1)
						+ ".", Text.warningAmbiguousTransaction, JOptionPane.WARNING_MESSAGE);
				return;
			}
			else
			{
				verificate.addTransaction(transaction);
				System.out.println("Successfully added transaction:\n" + transaction.toString());
			}

		}
		if( verificate.getTransactions().size() == 0 )
		{
			JOptionPane.showMessageDialog(this, Text.warningVerificateEmptyText,
					Text.warningVerificateEmpty, JOptionPane.WARNING_MESSAGE);
			return;
		}
		// check the balance
		double balance = verificate.GetBalance();
		System.out.println("Verificate balance: " + balance);
		if( Math.abs(balance) > Resources.maxError )
		{
			JOptionPane.showMessageDialog(this, Text.viewVerificateBalance + ": "
					+ Resources.decformat.format(balance), Text.warningVerificateUnbalanced,
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// publish to book
		if( newVerificate )
			book.addVerificate(verificate);
		else
		{
			book.updateVerificate(verificate);
		}
		GraphicsResources.VAListChanged();
		System.out.println("Verificate saved successfully");
		closeWindow();
	}

	public void showWindowAlreadyOpenDialog()
	{
		JOptionPane.showMessageDialog(null, Text.warningNewVerificateAlreadyOpenText,
				Text.warningNewVerificateAlreadyOpen, JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Opens a new verificate window, prepared with a end-of-month entry for a
	 * user-supplied month.
	 */
	public static VerificateWindow openNewEndOfMonthWindow(Book book) {
		if (!book.accountPlan.containsKey(2019) || !book.accountPlan.containsKey(8999)) {
			System.out.println("One of accounts 2019, 8999 not contained in Book.");
			return null;
		}

 		// guess month (from last verificate)
		int latestMonth = book.getVerificate(book.getNextVerificateID() - 1).getDate().month;
		// get input, with guessed month defaulted
		String choice = (String)JOptionPane.showInputDialog(null, Text.endOfMonthMonthChoiceText,
				Text.endOfMonthMonthChoiceTitle, JOptionPane.QUESTION_MESSAGE,
                                     null,
                                     Text.viewMonth,
             Text.viewMonth[latestMonth-1]);
		if (choice == null) {
		    return null;	
		}
		int chosenMonth = 1;
		for (; chosenMonth<=12;chosenMonth++) {
			if (Text.viewMonth[chosenMonth-1].equals(choice)) {
				break;
			}
		}
		// Compute the total balance.
		int year = book.getStartDate().getYearOfNextMonth(chosenMonth);
		Time start = new Time(year, chosenMonth, 1);
		Time end = new Time(year, chosenMonth, Time.getMonthLength(year, chosenMonth));
		ResultsReport report = ReportGenerator.generateResultsReport(book, start, end);
		double balance = report.getNetBalance();
		
		// Create a end-of-month verificate template.
		String verificateDescription = Text.lang.get("viewVerificateEndOfMonthDescription") + " " + Text.lang.get("viewMonth" + Integer.toString(chosenMonth-1));
		Verificate v = new Verificate(book.getNextVerificateID(), verificateDescription, end);
		Account debetAccount = book.accountPlan.get(8999), creditAccount = book.accountPlan.get(2019);
		if (balance < 0) {
			balance = -balance;
			Account tmp = debetAccount;
			debetAccount = creditAccount;
			creditAccount = tmp;
		}
		v.addTransaction(new Transaction(debetAccount, balance, TransactionType.DEBET));
		v.addTransaction(new Transaction(creditAccount, balance, TransactionType.CREDIT));
		
		return new VerificateWindow(book, v, true);
	}
}
