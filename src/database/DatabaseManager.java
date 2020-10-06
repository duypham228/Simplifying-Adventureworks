package database;

//Standard Library Imports
	import java.util.HashMap;
	import java.util.ArrayDeque;
	import java.util.ArrayList;
	import java.math.*;
	
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
		
		String[] transitionNodes = {
				"vendoraddress",
				"customeraddress",
				"storecontact",
				"vendorcontact",
				"contactcreditcard",
				"countryregioncurrency",
				"employeedepartmenthistory",
				"specialofferproduct",
				"productproductphoto",
				"productvendor",
				"productdocument",
				"productmodelproductdescriptionculture",
				"productmodelillustration",
				"salesorderheadersalesreason",
				"employeeaddress"//Not sure.
			};
		
		if(parsedValues.length == 0) {
			return "ERROR: Invalid arguments (parsedValues.length = 0)";
		}
		
		out:
		for(int i=0; i<customCommands.length;i++) {
			if(parsedValues[0].equals(customCommands[i])) {
				validCommandPrefix = true;
				break out;
			}		
		}
		
		if(!validCommandPrefix) {
			return "ERROR: Invalid command \"" + parsedValues[0] + "\"";
		}
	
		
		
		ArrayList<HashMap <String, Object>> all_tables = interpretResultSet(queryDatabase("show tables;"));
		ArrayList<String> primaryKeys = new ArrayList<String>(5);
		String output = "";
		String newcommand = "";
		ArrayList<HashMap<String, Object>> result;
		ArrayList<String> curr_path;
		ArrayList<HashMap <String, Object>> attributes;
		
		ResultSet rs;
		output = "\n";
		
		
		//PLACEHOLDER SWITCH TABLE TO CHOOSE COMMAND TO EXECUTE
		switch(parsedValues[0]) {
		
		// -- SHOW-RELATED-TABLES --
		case "jdb-show-related-tables":
			
			if(parsedValues.length!=2){
				return "ERROR: jdb-show-related-tables requires 1 argument";
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
				attributes = interpretResultSet(queryDatabase("show columns from "+curr_table_name +";"));
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
			return output;
//			break;
		
		// -- SHOW-ALL-PRIMARY-KEYS --
		case "jdb-show-all-primary-keys":
			
			if(parsedValues.length!=1){
				return "ERROR: jdb-show-all-primary-keys requires 0 arguments";
			}
			for(int i=0; i<all_tables.size(); i++) {	//loops through all tables
				String curr_table_name = all_tables.get(i).get("TABLE_NAME").toString();
				attributes = interpretResultSet(queryDatabase("show columns from "+curr_table_name +";"));
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
			
		// -- FIND-COLUMN --
		case "jdb-find-column":
			
			if(parsedValues.length!=2){
				return "ERROR: jdb-find-column requires 1 argument";
			}
			String column = parsedValues[1];
		    ArrayList<String> table_with_column = new ArrayList<String>(5);
		    for(int i=0; i<all_tables.size(); i++) {	//loops through all tables
		        String curr_table_name = all_tables.get(i).get("TABLE_NAME").toString();
		        attributes = interpretResultSet(queryDatabase("show columns from "+curr_table_name +";"));
		        for(int j=0; j<attributes.size(); j++) {	//loops through all attributes in each table
		            HashMap<String, Object> curr_attr = attributes.get(j);
		            if(curr_attr.get("COLUMN_NAME").equals(column)) {
		                table_with_column.add(curr_table_name+" has column: "+curr_attr.get("COLUMN_NAME").toString());
		            }
		        }
		    }
		    for(int i=0; i<table_with_column.size(); i++) {
		        output += table_with_column.get(i) + "\n";
		    }
		    System.out.println(output);
		    return output;
//			break;
		
		// -- SEARCH-PATH --
		case "jdb-search-path":
			
			if(parsedValues.length!=3){
				return "ERROR: jdb-search-path requires 2 arguments";
			}
			//GETS ALL PRIMARY KEYS.
			for(int i=0; i<all_tables.size(); i++) {	
				String curr_table_name = all_tables.get(i).get("TABLE_NAME").toString();
				attributes = interpretResultSet(queryDatabase("show columns from "+curr_table_name +";"));
				for(int j=0; j<attributes.size(); j++) {	//loops through all attributes in each table
					HashMap<String, Object> curr_attr = attributes.get(j);
					if(curr_attr.get("COLUMN_KEY").equals("PRI")) {
						primaryKeys.add(curr_table_name+", "+curr_attr.get("COLUMN_NAME").toString());
					}
				}
			}	
			HashMap<String, Object> curr_attr;	
			ArrayList<String>table2keys = new ArrayList<String>();
			//GETS Primary keys of table 2
			attributes = interpretResultSet(queryDatabase("show columns from "+parsedValues[2]+";"));
			for(int j=0; j<attributes.size(); j++) {	//loops through all attributes in each table
				curr_attr = attributes.get(j);
				if(curr_attr.get("COLUMN_KEY").equals("PRI")) {
					table2keys.add(curr_attr.get("COLUMN_NAME").toString());
				}
			}
			ArrayDeque<ArrayList<String>> ILOVEBFS =  new ArrayDeque<ArrayList<String>>();
			ArrayList<String> path = new ArrayList<String>();
			path.add(parsedValues[1]);
			ILOVEBFS.add(path);
			pathcomplete:
			while(ILOVEBFS.size()!=0) {
				curr_path = ILOVEBFS.pop();
				ArrayList<HashMap <String, Object>> temp = interpretResultSet(queryDatabase("show columns from "+curr_path.get(curr_path.size()-1)+";"));
				ArrayList<String> tempkeys = new ArrayList<String>();
				
				for(int j=0; j<temp.size(); j++) {	//loops through all attributes in each table
					curr_attr = temp.get(j);
					if(curr_attr.get("COLUMN_KEY").equals("PRI")) {
						tempkeys.add(curr_attr.get("COLUMN_NAME").toString());
					}
				}
				for(int k=0;k<tempkeys.size();k++) {
					for(int l=0;l<table2keys.size();l++) {
						if(tempkeys.get(k).equals(table2keys.get(l))) {
							path = curr_path;
							path.add(parsedValues[2]);
							break pathcomplete;
						}
					}
				}
				for(String s : transitionNodes) {
					//From transition node
					temp = interpretResultSet(queryDatabase("show columns from "+s+";"));
					ArrayList<String> tempkeys2 = new ArrayList<String>();
					for(int j=0; j<temp.size(); j++) {	//loops through all attributes in each table
						curr_attr = temp.get(j);
						if(curr_attr.get("COLUMN_KEY").equals("PRI")) {
							tempkeys2.add(curr_attr.get("COLUMN_NAME").toString());
						}
					}
					ArrayList<String> supertemporary = new ArrayList<String>(); 
					ArrayList<String> added = new ArrayList<String>(); 
					for(int k=0;k<tempkeys.size();k++) { 
						for(int l=0;l<tempkeys2.size();l++) { 
							duplicate: 
							if(tempkeys.get(k).equals(tempkeys2.get(l))) { 
								supertemporary = new ArrayList<String>(curr_path); 
								for(int m =0;m<supertemporary.size();m++) { 
									if(supertemporary.get(m).equals(s)) { break duplicate; }
								} 
								for(int n=0;n<added.size();n++) { 
									if(added.get(n).equals(s)) { break duplicate; } 
								} 
								supertemporary.add(s); 
								ILOVEBFS.add(supertemporary); 
								added.add(s); 
							} 
						} 
					}
				}
			}
			System.out.print("{ ");
			for(int i=0;i<path.size();i++) {
				System.out.print(path.get(i)+" => ");
				output += path.get(i) + " => ";
			}
			System.out.println("\b\b\b}");
			return output;
//			break;
		
		// -- SEARCH-AND-JOIN --
		case "jdb-search-and-join":
			
			if(parsedValues.length!=3) {
				return "ERROR: jdb-search-and-join requires 2 arguments";
			}	
			//GETS ALL PRIMARY KEYS.
			for(int i=0; i<all_tables.size(); i++) {	
				String curr_table_name = all_tables.get(i).get("TABLE_NAME").toString();
				attributes = interpretResultSet(queryDatabase("show columns from "+curr_table_name +";"));
				for(int j=0; j<attributes.size(); j++) {	//loops through all attributes in each table
					 curr_attr = attributes.get(j);
					if(curr_attr.get("COLUMN_KEY").equals("PRI")) {
						primaryKeys.add(curr_table_name+", "+curr_attr.get("COLUMN_NAME").toString());
					}
				}
			}	
			HashMap<String, Object> curr_attr2;	
			table2keys = new ArrayList<String>();
			//GETS Primary keys of table 2
			attributes = interpretResultSet(queryDatabase("show columns from "+parsedValues[2]+";"));
			for(int j=0; j<attributes.size(); j++) {	//loops through all attributes in each table
				curr_attr2 = attributes.get(j);
				if(curr_attr2.get("COLUMN_KEY").equals("PRI")) {
					table2keys.add(curr_attr2.get("COLUMN_NAME").toString());
				}
			}
			ILOVEBFS =  new ArrayDeque<ArrayList<String>>();
			path = new ArrayList<String>();
			path.add(parsedValues[1]);
			ILOVEBFS.add(path);
			pathcomplete:
			while(ILOVEBFS.size()!=0) {
				curr_path = ILOVEBFS.pop();
				ArrayList<HashMap <String, Object>> temp = interpretResultSet(queryDatabase("show columns from "+curr_path.get(curr_path.size()-1)+";"));
				ArrayList<String> tempkeys = new ArrayList<String>();
				for(int j=0; j<temp.size(); j++) {	//loops through all attributes in each table
					curr_attr2 = temp.get(j);
					if(curr_attr2.get("COLUMN_KEY").equals("PRI")) {
						tempkeys.add(curr_attr2.get("COLUMN_NAME").toString());
					}
				}
				for(int k=0;k<tempkeys.size();k++) {
					for(int l=0;l<table2keys.size();l++) {
						if(tempkeys.get(k).equals(table2keys.get(l))) {
							path = curr_path;
							path.add(parsedValues[2]);
							break pathcomplete;
						}
					}
				}
				for(String s : transitionNodes) {
					//From transition node
					temp = interpretResultSet(queryDatabase("show columns from "+s+";"));
					ArrayList<String> tempkeys2 = new ArrayList<String>();
					for(int j=0; j<temp.size(); j++) {	//loops through all attributes in each table
						curr_attr2 = temp.get(j);
						if(curr_attr2.get("COLUMN_KEY").equals("PRI")) {
							tempkeys2.add(curr_attr2.get("COLUMN_NAME").toString());
						}
					}
					ArrayList<String> supertemporary = new ArrayList<String>(); 
					ArrayList<String> added = new ArrayList<String>(); 
					for(int k=0;k<tempkeys.size();k++) { 
						for(int l=0;l<tempkeys2.size();l++) { 
							duplicate: 
							if(tempkeys.get(k).equals(tempkeys2.get(l))) { 
								supertemporary = new ArrayList<String>(curr_path); 
								for(int m =0;m<supertemporary.size();m++) { 
									if(supertemporary.get(m).equals(s)) { break duplicate; }
								} 
								for(int n=0;n<added.size();n++) { 
									if(added.get(n).equals(s)) { break duplicate; } 
								} 
								supertemporary.add(s); 
								ILOVEBFS.add(supertemporary); 
								added.add(s); 
							} 
						} 
					}
				}
			}
			ArrayList<String> keypath = new ArrayList<String>();
			String command2 = "";
			command2 += "select "+path.get(0)+".*,"+path.get(path.size()-1)+".*,"; //SELECT ALL from T1 and T2.
			firstKey:
			for (int k = 0; k < primaryKeys.size(); k++) {
				String[] temp= primaryKeys.get(k).split(", ");
				if(path.get(0).equals(temp[0])) {
					keypath.add(temp[1]);
					break firstKey;
				}
			}	
			for (int i = 1; i < path.size()-1; i++) {
				secondKey:
				for (int k = 0; k < primaryKeys.size(); k++) {
					String[] temp= primaryKeys.get(k).split(", ");
					if(path.get(i).equals(temp[0]) && !keypath.get(keypath.size()-1).equals(temp[1])) {
						keypath.add(temp[1]);
						break secondKey;
					}
				}
			}
			lastKey:
				for (int k = 0; k < primaryKeys.size(); k++) {
					String[] temp= primaryKeys.get(k).split(", ");
					if(path.get(path.size()-1).equals(temp[0])) {
						keypath.add(temp[1]);
						break lastKey;
					}
				}
			for(int i=1;i<path.size()-1;i++) {
				command2+= path.get(i)+"."+keypath.get(i);
				if(i!=path.size()-2) {command2+=",";}
			}
			command2+=" from "+path.get(0);
			for(int i =0;i<path.size()-1;i++) {
				command2+=" join "+path.get(i+1)+" on "+path.get(i)+"."+keypath.get(i)+"="+path.get(i+1)+"."+keypath.get(i);
			}
			rs = queryDatabase(command2);
			result = interpretResultSet(rs);
			for (int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i));
			}
			break;
			
		// -- GET-VIEW --
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
			result = interpretResultSet(queryDatabase(query+";"));
			for(int i=0; i<result.size(); i++) {
				output+=result.get(i)+"\n";
			}
			break;
		// -- STAT --
		case "jdb-stat":
			double mean = 0, median = 0;
			ArrayList<HashMap<String, Object>> columninfo = interpretResultSet(queryDatabase("select " + parsedValues[2] + " from " + parsedValues[1] +" order by "+parsedValues[2]));
			double min = (double) columninfo.get(0).get(parsedValues[2]);
			double max = (double) columninfo.get(0).get(parsedValues[2]);
			for (int i = 0; i < columninfo.size(); i++) {
				if (i == columninfo.size()/2){
					median = (double) columninfo.get(i).get(parsedValues[2]);
				}
				mean += (double) columninfo.get(i).get(parsedValues[2]);
				if ((double) columninfo.get(i).get(parsedValues[2]) < min){
					min = (double) columninfo.get(i).get(parsedValues[2]);
				}
				if ((double) columninfo.get(i).get(parsedValues[2]) > max){
					max = (double) columninfo.get(i).get(parsedValues[2]);
				}
			}
			mean /= columninfo.size();
			System.out.println("min: " + min);
			System.out.println("max: " + max);
			System.out.println("mean " + mean);
			System.out.println("median " + median);
			int yfreq = (int)max / 6;
			int xfreq = columninfo.size() / 5;
			int[] bins = new int[6];
			for(int i = 0; i<6; i++) {
				bins[i]=0;
			}
			String space = "____";
			for(int i = 0; i < columninfo.size(); i++) {
				if((double) columninfo.get(i).get(parsedValues[2])<yfreq) {
					bins[0]+=1;
				}
				else if((double) columninfo.get(i).get(parsedValues[2])<2*yfreq) {
					bins[1]+=1;
				}
				else if((double) columninfo.get(i).get(parsedValues[2])<3*yfreq) {
					bins[2]+=1;
				}
				else if((double) columninfo.get(i).get(parsedValues[2])<4*yfreq) {
					bins[3]+=1;
				}
				else if((double) columninfo.get(i).get(parsedValues[2])<5*yfreq) {
					bins[4]+=1;
				}
				else if((double) columninfo.get(i).get(parsedValues[2])<6*yfreq) {
					bins[5]+=1;
				}
			}
			String histogram = "";
			int spaceLength = 20;
			histogram += " ".repeat(spaceLength)+"0"+space+(xfreq)+space+(2*xfreq)+space+(3*xfreq)+space+(4*xfreq)+space+(5*xfreq)+"\n"; 
			int temp = histogram.length();
			for(int i =0;i<6;i++) { 
				if(i==5) {
					histogram += (i*yfreq)+"-"+(int)max;
				} 
				else {
					histogram += (i*yfreq)+"-"+((i+1)*yfreq);
				}
				histogram += " ".repeat(spaceLength-(histogram.length()-temp))+"|"+ "*".repeat((int)Math.ceil((bins[i]+0.0001)/(5.0*xfreq) * 33.0))+"\n"; 
				temp = histogram.length();
			}
			System.out.println(histogram);
			break; 
		
		// -- GET-ADDRESSES	--
		case "jdb-get-addresses":
			
			if(parsedValues.length!=2){
				return "ERROR: jdb-get-addresses requires 1 argument";
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
			
		// -- GET-REGION-INFO --
		case "jdb-get-region-info":
			
			if(parsedValues.length!=2){
				return "ERROR: jdb-get-region-info requires 1 argument";
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
			
		// -- GET-INFO-BY-NAME --
		case "jdb-get-info-by-name":
			
			String name = "";
			if (parsedValues.length < 3) {
				return "ERROR: jdb-get-info-by-name requires 2 arguments";
			}
			else if (parsedValues.length >= 3) {
				for (int i = 2; i < parsedValues.length; i++)
					name += parsedValues[i] + " ";
			}	
			name = name.substring(0, name.length() - 1);
			attributes = interpretResultSet(queryDatabase("select * from "+parsedValues[1] +";"));
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
			
		// -- GET-SCHEDULE --
		case "jdb-get-schedule":
			
			if(parsedValues.length!=2){
				return "ERROR: jdb-get-schedule requires 1 argument";
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
			
		// -- LOCATE-STORE --
		case "jdb-locate-store":
			
			if(parsedValues.length!=2){
				return "ERROR: jdb-locate-store requires 1 argument";
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
		
		return output;
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
