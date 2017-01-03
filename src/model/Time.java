package model;

import java.text.ParseException;
import java.util.Calendar;

public class Time implements Comparable<Time>
{
	public int year, month, day;

	public Time()
	{
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH)+1;
		day = c.get(Calendar.DAY_OF_MONTH);
	}

	public Time( int y, int m, int d )
	{
		year = y;
		month = m;
		day = d;
	}

	public Time( String s )
	{
		// takes a string YYYY-MM-DD
		year = Integer.parseInt(s.substring(0, 4));
		month = Integer.parseInt(s.substring(5, 7));
		day = Integer.parseInt(s.substring(8, 10));
	}

	public Time( Time t )
	{
		year = t.year;
		month = t.month;
		day = t.day;
	}

	public void validate() throws ParseException {

		if( 1 > month || month > 12 )
			throw new ParseException(month + " is not a recognizable month", 0);
		if( (1 > day || day > 31)
				&& (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
						|| month == 10 || month == 12) )
			throw new ParseException(day + " is not a day of month " + month, 0);
		if( (1 > day || day > 30) && (month == 4 || month == 6 || month == 9 || month == 11) )
			throw new ParseException(day + " is not a day of month " + month, 0);
		if( (1 > day || day > 29) && (month == 2) )
			throw new ParseException(day + " is not a day of month " + month, 0);
		if( month == 2 )
		{
			// check leap year
			if( day == 29 )
			{
				if( year % 4 != 0 )
				{
					throw new ParseException(year + " is not a leap year", 0);
				}
				else if( year % 100 != 0 )
				{
					// ok - leap year
				}
				else if( year % 400 != 0 )
				{
					throw new ParseException(year + " is not a leap year", 0);
				}
				// ok - leap year
			}
		}
	}
	
	public String toString()
	{
		// returns a string YYYY-MM-DD
		return String.format("%04d", year) + "-" + String.format("%02d", month) + "-"
				+ String.format("%02d", day);
	}

	public int compareTo(Time that)
	{
		Integer y = this.year-that.year;
		Integer m = this.month - that.month;
		Integer d = this.day - that.day;
		// if difference in year, this decides
		if (y.compareTo(0) != 0)
			return y.compareTo(0);
		// otherwise if difference in month, this decides
		if (m.compareTo(0) != 0)
			return m.compareTo(0);
		// otherwise if difference in day, this decides
		if (d.compareTo(0) != 0)
			return d.compareTo(0);
		// otherwise dates are equal
		return 0;
	}
}
