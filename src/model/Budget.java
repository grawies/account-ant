package model;

import java.util.HashMap;
import java.util.Map;

public class Budget
{
	int months;
	Map<Integer, AccountBudget> accountBudgets;

	public Budget( int months )
	{
		this.months = months;
		accountBudgets = new HashMap<Integer, AccountBudget>();
	}

	public Budget( int months, Map<Integer, AccountBudget> ABs )
	{
		this.months = months;
		accountBudgets = ABs;
	}

	public BudgetMonth getBudgetMonth(int month)
	{
		BudgetMonth bm = new BudgetMonth(month);
		for ( AccountBudget ab : accountBudgets.values() )
			bm.setBalance(ab.getAccID(), ab.getBalance(month));
		return bm;
	}

	public double getBalance(int accID)
	{
		if( !accountBudgets.containsKey(accID) )
			return 0.0;
		return accountBudgets.get(accID).getTotalBalance();
	}

	public AccountBudget getAccountBudget(int accID)
	{
		if( !accountBudgets.containsKey(accID) )
		{
			accountBudgets.get(accID);
			double[] empty = new double[months];
			for ( int i = 0; i < months; i++ )
				empty[i] = 0.0;
			addAccountBudget(accID, empty);
		}

		return accountBudgets.get(accID);
	}

	public void addAccountBudget(int accID, double[] values)
	{
		accountBudgets.put(accID, new AccountBudget(accID, values));
	}

	public String toString()
	{
		String s = "budget\t" + accountBudgets.size() + "\n";
		System.out.println("writing budget of " + accountBudgets.size() + " accounts.");
		for ( AccountBudget ab : accountBudgets.values() )
		{
			s += ab.toString();
		}
		return s;
	}
}
