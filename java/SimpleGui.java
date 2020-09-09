// From: https://www.guru99.com/java-swing-gui.html

// Choe: very simple swing example. Again, no event handling included.

import javax.swing.*;
class SimpleGui{
    public static void main(String args[]){
       JFrame frame = new JFrame("My First GUI");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(300,300);
       JButton button1 = new JButton("Press");
       frame.getContentPane().add(button1); // Adds Button to content pane of frame
       frame.setVisible(true);
    }
}
