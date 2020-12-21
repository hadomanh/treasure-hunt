package ngakinz.application;

import java.util.Scanner;

public class Test {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in).useDelimiter("\n");
		String input = "";
		do {
			System.out.print("Message: ");
			input = scanner.next();
			input = input.trim();
		} while (!input.matches("^\\d+\\s\\d+$"));
		
		scanner.close();
	}

}
