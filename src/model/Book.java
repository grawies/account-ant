package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book
{
	String companyName;
	Time startDate, endDate;
	private int months;
	public Map<Integer, Account> accountPlan;
	public List<Verificate> verificates;
	Budget budget;

	public Book( String companyName, Time startdate, Time enddate )
	{
		this.companyName = companyName;
		startDate = startdate;
		endDate = enddate;
		months = (endDate.year - startDate.year) * 12 + (endDate.month - startDate.month) + 1;
		accountPlan = new HashMap<Integer, Account>();
		verificates = new ArrayList<Verificate>();
		budget = new Budget(months);
	}

	public void addAccount(int id, String name, double value, boolean isActive)
	{
		accountPlan.put(id, new Account(id, name, value, isActive));
	}

	public void addAccount(int id, String name, double value)
	{
		addAccount(id, name, value, true);
	}

	public void addVerificate(Verificate v)
	{
		verificates.add(v);
		for ( Transaction t : v.getTransactions() )
		{
			t.getAccount().addVerificate(v);
		}
	}

	public void updateVerificate(Verificate v)
	{
		int id = v.getID();
		Verificate original = verificates.get(id - 1);
		for ( Transaction t : v.getTransactions() )
		{
			original.addTransaction(t);
			t.getAccount().updateTransaction(t);
		}
	}

	public void addBudget(Budget b)
	{
		budget = b;
	}

	public Budget getBudget()
	{
		return budget;
	}

	/*
	 * Method getBudgetClone() returns a deep copy of the current budget. It is
	 * achieved by serializing and deserializing the budget.
	 */
	@SuppressWarnings("unchecked")
	public Budget getBudgetClone()
	{
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(budget.accountBudgets);
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();

			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			Map<Integer, AccountBudget> accountBudgets = (HashMap<Integer, AccountBudget>) new ObjectInputStream(
					bais).readObject();
			return new Budget(months, accountBudgets);
		} catch ( Exception e )
		{
			e.printStackTrace();
			System.out.println("Failed to copy budget!");
			return null;
		}
	}

	// GetResultReport
	// GetBalanceReport
	public static void Save(Book book, String dir, String filename)
	{
		BookHandling.Save(book, dir, filename);
	}

	static public Book Load(String dir, String filename) throws ParseException,
			FileNotFoundException
	{
		return BookHandling.Load(dir, filename);
	}

	public int getNextVerificateID()
	{
		return verificates.size() + 1;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public int getMonths()
	{
		return months;
	}

	public Time getStartDate()
	{
		return new Time(startDate);
	}

	public Time getEndDate()
	{
		return new Time(endDate);
	}
}
