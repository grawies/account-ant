package view;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.JOptionPane;

import model.Resources;

public class Text {
	
	public static Locale locale;
	
	public static String viewVerificateAccountNum;
	public static String viewVerificateAccountName;
	public static String viewVerificateDebet;
	public static String viewVerificateCredit;
	public static String viewVerificateChangeSignature;
	public static String viewVerificateChangeDate;

	public static String viewVerificateNum;
	public static String viewVerificateDescr;
	public static String viewSum;
	public static String viewVerificateBalance;

	public static String viewVerificateSave;
	public static String viewBudgetSave;
	public static String viewCancel;

	public static String account;
	public static String viewAccountBudget;
	public static String editAccountBudget;
	public static String viewBudgetEditMonth;
	
	public static String viewAccountNum;
	public static String viewAccountVerificate;
	public static String viewAccountDescription;
	public static String warningVerificateUnbalanced;
	public static String warningVerificateEmpty;
	public static String warningVerificateEmptyText;
	public static String warningAmbiguousTransaction;
	public static String warningAmbiguousTransactionText;
	public static String warningNewVerificateAlreadyOpen;
	public static String warningNewVerificateAlreadyOpenText;
	public static String warningWrongFormatDate;
	public static String warningLoadFileNotFound;
	public static String warningLoadFileNotFoundText;
	public static String warningbudgetWindowAlreadyOpen;
	public static String warningbudgetWindowAlreadyOpenText;
	
	
	public static String viewMainTitle;
	public static String viewMainLoadFilename;
	public static String viewMainMenubarEdit;
	public static String viewMainMenubarEditNewVerificate;
	public static String viewMainMenubarEditNewVerificateToolTip;
	public static String viewMainMenubarEditNewEndOfMonthVerificate;
	public static String viewMainMenubarEditNewEndOfMonthVerificateToolTip;
	public static String endOfMonthMonthChoiceText;
	public static String endOfMonthMonthChoiceTitle;
	public static String viewMainMenubarEditEditVerificate;
	public static String viewMainMenubarEditEditVerificateToolTip;
	public static String viewMainMenubarViewViewAccount;
	public static String viewMainMenubarViewViewAccountToolTip;
	public static String viewMainMenubarViewToggleActiveAccount;
	public static String viewMainMenubarViewToggleActiveAccountToolTip;
	public static String viewMainMenubarViewViewActiveAccount;
	public static String viewMainMenubarViewViewActiveAccountToolTip;
	public static String viewMainMenubarViewViewEveryAccount;
	public static String viewMainMenubarViewViewEveryAccountToolTip;
	public static String viewMainAccountplanTabName;
	public static String viewMainVerificatesTabName;
	public static String viewMainMenubarBudgetNewBudget;
	public static String viewMainMenubarBudgetNewBudgetToolTip;
	
	public static String viewBudgetTitle;
	public static String viewBudgetMonthDescription;
	public static String viewBudgetMonthBalance;
	
	public static String viewReportResultsTitle;
	public static String viewMainMenubarReportResultsReport;
	public static String viewMainMenubarReportResultsReportToolTip;
	
	public static String viewSpreadSheetAccountNameEmpty;

	public static String noID;
	public static String alphabet;
	public static String digits;
	public static String specialLetters;

	public static String save;
	public static String saveToolTip;
	public static String load;
	public static String loadToolTip;
	public static String exit;
	public static String exitToolTip;
	
	public static String[] viewMonth;
	
	public static HashMap<String,String> lang;
	
	public static void initLang(String languageCode, String locationCode) {
		Locale L = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		Resources.decformat = new DecimalFormat("#,##0.00");
		Locale.setDefault(L);
		
		locale = new Locale(languageCode, locationCode);
		Locale.setDefault(locale);
		JOptionPane.setDefaultLocale(locale);
		System.out.println("current Locale: " + Locale.getDefault());
		System.out.println("Importing language pack: " + languageCode);
		lang = new HashMap<String,String>();
		String line = "";
		try {
			Scanner sc = new Scanner(new File("lang." + languageCode));
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				if (line.contains("=")) {
					while (!line.endsWith("\"")) {
						String add = sc.nextLine();
						//ystem.out.println("Ends without \": " + add + "\nfinal char = " + line.substring(line.length()-1,line.length()) + "\".");
						line += "\n" + add;
					}
					// each non-foo line in lang.* reaches here, to be parsed as a key-value String pair
					String key = line.substring(0, line.indexOf("="));
					while (key.endsWith(" ") || key.endsWith("\t")) {
						key = key.substring(0, key.length()-1);
					}
					String value = line.substring(line.indexOf("\"")+1, line.lastIndexOf("\""));
					lang.put(key, value);
				}
			}
			sc.close();
			System.out.println("Import successful!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Import failed!");
			System.out.println("Error at line:\n" + line);
		}
		
