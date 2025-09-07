package com.yana.PrivateNetWorking.Node.gui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.yana.PrivateNetWorking.Node.localServer.httpServer.HttpServerCreateFactory;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.IHttpServer;
import com.yana.PrivateNetWorking.Node.localServer.httpServer.requetHandle.RequestHandleWorkerOperator;
import com.yana.PrivateNetWorking.Node.privateNetWorker.PrivateNetSocketCreator;
import com.yana.PrivateNetWorking.Node.privateNetWorker.analyzer.INodeAnalyzer;
import com.yana.PrivateNetWorking.Node.privateNetWorker.analyzer.NodeAnalyzerFactory;
import com.yana.PrivateNetWorking.Node.privateNetWorker.command.CommandOperator;
import com.yana.PrivateNetWorking.Node.shutDownIF.InvokeShutDownEvent;
import com.yana.privateNetSocket2.PrivateNetSocket;

public class GuiController extends JFrame implements ActionListener {
	private static final long serialVersionUID = 2017449285968090315L;
	private static final int FRAME_WIDTH = 250;
	private static final int FRAME_HEIGHT = 100;
	private static final String COMMAND_START = "START";
	private static final String COMMAND_END = "END";

	private JPanel panel;
	private JButton serviceStartButton;
	private JButton serviceEndButton;
	private JTextField filed;

	//PrivateNetWork relation field
	private PrivateNetSocket privateNetSocket;
	private GuiController() {
		this.setLayout(null);
		this.setBounds(10, 10, FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle("privateNet");
		Insets insets = this.getInsets();
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.GREEN);
//		panel.setBounds(insets.left, insets.top, FRAME_WIDTH - insets.left - insets.right, 
//				FRAME_HEIGHT - insets.top - insets.bottom);
		panel.setBounds(0, 0, FRAME_WIDTH - insets.left - insets.right, 
				FRAME_HEIGHT - insets.top - insets.bottom);
		serviceStartButton = new JButton("start");
		serviceStartButton.setBounds(0, 0, panel.getSize().width / 2, 30);
		serviceStartButton.setActionCommand(COMMAND_START);
		serviceStartButton.addActionListener(this);
		panel.add(serviceStartButton);

		serviceEndButton = new JButton("end");
		serviceEndButton.setBounds(panel.getSize().width / 2, 0, panel.getSize().width / 2, 30);
		serviceEndButton.setActionCommand(COMMAND_END);
		serviceEndButton.addActionListener(this);
		panel.add(serviceEndButton);

		filed = new JTextField();
		filed.setBounds(0, 30, panel.getSize().width, 20);
		panel.add(filed);

		this.getContentPane().add(panel);
	}

	public static GuiController displayOpen() {
		GuiController guiController = new GuiController();
		InvokeShutDownEvent.getInstance().setGuiController(guiController);
		guiController.repaint();
		return guiController;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(COMMAND_START.equals(e.getActionCommand())) {
			joinNetWorking();
		}
		if(COMMAND_END.equals(e.getActionCommand())) {
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}

	public void invokeExitEvent() {
		serviceEndButton.doClick();
	}

	private boolean joinNetWorking() {
		try {
			//Private NetWork Analyzer
			PrivateNetSocket privateNetSocket = PrivateNetSocketCreator.create();
			if(privateNetSocket == null) {
				return false;
			}
			int analyzerThreadCount = 2;
			ExecutorService analyzerExecutors = Executors.newFixedThreadPool(analyzerThreadCount);
			for(int i = 0; i < analyzerThreadCount; i++) {
				INodeAnalyzer nodeAnalyzer = NodeAnalyzerFactory.createNodeAnalyzer();
				nodeAnalyzer.setPrivateNetSocket(privateNetSocket);
				analyzerExecutors.execute(nodeAnalyzer);
			}

			//CommandOpreator
			if(!CommandOperator.readyCommandOperator(privateNetSocket)) {
				return false;
			}

			IHttpServer server = HttpServerCreateFactory.createHttpServer();
			filed.setText("http://localhost:" + server.getLocalPort());
			RequestHandleWorkerOperator requestHandleWorkerOperator = new RequestHandleWorkerOperator(server, 10);
			requestHandleWorkerOperator.workerStart();

			this.privateNetSocket = privateNetSocket;
		} catch(IOException ex) {
			return false;
		}
		return true;
	}

	public PrivateNetSocket getPrivateNetSocket() {
		return this.privateNetSocket;
	}
}
