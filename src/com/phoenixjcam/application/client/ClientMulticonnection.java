package com.phoenixjcam.application.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMulticonnection
{
	private final static String NEWLINE = "\n";
	private final static String CLEAR = "";
	private ClientGUI clientGUI;
	private int port;
	private String host;
	private Socket clientSocket;

	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	public ClientMulticonnection(int port, String host)
	{
		clientGUI = new ClientGUI();
		this.port = port;
		this.host = host;

		clientGUI.getUserText().addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String msg = e.getActionCommand();

				try
				{
					String clientMsg = clientGUI.currentTime() + " Client: " + msg + NEWLINE;// prepare full msg from
																								// client to display in
																								// both
					objectOutputStream.writeObject(clientMsg);
					clientGUI.getTextArea().append(clientMsg);
					clientGUI.getUserText().setText(CLEAR);
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});

		try
		{
			clientSocket = new Socket(host, port);
			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
			clientGUI.getUserText().setEnabled(true);

			String msg = null;
			do
			{
				try
				{
					msg = objectInputStream.readObject().toString();
					clientGUI.getTextArea().append(msg);
				}
				catch (ClassNotFoundException e1)
				{
					e1.printStackTrace();
				}
			}
			while (msg != "END");
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			shutdownStreams();
			closeSockets();
		}
	}

	private void shutdownStreams()
	{
		try
		{
			clientSocket.shutdownOutput();
			clientSocket.shutdownInput();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	private void closeSockets()
	{
		try
		{
			clientSocket.close();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	public static void main(String... args)
	{
		new ClientMulticonnection(9000, "localhost");
	}
}
