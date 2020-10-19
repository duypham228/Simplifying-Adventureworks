package main;

	import java.awt.BorderLayout;
	import java.awt.CardLayout;
	import java.awt.Color;
	import java.awt.Container;
	import java.awt.Dimension;
	import java.awt.Image;
	import java.awt.Toolkit;
	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.awt.event.ItemEvent;
	import java.awt.event.ItemListener;
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
	//import org.jfree.util.Rotation;
	import org.jfree.data.statistics.HistogramType;
	import java.awt.image.BufferedImage;
	import javax.imageio.ImageIO;

	import java.util.ArrayList;
	import java.util.List;
	import java.util.Scanner;
	
	import javax.swing.Box;
	
	import javax.swing.JButton;
	import javax.swing.JComboBox;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JTable;
	import javax.swing.JTextField;
	import javax.swing.UIManager;
	import javax.swing.UnsupportedLookAndFeelException;
	import javax.swing.table.DefaultTableModel;
	import javax.swing.border.*;


//Manager Imports
	import database.DatabaseManager;
	import page.PageManager;
	import error.ErrorManager;


public class GUIInterface extends JPanel implements MouseListener, MouseWheelListener, KeyListener, ItemListener {

	//private static JFrame frame = new JFrame();
	
	private static JButton listTables;
	private static JLabel fcLB;
	private static JTextField fcTF;
	private static JButton findColumn;

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
	private static JLabel locateStore_LB;
	
	
	JPanel cards; //a panel that uses CardLayout
    final static String FUNCTIONS = "JDBC Functions";
    final static String DASHBOARD = "Dashboard";
    public void addComponentToPane(Container pane) {
        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel menu = new JPanel(); //use FlowLayout, could maybe add more things to this
        String pages[] = { DASHBOARD, FUNCTIONS };
        JComboBox cb = new JComboBox(pages);
        cb.setEditable(false);
        cb.addItemListener(this);
        menu.add(cb);
         
        //Create the "cards". TODO: maybe add a menu card
        JPanel card1 = new JPanel();
        createFunctionsPage(card1);
        JPanel card2 = new JPanel();
        createDashboardPage(card2);
         
        //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(card2, DASHBOARD);
        cards.add(card1, FUNCTIONS);
         
        pane.add(menu, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
    }
    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
    }
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("CSCE 315 Project 2 - SAW (Simplifying Adventure Works) GUI Interface");
        frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				DatabaseManager.closeConnection();
				System.exit(0);
			}
		});
        //Create and set up the content pane.
        GUIInterface demo = new GUIInterface();
        demo.addComponentToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    public static void createDashboardPage(JPanel card) {
    	Dashboard.runDash(card);
    }
    public static void createFunctionsPage(JPanel card) {
    	card.setLayout(null);
    	card.setPreferredSize(new Dimension(810, 650));
    	
/*--------------------------3rd column start-----------------------------------------*/
    	
		////////////////////////////
		//    jdb-locate-store    //
		////////////////////////////	
    	locateStore_LB = new JLabel("ProductID: ");
    	locateStore_LB.setBounds(700, 10, 70, 25);
    	card.add(locateStore_LB);
    	
		locateStore_TF = new JTextField("710");

		locateStore_TF.setBounds(770, 10, 200, 25);
		card.add(locateStore_TF);
		
		locateStore = new JButton("jdb-locate-store");
		locateStore.setBounds(700, 40, 150, 25);
		locateStore.addMouseListener(new GUIInterface());
		card.add(locateStore);
    	
 /*--------------------------2nd column start-----------------------------------------*/
    	
		////////////////////////////
		//  jdb-search-and-join   //
		////////////////////////////
		sj_table1_LB = new JLabel("Table 1:");
		sj_table1_LB.setBounds(400, 10, 200, 25);
		card.add(sj_table1_LB);
		sj_table1_TF = new JTextField("address");
		sj_table1_TF.setBounds(475, 10, 165, 25);
		card.add(sj_table1_TF);
		sj_table2_LB = new JLabel("Table 2:");
		sj_table2_LB.setBounds(400, 40, 150, 25);
		card.add(sj_table2_LB);
		sj_table2_TF = new JTextField("vendor");
		sj_table2_TF.setBounds(475, 40, 165, 25);
		card.add(sj_table2_TF);
		
		search_and_join = new JButton ("Search and Join");
		search_and_join.setBounds(400, 70, 200, 25);
		search_and_join.addMouseListener(new GUIInterface());
		card.add(search_and_join);
		
		////////////////////////////
		//    jdb-get-schedule    //
		////////////////////////////
		getSchedule_LB = new JLabel("Purchase order ID:");
		getSchedule_LB.setBounds(400, 100, 150, 25);
		card.add(getSchedule_LB);
		
		getSchedule_TF = new JTextField("5");
		getSchedule_TF.setBounds(515, 100, 165, 25);
		card.add(getSchedule_TF);
		
		getSchedule = new JButton ("jdb-get-schedule");
		getSchedule.setBounds(400, 130, 200, 25);
		getSchedule.addMouseListener(new GUIInterface());
		card.add(getSchedule);
		
		getSchedule_LB = new JLabel("Result:");
		getSchedule_LB.setBounds(400, 160, 150, 25);
		card.add(getSchedule_LB);
		
		getScheduleResult = new JLabel("");
		getScheduleResult.setBounds(400, 190, 500, 25);
		card.add(getScheduleResult);
		
		////////////////////////////
		//     jdb-plot-schema    //
		////////////////////////////	
		plotSchema = new JButton("jdb-plot-schema");
		plotSchema.setBounds(400, 225, 150, 25);
		plotSchema.addMouseListener(new GUIInterface());
		card.add(plotSchema);
				
		/////////////////////////////
		// Process Raw SQL queries //
		/////////////////////////////
		query_LB = new JLabel("SQL query input:");
		query_LB.setBounds(400, 260, 150, 25);
		card.add(query_LB);
		query_TF = new JTextField("SELECT * FROM employeepayhistory;");
		query_TF.setBounds(500, 260, 265, 25);
		card.add(query_TF);
		query = new JButton ("Raw SQL Query");
		query.setBounds(400, 290, 200, 25);
		query.addMouseListener(new GUIInterface());
		card.add(query);
		
		////////////////
		//  jdb-stat  //
		////////////////
		stat_LB = new JLabel("jdb-stat:");
		stat_LB.setBounds(400, 325, 70, 25);
	
		statName_TF = new JTextField("employeepayhistory");
		statName_TF.setBounds(475, 325, 150, 25);
	
		colName_TF = new JTextField("Rate");
		colName_TF.setBounds(630, 325, 110, 25);
		
		getStat = new JButton("jdb-stat");
		getStat.setBounds(400, 355, 200, 25);
		getStat.addMouseListener(new GUIInterface());
		card.add(getStat);
		card.add(stat_LB);
		card.add(statName_TF);
		card.add(colName_TF);
		
		////////////////////////////
		//  Join up to 4 tables   //
		////////////////////////////	
		joinTables_LB = new JLabel("Table names and keys:");
		joinTables_LB.setBounds(400, 390, 200, 25);
		card.add(joinTables_LB);
		
		joinTables_t1k1TF = new JTextField("product ProductID");
		joinTables_t1k1TF.setBounds(505, 420, 135, 25);
		card.add(joinTables_t1k1TF);

		joinTables_t2k2TF = new JTextField("productvendor VendorID");
		joinTables_t2k2TF.setBounds(505, 450, 135, 25);
		card.add(joinTables_t2k2TF);
		
		joinTables_t3k3TF = new JTextField("vendor VendorID");
		joinTables_t3k3TF.setBounds(505, 480, 135, 25);
		card.add(joinTables_t3k3TF);

		joinTables_t4TF = new JTextField("vendorcontact");
		joinTables_t4TF.setBounds(505, 510, 100, 25);
		card.add(joinTables_t4TF);
		
		joinTables = new JButton("Show results of joining up to 4 tables");
		joinTables.setBounds(505, 540, 200, 25);
		joinTables.addMouseListener(new GUIInterface());
		card.add(joinTables);
		
		////////////////////////////
		//  jdb-get-info-by-name  //
		////////////////////////////
		getInfo_LB = new JLabel("Table Name & Attribute Name:");
		getInfo_LB.setBounds(400, 575, 200, 25);
		card.add(getInfo_LB);
		
		getInfo_tableTF = new JTextField("product");
		getInfo_tableTF.setBounds(590, 575, 100, 25);
		card.add(getInfo_tableTF);
		
		getInfo_attributeTF = new JTextField("Adjustable Race");
		getInfo_attributeTF.setBounds(690, 575, 100, 25);
		card.add(getInfo_attributeTF);
		
		getInfo = new JButton("jdb-get-info-by-name");
		getInfo.setBounds(400, 605, 200, 25);
		getInfo.addMouseListener(new GUIInterface());
		card.add(getInfo);
			
		
/*--------------------------1st column start-----------------------------------------*/
				
		/////////////////////
		// jdb-list-tables //
		/////////////////////
		listTables = new JButton("Show List of Tables");
		listTables.setBounds(5, 20, 200, 25);
		listTables.addMouseListener(new GUIInterface());
		card.add(listTables);
		
		/////////////////////
		// jdb-find-column //
		/////////////////////
		fcLB = new JLabel("Column Name:");
		fcLB.setBounds(10, 60, 150, 25);
		card.add(fcLB);
		fcTF = new JTextField("ProductID");
		fcTF.setBounds(110, 60, 165, 25);
		card.add(fcTF);
		findColumn = new JButton("jdb-find-column");
		findColumn.setBounds(10, 90, 200, 25);
		findColumn.addMouseListener(new GUIInterface());
		card.add(findColumn);
		
		/////////////////////////////////////////////////
		// Show one or more columns for specific table //
		/////////////////////////////////////////////////
		columns_LB = new JLabel("Column Name:");
		columns_LB.setBounds(10, 125, 150, 25);
		card.add(columns_LB);
		columns_TF = new JTextField("VendorID, AccountNumber, Name");
		columns_TF.setBounds(110, 125, 165, 25);
		card.add(columns_TF);
		table_LB = new JLabel("Table Name:");
		table_LB.setBounds(10, 155, 150, 25);
		card.add(table_LB);
		table_TF = new JTextField("vendor");
		table_TF.setBounds(110, 155, 165, 25);
		card.add(table_TF);
		
		showColumns = new JButton ("Show one or more Columns of Specific Table");
		showColumns.setBounds(10, 185, 200, 25);
		showColumns.addMouseListener(new GUIInterface());
		card.add(showColumns);

		/////////////////////
		// jdb-search-path //
		/////////////////////
		table1_LB = new JLabel("Table 1:");
		table1_LB.setBounds(10, 220, 150, 25);
		card.add(table1_LB);
		table1_TF = new JTextField("customer");
		table1_TF.setBounds(110, 220, 165, 25);
		card.add(table1_TF);
		table2_LB = new JLabel("Table 2:");
		table2_LB.setBounds(10, 250, 150, 25);
		card.add(table2_LB);
		table2_TF = new JTextField("vendor");
		table2_TF.setBounds(110, 250, 165, 25);
		card.add(table2_TF);

		searchPath = new JButton ("Search Path");
		searchPath.setBounds(10, 280, 200, 25);
		searchPath.addMouseListener(new GUIInterface());
		card.add(searchPath);
		searchPath_LB = new JLabel("Result:");
		searchPath_LB.setBounds(10, 310, 70, 25);
		card.add(searchPath_LB);
		searchPathResult = new JLabel("");
		searchPathResult.setBounds(60, 310, 500, 25);
		card.add(searchPathResult);
		
		/////////////////////////////
		// jdb-show-related-tables //
		/////////////////////////////
		relatedTables_LB = new JLabel("Table name: ");
		relatedTables_LB.setBounds(10, 345, 150, 25);
		card.add(relatedTables_LB);
		relatedTables_TF = new JTextField("product");
		relatedTables_TF.setBounds(110, 345, 160, 25);
		card.add(relatedTables_TF);
		relatedTables = new JButton("jdb-show-related-tabels");
		relatedTables.setBounds(10, 375, 200, 25);
		relatedTables.addMouseListener(new GUIInterface());
		card.add(relatedTables);

		/////////////////////////////
		//jdb-show-all-primary-keys//
		/////////////////////////////
		primaryKey = new JButton("jdb-show-all-primary-keys");
		primaryKey.setBounds(10, 410, 200, 25);
		primaryKey.addMouseListener(new GUIInterface());
		card.add(primaryKey);

		////////////////
		//jdb-get-view//
		////////////////
		view_LB = new JLabel("jdb-get-view:");
		view_LB.setBounds(10, 450, 100, 25);
		viewName_TF = new JTextField("(name)");
		viewName_TF.setBounds(115, 450, 50, 25);
		viewQuery_TF = new JTextField("select * from employee;");
		viewQuery_TF.setBounds(170, 450, 100, 25);
		getView = new JButton("jdb-get-view");
		getView.setBounds(10, 480, 200, 25);
		getView.addMouseListener(new GUIInterface());
		card.add(getView);
		card.add(view_LB);
		card.add(viewName_TF);
		card.add(viewQuery_TF);

		////////////////////////////
		//    jdb-get-addresses   //
		////////////////////////////
		getAddresses_LB = new JLabel("Table Name & Range:");
		getAddresses_LB.setBounds(10, 515, 200, 25);
		card.add(getAddresses_LB);

		getAddresses_TF = new JTextField("address");
		getAddresses_TF.setBounds(150, 515, 80, 25);
		card.add(getAddresses_TF);
		
		getAddresses_TF2 = new JTextField("Range (1 = 0-4999)");
		getAddresses_TF2.setBounds(230, 515, 140, 25);
		card.add(getAddresses_TF2);

		getAddresses = new JButton("jdb-get-addresses");
		getAddresses.setBounds(10, 545, 200, 25);
		getAddresses.addMouseListener(new GUIInterface());
		card.add(getAddresses);

		////////////////////////////
		//  jdb-get-region-info   //
		////////////////////////////
		getRegion_LB = new JLabel("Region & Range:");
		getRegion_LB.setBounds(10, 585, 200, 25);
		card.add(getRegion_LB);

		getRegion_TF = new JTextField("US");
		getRegion_TF.setBounds(150, 585, 80, 25);
		card.add(getRegion_TF);
		
		getRegion_TF2 = new JTextField("Range (1 = 0-4999)");
		getRegion_TF2.setBounds(230, 585, 140, 25);
		card.add(getRegion_TF2);

		getRegion = new JButton("jdb-get-region-info");
		getRegion.setBounds(10, 615, 200, 25);
		getRegion.addMouseListener(new GUIInterface());
		card.add(getRegion);
    }
	
	public static void main(String[]args) {
		 /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
         
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	DatabaseManager.openConnection();
        		DatabaseManager.queryDatabase("use adventureworks;");
                createAndShowGUI();
            }
        });
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
		
		////////////////////////////
		//    jdb-locate-store    //
		////////////////////////////
		if (mouse.getSource() == locateStore) {
			JFrame frame = new JFrame();
			frame.setVisible(true);
			GUIInterface panel = new GUIInterface();
			DefaultTableModel model = new DefaultTableModel();
			frame.setSize(800, 600);
			frame.add(panel);
			frame.setLocationRelativeTo(null);
			
			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			sp.setPreferredSize(new Dimension(600, 550));
			
			String productID = locateStore_TF.getText();
			System.out.println("Entered productID: " + productID);
			//int rangenum = Integer.parseInt(range);
			String output = DatabaseManager.handleCustomCommand("jdb-locate-store " + productID);
			String line[] = output.split("\n");
			System.out.println(output);
		
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
		/////////////////////////////////
		// Handler for jdb-find-column //
		/////////////////////////////////
		else if (mouse.getSource() == findColumn) { // jdb-find-column
			JFrame frame_find_column = new JFrame();
			GUIInterface panel_find_column = new GUIInterface();
			frame_find_column.setVisible(true);
			DefaultTableModel model1 = new DefaultTableModel();

			JTable table1 = new JTable(model1);
			table1.setShowGrid(true);
			table1.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table1);
			sp.setPreferredSize(new Dimension(700, 550));
			model1.addColumn("Table Name");

			frame_find_column.add(panel_find_column);
			frame_find_column.setSize(800, 600);
			frame_find_column.setLocationRelativeTo(null);

			String column_name = fcTF.getText();
			String output1 = DatabaseManager.handleCustomCommand("jdb-find-column " + column_name);
			String line[] = output1.split("\n");
			for (String token : line) {
				String row[] = token.split(" ");
				model1.addRow(new Object[] {row[0]});

			}
			table1.getColumnModel().getColumn(0).setPreferredWidth(200);
			panel_find_column.add(sp);
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
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(700, 550));
			model.addColumn("Table Name");

			frame_list_tables.add(panel_list_tables);
			frame_list_tables.setSize(800, 600);
			frame_list_tables.setLocationRelativeTo(null);

			String output2 = DatabaseManager.handleSQLCommand("show tables;");
			String line[] = output2.split("\n");
			for (String token : line) {
				token = token.substring(12, token.length() - 1);
				String row[] = token.split(" ");
				model.addRow(new Object[] {row[0]});

			}
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
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
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(600, 550));

			frame_columns_table.add(panel_show_column);
			frame_columns_table.setSize(800, 600);
			frame_columns_table.setLocationRelativeTo(null);

			String columnName = columns_TF.getText();
			String tableName = table_TF.getText();

			String columnList[] = columnName.split(",");
			for (String token : columnList) {
				model.addColumn(token.trim());
			}

			String output = DatabaseManager.handleSQLCommand("select " + columnName + " from " + tableName + ";");
			System.out.println(columnName);
			String line[] = output.split("\n");
			for (String token : line) {
				token = token.replace("{", "");
				token = token.replace("}", "");
				System.out.println(token);
				String row[] = token.split(","); //FIXME: not work for column has , in their data. can fix by split using regex
				List<String> single_row = new ArrayList<String>();
				for (String rowToken : row) {
					if(rowToken.indexOf("=")!=-1){
						String elem[] = rowToken.split("=");
						single_row.add(elem[1]);
					}
				}
				model.addRow(single_row.toArray());

			}
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
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(700, 550));

			frame.add(panel);
			frame.setSize(800, 600);
			frame.setLocationRelativeTo(null);

			String sqlQuery = query_TF.getText();
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
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(700,550));
			
			frame.add(panel);
			frame.setSize(800, 600);
			frame.setLocationRelativeTo(null);

			String tableName1 = sj_table1_TF.getText();
			String tableName2 = sj_table2_TF.getText();
			
			
			String output = DatabaseManager.handleCustomCommand("jdb-search-and-join " + tableName1 + " " + tableName2);
			String[] line = output.split("\n");
			String firstLine = line[0];
			firstLine = firstLine.replace("{", "");
			firstLine = firstLine.replace("}", "");
			String firstRow[] = firstLine.split(","); //FIXME: not work for column has , in their data. can fix by split using regex
			
			for (String token : firstRow) {
				String elem[] = token.split("=");
				if (elem.length > 1)
					model.addColumn(elem[0]);
			}
			for (String token : line) {
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
				}
				model.addRow(row);
				
			}
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
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
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(700, 550));
			model.addColumn("Table Name");

			frame.add(panel);
			frame.setSize(800, 600);
			frame.setLocationRelativeTo(null);

			String tableName = relatedTables_TF.getText();
			String output = DatabaseManager.handleCustomCommand("jdb-show-related-tables " + tableName);
			String line[] = output.split("\n");
			for (String token : line) {
				model.addRow(new Object[] {token});

			}
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
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
			frame.setSize(800, 600);
			frame.add(panel);
			frame.setLocationRelativeTo(null);

			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(700, 550));
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
			frame.setSize(800, 600);
			frame.add(panel);
			frame.setTitle(viewName_TF.getText());
			frame.setLocationRelativeTo(null);

			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			
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
	   
			frame.setSize(800, 600);
			frame.add(panel);
			frame.setTitle(viewName_TF.getText());
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);

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
			frame.setSize(800, 600);
			frame.add(panel);
			frame.setLocationRelativeTo(null);

			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			sp.setPreferredSize(new Dimension(700, 550));

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
			frame.setSize(800, 600);
			frame.add(panel);
			frame.setLocationRelativeTo(null);

			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			sp.setPreferredSize(new Dimension(700, 550));

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
			frame.setSize(800, 600);
			frame.add(panel);
			frame.setLocationRelativeTo(null);

			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			sp.setPreferredSize(new Dimension(700, 550));

			String tableName = getInfo_tableTF.getText();
			String attributeName = getInfo_attributeTF.getText();
			String output = DatabaseManager.handleCustomCommand("jdb-get-info-by-name " +  tableName + " " + attributeName);
			String line[] = output.split("\n");

			// add column names
			String firstLine = line[1];
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
				if (token.length() > 0) {
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
		//Joining up to 4 tables
		else if (mouse.getSource() == joinTables) {
			JFrame frame = new JFrame();
			frame.setVisible(true);
			GUIInterface panel = new GUIInterface();
			DefaultTableModel model = new DefaultTableModel();
			frame.setSize(800, 600);
			frame.add(panel);
			frame.setLocationRelativeTo(null);

			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setGridColor(Color.black);
			JScrollPane sp = new JScrollPane(table);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			sp.setPreferredSize(new Dimension(700, 550));
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

			// add column names
			String firstLine = line[1];
			//System.out.println(firstLine);
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
				if (token.length() > 0) {
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
				f = new File("external/schema.png");
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
			frame.setLocationRelativeTo(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
