package main;

	import java.awt.Color;
	import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException; 

//Standard Library Imports
	import java.awt.event.KeyEvent;
	import java.awt.event.KeyListener;

	import java.awt.event.MouseEvent;
	import java.awt.event.MouseListener;

	import java.awt.event.MouseWheelEvent;
	import java.awt.event.MouseWheelListener;

	import java.awt.event.WindowAdapter;
	import java.awt.event.WindowEvent;
import java.text.NumberFormat;

//histogram
	import org.jfree.chart.ChartFactory;
	import org.jfree.chart.ChartPanel;
	import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
	import java.awt.image.BufferedImage;
	import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.Box;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;


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
	
	// jdb-search-and-join <table1> <table2>
	private static JLabel sj_table1_LB;
	private static JLabel sj_table2_LB;
	private static JTextField sj_table1_TF;
	private static JTextField sj_table2_TF;
	private static JButton search_and_join;
	private static JLabel search_and_join_LB;
	private static JLabel search_and_join_result;
	
	// jdb-get-schedule
	private static JLabel getSchedule_LB;
	private static JTextField getSchedule_TF;
	private static JButton getSchedule;
	private static JLabel getScheduleResult;

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
	private static JTextField getAddresses_TF2;
	private static JButton getAddresses;

	//jdb-get-region-info
	private static JLabel getRegion_LB;
	private static JTextField getRegion_TF2;
	private static JTextField getRegion_TF;
	private static JButton getRegion;

	//jdb-get-info-by-name
	private static JLabel getInfo_LB;
	private static JTextField getInfo_tableTF;
	private static JTextField getInfo_attributeTF;
	private static JButton getInfo;

	//join up to 4 tables
	private static JLabel joinTables_LB;
	private static JTextField joinTables_t1k1TF;
	private static JTextField joinTables_t2k2TF;
	private static JTextField joinTables_t3k3TF;
	private static JTextField joinTables_t4TF;
	private static JButton joinTables;
	
	//jdb-stat
	private static JFreeChart chart;
	private static ChartPanel chartPanel;
	private static HistogramDataset dataset;
	private static JButton getStat;
	private static JLabel stat_LB;
	private static JTextField statName_TF;
	private static JTextField colName_TF;
	
	//jdb-plot-schema
	private static JButton plotSchema;
	
	//jdb-locate-store
	private static JButton locateStore;
	private static JTextField locateStore_TF;

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
		frame.setSize(700, 850);
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
		
		////////////////////////////
		//  jdb-search-and-join   //
		////////////////////////////
		sj_table1_LB = new JLabel("Table 1:");
		sj_table1_LB.setBounds(450, 10, 200, 25);
		gui.add(sj_table1_LB);
		
		sj_table1_TF = new JTextField(20);
		sj_table1_TF.setBounds(525, 10, 165, 25);
		gui.add(sj_table1_TF);
		
		sj_table2_LB = new JLabel("Table 2:");
		sj_table2_LB.setBounds(450, 40, 150, 25);
		gui.add(sj_table2_LB);
		
		sj_table2_TF = new JTextField(20);
		sj_table2_TF.setBounds(525, 40, 165, 25);
		gui.add(sj_table2_TF);
		
		// Button for show one or more columns from specific table
		search_and_join = new JButton ("Search and Join");
		search_and_join.setBounds(450, 70, 200, 25);
		search_and_join.addMouseListener(new GUIInterface());
		gui.add(search_and_join);
		
		////////////////////////////
		//    jdb-get-schedule    //
		////////////////////////////
		getSchedule_LB = new JLabel("Purchase order ID:");
		getSchedule_LB.setBounds(450, 100, 200, 25);
		gui.add(getSchedule_LB);
		
		getSchedule_TF = new JTextField(20);
		getSchedule_TF.setBounds(525, 100, 165, 25);
		gui.add(getSchedule_TF);
		
		getSchedule = new JButton ("jdb-get-schedule");
		getSchedule.setBounds(450, 130, 200, 25);
		getSchedule.addMouseListener(new GUIInterface());
		gui.add(getSchedule);
		
		getSchedule_LB = new JLabel("Result:");
		getSchedule_LB.setBounds(450, 160, 150, 25);
		gui.add(getSchedule_LB);

		getScheduleResult = new JLabel("");
		getScheduleResult.setBounds(450, 190, 500, 25);
		gui.add(getScheduleResult);

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
		
		////////////////
		//  jdb-stat  //
		////////////////
		stat_LB = new JLabel("jdb-stat:");
		stat_LB.setBounds(300, 480, 70, 25);
	
		statName_TF = new JTextField("View or Table Name");
		statName_TF.setBounds(375, 480, 150, 25);

		colName_TF = new JTextField("Column Name");
		colName_TF.setBounds(530, 480, 110, 25);
		
		getStat = new JButton("jdb-stat");
		getStat.setBounds(300, 510, 200, 25);
		getStat.addMouseListener(new GUIInterface());
