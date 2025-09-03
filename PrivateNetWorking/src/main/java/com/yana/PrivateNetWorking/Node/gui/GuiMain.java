package com.yana.PrivateNetWorking.Node.gui;

import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GuiMain extends JFrame{
	private static final long serialVersionUID = 8069487455277048870L;
	private static final int FRAME_WIDTH = 200;
	private static final int FRAME_HEIGHT = 200;

	private JPanel panel;
	private JTextField textField;
	private GuiMain() {
		// Frame
		this.setLayout(null);
		this.setBounds(10, 10, FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Pannel
		Insets insets = this.getInsets();
		int inLeft = insets.left;
		int inTop = insets.top;
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(inLeft, inTop, FRAME_WIDTH - inLeft, FRAME_HEIGHT - inTop);

		int buttonSize = 50;
		// creat textField
		textField = new JTextField();
		textField.setBounds(0, 0, FRAME_WIDTH - buttonSize, 50);
		panel.add(textField);

		getContentPane().add(panel);
		
		//this.paint(this.getGraphics());
		this.repaint();
		this.setVisible(true);
	}

	public static void start() {
		new GuiMain();
	}
}
