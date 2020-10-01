package main;

import java.awt.Color;
import java.awt.Dimension;
//Standard Library Imports
	import java.awt.event.KeyEvent;
	import java.awt.event.KeyListener; 
	
	import java.awt.event.MouseEvent; 
	import java.awt.event.MouseListener;
	
	import java.awt.event.MouseWheelEvent;
	import java.awt.event.MouseWheelListener;
	
	import java.awt.event.WindowAdapter;
	import java.awt.event.WindowEvent;
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
	private static JLabel label1;
	private static JButton button1;
	private static JTextField textfield1;
	
	
	
	public GUIInterface() {
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
		frame.setSize(350, 350);
		frame.setVisible(true);
		this.repaint();
	}
	public static void main(String[]args) {
//		new Dashboard();
		
		GUIInterface gui = new GUIInterface();
//		panel = new JPanel();
		
		frame.add(gui);
		gui.setLayout(null);
		
		label1 = new JLabel("Testing");
		label1.setBounds(10, 20, 80, 25);
		gui.add(label1);
		
		textfield1 = new JTextField(20);
		textfield1.setBounds(10, 50, 150, 25);
		gui.add(textfield1);
		
		button1 = new JButton("jdb-find-column");
		button1.setBounds(10, 80, 200, 25);
		button1.addMouseListener(new GUIInterface());
		gui.add(button1);
		
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
		if (mouse.getSource() == button1) { // jdb-find-column
			DatabaseManager.openConnection();
			DatabaseManager.queryDatabase("use adventureworks;");
			
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
	
			DatabaseManager.closeConnection();
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
