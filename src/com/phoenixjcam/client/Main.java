package com.phoenixjcam.client;

import javax.swing.JOptionPane;

public class Main
{
	public static void main(String[] args)
	{
		ClientGUI clientGUI = new ClientGUI();
		
		String tmpHost = "phoenixjcam.no-ip.biz";
		String label = "Host name: localhost/127.0.0.1/phoenixjcam.no-ip.biz";
		String givenHost = JOptionPane.showInputDialog(clientGUI.getFrame(), label, tmpHost);
		
		String givenPort = JOptionPane.showInputDialog(clientGUI.getFrame(), "Type port (9002) or similar", 9002);

		int port = Integer.valueOf(givenPort);

		//String host = "127.0.0.1";
		// http://83.26.86.238/

		new ClientSide(givenHost, port, clientGUI);
		
		
		// new Thread(new ClientSide(host, port, clientGUI)).start();
	}
}
