package com.phoenixjcam.application.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatterClient extends JFrame {

    private static final long serialVersionUID = 1L;
    private final int width = 640;
    private final int height = 480;

    private String serverIP;
    private Socket connectionSocket;
    private int port;

    private JScrollPane scPane;
    private JTextArea txtArea;

    public ChatterClient(String serverIP, int port) {
	super("Chatter Client");

	this.serverIP = serverIP;
	this.port = port;

	scPane = new JScrollPane(getTxtArea());
	add(scPane, BorderLayout.CENTER);

	frameSettings();
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);
    }

    private void frameSettings() {
	setSize(width, height);
	Point centerPoint = GraphicsEnvironment.getLocalGraphicsEnvironment()
		.getCenterPoint();
	setLocation(900, (centerPoint.y) - (height / 2));
    }

    private JTextArea getTxtArea() {
	txtArea = new JTextArea();
	txtArea.setFont(new Font("Arial", 0, 20));
	return txtArea;
    }

    public void runClient() {

	txtArea.setText("Attempting connection");

	try {
	    InetAddress address = InetAddress.getByName(serverIP);
	    connectionSocket = new Socket(address, port);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	String hostName = connectionSocket.getInetAddress().getHostName();
	txtArea.setText("connected to " + hostName + "\n");

	byte[] b = new byte[10];

	for (int i = 0; i < b.length; i++) {
	    try {
		connectionSocket.getInputStream().read(b);
		txtArea.append("byte read " + b[i] + "\n");
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    public static void main(String[] args) {

	int port = 9002;
	String serverIP = "127.0.0.1";

	new ChatterClient(serverIP, port).runClient();
    }
}
