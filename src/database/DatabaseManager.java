package database;

public final class DatabaseManager {
	public DatabaseManager() {
		//probably set up connection here rather than open and close each time one of the methods below is called.
	}

	//Could also send it straight here to some intermediate function like "handleline" which makes the decision instead of main
	public static String handleCustomCommand(String command) {
		//First, check existing commands
		/*if exists in list of commands{
		*		try and get info from db
		*		on success: return db info string formatted nicely
		*		on failure: return corresponding error message from ErrorManager
		*}
		*else{
		*		return corresponding error message from ErrorManager
		*}
		*/
		return "";//TODO: implement function
	}
	public static String handleSQLCommand(String command) {
		//try and get info from db
		//on success: return db info string formatted nicely
		//on failure: return corresponding error message from ErrorManager
		return "";//TODO: implement function
	}
	
}
