package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.AccountBudget;
import model.Book;
import model.Budget;
import model.Resources;
import model.Time;

@SuppressWarnings("serial")
public class BudgetWindow extends UniqueJFrame
{
	static boolean windowOpen = false;
	public boolean isWindowOpen() {
		return windowOpen;
	}
	public void setWindowOpenness(boolean open) {
		windowOpen = open;
	}
	Book book;
	Budget budget;
	int selectedAccID = -1;

	MultiColumnPanel accountListPanel;
	AccountBudgetPanel accountBudgetPanel;

	public BudgetWindow( Book b )
	{
		super();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		tryCreatingNewUniqueWindow();
		book = b;
		budget = b.getBudgetClone();
		if( budget == null )
		{
			System.out.println("Closing budget window.");
			closeWindow();
		}

		// structure:
		// JTABBEDPANE
		// ACCOUNTLIST accounts, balance
		// ACCOUNTBUDGET acc info + balance for each month
		// BUTTON save
		// BUTTON cancel

		// ----------------------------------------

		// ACCOUNTLIST accounts, balance
		accountListPanel = new MultiColumnPanel(b.accountPlan, b.getBudget());
		accountListPanel.list.setSelectedIndex(0);
		accountListPanel.list.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				accountListSelectionChanged();
			}
		});

		// ACCOUNTBUDGET acc info + balance for each month
		accountBudgetPanel = new AccountBudgetPanel();

		// JTABBEDPANE
		JTabbedPane accountPanel = new JTabbedPane();
		accountPanel.add(Text.viewMainAccountplanTabName, accountListPanel);
		accountPanel.add(Text.editAccountBudget, accountBudgetPanel);

		// BUTTON save
		// BUTTON cancel

		GridBagLayout gb = new GridBagLayout();
		JPanel buttonBar = new JPanel(gb);
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 10;
		c.ipady = 10;
		gb.setConstraints(buttonBar, c);
		buttonBar.setPreferredSize(new Dimension(300, 50));

		JButton saveButton = new JButton(Text.viewBudgetSave);
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				book.addBudget(budget);
				System.out.println("Budget updated!");
			}
		});
		JButton cancelButton = getCancelButton();

		NavigateAccountListListener nall = new NavigateAccountListListener();
		JButton left = new JButton("<");
		left.setActionCommand("<");
		left.addActionListener(nall);
		JButton right = new JButton(">");
		right.setActionCommand(">");
		right.addActionListener(nall);

		buttonBar.add(left);
		buttonBar.add(saveButton);
		buttonBar.add(cancelButton);
		buttonBar.add(right);

		JPanel p = new JPanel();
		p.add(accountPanel, "Center");
		p.add(buttonBar, "South");
		add(p);

		setTitle(Text.viewBudgetTitle);
		setSize(700, 500);
		setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public class AccountBudgetPanel extends JPanel implements KeyListener
	{

		// structure:
		// JLABEL Account ID
		// JLABEL Account description
		// LIST months | balance

		// ----------------------------------------

		JTextField accIDLabel;
		JTextField accDescrLabel;
		int textFieldSize = 7, labelFieldSize = 15;
		private JTextField[] monthBalances;
		JTextField totalBalance;
		HashMap<JTextField,String> backupText;
		
		public AccountBudgetPanel()
		{
			GridBagLayout gb = new GridBagLayout();
			JPanel monthList = new JPanel(gb);
			GridBagConstraints c = new GridBagConstraints();
			c.ipadx = 4;
			c.ipady = 4;
			gb.setConstraints(monthList, c);

			int row = 0;

			// JLABEL Account ID
			c.gridy = row++;
			c.gridx = 0;
			monthList.add(new JLabel(Text.account), c);
			c.gridx = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			accIDLabel = new JTextField(4);
			accIDLabel.setEditable(false);
			accIDLabel.setHorizontalAlignment(JTextField.CENTER);
			accIDLabel.setText("####");
			monthList.add(accIDLabel, c);

			// JLABEL Account description
			c.gridy = row++;
			c.gridx = 0;
			c.gridwidth = 2;
			accDescrLabel = new JTextField(labelFieldSize + textFieldSize + 1);
			accDescrLabel.setEditable(false);
			accDescrLabel.setText("Account description goes here");
			monthList.add(accDescrLabel, c);
			c.gridwidth = 1;

			// LIST months | balance
			// create the rows
			monthBalances = new JTextField[book.getMonths()];
			Time t = book.getStartDate();
			int year = t.year;
			int month = t.month;
			for ( int i = 0; i < book.getMonths(); i++ )
			{
				c.gridy = row++;
				c.gridx = 0;
				// LABEL
				String monthText = (i <= 9 ? "  " : "") + i + " | " + year + " "
						+ Text.viewMonth[i];
				// JLabel monthLabel = new JLabel(monthText,JLabel.LEFT);
				JTextField jt = new JTextField(labelFieldSize);
				jt.setEditable(false);
				jt.setText(monthText);
				System.out.println(monthText);
				monthList.add(jt, c);

				c.gridx = 1;
				// INPUT FIELD
				monthBalances[i] = new JTextField(textFieldSize);
				monthBalances[i].setText("");
				monthBalances[i].addKeyListener(this);
				monthList.add(monthBalances[i], c);

				// turn year when month is too high
				month++;
				if( month >= 12 )
				{
					month -= 12;
					year += 1;
				}
			}

			c.gridy = row++;
			c.gridx = 0;
			monthList.add(new JLabel(Text.viewSum), c);
			c.gridx = 1;
			totalBalance = new JTextField(textFieldSize);
			totalBalance.setText("0.00");
			totalBalance.setEditable(false);
			monthList.add(totalBalance, c);

			add(monthList, "Center");

			backupText = new HashMap<JTextField, String>();
			for (JTextField jt : monthBalances)
				backupText.put(jt, jt.getText());
		}
		
		public void setMonthBalance(int month, double balance) {
			monthBalances[month].setText(Resources.decformat.format(balance));
			backupText.put(monthBalances[month], monthBalances[month].getText());
		}

		public void updateBalanceFields()
		{

			String totalBalanceString;
			try
			{
				double sum = 0;
				for ( int i = 0; i < monthBalances.length; i++ )
				{
					String s = monthBalances[i].getText();
					if( !"".equals(s) )
						sum += Double.parseDouble(s);
				}
				totalBalanceString = Resources.decformat.format(sum);
			} catch ( Exception ex )
			{
				totalBalanceString = Text.noID;
				System.out.println("Not nice numbers in the account balance fields");
			}
			totalBalance.setText(totalBalanceString);
			accountListPanel.list.getSelectedValue()[2] = totalBalanceString;
		}

		public void keyReleased(KeyEvent e)
		{
			updateBalanceFields();
			// if uncalculable sum, the key pressed was not cool.
			JTextField src = (JTextField)e.getSource();
			if (totalBalance.getText().equals(Text.noID)) {
				
				src.setText(backupText.get(src));
			} else {
				backupText.put(src,src.getText());
			}
			updateBalanceFields();
		}

		public void keyPressed(KeyEvent e)
		{
		}

		public void keyTyped(KeyEvent e)
		{
		}
	}

	public class NavigateAccountListListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{

			int indexShift = e.getActionCommand().equals(">") ? 1 : -1;
			int selectedIndex = accountListPanel.list.getSelectedIndex() + indexShift;
			if( selectedIndex < 0 )
				selectedIndex = accountListPanel.columnData.length - 1;
			if( selectedIndex >= accountListPanel.columnData.length )
				selectedIndex = 0;
			accountListPanel.list.setSelectedIndex(selectedIndex);
			// TODO ensure accountListSelectionChanged() is invoked implicitly
			// by this method
		}

	}

	public void accountListSelectionChanged()
	{
		String selectedAcc;
		int selectedAccID;

		// save budget of current selected account
		selectedAcc = accountBudgetPanel.accIDLabel.getText();
		if( !Text.noID.equals(selectedAcc) )
		{
			selectedAccID = Integer.parseInt(selectedAcc);
			double[] monthBalances = new double[book.getMonths()];
			for ( int i = 0; i < book.getMonths(); i++ )
			{
				monthBalances[i] = Double
						.parseDouble(accountBudgetPanel.monthBalances[i].getText());
			}
			budget.addAccountBudget(selectedAccID, monthBalances);
		}
		// load selected account into budget panel
		selectedAcc = accountListPanel.list.getSelectedValue()[0];
		selectedAccID = Integer.parseInt(selectedAcc);
		accountBudgetPanel.accIDLabel.setText(selectedAcc);
		accountBudgetPanel.accDescrLabel.setText(book.accountPlan.get(selectedAccID).getName());
		AccountBudget accBudget = budget.getAccountBudget(selectedAccID);
		for ( int i = 0; i < book.getMonths(); i++ )
		{
			accountBudgetPanel.setMonthBalance(i, accBudget.getBalance(i));
		}
		accountBudgetPanel.updateBalanceFields();
	}

	public void showWindowAlreadyOpenDialog()
	{
		JOptionPane.showMessageDialog(null, Text.warningbudgetWindowAlreadyOpenText,
				Text.warningbudgetWindowAlreadyOpen, JOptionPane.WARNING_MESSAGE);
	}
}
