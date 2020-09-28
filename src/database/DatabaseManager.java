package database;

//Standard Library Imports
	import java.util.HashMap;
	import java.util.ArrayList;
	
//Manager Imports
	import error.ErrorManager;

//External Library Imports
//	import java.sql.Connection;
//	import java.sql.DriverManager;
//	import java.sql.SQLException;
//	import java.sql.ResultSet;
//	import java.sql.ResultSetMetaData;
	import java.sql.*;

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
	@SuppressWarnings("unchecked")
	private static ArrayList<HashMap<String, Object>> interpretResultSet(ResultSet rs) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>(100);
//		if(rs == null) {
//			return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
//		}
		try { //TODO:implement function
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			while (rs.next()){
				HashMap<String, Object> row = new HashMap<String, Object>(columns);
			    for(int i=1; i<=columns; i++){
			    	row.put(md.getColumnName(i), rs.getObject(i));
			    }
			    list.add(row);
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
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
		String[] parsedValues = command.split(" ");
		boolean validCommandPrefix = false;
		
		if(parsedValues.length ==0) {
			return ErrorManager.getErrorMessage(0);//TODO: Fix Error Codes, or come up with a better way to do this.
		}
		
		out:
		for(int i=0; i<customCommands.length;i++) {
			if(parsedValues[0].equals(customCommands[i])) {
				validCommandPrefix = true;
				break out;
			}		
		}
		
		if(!validCommandPrefix) {
			return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
		}
	
		
		
		ArrayList<HashMap <String, Object>> all_tables = interpretResultSet(queryDatabase("show tables;"));
		ArrayList<String> primaryKeys = new ArrayList<String>(5);
		String output = "";
		String newcommand = "";
		ArrayList<HashMap<String, Object>> result;
		
		ResultSet rs;
		output = "\n";
		//PLACEHOLDER SWITCH TABLE TO CHOOSE COMMAND TO EXECUTE
		switch(parsedValues[0]) {
		case "jdb-show-related-tables":
			if(parsedValues.length!=2){
				return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
			}
			String input_table_name = parsedValues[1];
			ArrayList<HashMap <String, Object>> input_table = interpretResultSet(queryDatabase("show columns from " + input_table_name + ";"));
			for(int i=0; i<input_table.size(); i++) {	//gets primary keys
				if(input_table.get(i).get("COLUMN_KEY").equals("PRI")) {
					primaryKeys.add(input_table.get(i).get("COLUMN_NAME").toString());
				}
			}
			ArrayList<String> relatedTables = new ArrayList<String>(10);
			for(int i=0; i<all_tables.size(); i++) {	//loops through all tables
				String curr_table_name = all_tables.get(i).get("TABLE_NAME").toString();
				ArrayList<HashMap <String, Object>> attributes = interpretResultSet(queryDatabase("show columns from "+curr_table_name +";"));
				for(int j=0; j<attributes.size(); j++) {	//loops through all attributes in each table
					HashMap<String, Object> curr_attr = attributes.get(j);
					if(curr_attr.get("COLUMN_KEY").equals("PRI")) {
						for(int k=0; k<primaryKeys.size(); k++) {	//loops through primaryKeys testing for match
							if(primaryKeys.get(k).equals(curr_attr.get("COLUMN_NAME").toString())) {
								relatedTables.add(curr_table_name);
							}
						}
					}
				}
			}
			for(int i=0; i<relatedTables.size(); i++) {
				output += relatedTables.get(i) + "\n";
			}
			System.out.println(output);
			break;
		case "jdb-show-all-primary-keys":
			if(parsedValues.length!=1){
				return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
			}
			for(int i=0; i<all_tables.size(); i++) {	//loops through all tables
				String curr_table_name = all_tables.get(i).get("TABLE_NAME").toString();
				ArrayList<HashMap <String, Object>> attributes = interpretResultSet(queryDatabase("show columns from "+curr_table_name +";"));
				for(int j=0; j<attributes.size(); j++) {	//loops through all attributes in each table
					HashMap<String, Object> curr_attr = attributes.get(j);
					if(curr_attr.get("COLUMN_KEY").equals("PRI")) {
						primaryKeys.add(curr_table_name+", "+curr_attr.get("COLUMN_NAME").toString());
					}
				}
			}
			for(int i=0; i<primaryKeys.size(); i++) {
				output += primaryKeys.get(i) + "\n";
			}
			System.out.println(output);
			break;
		case "jdb-find-column":
			break;
		case "jdb-search-path":
			break;
		case "jdb-search-and-join":
			break;
		case "jdb-get-view":
			Statement stmt;
			String view = parsedValues[1];
			String query = "";
			for(int i=3; i<parsedValues.length-1; i++) {
				if(i!=parsedValues.length-1) {
					query+=parsedValues[i]+" ";
				}
				else {
					query+=parsedValues[i];
				}
			}
			System.out.println(query);
			try {
				stmt = db.createStatement();
				stmt.executeUpdate("CREATE VIEW "+view+" AS "+query+";");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "jdb-stat":
			break;
		case "jdb-get-addresses":
			if(parsedValues.length!=2){
				return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
			}
			String table = parsedValues[1];
			if (table.equals("address")) {
				newcommand = "select address.AddressID, address.AddressLine1, address.AddressLine2, "
						+ "address.City, address.StateProvinceID, address.PostalCode, customeraddress.CustomerID"
						+ " AS 'CustomerID/VendorID', customer.AccountNumber AS 'AccountNumber', addresstype.AddressTypeID"
						+ " AS 'AddressTypeID', addresstype.Name AS 'BuildingType' from address join customeraddress"
						+ " on customeraddress.AddressID = address.AddressID  join addresstype on addresstype.AddressTypeID"
						+ " = customeraddress.AddressTypeID join customer on customeraddress.CustomerID = "
						+ "customer.CustomerID union select address.AddressID, address.AddressLine1, address.AddressLine2,"
						+ " address.City, address.StateProvinceID, address.PostalCode, vendoraddress.VendorID AS "
						+ "'CustomerID/VendorID', vendor.AccountNumber AS 'AccountNumber', addresstype.AddressTypeID"
						+ " AS 'AddressTypeID', addresstype.Name AS 'BuildingType' from address join vendoraddress on"
						+ " vendoraddress.AddressID = address.AddressID join addresstype on addresstype.AddressTypeID"
						+ " = vendoraddress.AddressTypeID join vendor on vendoraddress.VendorID = vendor.VendorID";
			}
			else if (table.equals("customeraddress")) {
				newcommand = "select customeraddress.AddressID, address.AddressLine1, address.AddressLine2, "
						+ "address.City, address.StateProvinceID, address.PostalCode, customer.CustomerID, "
						+ "customer.AccountNumber, customer.CustomerType, addresstype.AddressTypeID, addresstype.Name "
						+ "from customeraddress join address on customeraddress.AddressID = address.AddressID join "
						+ "customer on customer.CustomerID = customeraddress.CustomerID join addresstype on "
						+ "addresstype.AddressTypeID = customeraddress.AddressTypeID";
			}
			else if (table.equals("vendoraddress")) {
				newcommand = "select vendoraddress.AddressID, address.AddressLine1, address.AddressLine2, "
						+ "address.City, address.StateProvinceID, address.PostalCode, vendor.VendorID, "
						+ "vendor.AccountNumber, vendor.Name, addresstype.AddressTypeID, addresstype.Name AS "
						+ "'BuildingName' from vendoraddress join address on vendoraddress.AddressID = address.AddressID"
						+ " join vendor on vendor.VendorID = vendoraddress.VendorID join addresstype on "
						+ "addresstype.AddressTypeID = vendoraddress.AddressTypeID";
			}
			else if (table.equals("employeeaddress")) {
				newcommand = "select employeeaddress.EmployeeID, employeeaddress.AddressID, address.AddressLine1,"
						+ " address.AddressLine2, address.City, address.StateProvinceID, address.PostalCode from "
						+ "employeeaddress join address on address.AddressID = employeeaddress.AddressID";
			}
			else 
			{
				return "jdb-get-addresses only accepts address, customeraddrsess, vendoraddress, and employeeaddress as arguments";
			}
			rs = queryDatabase(newcommand);
			result = interpretResultSet(rs);
			for (int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i));
			}
			break;
		case "jdb-get-region-info":
			if(parsedValues.length!=2){
				return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
			}
			String region = parsedValues[1];
			newcommand = "select countryregion.CountryRegionCode, countryregion.Name, 'CurrencyCode', 'TaxRate', "
					+ "'Contact' from countryregion where countryregion.CountryRegionCode LIKE '?$' union select "
					+ "countryregion.CountryRegionCode, countryregion.Name, countryregioncurrency.CurrencyCode, 'TaxRate', "
					+ "'Contact' from countryregion join countryregioncurrency on countryregion.CountryRegionCode = "
					+ "countryregioncurrency.CountryRegionCode where countryregion.CountryRegionCode LIKE '?$' union "
					+ "select countryregion.CountryRegionCode, countryregion.Name, countryregioncurrency.CurrencyCode, "
					+ "salestaxrate.TaxRate AS 'TaxRate', salesorderheader.ContactID AS 'Contact' from countryregion join "
					+ "countryregioncurrency on countryregion.CountryRegionCode = countryregioncurrency.CountryRegionCode "
					+ "join stateprovince on stateprovince.CountryRegionCode = countryregion.CountryRegionCode join salestaxrate "
					+ "on salestaxrate.StateProvinceID = stateprovince.StateProvinceID join salesterritory on "
					+ "salesterritory.CountryRegionCode = countryregion.CountryRegionCode join salesorderheader on "
					+ "salesorderheader.TerritoryID = salesterritory.TerritoryID where countryregion.CountryRegionCode "
					+ "LIKE 'AE' union select countryregion.CountryRegionCode, countryregion.Name, "
					+ "countryregioncurrency.CurrencyCode, salestaxrate.TaxRate AS 'TaxRate', salesorderheader.ContactID "
					+ "AS 'Contact' from countryregion join countryregioncurrency on countryregion.CountryRegionCode = "
					+ "countryregioncurrency.CountryRegionCode join stateprovince on stateprovince.CountryRegionCode = "
					+ "countryregion.CountryRegionCode join salestaxrate on salestaxrate.StateProvinceID = "
					+ "stateprovince.StateProvinceID join salesterritory on salesterritory.CountryRegionCode = "
					+ "countryregion.CountryRegionCode join salesorderheader on salesorderheader.TerritoryID = "
					+ "salesterritory.TerritoryID where countryregion.CountryRegionCode LIKE '?$'";
			newcommand = newcommand.replace('?', region.charAt(0));
			newcommand = newcommand.replace('$', region.charAt(1));
			rs = queryDatabase(newcommand);
			result = interpretResultSet(rs);
			for (int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i));
			}
			break;
		case "jdb-get-info-by-name":
			String name = "";
			if (parsedValues.length < 3) {
				return "jdb-get-info-by-name needs 2 arguments";
			}
			else if (parsedValues.length >= 3) {
				for (int i = 2; i < parsedValues.length; i++)
					name += parsedValues[i] + " ";
			}	
			name = name.substring(0, name.length() - 1);
			ArrayList<HashMap <String, Object>> attributes = interpretResultSet(queryDatabase("select * from "+parsedValues[1] +";"));
			ArrayList<HashMap <String, Object>> matches = new ArrayList<HashMap <String, Object>> (100);
			for (int i = 0; i < attributes.size(); i++) {
				for (HashMap.Entry<String, Object> rows_key : attributes.get(i).entrySet()) {
					if (attributes.get(i).get(rows_key.getKey()) != null)
					{
						if (attributes.get(i).get(rows_key.getKey()).toString().equals(name)) {
							matches.add(attributes.get(i));
						}
					}
				}
			}
			for (int i = 0; i < matches.size(); i++)
			{
				System.out.println(matches.get(i));
			}
			break;
		case "jdb-get-schedule":
			if(parsedValues.length!=2){
				return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
			}
			newcommand = "SELECT DISTINCT purchaseorderheader.OrderDate,"
				+"purchaseorderheader.ShipDate-purchaseorderheader.OrderDate AS OrderToShip,"
				+" purchaseorderheader.ShipDate,"
				+" purchaseorderdetail.DueDate-purchaseorderheader.ShipDate AS ShipToFinish,"
				+" purchaseorderdetail.DueDate"
				+" from purchaseorderheader, purchaseorderdetail"
				+" WHERE purchaseorderheader.PurchaseOrderID="+parsedValues[1]
				+" AND purchaseorderdetail.PurchaseOrderID="+parsedValues[1]+";";
			rs = queryDatabase(newcommand);
			result = interpretResultSet(rs);
			System.out.println(result.get(0).get("OrderDate")+" ===== "+ (long) result.get(0).get("OrderToShip")/1000000.0 + " DAYS ===== "
						+result.get(0).get("ShipDate") + " ===== " + (long) result.get(0).get("ShipToFinish")/1000000.0 + " DAYS ====== "
						+ result.get(0).get("DueDate"));
			break;
		case "jdb-locate-store":
			if(parsedValues.length!=2){
				return ErrorManager.getErrorMessage(0); //TODO: Fix Error Codes, or come up with a better way to do this.
			}
			newcommand = "select store.Name from salesorderdetail join salesorderheader on salesorderdetail.SalesOrderID = "
					+ "salesorderheader.SalesOrderID join store on store.CustomerID = salesorderheader.CustomerID where "
					+ "salesorderdetail.ProductID Like "+parsedValues[1]+";";
			rs = queryDatabase(newcommand);
			result = interpretResultSet(rs);
			for (int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i));
			}
			break;
		}
		//TODO:FORM SQL COMMAND HERE.
		newcommand = "select * from address;"; //example query to test
		rs = queryDatabase(newcommand);
		//return interpretResultSet(rs);
		return "Done.";
	}
	
	
	
	public static String handleSQLCommand(String command) {
		if(command.equals("jdb")) {
			System.out.println("Command: " + command);
			ArrayList<HashMap <String, Object>> tables = interpretResultSet(queryDatabase("show tables;"));
			for(int i=0; i<tables.size(); i++) {
				System.out.println(tables.get(i));
				String tableName = tables.get(i).get("TABLE_NAME").toString(); 
				ArrayList <HashMap <String, Object>> attributes = interpretResultSet(queryDatabase("show columns from "+tableName+";"));
				for(int j=0; j<attributes.size(); j++) {
					System.out.println(attributes.get(j));
				}
			}
		}
		String test = ""; //example query to test
		System.out.println("Command: " + test);
		//return interpretResultSet(queryDatabase(test));
		//return "Done.";
		String output = "";
		if(command.equals("help")) {
			output+="Available Commands :: \n";
			for(int i=0; i<customCommands.length; i++) {
				output+="\t" + customCommands[i]+"\n";
			}
			output+= "\tOR you can enter a direct SQL query.";
		}
		else {
			ArrayList<HashMap <String, Object>> rsList = interpretResultSet(queryDatabase(command));
			for(int i=0; i<rsList.size(); i++) {
				output+= rsList.get(i) + "\n";
			}
		}
		return output;
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
