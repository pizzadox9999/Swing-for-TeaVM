package org.tgui;

import org.tgui.applet.Applet;
import org.tgui.awt.Button;
import org.tgui.awt.event.ActionEvent;
import org.tgui.awt.event.ActionListener;
import org.tgui.swing.JButton;
import org.tgui.swing.JFrame;

public class Driver {
	public static void main(String[] args) {
		
		JFrame frame=new JFrame("testTitle:-)");
		frame.setSize(200, 200);
		JButton myButton=new JButton("Click me!");
		myButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.out.println(ae.getActionCommand());
				System.out.println(ae.getID());
			}
		});
		frame.add(myButton);
		frame.setVisible(true);
		//UI.main(args);
	}
}