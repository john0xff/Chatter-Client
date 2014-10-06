package com.phoenixjcam.chat.client;

import java.io.IOException;
import java.net.Socket;

public class Client
{
	private Socket connectionSocket;
	private int port;
	private String host = "127.0.0.1";

	public Client(int port)
	{
		this.port = port;

		try
		{
			connectionSocket = new Socket(host, port);
			System.out.println("client connected to server");
			
			Thread.sleep(3000);
			
			//communication();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				connectionSocket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			System.out.println("client disconnected");
		}
	}
	
	private void communication()
	{

		for (int i = 0; i < 100; i++)
		{
			try
			{
				Thread.sleep(1000);
				System.out.println("client loop nr: " + i);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args)
	{
		int port = 9005;

		Client client = new Client(port);
	}
}
