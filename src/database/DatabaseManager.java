package database;

//Standard Library Imports
	import java.util.HashMap;
	
//Manager Imports
	import error.ErrorManager;

//External Library Imports
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.SQLException;

	import java.sql.ResultSet;

public final class DatabaseManager {
	
	//This is for the predefined special commands, could be read in from a file if extra time
	private static final String[] customCommands = { 
		"jdb-show-related-tables",
		"jdb-show-all-primary-keys",
		"jdb-find-column",
		"jdb-search-path",
		"jdb-search-and-join",
		"jdb-get-view",
		"jdb-stat",
		"jdb-get-addresses",
		"jdb-get-region-info",
		"jdb-get-info-by-name",
		"jdb-get-schedule",
		"jdb-locate-store"
	};
	
	//A container to hold our 20 common queries (TODO:implement for part 3)
	private static HashMap<String, String> commonCommands = new HashMap<String, String>();
	
	
	private static Connection db = null;
	
	public DatabaseManager() {
		try {
			//FIXME: I really don't think this is a good way to do it, but online tutorials do it?
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 			
			
			db = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=password"); //TODO: make sure this url is right
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO: populate HashMap with pairs like "Job candidates who were not hired", "SELECT * FROM jobcandidate WHERE EmployeeID IS NULL;"
		
	}
	
	//Neatly formats results into a string for printing. Could be an error, actual query, etc.
	private static String interpretResultSet(ResultSet result) {
		if(result == null) {
			return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
		}
		return ""; //TODO:implement function
	}

	public static ResultSet queryDatabase(String command) {
		try {
			return db.createStatement().executeQuery(command);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//Could also send it straight here to some intermediate function like "handleline" which makes the decision instead of main
	public static String handleCustomCommand(String command) {
		boolean validCommandPrefix = false;
		
		out:
		for(int i=0;i<customCommands.length;i++) {
			if(command.length()>=customCommands[i].length()) {
				if(command.substring(0,customCommands[i].length()).equals(customCommands[i])) {
					validCommandPrefix = true;
					break out;
				}
			}		
		}
		
		if(!validCommandPrefix) {
			return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
		}
		
		
		//TODO:FORM SQL COMMAND HERE.
		String newcommand = "special command replaced with sql and custom tables and other fields";
		return interpretResultSet(queryDatabase(newcommand));
		
	}
	public static String handleSQLCommand(String command) {
		return interpretResultSet(queryDatabase(command));
	}
	
}
