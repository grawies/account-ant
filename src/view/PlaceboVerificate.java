package view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.Book;
import model.Resources;
import model.Time;
import model.Transaction;
import model.TransactionType;
import model.Verificate;

@SuppressWarnings("serial")
public class PlaceboVerificate extends AbstractTableModel
{
	int id;
	String descr, date;
	String[][] table;
	int firstEditLength;
	Book book;
	boolean newVerificate;

	public PlaceboVerificate( Book b, Verificate v, boolean newVer )
	{
		book = b;
		newVerificate = newVer;
		id = v.getID();
		descr = v.getDescription();
		if( v.getDate() != null )
			date = v.getDate().toString();
		else
			date = Resources.dateformatpattern;
		ArrayList<Transaction> transactions = v.getTransactions();
		firstEditLength = transactions.size();

		table = new String[Resources.verificateMaxLen][6];
		for ( int i = 0; i < firstEditLength; i++ )
		{
			Transaction t = transactions.get(i);
			table[i][0] = Integer.toString(t.getAccount().getID());
			table[i][1] = t.getAccount().getName();
			if( t.getType() == TransactionType.DEBET )
			{
				table[i][2] = Resources.decformat.format(t.getValue());
				table[i][3] = "";
			}
			else
			{
				table[i][2] = "";
				table[i][3] = Resources.decformat.format(t.getValue());
			}
			table[i][4] = t.getChangeSignature();
			Time d = t.getChangeDate();
			if( d != null )
				table[i][5] = d.toString();
			else
				table[i][5] = "";
		}
		for ( int i = firstEditLength; i < Resources.verificateMaxLen; i++ )
		{
			for ( int j = 0; j < 5; j++ )
			{
				table[i][j] = "";
			}
			table[i][getColumnIndex(Text.viewVerificateAccountName)] = Text.viewSpreadSheetAccountNameEmpty;
			table[i][getColumnIndex(Text.viewVerificateChangeDate)] = "";
		}
	}

	public Verificate toVerificate()
	{
		return new Verificate(0, "");
	}

	String[] columnNames =
	{ Text.viewVerificateAccountNum, Text.viewVerificateAccountName, Text.viewVerificateDebet,
			Text.viewVerificateCredit, Text.viewVerificateChangeSignature,
			Text.viewVerificateChangeDate };

	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	public int getColumnIndex(String str)
	{
		for ( int i = 0; i < columnNames.length; i++ )
			if( columnNames[i].equals(str) )
				return i;
		return -1;
	}

	public int getColumnCount()
	{
		return 6;
	}

	public int getRowCount()
	{
		return Resources.verificateMaxLen + 1;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		// if it is the last row
		if( rowIndex == Resources.verificateMaxLen )
		{
			String colName = getColumnName(columnIndex);
			// return "sum"-label
			if( colName.equals(Text.viewVerificateAccountName) )
				return Text.viewSum + ":";
			// return column sum of debets and credits
			if( colName.equals(Text.viewVerificateDebet)
					|| colName.equals(Text.viewVerificateCredit) )
			{
				double sum = 0;
				for ( int i = 0; i < Resources.verificateMaxLen; i++ )
				{
					try
					{
						String s = table[i][columnIndex];
						if( !"".equals(s) )
						{
							int dotIndex = s.indexOf(".");
							if( dotIndex < 0 )
								dotIndex = s.length();
							else
								dotIndex += 2;
							sum += Double.parseDouble(s.substring(0, dotIndex));
						}
					} catch ( Exception e )
					{
						// e.printStackTrace();
						return "NaN";
					}
				}
				return Resources.decformat.format(sum);
			}
			// return empty celltext
			return "";
		}
		// otherwise return the transaction table text
		return table[rowIndex][columnIndex];
	}

	public int parseInt(String s)
	{
		try
		{
			return Integer.parseInt(s);
		} catch ( Exception e )
		{
			return -1;
		}
	}

	public void setValueAt(Object o, int rowIndex, int columnIndex)
	{
		System.out.println("setValueAt() called for PlaceboVerificate");
		String colName = getColumnName(columnIndex);
		// if it is an immutable cell, do nothing
		if( rowIndex < firstEditLength
				|| rowIndex == Resources.verificateMaxLen
				|| colName.equals(Text.viewVerificateAccountName)
				|| (newVerificate && (colName.equals(Text.viewVerificateChangeSignature) || colName
						.equals(Text.viewVerificateChangeDate))) )
			return;
		// set table value
		table[rowIndex][columnIndex] = (String) o;

		// if verificate account number, update account name
		if( colName.equals(Text.viewVerificateAccountNum) )
		{
			int accNum = parseInt(table[rowIndex][columnIndex]);
			if( book.accountPlan.containsKey(accNum) )
			{
				table[rowIndex][getColumnIndex(Text.viewVerificateAccountName)] = (String) book.accountPlan
						.get(accNum).getName();
			}
			else
			{
				if( "".equals((String) o) )
					table[rowIndex][getColumnIndex(Text.viewVerificateAccountName)] = "";
				else
					table[rowIndex][getColumnIndex(Text.viewVerificateAccountName)] = Text.viewSpreadSheetAccountNameEmpty;
			}
		}
		// if date, do nothing (handled automagically)
	}
}
