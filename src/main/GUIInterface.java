package main;

	import java.awt.Color;
	import java.awt.Dimension;
	import java.awt.Toolkit;

//Standard Library Imports
	import java.awt.event.KeyEvent;
	import java.awt.event.KeyListener;

	import java.awt.event.MouseEvent;
	import java.awt.event.MouseListener;

	import java.awt.event.MouseWheelEvent;
	import java.awt.event.MouseWheelListener;

	import java.awt.event.WindowAdapter;
	import java.awt.event.WindowEvent;

	import java.util.ArrayList;
	import java.util.List;
	import java.util.Scanner;

	import javax.swing.JButton;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JTable;
	import javax.swing.JTextField;
	import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

//Manager Imports
	import database.DatabaseManager;
	import page.PageManager;
	import error.ErrorManager;

public class GUIInterface extends JPanel implements MouseListener, MouseWheelListener, KeyListener {

	private static JFrame frame = new JFrame();
//	private static JPanel panel;

	private static JButton findColumn;
	private static JButton listTables;
	private static JTextField textfield1;

	// show one or more columns of specific table
	private static JLabel columns_LB;
	private static JLabel table_LB;
	private static JTextField columns_TF;
	private static JTextField table_TF;
	private static JButton showColumns;

	// process raw sql queries
	private static JLabel query_LB;
	private static JTextField query_TF;
	private static JButton query;

	// jdb-search-path <table1> <table2>
	private static JLabel table1_LB;
	private static JLabel table2_LB;
	private static JTextField table1_TF;
	private static JTextField table2_TF;
	private static JButton searchPath;
	private static JLabel searchPath_LB;
	private static JLabel searchPathResult;

	// jdb-show-related-tables
	private static JButton relatedTables;
	private static JTextField relatedTables_TF;
	private static JLabel relatedTables_LB;

	//show-all-primary-keys
	private static JButton primaryKey;

	//jdb-get-view
	private static JButton getView;
	private static JLabel view_LB;
	private static JTextField viewName_TF;
	private static JTextField viewQuery_TF;

	//jdb-get-addresses
	private static JLabel getAddresses_LB;
	private static JTextField getAddresses_TF;
	private static JButton getAddresses;

	//jdb-get-region-info
	private static JLabel getRegion_LB;
	private static JTextField getRegion_TF;
	private static JButton getRegion;

	//jdb-get-info-by-name
	private static JLabel getInfo_LB;
	private static JTextField getInfo_tableTF;
	private static JTextField getInfo_attributeTF;
	private static JButton getInfo;

	// jdb-show-all-primary-keys
	private static JButton primaryKeys;

	
	public GUIInterface() {
		super();
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseWheelListener(this);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				DatabaseManager.closeConnection();
			}
		});
		frame.add(this);
		frame.setSize(700, 800);
		frame.setVisible(true);
		this.repaint();
	}
	public static void main(String[]args) {
		DatabaseManager.openConnection();
		DatabaseManager.queryDatabase("use adventureworks;");

		int windowWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2);
		frame.getContentPane().setPreferredSize( new Dimension( windowWidth, (int) (windowWidth / (8.0/6.0)) ) );
		GUIInterface gui = new GUIInterface();
		frame.setTitle("SAW: Simiplifying Adventure Works");
//		panel = new JPanel();

		frame.add(gui);
		gui.setLayout(null);


