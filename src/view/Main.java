package view;

import java.io.File;
import java.util.Scanner;

public class Main
{

	public static void main(String[] args)
	{
		System.out.println("Here we go!");
		// ========== READ SETTINGS ==========
		// !TODO read settings in an appropriate place
		
		String langCode = "en", locCode = "GB";
		try {
			Scanner sc = new Scanner(new File("settings.conf"));
			langCode = sc.next();
			locCode = sc.next();
			
			// ...
			
			sc.close();
		} catch (Exception e) {
			System.out.println("Error: could not load settings!");
			e.printStackTrace();
		}
		Text.initLang(langCode, locCode);
		
		MainWindow mm = new MainWindow();
		GraphicsResources.mw = mm;
	}

}
