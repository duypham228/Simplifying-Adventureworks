/*********************
*	CSCE 315-913
*	Group 4
*
*	Team Members:
*	Cade Conner, 
*	Prajwal Iyer, 
*	Wyatt Masterson, 
*	Duy Pham, 
*	Matthew Davis
*
*/


package main;


//Standard Library Imports
	import java.util.Scanner;

//Manager Imports
	import database.DatabaseManager;
	import error.ErrorManager;

public class CommandInterface {

	static String userline = "";
	
	public static void main(String[]args) {
		DatabaseManager.openConnection();
		Scanner input = new Scanner(System.in);
		while(!userline.equalsIgnoreCase("quit")&&!userline.equalsIgnoreCase("exit")) {
			System.out.print("jdb > ");
			userline = input.nextLine();
			if(!userline.equalsIgnoreCase("quit")&&!userline.equalsIgnoreCase("exit")) {
				System.out.println(parseLine(userline)); //Formatting could either be done here w/ function or in the dbmanager.
			}
		}
		input.close();
		DatabaseManager.closeConnection();
	}
	
	final static String parseLine(String line) {
		try{
			if(!line.substring(0,3).equals("jdb")) {
				return DatabaseManager.handleCustomCommand(line);
			}
			else {
				return DatabaseManager.handleSQLCommand(line);
			}
		}
		catch(StringIndexOutOfBoundsException e) {
			return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
		}
	}
	

}
