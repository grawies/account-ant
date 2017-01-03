package model;

import java.util.ArrayList;

public class Verificate {
	private int ID;
	// description may not contain newline
	private String description;
	private Time date;
	private ArrayList<Transaction> transactions;
	
	public Verificate(int id, String descr, Time d, int n) {
		setDate(d);
		setID(id);
		setDescription(descr);
		setTransactions(new ArrayList<Transaction>(n));
	}

	public Verificate(int id, String descr, Time d) {
		this(id, descr, d, 2);
	}

	public Verificate(int id, String descr) {
		this(id, descr, new Time(), 2);
	}
	
	public void addTransaction(Transaction t) {
		if (getTransactions().size() < Resources.verificateMaxLen) {
		getTransactions().add(t);
		}
	}
	public double GetValue() {
		double sum = 0;
		for (Transaction t : getTransactions()) {
			if (t.getType() == TransactionType.CREDIT) {
				sum += t.getValue();
			}
		}
		return sum;		
	}
	public double GetBalance() {
		double sum = 0;
		for (Transaction t : getTransactions()) {
			switch (t.getType()) {
			case CREDIT:
				sum += t.getValue();
				break;
			case DEBET:
				sum -= t.getValue();
				break;
			default:
				break;
			}
		}
		return sum;
	}

	/*
	 * verif 3047 mat och sånt (date) (#transactions) (transaction)
	 * (transaction) ... (transaction)
	 */
	public String toLabelText() {
		String s = String.format("%1$5s", ID) + " | ";
		s += Resources.decformat.format(GetValue()) + " | ";
		s += description;//.length() > Resources.descriptionMaxLen-2 ? description.substring(0,Resources.descriptionMaxLen-3) + "..." : description;
		return s; 
	}
	
	public String toString() {
		String s = "verif\t" + Integer.toString(getID()) + "\n" + getDescription() + "\n"
				+ getDate() + "\n" + getTransactions().size() + "\n";
		for (Transaction t : getTransactions())
			s += t.toString();
		return s;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Time getDate() {
		return date;
	}

	public void setDate(Time date) {
		this.date = date;
	}

	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}

}