//

		// Text field for input text
		textfield1 = new JTextField(20);
		textfield1.setBounds(10, 50, 150, 25);
		gui.add(textfield1);

		// Button for jdb-find-column
		findColumn = new JButton("jdb-find-column");
		findColumn.setBounds(10, 80, 200, 25);
		findColumn.addMouseListener(new GUIInterface());
		gui.add(findColumn);

		// Button for show list of table
		listTables = new JButton("Show List of Tables");
		listTables.setBounds(10, 20, 200, 25);
		listTables.addMouseListener(new GUIInterface());
		gui.add(listTables);


		/////////////////////////////////////////////////
		// Show one or more columns for specific table //
		/////////////////////////////////////////////////

		columns_LB = new JLabel("Column Name:");
		columns_LB.setBounds(10, 110, 150, 25);
		gui.add(columns_LB);

		columns_TF = new JTextField(20);
		columns_TF.setBounds(110, 110, 165, 25);
		gui.add(columns_TF);

		table_LB = new JLabel("Table Name:");
		table_LB.setBounds(10, 150, 150, 25);
		gui.add(table_LB);

		table_TF = new JTextField(20);
		table_TF.setBounds(110, 150, 165, 25);
		gui.add(table_TF);

		// Button for show one or more columns from specific table
		showColumns = new JButton ("Show one or more Columns of Specific Table");
		showColumns.setBounds(10, 180, 200, 25);
		showColumns.addMouseListener(new GUIInterface());
		gui.add(showColumns);

		/////////////////////////////
		// Process Raw SQL queries //
		/////////////////////////////
		query_LB = new JLabel("SQL query input:");
		query_LB.setBounds(10, 210, 150, 25);
		gui.add(query_LB);

		query_TF = new JTextField(20);
		query_TF.setBounds(110, 210, 165, 25);
		gui.add(query_TF);

		// Button for show one or more columns from specific table
		query = new JButton ("Raw SQL Query");
		query.setBounds(10, 240, 200, 25);
		query.addMouseListener(new GUIInterface());
		gui.add(query);

		/////////////////////
		// jdb-search-path //
		/////////////////////
		table1_LB = new JLabel("Table 1:");
		table1_LB.setBounds(10, 270, 150, 25);
		gui.add(table1_LB);

		table1_TF = new JTextField(20);
		table1_TF.setBounds(110, 270, 165, 25);
		gui.add(table1_TF);

		table2_LB = new JLabel("Table 2:");
		table2_LB.setBounds(10, 300, 150, 25);
		gui.add(table2_LB);

		table2_TF = new JTextField(20);
		table2_TF.setBounds(110, 300, 165, 25);
		gui.add(table2_TF);

		// Button for show one or more columns from specific table
		searchPath = new JButton ("Search Path");
		searchPath.setBounds(10, 330, 200, 25);
		searchPath.addMouseListener(new GUIInterface());
		gui.add(searchPath);

		searchPath_LB = new JLabel("Result:");
		searchPath_LB.setBounds(10, 360, 150, 25);
		gui.add(searchPath_LB);

		searchPathResult = new JLabel("");
		searchPathResult.setBounds(110, 360, 500, 25);
		gui.add(searchPathResult);

		/////////////////////////////
		// jdb-show-related-tables //
		/////////////////////////////
		relatedTables_LB = new JLabel("Enter table name:");
		relatedTables_LB.setBounds(10, 390, 200, 25);
		gui.add(relatedTables_LB);

		relatedTables_TF = new JTextField("table");
		relatedTables_TF.setBounds(110, 390, 200, 25);
		gui.add(relatedTables_TF);

		relatedTables = new JButton("jdb-show-related-tabels");
		relatedTables.setBounds(10, 420, 200, 25);
		relatedTables.addMouseListener(new GUIInterface());
		gui.add(relatedTables);

		
		/////////////////////////////
		//jdb-show-all-primary-keys//
		/////////////////////////////
		primaryKey = new JButton("jdb-show-all-primary-keys");
		primaryKey.setBounds(10, 450, 200, 25);
		primaryKey.addMouseListener(new GUIInterface());
		gui.add(primaryKey);

		////////////////
		//jdb-get-view//
		////////////////
		view_LB = new JLabel("Create a view:");
		view_LB.setBounds(10, 480, 100, 25);

		viewName_TF = new JTextField("name");
		viewName_TF.setBounds(115, 480, 50, 25);

		viewQuery_TF = new JTextField("query");
		viewQuery_TF.setBounds(170, 480, 100, 25);

		getView = new JButton("jdb-get-view");
		getView.setBounds(10, 510, 200, 25);
		getView.addMouseListener(new GUIInterface());

		gui.add(getView);
		gui.add(view_LB);
		gui.add(viewName_TF);
		gui.add(viewQuery_TF);



		////////////////////////////
		//    jdb-get-addresses   //
		////////////////////////////
		getAddresses_LB = new JLabel("Name of table:");
		getAddresses_LB.setBounds(10, 540, 200, 25);
		gui.add(getAddresses_LB);

		getAddresses_TF = new JTextField(20);
		getAddresses_TF.setBounds(110, 540, 200, 25);
		gui.add(getAddresses_TF);

		getAddresses = new JButton("jdb-get-addresses");
		getAddresses.setBounds(10, 570, 200, 25);
		getAddresses.addMouseListener(new GUIInterface());
		gui.add(getAddresses);

		////////////////////////////
		//  jdb-get-region-info   //
		////////////////////////////
		getRegion_LB = new JLabel("Enter Region:");
		getRegion_LB.setBounds(10, 600, 200, 25);
		gui.add(getRegion_LB);

		getRegion_TF = new JTextField(20);
		getRegion_TF.setBounds(110, 600, 200, 25);
		gui.add(getRegion_TF);

		getRegion = new JButton("jdb-get-region-info");
		getRegion.setBounds(10, 630, 200, 25);
		getRegion.addMouseListener(new GUIInterface());
		gui.add(getRegion);

		////////////////////////////
		//  jdb-get-info-by-name  //
		////////////////////////////
		getInfo_LB = new JLabel("Enter table and attribute name:");
		getInfo_LB.setBounds(10, 660, 200, 25);
		gui.add(getInfo_LB);

		getInfo_tableTF = new JTextField("table");
		getInfo_tableTF.setBounds(150, 660, 200, 25);
		gui.add(getInfo_tableTF);

		getInfo_attributeTF = new JTextField("name");
		getInfo_attributeTF.setBounds(350, 660, 200, 25);
		gui.add(getInfo_attributeTF);

		getInfo = new JButton("jdb-get-info-by-name");
		getInfo.setBounds(10, 690, 200, 25);
		getInfo.addMouseListener(new GUIInterface());
		gui.add(getInfo);

		//JTable table = new JTable();
		frame.setVisible(true);
		gui.revalidate();
		gui.repaint();
		///////////End of Commands/////////////
		frame.setVisible(true);
		
	} //This escapes "static-ness" of main

	//Could make listeners frame-synchronized or based on press & release combo
	@Override
	public void keyPressed(KeyEvent key) {}

	@Override
	public void keyReleased(KeyEvent key) {}

	@Override
	public void keyTyped(KeyEvent key) {
		int keycode = key.getKeyCode();
	}

	@Override
	public void mouseClicked(MouseEvent mouse) {

//		DatabaseManager.openConnection();
//		DatabaseManager.queryDatabase("use adventureworks;");

		/////////////////////////////////
		// Handler for jdb-find-column //
		/////////////////////////////////
		if (mouse.getSource() == findColumn) { // jdb-find-column
//

			JFrame frame_find_column = new JFrame();
			GUIInterface panel_find_column = new GUIInterface();
			frame_find_column.setVisible(true);
			DefaultTableModel model1 = new DefaultTableModel();


			JTable table1 = new JTable(model1);
//			table1.setBounds(5, 5, 100, 300);
			table1.setShowGrid(true);
			table1.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table1);
			sp.setPreferredSize(new Dimension(250, 300));
			model1.addColumn("Table Name");
//			model1.addColumn("Column");

			frame_find_column.add(panel_find_column);
			frame_find_column.setSize(300, 300);

			String column_name = textfield1.getText();
			String output1 = DatabaseManager.handleCustomCommand("jdb-find-column " + column_name);
			String line[] = output1.split("\n");
			for (String token : line) {
				String row[] = token.split(" ");
				model1.addRow(new Object[] {row[0]});

			}
			table1.getColumnModel().getColumn(0).setPreferredWidth(200);
//			System.out.println(output1);
			panel_find_column.add(sp);

//			DatabaseManager.closeConnection();
		}

		/////////////////////////////////////
		// Handler for Show list of tables //
		/////////////////////////////////////
		else if (mouse.getSource() == listTables)
		{
			JFrame frame_list_tables = new JFrame();
			GUIInterface panel_list_tables = new GUIInterface();
			frame_list_tables.setVisible(true);
			DefaultTableModel model = new DefaultTableModel();


			JTable table = new JTable(model);
//			table1.setBounds(5, 5, 100, 300);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(250, 300));
			model.addColumn("Table Name");
//			model1.addColumn("Column");

			frame_list_tables.add(panel_list_tables);
			frame_list_tables.setSize(300, 300);

			String output2 = DatabaseManager.handleSQLCommand("show tables;");
			String line[] = output2.split("\n");
			for (String token : line) {
				token = token.substring(12, token.length() - 1);
				String row[] = token.split(" ");
				model.addRow(new Object[] {row[0]});

			}
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
//			System.out.println(output1);
			panel_list_tables.add(sp);


		}
		////////////////////////////////////////////////////////////
		// Handler for show one or more columns of specific table //
		////////////////////////////////////////////////////////////
		else if (mouse.getSource() == showColumns) {
			JFrame frame_columns_table = new JFrame();
			GUIInterface panel_show_column = new GUIInterface();
			frame_columns_table.setVisible(true);
			DefaultTableModel model = new DefaultTableModel();


			JTable table = new JTable(model);
//			table.setBounds(5, 5, 100, 300);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(250, 300));

//			model1.addColumn("Column");

			frame_columns_table.add(panel_show_column);
			frame_columns_table.setSize(300, 300);

			String columnName = columns_TF.getText();
			String tableName = table_TF.getText();

			String columnList[] = columnName.split(",");
			for (String token : columnList) {
				model.addColumn(token.trim());
			}


			String output = DatabaseManager.handleSQLCommand("select " + columnName + " from " + tableName + ";");
			String line[] = output.split("\n");
			for (String token : line) {
				token = token.replace("{", "");
				token = token.replace("}", "");
				String row[] = token.split(","); //FIXME: not work for column has , in their data. can fix by split using regex
				List<String> single_row = new ArrayList<String>();
				for (String rowToken : row) {
					String elem[] = rowToken.split("=");
					single_row.add(elem[1]);
				}
				model.addRow(single_row.toArray());

			}
//			table.getColumnModel().getColumn(0).setPreferredWidth(200);
//			System.out.println(output1);
			panel_show_column.add(sp);

		}
		/////////////////////////////
		// Process Raw SQL queries //
		/////////////////////////////
		else if (mouse.getSource() == query) {
			JFrame frame = new JFrame();
			GUIInterface panel = new GUIInterface();
			frame.setVisible(true);
			DefaultTableModel model = new DefaultTableModel();


			JTable table = new JTable(model);
//			table.setBounds(5, 5, 100, 300);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(250, 300));

//			model1.addColumn("Column");

			frame.add(panel);
			frame.setSize(300, 300);

			String sqlQuery = query_TF.getText();

//			String columnList[] = columnName.split(",");
//			for (String token : columnList) {
//				model.addColumn(token.trim());
//			}


			String output = DatabaseManager.handleSQLCommand(sqlQuery);
			String line[] = output.split("\n");

			// add column names
			String firstLine = line[0];
			firstLine = firstLine.replace("{", "");
			firstLine = firstLine.replace("}", "");
			String firstRow[] = firstLine.split(","); //FIXME: not work for column has , in their data. can fix by split using regex
			for (String token : firstRow) {
				String elem[] = token.split("=");
				if (elem.length > 1)
					model.addColumn(elem[0]);
			}

			// add rows data
			for (String token : line) {
				token = token.replace("{", "");
				token = token.replace("}", "");

				String row[] = token.split(",[a-zA-Z0-9 ]*[^,]*="); //FIXME: not work for column has , in their data. can fix by split using regex
				List<String> single_row = new ArrayList<String>();
				for (String rowToken : row) {
					System.out.println(rowToken);
					String elem[] = rowToken.split("=");
					if (elem.length > 1)
						single_row.add(elem[1]);
					else
						single_row.add(elem[0]);
				}
				model.addRow(single_row.toArray());

			}
//			table.getColumnModel().getColumn(0).setPreferredWidth(200);
//			System.out.println(output1);
			panel.add(sp);
		}
		/////////////////////
		// jdb-search-path //
		/////////////////////
		else if (mouse.getSource() == searchPath) {
			String table1 = table1_TF.getText();
			String table2 = table2_TF.getText();
			String output = DatabaseManager.handleCustomCommand("jdb-search-path " + table1 + " " + table2);
			output = output.substring(0, output.length() - 3);
			searchPathResult.setText(output);
		}
		/////////////////////////////
		// jdb-show-related-tables //
		/////////////////////////////
		else if (mouse.getSource() == relatedTables) {
			JFrame frame = new JFrame();
			GUIInterface panel = new GUIInterface();
			frame.setVisible(true);
			DefaultTableModel model = new DefaultTableModel();


			JTable table = new JTable(model);
//			table1.setBounds(5, 5, 100, 300);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(250, 300));
			model.addColumn("Table Name");
//			model1.addColumn("Column");

			frame.add(panel);
			frame.setSize(300, 300);

			String tableName = relatedTables_TF.getText();
			String output = DatabaseManager.handleCustomCommand("jdb-show-related-tables " + tableName);
			String line[] = output.split("\n");
			for (String token : line) {
				model.addRow(new Object[] {token});

			}
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
//			System.out.println(output1);
			panel.add(sp);
		}
		/////////////////////////////
		//jdb-show-all-primary-keys//
		/////////////////////////////
		else if(mouse.getSource() == primaryKey) {
			JFrame frame = new JFrame();
			frame.setVisible(true);
			GUIInterface panel = new GUIInterface();
			DefaultTableModel model = new DefaultTableModel();
			frame.setSize(500, 350);
			frame.add(panel);


			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(450, 300));
			model.addColumn("Table Name");
			model.addColumn("Primary Key");

			String output = DatabaseManager.handleCustomCommand("jdb-show-all-primary-keys");
			String line[] = output.split("\n");
			for (String token : line) {
				if(!token.isEmpty()) {
					model.addRow(token.split(","));
				}
			}
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
			table.getColumnModel().getColumn(1).setPreferredWidth(200);
			panel.add(sp);
		}
		////////////////
		//jdb-get-view//
		////////////////
		else if(mouse.getSource() == getView) {
			JFrame frame = new JFrame();
			frame.setVisible(true);
			GUIInterface panel = new GUIInterface();
			DefaultTableModel model = new DefaultTableModel();
			frame.setSize(500, 350);
			frame.add(panel);
			frame.setTitle(viewName_TF.getText());

			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
//			sp.setPreferredSize(new Dimension(450, 300));

			String tableName = table1_TF.getText();
			String output = DatabaseManager.handleCustomCommand("jdb-get-view "+viewName_TF.getText()+" ( "+viewQuery_TF.getText() +"; )");
			String line[] = output.split("\n");
			String headers[] = line[1].split(",");

			System.out.println("---------------------");
			for(int i=0; i<headers.length; i++) {
				if(headers[i].indexOf("=") != -1) {
					System.out.println(headers[i].substring(0, headers[i].indexOf("=")));
					model.addColumn(headers[i].substring(0, headers[i].indexOf("=")));
				}
			}
			sp.setPreferredSize(new Dimension(headers.length * 70, 300));
			System.out.println("---------------------");
			for (String token : line) {
				if(!token.isEmpty()) {
					token = token.replace("{", "");
					token = token.replace("}", "");
					String row[] = token.split(","); //FIXME: not work for column has , in their data. can fix by split using regex
					ArrayList<String> single_row = new ArrayList<String>();
					for (String rowToken : row) {
						String elem[] = rowToken.split("=");
						single_row.add(elem[1]);
					}
					model.addRow(single_row.toArray());
				}
			}
			panel.add(sp);
		}
