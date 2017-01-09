package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class BookHandling
{
	private BookHandling()
	{
	}

	public static void save (Book book, String dir, String filename)
	{
		try
		{
			// TODO make this OS-unspecific
			System.out.println("Saving Book to file:\n" + ("".equals(dir) ? "./" : dir) + filename
					+ ".acc");
			if( !".acc".equals(filename.substring(filename.length() - 4, filename.length())) )
				filename += ".acc";
			FileWriter fw = new FileWriter(new File(dir + filename));
			BufferedWriter file = new BufferedWriter(fw);

			file.write("accounting book, saved " + (new Time()).toString() + "\n");
			file.write(book.companyName + "\n");
			file.write(book.startDate + "\n" + book.endDate + "\n\n");

			// get a sorted list of accounts
			Account[] accList = new Account[book.accountPlan.size()];
			book.accountPlan.values().toArray(accList);
			Arrays.sort(accList, new Comparator<Account>() {
				public int compare(Account a, Account b)
				{
					return a.getID()-b.getID();
				}
			});
					
			// write account balances in account number order
			for ( Account acc : accList )
			{
				// System.out.println("writing acc: " + acc.toString());
				file.write(acc.toString());
				file.write("\n");
			}
			// write verificates
			for ( Verificate ver : book.verificates )
			{
				file.write(ver.toString());
				file.write("\n");
			}
			// write budget
			if( book.budget != null )
				file.write(book.budget.toString());

			file.write("\n");
			file.write("EOF.");
			file.close();
			System.out.println("Saving complete!");
		} catch ( IOException e )
		{
			e.printStackTrace();
			System.out.println("Unsuccessful saving file!");
		}
	}

	public static Book load(String dir, String filename) throws FileNotFoundException,
			ParseException
	{
		try
		{
			Book book;
			scanner = new Scanner(new File(dir + filename));

			System.out.println("Loading " + readNextLine());
			book = new Book(readNextLine(), new Time(readNextLine()), new Time(readNextLine()));
			System.out.println("Company: " + book.companyName);
			System.out.println("Fiscal year starts:\t" + book.startDate);
			System.out.println("Fiscal year ends:\t" + book.endDate);
			System.out.println("Fiscal year length:\t" + book.getMonths() + " months");
			while( true )
			{
				String s = readNextLine();
				// System.out.println("Next line: " + s);
				if( s.startsWith("account\t") )
				{
					// read accounts
					boolean isActive;
					int id;

					String activity = s.substring(8, 9);
					// if no activity letter, default to active and read id
					if( "0123456789".contains(activity) )
					{
						isActive = true;
						id = Integer.parseInt(s.substring(8));
					}
					else
					{
						isActive = "a".equals(activity) ? true : false;
						id = Integer.parseInt(s.substring(10));
					}
					String name = readNextLine();
					double value = Resources.decformat.parse(readNextLine()).doubleValue();
					System.out.println("Reading account: " + id + " " + name);
					book.addAccount(id, name, value, isActive);
				}
				else if( s.startsWith("verif\t") )
				{
					// read verificates
					int id = Integer.parseInt(s.substring(6));
					String descr = readNextLine();
					System.out.println("Reading verificate: " + id);
					Time date = new Time(readNextLine());
					int transCount = Integer.parseInt(readNextLine());
					Verificate verificate = new Verificate(id, descr, date, transCount);
					for ( int i = 0; i < transCount; i++ )
					{
						// read transactions
						verificate.addTransaction(ReadTransaction(book));
					}
					book.addVerificate(verificate);
				}
				else if( s.startsWith("budget") )
				{
					System.out.println("Reading budget");
					// read budget
					Budget budget = new Budget(book.getMonths());
					int accCount = Integer.parseInt(s.substring(7));
					// read each account budget
					for ( int i = 0; i < accCount; i++ )
					{
						double[] balances = new double[book.getMonths()];
						int accID = Integer.parseInt(readNextLine());
						// read monthly balances
						for ( int j = 0; j < book.getMonths(); j++ )
						{
							balances[j] = Resources.decformat.parse(readNextLine()).doubleValue();
						}
						budget.addAccountBudget(accID, balances);
					}
					book.addBudget(budget);
				}
				else if( s.startsWith("EOF.") )
				{
					// done reading
					break;
				}
			}
			// connect verificates to accounts
			scanner.close();
			System.out.println("Loading complete!");
			return book;
		} catch ( IOException e )
		{
			e.printStackTrace();
			System.out.println("Unsuccessful loading file!");
		}
		return null;
	}

	public static Scanner scanner;

	public static String readNextLine()
	{
		String s;
		do
		{
			s = scanner.nextLine();
		} while( s.startsWith("\t") );
		return s;
	}

	public static Transaction ReadTransaction(Book book) throws ParseException
	{
		String st = readNextLine();
		// System.out.println("nextline: " + st);
		TransactionType type = TransactionType.parseType(st.substring(6));
		int t_id = Integer.parseInt(readNextLine());
		double value = Resources.decformat.parse(readNextLine()).doubleValue();
		if( type != TransactionType.BUDGET )
		{
			String changeSignature = readNextLine();
			Time changeDate;
			if( "-".equals(changeSignature) )
			{
				changeSignature = "";
				changeDate = null;
			}
			else
			{
				changeDate = new Time(readNextLine());
			}
			return new Transaction(book.accountPlan.get(t_id), value, type, changeSignature,
					changeDate);
		}
		else
		{
			return new Transaction(book.accountPlan.get(t_id), value, type);
		}
	}
}
