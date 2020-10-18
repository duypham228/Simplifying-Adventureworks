package main;

import java.awt.BorderLayout;
//Standard Library Imports
	import java.awt.event.KeyEvent;
	import java.awt.event.KeyListener; 
	
	import java.awt.event.MouseEvent; 
	import java.awt.event.MouseListener;
	
	import java.awt.event.MouseWheelEvent;
	import java.awt.event.MouseWheelListener;
	
	import java.awt.event.WindowAdapter;
	import java.awt.event.WindowEvent;

	import javax.swing.JButton;
	import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.*;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

//Manager Imports
	import database.DatabaseManager;
	import page.PageManager;
	import error.ErrorManager;
	
public class Dashboard extends JPanel implements MouseListener, MouseWheelListener, KeyListener {
	
	private static JFrame frame = new JFrame();
	public static JButton yearRev;
	public static JPanel mainPanel;
	
	public Dashboard() {
		super();
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseWheelListener(this);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
		frame.add(this);
//		frame.setSize(700, 850);
		frame.setVisible(true);
		this.repaint();
	}
	public static void main(String[]args) {
		DatabaseManager.openConnection();
		DatabaseManager.queryDatabase("use adventureworks;");
		Dashboard das = new Dashboard();
		
//		yearRev = new JButton("Revenue by year");
//		yearRev.setBounds(10, 50, 150, 25);
//		yearRev.addMouseListener(new Dashboard());
//		das.add(yearRev);
		
		///////////////////////////
		// Revenue by year chart //
		///////////////////////////
		CategoryChart chart1 = new CategoryChartBuilder().width(800).height(600).title("Revenue by Year").xAxisTitle("Year").yAxisTitle("Revenue").build();
		
		chart1.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart1.getStyler().setHasAnnotations(true);
		chart1.getStyler().setPlotGridLinesVisible(false);
		
		String output = DatabaseManager.handleSQLCommand("select sum(LineTotal), year(ModifiedDate) from salesorderdetail group by year(ModifiedDate);");
		System.out.println(output);
		
		List<Double> rev = new ArrayList<Double>();
		List<Integer> year = new ArrayList<Integer>();
		
		String line[] = output.split("\n");
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
			rev.add(Double.parseDouble(single_row.get(0)));
			year.add(Integer.parseInt(single_row.get(1)));
		}
		
		chart1.addSeries("Revenue by Year", year, rev);
		
		/////////////////////////////////////////
		// Revenue by month of each year chart //
		/////////////////////////////////////////
		XYChart chart2 =
		        new XYChartBuilder()
		            .width(800)
		            .height(600)
		            .title("Revenue by month from each year")
		            .xAxisTitle("Month")
		            .yAxisTitle("Revenue")
		            .build();

	    // Customize Chart
	    chart2.getStyler().setLegendPosition(LegendPosition.OutsideE);
	    chart2.getStyler().setAxisTitlesVisible(true);
	    chart2.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);

	    chart2.getStyler().setCursorEnabled(true);
	    
	    String output2 = DatabaseManager.handleSQLCommand("select sum(LineTotal), month(ModifiedDate), year(ModifiedDate) from salesorderdetail group by year(ModifiedDate), month(ModifiedDate);");
