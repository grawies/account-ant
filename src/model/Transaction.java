package model;


public class Transaction
{
	public static final Transaction BrokenTransaction = new Transaction(null, Double.MIN_VALUE);
	private Account acc;
	private double value;
	private TransactionType type;
	// signature may not contain newline
	private String changeSignature;
	private Time changeDate;

	public Transaction( Account a, double Dr )
	{
		this(a, Dr, TransactionType.BUDGET, "", null);
	}

	public Transaction( Account a, double cash, TransactionType t )
	{
		this(a, cash, t, "", null);
	}

	public Transaction( Account a, double cash, TransactionType t, String sign, Time date )
	{
		setAcc(a);
		setValue(cash);
		setType(t);
		setChangeSignature(sign);
		setChangeDate(date);
	}

	public String toString()
	{
		String s = "trans\t" + getType() + "\n" + getAccount().getID() + "\n" + getValue() + "\n";
		if( getType() != TransactionType.BUDGET )
		{
			if( changeDate == null )
			{
				s += "-\n";
			}
			else
			{
				s += changeSignature + "\n" + changeDate + "\n";
			}
		}
		return s;
	}

	public Account getAccount()
	{
		return acc;
	}

	public void setAcc(Account acc)
	{
		this.acc = acc;
	}

	public TransactionType getType()
	{
		return type;
	}

	public void setType(TransactionType type)
	{
		this.type = type;
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	public String getChangeSignature()
	{
		return changeSignature;
	}

	public void setChangeSignature(String changeSignature)
	{
		this.changeSignature = changeSignature;
	}

	public Time getChangeDate()
	{
		return changeDate;
	}

	public void setChangeDate(Time changeDate)
	{
		this.changeDate = changeDate;
	}
}
