package view;

import model.Resources;

public class Test {

	public Test() {
		
	}
	
	public static void main(String args[]) {
		System.out.println("Test initiated!");
		System.out.println("Decformat: 0.00 -> " + Resources.decformat.format(0.00));
	}
}