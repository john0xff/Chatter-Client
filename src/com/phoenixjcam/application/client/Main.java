package com.phoenixjcam.application.client;

public class Main
{
	public static void main(String[] args)
	{
		ClientGUI clientGUI = new ClientGUI();

		int port = 9002;
		String host = "127.0.0.1";

		new ClientSide(host, port, clientGUI);
		
		
		// new Thread(new ClientSide(host, port, clientGUI)).start();
	}
}
