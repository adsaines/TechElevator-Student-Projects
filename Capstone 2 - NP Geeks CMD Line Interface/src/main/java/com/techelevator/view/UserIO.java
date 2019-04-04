package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * @author bmartis
 *
 */
public class UserIO {

	private PrintWriter out;
	private Scanner in;

	public UserIO(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public void println( String text ) {
		out.println(text);
		out.flush();
	}

	public String getChoiceFromOptions( String[] menuOptions, String exitPrompt, String header ) {
		final String exitSymbol = exitPrompt.substring(0, 1).toUpperCase();
		final String regex = "[1-" + menuOptions.length + exitSymbol + exitSymbol.toLowerCase() + "]";

		String choice = null;
		while( choice == null ) {
			displayMenuOptions(menuOptions, exitPrompt, header);

			choice = getUserInputAsString("\nChoose a menu item [", 
					 symbol -> symbol.matches(regex),
					"Invalid menu option. Select an option from the list.");
			out.println();
		}
		
		if( choice.equalsIgnoreCase(exitSymbol) ) {
			return exitPrompt;
		} else {
			return menuOptions[Integer.parseInt(choice) - 1];
		}
	}
	private void displayMenuOptions(String[] options, String exitPrompt, String header) {
		// header
		out.println();
		out.println(header);
		out.println(repeat("-", header.length()));

		// menu options with numeric selector
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+"] "+options[i]);
		}
		out.println(exitPrompt.charAt(0) + "] " + exitPrompt);
		out.flush();
	}

	public Integer getUserInputAsInteger(String prompt) {
		return getUserInputAsInteger(prompt, n -> true, "Invalid integer number.");
	}
	public Integer getUserInputAsInteger(String prompt, Predicate<Integer> validation, String errorMsg) {
		Integer number;
		boolean notValid = true;
		do {              // while number is not invalid 
			while(true) { // while input is not numeric
				out.print(prompt); out.flush();
				if( in.hasNextInt() ) break;
				out.println(errorMsg);
				in.next();
			} 
			number = in.nextInt();in.nextLine();
			notValid = !validation.test(number); 
			if( notValid ) {
				out.println();
				out.println(errorMsg);
			}
		} while( notValid );
		return number;
	}

	public double getUserInputAsDouble(String prompt) {
		return getUserInputAsDouble(prompt, n -> true, "Invalid floating point number.");
	}
	public double getUserInputAsDouble(String prompt, Predicate<Double> validation, String errorMsg) {
		double number;
		boolean notValid = true;
		do {              // while number is not invalid 
			while(true) { // while input is not numeric
				out.print(prompt); out.flush();
				if( in.hasNextDouble() ) break;
				out.println(errorMsg);
				in.next();
			} 
			number = in.nextDouble();in.nextLine();
			notValid = !validation.test(number); 
			if( notValid ) {
				out.println();
				out.println(errorMsg);
			}
		} while( notValid );
		return number;
	}	

	public String getUserInputAsString(String prompt, Predicate<String> validation, String errorMsg) {
		String text;
		boolean notValid = true;
		do {
			out.print(prompt);
			out.flush();
			text = in.nextLine().trim();
			notValid = !validation.test(text); 
			if( notValid ) {
				out.println();
				out.println(errorMsg);
			}
		} while( notValid );
		return text;
	}
	
	public static String repeat(String str, int count) {
		StringBuilder buffer = new StringBuilder(str.length()*count);
		for(int n = 0; n < count; n++) {
			buffer.append(str);
		}
		return buffer.toString();
	}
}
