package model;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

public class Resources {
	public static final int verificateMaxLen = 8;
	public static final int descriptionMaxLen = 20;
	public static String dateformatpattern = "yyyy-MM-dd";
	public static String localeCode = "se";
	public static double maxError = 0.000001;
	//public static EnglishDecimalFormat decformat = new EnglishDecimalFormat("0.00");
	public static DecimalFormat decformat = new DecimalFormat("0.00");
	//public static DecimalFormat intformat = new DecimalFormat("");
}