//	    System.out.println(output2);
	    List<Double> rev2001 = new ArrayList<Double>();
		List<Integer> month2001 = new ArrayList<Integer>();
		
		List<Double> rev2002 = new ArrayList<Double>();
		List<Integer> month2002 = new ArrayList<Integer>();
		
		List<Double> rev2003 = new ArrayList<Double>();
		List<Integer> month2003 = new ArrayList<Integer>();
		
		List<Double> rev2004 = new ArrayList<Double>();
		List<Integer> month2004 = new ArrayList<Integer>();
		
		String line2[] = output2.split("\n");
		for (String token : line2) {
			token = token.replace("{", "");
			token = token.replace("}", "");

			String row[] = token.split(",[a-zA-Z0-9 ]*[^,]*="); //FIXME: not work for column has , in their data. can fix by split using regex
			List<String> single_row = new ArrayList<String>();
			for (String rowToken : row) {
//				System.out.println(rowToken);
				String elem[] = rowToken.split("=");
				if (elem.length > 1)
					single_row.add(elem[1]);
				else
					single_row.add(elem[0]);
			}
			System.out.println(single_row.get(2));
			if (single_row.get(2).equals("2001")) {
				rev2001.add(Double.parseDouble(single_row.get(0)));
				month2001.add(Integer.parseInt(single_row.get(1)));
			} else if (single_row.get(2).equals("2002")) {
				rev2002.add(Double.parseDouble(single_row.get(0)));
				month2002.add(Integer.parseInt(single_row.get(1)));
			} else if (single_row.get(2).equals("2003")) {
				rev2003.add(Double.parseDouble(single_row.get(0)));
				month2003.add(Integer.parseInt(single_row.get(1)));
			} else if (single_row.get(2).equals("2004")) {
				rev2004.add(Double.parseDouble(single_row.get(0)));
				month2004.add(Integer.parseInt(single_row.get(1)));
			}
			
		}
		System.out.println(rev2001.toString());
		System.out.println(month2001.toString());
	    // Series
	    chart2.addSeries("2001", month2001, rev2001);
	    chart2.addSeries("2002", month2002, rev2002);
	    chart2.addSeries("2003", month2003, rev2003);
	    chart2.addSeries("2004", month2004, rev2004);
		
		///////////////////////////
		// Gender Ratio employee //
		///////////////////////////
	    PieChart chart3 =
            new PieChartBuilder()
                .width(800)
                .height(600)
                .title("Pie Chart GGPlot2 Theme")
                .theme(ChartTheme.GGPlot2)
                .build();

        // Customize Chart
        chart3.getStyler().setLegendVisible(false);
        chart3.getStyler().setAnnotationDistance(1.15);
        chart3.getStyler().setPlotContentSize(.7);
        chart3.getStyler().setStartAngleInDegrees(90);
        
        String output3 = DatabaseManager.handleSQLCommand("select Gender, count(Gender) from employee group by Gender;");
        int male = 0;
        int female = 0;
        System.out.println(output3);
        String line3[] = output3.split("\n");
		for (String token : line3) {
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
			if (single_row.get(0).equals("F")) {
				female = Integer.parseInt(single_row.get(1));
			} else if (single_row.get(0).equals("M")) {
				male = Integer.parseInt(single_row.get(1));
			}
		}
        // Series
        chart3.addSeries("Male", male);
        chart3.addSeries("Female", female);
        
        
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				
				// Create and set up the window.
//				JFrame frame2 = new JFrame();
			    frame.setLayout(new BorderLayout());
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    
			    

			    
			    // chart
			    JPanel chartPanel1 = new XChartPanel<CategoryChart>(chart1);
			    JPanel chartPanel2 = new XChartPanel<XYChart>(chart2);
			    JPanel chartPanel3 = new XChartPanel<PieChart>(chart3);
//			    mainPanel.add(chartPanel);
			    frame.add(chartPanel1, BorderLayout.CENTER);
			    frame.add(chartPanel2, BorderLayout.PAGE_END);
			    frame.add(chartPanel3, BorderLayout.WEST);
			    // label
//			    JLabel label = new JLabel("Blah blah blah.", SwingConstants.CENTER);
//			    frame.add(label, BorderLayout.SOUTH);

			    // Display the window.
			    frame.pack();
			    frame.setVisible(true);
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
//		if (mouse.getSource() == yearRev) {
//			CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Revenue by Year").xAxisTitle("Year").yAxisTitle("Revenue").build();
//			
//			
//			chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
//			chart.getStyler().setHasAnnotations(true);
//			chart.getStyler().setPlotGridLinesVisible(false);
//			
//			String output = DatabaseManager.handleSQLCommand("select sum(LineTotal), year(ModifiedDate) from salesorderdetail group by year(ModifiedDate);");
//			System.out.println(output);
//			
//			List<Double> rev = new ArrayList<Double>();
//			List<Integer> year = new ArrayList<Integer>();
//			
//			String line[] = output.split("\n");
//			for (String token : line) {
//				token = token.replace("{", "");
//				token = token.replace("}", "");
//
//				String row[] = token.split(",[a-zA-Z0-9 ]*[^,]*="); //FIXME: not work for column has , in their data. can fix by split using regex
//				List<String> single_row = new ArrayList<String>();
//				for (String rowToken : row) {
//					System.out.println(rowToken);
//					String elem[] = rowToken.split("=");
//					if (elem.length > 1)
//						single_row.add(elem[1]);
//					else
//						single_row.add(elem[0]);
//				}
//				rev.add(Double.parseDouble(single_row.get(0)));
//				year.add(Integer.parseInt(single_row.get(1)));
//			}
//			
//			chart.addSeries("Revenue by Year", year, rev);
//			
//			javax.swing.SwingUtilities.invokeLater(new Runnable() {
//				@Override
//				public void run() {
//					
//					
//					// Create and set up the window.
//					JFrame frame2 = new JFrame();
//				    frame2.setLayout(new BorderLayout());
//				    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//				    // chart
//				    JPanel chartPanel = new XChartPanel<CategoryChart>(chart);
//				    frame2.add(chartPanel, BorderLayout.CENTER);
//
//				    // label
//				    JLabel label = new JLabel("Blah blah blah.", SwingConstants.CENTER);
//				    frame2.add(label, BorderLayout.SOUTH);
//
//				    // Display the window.
//				    frame2.pack();
//				    frame2.setVisible(true);
//				}
//			});
//			
//		}
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
