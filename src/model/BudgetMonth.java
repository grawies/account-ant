package model;

import java.util.HashMap;

public class BudgetMonth
{
	private int number;
	private HashMap<Integer, Double> accountBalances;

	public BudgetMonth( int number )
	{
		this.number = number;
		accountBalances = new HashMap<Integer, Double>();
	}

	public int getMonth()
	{
		return number;
	}

	public void setBalance(int accID, double balance)
	{
		accountBalances.put(accID, balance);
	}

	public double getBalance(int accID, double balance)
	{
		if( accountBalances.containsKey(accID) )
			return accountBalances.get(accID);
		return 0.0;
	}
}