		noID									= "####";
		alphabet								= "abcdefghijklmnopqrstuvwxyzåäöABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ";
		digits									= "0123456789";
		specialLetters							= "<>|,;.:-_'*~¨^´`+\\?@£$€¥{[]}!\"#¤%&/()=µn”“©»«ªßðđŋħłøæ¨þœ→↓←þ®€ł@";
		viewMainTitle							= "Bokföring för ";
		viewMainLoadFilename					= "Skriv in bokföringsfilens sökväg";
		viewMainMenubarEdit						= "Nytt";
		viewMainMenubarEditNewVerificate	    	= "Ny verifikation";
		viewMainMenubarEditNewVerificateToolTip	= "Skapa ny verifikation";
		viewMainMenubarEditNewEndOfMonthVerificate	    	= "Nytt månadsbokslut";
		viewMainMenubarEditNewEndOfMonthVerificateToolTip	= "Generera automatiskt ett månadsbokslut";
		endOfMonthMonthChoiceText				= "Välj månad för månadsbokslut";
		endOfMonthMonthChoiceTitle				= "Månadsbokslut";
		viewMainMenubarEditEditVerificate		= "Visa/Ändra verifikation";
		viewMainMenubarEditEditVerificateToolTip	= "Visa och eventuellt ändra existerande verifikation";
		viewMainMenubarViewViewAccount			= "Visa verifikationer för konto";
		viewMainMenubarViewViewAccountToolTip	= "Visa valt kontos verifikationer";
		viewMainMenubarViewToggleActiveAccount	= "Aktivera/Avaktivera konto";
		viewMainMenubarViewToggleActiveAccountToolTip	= "Ändra kontots status som aktivt eller inaktivt";
		viewMainMenubarViewViewEveryAccount		= "Visa alla konton";
		viewMainMenubarViewViewEveryAccountToolTip	= "Visar både aktiva och inaktiva konton i kontolistan";
		viewMainMenubarViewViewActiveAccount	= "Visa aktiva konton";
		viewMainMenubarViewViewActiveAccountToolTip	= "Visar endast aktiva konton i kontolistan";
		viewMainMenubarBudgetNewBudget			= "Visa budget";
		viewMainMenubarBudgetNewBudgetToolTip	= "Visa/Ändra budgeten för räkenskapsåret";
		
		viewMainAccountplanTabName				= "Kontoplan";
		viewMainVerificatesTabName				= "Verifikationer";
		
		viewVerificateAccountNum				= "Konto #";
		viewVerificateAccountName				= "Konto";
		viewVerificateDebet						= "DEBET";
		viewVerificateCredit					= "KREDIT";
		viewVerificateChangeSignature			= "Signatur";
		viewVerificateChangeDate				= "Tidpunkt";
		
		viewVerificateNum						= "Verifikation #";
		viewVerificateDescr						= "Beskrivning";
		viewSum									= "Summa";
		viewVerificateBalance					= "Balans";
		
		viewVerificateSave						= "Spara verifikation";
		viewBudgetSave							= "Spara budget";
		viewCancel								= "Stäng";
		
		account									= "Konto";
		viewAccountNum							= "Kontonummer";
		viewAccountVerificate					= "Visa verifikation";
		viewAccountDescription					= "Kontonamn";
		viewBudgetEditMonth						= "Visa/Ändra månadsbudget";
		viewAccountBudget						= "Visa kontobudget";
		editAccountBudget						= "Månadsfördelning";
		
		viewBudgetTitle							= "Budget";
		viewBudgetMonthDescription				= "Månad";
		viewBudgetMonthBalance					= "Balans";
		
		viewReportResultsTitle					= "Resultatrapport";
		viewMainMenubarReportResultsReport		= "Resultatrapport";
		viewMainMenubarReportResultsReportToolTip	= "Generera resultatrapport";
		
		viewSpreadSheetAccountNameEmpty			= "- - - - - -";
		
		warningVerificateUnbalanced				= "Obalanserad verifikation";
		warningVerificateEmpty					= "Tomt verifikat";
		warningVerificateEmptyText				= "Inga transaktioner registrerade, verifikationen sparas ej.";
		warningAmbiguousTransaction				= "Tvetydig transaktion";
		warningAmbiguousTransactionText			= "En transaktion är otydligt formatterad.\nSe till att kontonumret går till ett giltigt konto,\nendast en av debet och kredit är ifylld\noch eventuell signatur och datum korrekt ifyllda.\nAlternativt se till att raden är tom.\nGäller rad ";
		warningNewVerificateAlreadyOpen			= "Avsluta pågående ny verifikation";
		warningNewVerificateAlreadyOpenText		= "En fönster för ny verifikation är redan öppet.\nAvsluta det nuvarande om du vill öppna ett nytt.";
		
		warningWrongFormatDate					= "Felformaterat datum, skall vara på formen YYYY-MM-DD";
		
		warningLoadFileNotFound					= "Ogiltig sökväg!";
		warningLoadFileNotFoundText				= "Filens sökväg hittades inte";
		
		warningbudgetWindowAlreadyOpen			= "Nytt budgetfönster kan ej öppnas";
		warningbudgetWindowAlreadyOpenText		= "Budgeten syns och används redan i ett annat fönster.";
		save									= "Spara";
		saveToolTip								= "Spara fil till disk";
		load									= "Öppna";
		loadToolTip								= "Ladda in en fil från disk";
		exit									= "Avsluta";
		exitToolTip								= "Avsluta programmet utan att spara";
		
		viewMonth = new String[12];
		for (int i=0; i<12; i++) {
			viewMonth[i] = lang.get("viewMonth" + Integer.toString(i));
		}
	}
}
