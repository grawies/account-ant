package model;

import java.io.Serializable;

/*
 * This class contains the projected balances of an account over a fiscal year.
 */
public class AccountBudget implements Serializable
{
	// TODO have better serialVersionUIDs
	private static final long serialVersionUID = 42L;

	private int accID;
	private double[] monthBalances;

	public AccountBudget( int accnum, double[] balances )
	{
		accID = accnum;
		monthBalances = balances;
	}

	public int getAccID()
	{
		return accID;
	}

	public void setBalances(double[] balances)
	{
		monthBalances = balances;
	}

	public void setBalance(int month, double value)
	{
		monthBalances[month] = value;
	}

	public double getBalance(int month)
	{
		return monthBalances[month];
	}
	public double getPeriodBalance(Book b, Time start, Time end) {
		Time bookStart = b.getStartDate();
		double sum = 0;
		for (int monthIndex = start.month-bookStart.month; monthIndex<=end.month-bookStart.month; monthIndex++) {
			sum += monthBalances[monthIndex];
		}
		return sum;
	}

	public double getTotalBalance()
	{
		double sum = 0;
		for ( int i = 0; i < monthBalances.length; i++ )
			sum += monthBalances[i];
		return sum;
	}

	public String toString()
	{
		String s = Integer.toString(accID) + "\n";
		for ( int i = 0; i < monthBalances.length; i++ )
			s += Resources.decformat.format(monthBalances[i]) + "\n";
		return s;
	}
}