//		DatabaseManager.closeConnection();
		////////////////////////////
		//    jdb-get-addresses   //
		////////////////////////////
		else if (mouse.getSource() == getAddresses) {
			JFrame frame = new JFrame();
			frame.setVisible(true);
			GUIInterface panel = new GUIInterface();
			DefaultTableModel model = new DefaultTableModel();
			frame.setSize(500, 350);
			frame.add(panel);

			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			sp.setPreferredSize(new Dimension(500, 300));

			String tableName = getAddresses_TF.getText();
			String output = DatabaseManager.handleCustomCommand("jdb-get-addresses " + tableName);
			String line[] = output.split("\n");

			// add column names
			String firstLine = line[0];
			firstLine = firstLine.replace("{", "");
			firstLine = firstLine.replace("}", "");
			String firstRow[] = firstLine.split(","); //FIXME: not work for column has , in their data. can fix by split using regex
			for (String token : firstRow) {
					String elem[] = token.split("=");
					if (elem.length > 1)
						model.addColumn(elem[0]);
				}

			// add rows data
			//String
			for (String token : line) {
				token = token.replace("{", "");
				token = token.replace("}", "");

				String row[] = token.split(",[a-zA-Z0-9 ]*[^,]*="); //FIXME: not work for column has , in their data. can fix by split using regex
				List<String> single_row = new ArrayList<String>();
				for (String rowToken : row) {
					System.out.println(rowToken);
					String elem[] = rowToken.split("=");
					if (elem.length > 1)
						single_row.add(elem[1]);
					else
						single_row.add(elem[0]);
				}
				model.addRow(single_row.toArray());

			}
			panel.add(sp);
		}
		////////////////////////////
		//  jdb-get-region-info   //
		////////////////////////////
		else if (mouse.getSource() == getRegion) {
			JFrame frame = new JFrame();
			frame.setVisible(true);
			GUIInterface panel = new GUIInterface();
			DefaultTableModel model = new DefaultTableModel();
			frame.setSize(500, 350);
			frame.add(panel);

			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			sp.setPreferredSize(new Dimension(500, 300));

			String regionName = getRegion_TF.getText();
			String output = DatabaseManager.handleCustomCommand("jdb-get-region-info " + regionName);
			String line[] = output.split("\n");

			// add column names
			String firstLine = line[0];
			firstLine = firstLine.replace("{", "");
			firstLine = firstLine.replace("}", "");
			String firstRow[] = firstLine.split(","); //FIXME: not work for column has , in their data. can fix by split using regex
			for (String token : firstRow) {
					String elem[] = token.split("=");
					if (elem.length > 1)
						model.addColumn(elem[0]);
				}

			// add rows data
			//String
			for (String token : line) {
				token = token.replace("{", "");
				token = token.replace("}", "");

				String row[] = token.split(",[a-zA-Z0-9 ]*[^,]*="); //FIXME: not work for column has , in their data. can fix by split using regex
				List<String> single_row = new ArrayList<String>();
				for (String rowToken : row) {
					//System.out.println(rowToken);
					String elem[] = rowToken.split("=");
					if (elem.length > 1)
						single_row.add(elem[1]);
					else
						single_row.add(elem[0]);
				}
				model.addRow(single_row.toArray());

			}
			panel.add(sp);
		}
		////////////////////////////
		//  jdb-get-info-by-name  //
		////////////////////////////
		else if (mouse.getSource() == getInfo) {
			JFrame frame = new JFrame();
			frame.setVisible(true);
			GUIInterface panel = new GUIInterface();
			DefaultTableModel model = new DefaultTableModel();
			frame.setSize(500, 350);
			frame.add(panel);

			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			sp.setPreferredSize(new Dimension(500, 300));

			String tableName = getInfo_tableTF.getText();
			String attributeName = getInfo_attributeTF.getText();
			String output = DatabaseManager.handleCustomCommand("jdb-get-info-by-name " +  tableName + " " + attributeName);
			String line[] = output.split("\n");
			//System.out.println(line.length);

			// add column names
			String firstLine = line[1];
			//System.out.println(firstLine);
			firstLine = firstLine.replace("{", "");
			firstLine = firstLine.replace("}", "");
			String firstRow[] = firstLine.split(","); //FIXME: not work for column has , in their data. can fix by split using regex
			for (String token : firstRow) {
					String elem[] = token.split("=");
					System.out.println("ColumnName: " + elem[0]);
					if (elem.length > 1)
						model.addColumn(elem[0]);
				}

			// add rows data
			//String
			for (String token : line) {
				if (token.length() > 0) {
				token = token.replace("{", "");
				token = token.replace("}", "");

				String row[] = token.split(",[a-zA-Z0-9 ]*[^,]*="); //FIXME: not work for column has , in their data. can fix by split using regex
				List<String> single_row = new ArrayList<String>();
				for (String rowToken : row) {
					System.out.println(rowToken);
					String elem[] = rowToken.split("=");
					if (elem.length > 1)
						single_row.add(elem[1]);
					else
						single_row.add(elem[0]);
				}
				model.addRow(single_row.toArray());
				}
			}
			panel.add(sp);
		}

		//DatabaseManager.closeConnection();
		
		DatabaseManager.closeConnection();
	}

	@Override
	public void mouseEntered(MouseEvent mouse) {}

	@Override
	public void mouseExited(MouseEvent mouse) {}

	@Override
	public void mousePressed(MouseEvent mouse) {}

	@Override
	public void mouseReleased(MouseEvent mouse) {}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mousewheel) {}
}
