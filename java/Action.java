// From https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html
// Choe: This is a simple app, but it includes event handling, so it could
// be a good starting point. Caveat: This example does not include swing.
// It'd be much easier to use swing when creating the UI.

import java.awt.*;
import java.awt.event.*;

public class Action extends Frame implements WindowListener,ActionListener {
        TextField text = new TextField(20);
        Button b;
        private int numClicks = 0;

        public static void main(String[] args) {
                Action myWindow = new Action("My first window");
                myWindow.setSize(350,100);
                myWindow.setVisible(true);
        }

        public Action(String title) {

                super(title);
                setLayout(new FlowLayout());
                addWindowListener(this);
                b = new Button("Click me");
                add(b);
                add(text);
                b.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
                numClicks++;
                text.setText("Button Clicked " + numClicks + " times");
        }

        public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
        }

        public void windowOpened(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}

}
