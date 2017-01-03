package model;

import java.util.ArrayList;

public class Account
{
	private int ID;
	// name may not contain newline
	private String name;
	private ArrayList<Verificate> verificates;
	double initialBalance;
	double outgoingBalance;
	boolean isActive;

	public Account( int i, String n )
	{
		this(i, n, 0.0, true);
	}

	public Account( int i, String n, double inbalance )
	{
		this(i, n, inbalance, true);
	}

	public Account( int i, String n, double inbalance, boolean active )
	{
		setID(i);
		setName(n);
		verificates = new ArrayList<Verificate>();
		initialBalance = inbalance;
		outgoingBalance = inbalance;
		isActive = active;
	}

	public void addVerificate(Verificate v)
	{
		for (Verificate v2 : verificates) {
			if (v.getID() == v2.getID())
				return;
		}
		
		verificates.add(v);
		for ( Transaction t : v.getTransactions() )
		{
			if( t.getAccount().getID() == ID )
			{
				updateTransaction(t);
			}
		}
	}

	public void updateTransaction(Transaction t)
	{
		if (ID == 1930)
			System.out.println("Adding " + t.getValue() * (t.getType() == TransactionType.DEBET ? 1 : -1));
		switch( t.getType() )
		{
		case DEBET:
			outgoingBalance += t.getValue();
			break;
		case CREDIT:
			outgoingBalance -= t.getValue();
			break;
		default:
			System.err.println("Error in interpreting verificate transaction as debet/credit!");
			break;
		}
	}

	public double getInBalance()
	{
		return initialBalance;
	}

	public double getOutBalance()
	{
		return outgoingBalance;
	}

	/*
	 * GetOutBalance(Time date) returns the balance of the account over the
	 * inclusive time interval [start,end]
	 */
	public double getOutBalance(Time start, Time end)
	{
		double balance = initialBalance;
		ArrayList<Integer> accountedFor = new ArrayList<Integer>();
		for ( Verificate v : verificates )
		{
			// ensure we don't count any verificate twice
			boolean doubleCounting = false;
			for (Integer i : accountedFor) {
				if (i.intValue() == v.getID())
					doubleCounting = true;
			}
			if (doubleCounting)
				continue;
			
			accountedFor.add(v.getID());
			
			Time time = v.getDate();
			if( time.compareTo(start) < 0 || time.compareTo(end) > 0 )
			{
				// these are not the transactions we're looking for
				continue;
			}
			for ( Transaction t : v.getTransactions() )
			{
				if( t.getAccount().getID() == ID )
				{
					balance += t.getValue() * (t.getType() == TransactionType.DEBET ? 1 : -1);
				}
			}
		}
		return balance;
	}

	public String toString()
	{
		return "account\t" + (isActive ? "a" : "i") + "\t" + getID() + "\n" + getName() + "\n"
				+ Resources.decformat.format(getInBalance()) + "\n";
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int iD)
	{
		ID = iD;
	}

	public String getName()
	{
		return name;
	}

	public boolean isActive()
	{
		return isActive;
	}

	public void setActive(boolean a)
	{
		isActive = a;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ArrayList<Verificate> getVerificates()
	{
		return verificates;
	}

	public void setVerificates(ArrayList<Verificate> verificates)
	{
		this.verificates = verificates;
	}
}