//        dataset = new HistogramDataset();
//        chart = createChart(dataset, );
		gui.add(getStat);
		gui.add(stat_LB);
		gui.add(statName_TF);
		gui.add(colName_TF);


		////////////////////////////
		//    jdb-get-addresses   //
		////////////////////////////
		getAddresses_LB = new JLabel("Name of table and range:");
		getAddresses_LB.setBounds(10, 540, 200, 25);
		gui.add(getAddresses_LB);

		getAddresses_TF = new JTextField("Table");
		getAddresses_TF.setBounds(150, 540, 100, 25);
		gui.add(getAddresses_TF);
		
		getAddresses_TF2 = new JTextField("Range (1 = 0-4999)");
		getAddresses_TF2.setBounds(250, 540, 110, 25);
		gui.add(getAddresses_TF2);

		getAddresses = new JButton("jdb-get-addresses");
		getAddresses.setBounds(10, 570, 200, 25);
		getAddresses.addMouseListener(new GUIInterface());
		gui.add(getAddresses);

		////////////////////////////
		//  jdb-get-region-info   //
		////////////////////////////
		getRegion_LB = new JLabel("Enter Region and range:");
		getRegion_LB.setBounds(10, 600, 200, 25);
		gui.add(getRegion_LB);

		getRegion_TF = new JTextField("Region");
		getRegion_TF.setBounds(150, 600, 100, 25);
		gui.add(getRegion_TF);
		
		getRegion_TF2 = new JTextField("Range (1 = 0-4999)");
		getRegion_TF2.setBounds(250, 600, 110, 25);
		gui.add(getRegion_TF2);

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
		getInfo_tableTF.setBounds(150, 660, 100, 25);
		gui.add(getInfo_tableTF);

		getInfo_attributeTF = new JTextField("name");
		getInfo_attributeTF.setBounds(250, 660, 200, 25);
		gui.add(getInfo_attributeTF);

		getInfo = new JButton("jdb-get-info-by-name");
		getInfo.setBounds(10, 690, 200, 25);
		getInfo.addMouseListener(new GUIInterface());
		gui.add(getInfo);
		
		////////////////////////////
		//  Join up to 4 tables   //
		////////////////////////////	
		joinTables_LB = new JLabel("Table names and keys:");
		joinTables_LB.setBounds(10, 720, 100, 25);
		gui.add(joinTables_LB);
		
		joinTables_t1k1TF = new JTextField("product ProductID");
		joinTables_t1k1TF.setBounds(110, 720, 135, 25);
		gui.add(joinTables_t1k1TF);

		joinTables_t2k2TF = new JTextField("productvendor VendorID");
		joinTables_t2k2TF.setBounds(245, 720, 135, 25);
		gui.add(joinTables_t2k2TF);
		
		joinTables_t3k3TF = new JTextField("vendor VendorID");
		joinTables_t3k3TF.setBounds(380, 720, 135, 25);
		gui.add(joinTables_t3k3TF);

		joinTables_t4TF = new JTextField("vendorcontact");
		joinTables_t4TF.setBounds(515, 720, 100, 25);
		gui.add(joinTables_t4TF);
		
		joinTables = new JButton("Show results of joining up to 4 tables");
		joinTables.setBounds(10, 750, 200, 25);
		joinTables.addMouseListener(new GUIInterface());
		gui.add(joinTables);
		
		////////////////////////////
		//     jdb-plot-schema    //
		////////////////////////////	
		plotSchema = new JButton("jdb-plot-schema");
		plotSchema.setBounds(450, 250, 150, 25);
		plotSchema.addMouseListener(new GUIInterface());
		gui.add(plotSchema);
		
		////////////////////////////
		//    jdb-locate-store    //
		////////////////////////////	
		locateStore_TF = new JTextField("Enter ProductID");
		locateStore_TF.setBounds(450, 280, 200, 25);
		gui.add(locateStore_TF);
		
		locateStore = new JButton("jdb-locate-store");
		locateStore.setBounds(450, 310, 150, 25);
		locateStore.addMouseListener(new GUIInterface());
		gui.add(locateStore);

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
		/////////////////////////
		// jdb-search-and-join //
		/////////////////////////
		else if (mouse.getSource() == search_and_join) {
			JFrame frame = new JFrame();
			GUIInterface panel = new GUIInterface();
			frame.setVisible(true);
			DefaultTableModel model = new DefaultTableModel();
			
			JTable table = new JTable(model);
//			table1.setBounds(5, 5, 100, 300);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(1600,800));
			
//			model1.addColumn("Column");
			
			frame.add(panel);
			frame.setSize(300, 300);

			String tableName1 = sj_table1_TF.getText();
			String tableName2 = sj_table2_TF.getText();
			
			
			String output = DatabaseManager.handleCustomCommand("jdb-search-and-join " + tableName1 + " " + tableName2);
			String[] line = output.split("\n");
			String firstLine = line[0];
			firstLine = firstLine.replace("{", "");
			firstLine = firstLine.replace("}", "");
			String firstRow[] = firstLine.split(","); //FIXME: not work for column has , in their data. can fix by split using regex
			
			for (String token : firstRow) {
				//if (!token .equals (firstRow[0])) {
					String elem[] = token.split("=");
					if (elem.length > 1)
						model.addColumn(elem[0]);
				//}
			}
			
			for (String token : line) {
				// model.addRow(new Object[] {token});
				token = token.replace("{", "");
				token = token.replace("}", "");
				String[] row = token.split(",");
				
				for (int i=0; i < row.length; i++) {
					String[] elem = row[i].split("=");
					
					try {
						row[i] = elem[1];
					}
					catch (Exception e){
						;
					}
					// System.out.println(row[i]);
				}
				model.addRow(row);
				
			}
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
//			System.out.println(output1);
			panel.add(sp);
		}
		
		//////////////////////////////////
		// Handler for jdb-get-schedule //
		//////////////////////////////////
		else if (mouse.getSource() == getSchedule) {
			String table1 = getSchedule_TF.getText();
			String output = DatabaseManager.handleCustomCommand("jdb-get-schedule " + table1);
			output = output.substring(0, output.length() - 3);
			getScheduleResult.setText(output);
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
		////////////////
		//jdb-get-stat//
		////////////////
		else if(mouse.getSource() == getStat) {
			JFrame frame = new JFrame();
			frame.setVisible(true);
			GUIInterface panel = new GUIInterface();
			String output = DatabaseManager.handleCustomCommand("jdb-stat "+statName_TF.getText()+" "+colName_TF.getText());
			
			String line[] = output.split("\n");
			System.out.println("start");
			for(int i=0; i<line.length; i++) {
				System.out.println(line[i]);
			}
			double min = Double.parseDouble(line[1]);
			double max = Double.parseDouble(line[2]);
			double mean = Double.parseDouble(line[3]);
			double median = Double.parseDouble(line[4]);
			String dataStr[] = line[5].split(",");
			double[] data = new double[dataStr.length];
			for(int i=0; i<dataStr.length; i++) {
				data[i] = Double.parseDouble(dataStr[i]);
			}
			
			//creates histogram data and adds series
			dataset = new HistogramDataset();
			dataset.setType(HistogramType.RELATIVE_FREQUENCY);
	        dataset.addSeries("Number Density", data, data.length, min, max); 
	        
	        //create and customize chart settings
			chart = ChartFactory.createHistogram(	
					statName_TF.getText(), null, null, dataset, PlotOrientation.HORIZONTAL, true, false, false);
			XYPlot plot = (XYPlot) chart.getPlot();
	        plot.setForegroundAlpha(0.75f);
	        NumberAxis axis = (NumberAxis) plot.getDomainAxis();
	        axis.setAutoRangeIncludesZero(false);
	        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        rangeAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
	        
	        //adding chart to panel
			chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new Dimension(500, 300));
	        chartPanel.setMouseZoomable(true, false);
	        chartPanel.setBounds(0, 0, 500, 300);
	   
			frame.setSize(500, 350);
			frame.add(panel);
			frame.setTitle(viewName_TF.getText());
			frame.setResizable(false);

			panel.add(chartPanel);
			JLabel min_LB = new JLabel("min = "+min);
			min_LB.setBounds(550, 20, 50, 20);
			JLabel max_LB = new JLabel("max = "+max);
			max_LB.setBounds(570, 20, 50, 20);
			JLabel mean_LB = new JLabel("mean = "+mean);
			mean_LB.setBounds(590, 20, 50, 20);
			JLabel median_LB = new JLabel("median = "+median);
			median_LB.setBounds(610, 20, 50, 20);
			
			
			panel.add(Box.createHorizontalStrut(5));
			panel.add(min_LB);
			panel.add(Box.createHorizontalStrut(5));
			panel.add(max_LB);
			panel.add(Box.createHorizontalStrut(5));
			panel.add(median_LB);
			panel.add(Box.createHorizontalStrut(5));
			panel.add(mean_LB);
		}
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
			String range = getAddresses_TF2.getText();
			//int rangenum = Integer.parseInt(range);
			String output = DatabaseManager.handleCustomCommand("jdb-get-addresses " + tableName + " " + range);
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
			String range = getRegion_TF2.getText();
			String output = DatabaseManager.handleCustomCommand("jdb-get-region-info " + regionName + " " + range);
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
					//System.out.println("ColumnName: " + elem[0]);
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
					//System.out.println(rowToken);
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
		//Joining up to 4 tables
		else if (mouse.getSource() == joinTables) {
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
			String t1k1 = joinTables_t1k1TF.getText();
			String [] splitfieldt1k1 = t1k1.split(" ");
			String t1 = splitfieldt1k1[0];
			String k1 = "";
			if (splitfieldt1k1.length > 1)
				k1 = splitfieldt1k1[1];
			String t2k2 = joinTables_t2k2TF.getText();
			String [] splitfieldt2k2 = t2k2.split(" ");
			String t2 = splitfieldt2k2[0];
			String k2 = "";
			if (splitfieldt2k2.length > 1)
				k2 = splitfieldt2k2[1];
			String t3k3 = joinTables_t3k3TF.getText();
			String [] splitfieldt3k3 = t3k3.split(" ");
			String t3 = splitfieldt3k3[0];
			String k3 = "";
			if (splitfieldt3k3.length > 1)
				k3 = splitfieldt3k3[1];
			String t4 = joinTables_t4TF.getText();
			String query = "";
			if (splitfieldt1k1.length > 1 && splitfieldt2k2.length > 1 && splitfieldt3k3.length > 1 && t4.length() > 0) {
				query = "select * from " + t1 + " join " + t2 + " on " + t1 + "." + k1 + " = " + t2 + "." + k1 + " join "
						+ t3 + " on " + t3 + "." + k2 + " = " + t2 + "." + k2 + " join " + t4 + " on " + t4 + "." + k3 + " = " + t3 + "." + k3;
			}
			else if (splitfieldt1k1.length > 1 && splitfieldt2k2.length > 1 && splitfieldt3k3.length > 0) {
				query = "select * from " + t1 + " join " + t2 + " on " + t1 + "." + k1 + " = " + t2 + "." + k1 + " join "
						+ t3 + " on " + t3 + "." + k2 + " = " + t2 + "." + k2;
			}
			else if (splitfieldt1k1.length > 1 && splitfieldt2k2.length > 0) {
				query = "select * from " + t1 + " join " + t2 + " on " + t1 + "." + k1 + " = " + t2 + "." + k1;
			}
 			String output = DatabaseManager.handleSQLCommand(query);
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
					//System.out.println("ColumnName: " + elem[0]);
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
					//System.out.println(rowToken);
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
		////////////////////////////
		//     jdb-plot-schema    //
		////////////////////////////	
		else if (mouse.getSource() == plotSchema) {
			ArrayList<String> columnNames = new ArrayList<String>();
			String output = DatabaseManager.handleCustomCommand("jdb-show-all-primary-keys");
			String[] lines = output.split("\n");
			String finalOutput = "";
			for (int i = 1; i < lines.length; i++) {
				String token = lines[i];
				String[] columns = token.split(",");
				//System.out.println(columns[1]);
				columnNames.add(columns[1]);
			}
			String findResults = "";
			String[] resultsArray;
			String[] splitResults;
			for (int i = 0; i < columnNames.size(); i++) {
				ArrayList<String> dotpi = new ArrayList<String>();
				findResults = DatabaseManager.handleCustomCommand("jdb-find-column " + columnNames.get(i).trim());
				resultsArray = findResults.split("\n");
				for (String token : resultsArray) {
					splitResults = token.split(" ");
					if (splitResults[0].length() > 0) {
						dotpi.add(splitResults[0]);
						//System.out.println("final loop: " + splitResults[0]);
					}
				}
				String finalResult = "";
				for (int k = 0; k < dotpi.size(); k++) {
					if (k == dotpi.size() - 1) {
						finalResult += dotpi.get(k);
					}
					else {
						finalResult += dotpi.get(k) + " -- ";
					}
				}
				finalOutput += finalResult + "\n";
			}
			System.out.println(finalOutput);
			try {
			File schemaDot = new File("external/schema.dot");
			schemaDot.createNewFile();
			FileWriter schemaWrite = new FileWriter("external/schema.dot");
			schemaWrite.write("graph {\n");
			schemaWrite.write(finalOutput);
			schemaWrite.write("}");
			schemaWrite.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Runtime term = Runtime.getRuntime();
			try {
			term.exec("cmd /c start \"\" generategraph.bat");
			Thread.sleep(1000);
			File f;
			found:
			while (true) {
				f = new File("external/schema.jpg");
				if (f.exists()) {
					break found;
				}
			}
			Image image;
			image = ImageIO.read(f);
			image = image.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
			JFrame frame = new JFrame();
			GUIInterface gui = new GUIInterface();
			JLabel imageHolder = new JLabel();
			imageHolder.setIcon(new javax.swing.ImageIcon(image));
			gui.add(imageHolder);
			frame.add(gui);
			frame.setVisible(true);
			frame.setSize(1200, 800);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		////////////////////////////
		//    jdb-locate-store    //
		////////////////////////////
		else if (mouse.getSource() == locateStore) {
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

			String productID = locateStore_TF.getText();
			//int rangenum = Integer.parseInt(range);
			String output = DatabaseManager.handleCustomCommand("jdb-locate-store " + productID);
			String line[] = output.split("\n");
			
			// add column names
			model.addColumn("Name");

			// add rows data
			//String
			for (String token : line) {
				if (token.length() > 1) {
					token = token.replace("{", "");
					token = token.replace("}", "");

					String row[] = token.split(",[a-zA-Z0-9 ]*[^,]*="); //FIXME: not work for column has , in their data. can fix by split using regex
					List<String> single_row = new ArrayList<String>();
					for (String rowToken : row) {
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

		
		// DatabaseManager.closeConnection();

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
