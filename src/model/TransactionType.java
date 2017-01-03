package model;

import java.util.InputMismatchException;

public enum TransactionType {
	CREDIT,
	DEBET,
	BUDGET;
	// better model: TransactionType.UNKNOWN
	public static TransactionType parseType(String s) throws InputMismatchException {
		if (s.equals("CREDIT"))
			return CREDIT;
		if (s.equals("DEBET"))
			return DEBET;
		if (s.equals("BUDGET"))
			return BUDGET;
		else {
			throw new InputMismatchException("Unknown TransactionType: " + s);
		}
	}
}
