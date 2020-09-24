package database;

//Standard Library Imports
	import java.util.HashMap;
	import java.util.ArrayList;
	import java.util.Arrays;
	
//Manager Imports
	import error.ErrorManager;

//External Library Imports
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.SQLException;
	import java.sql.ResultSet;
	import java.sql.ResultSetMetaData;

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
			System.out.println("DatabaseManager is being called here.");
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
	private static String interpretResultSet(ResultSet rs) {
		String output = "There was a problem in interpretResultSet";
		if(rs == null) {
			return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
		}
		try { //TODO:implement function
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			ArrayList list = new ArrayList(100);
			while (rs.next()){
				HashMap row = new HashMap(columns);
			    for(int i=1; i<=columns; ++i){           
			    	row.put(md.getColumnName(i),rs.getObject(i));
			    }
			    list.add(row);
			}
			for(int i=0; i<list.size(); i++) {
				output+=list.get(i) + "\n";
			}
			return output;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
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
		String newcommand = "select * from address;"; //example query to test
		ResultSet rs = queryDatabase(newcommand);
		return interpretResultSet(rs);
	}
	
	
	
	public static String handleSQLCommand(String command) {
		String test = "select * from address;"; //example query to test
		System.out.println("Command: " + test);
		return interpretResultSet(queryDatabase(test));
	}
	
	
	
	public static void closeConnection() {
		try {
			db.close();
			System.out.println("Connection Closed.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void openConnection() {
		try {
			//FIXME: I really don't think this is a good way to do it, but online tutorials do it?
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			System.out.println("Connection Opened.");
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
	}
	
}
