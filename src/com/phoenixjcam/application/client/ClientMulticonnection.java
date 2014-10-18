package com.phoenixjcam.application.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientMulticonnection
{
	private int port;
	private String host;
	private Socket clientSocket;

	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	private JFrame frame;
	private JTextField userText;
	private JTextArea textArea;

	public ClientMulticonnection(int port, String host)
	{
		this.port = port;
		this.host = host;

		frame = new JFrame("Client");

		userText = new JTextField();
		userText.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String msg = e.getActionCommand();

				try
				{
					objectOutputStream.writeObject(msg);
					textArea.append("Client: " + msg + "\n");
				}
				catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		userText.setEnabled(false);
		frame.add(userText, BorderLayout.NORTH);

		textArea = new JTextArea();
		textArea.setEnabled(false);
		frame.add(textArea, BorderLayout.CENTER);

		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		try
		{
			clientSocket = new Socket(host, port);
			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
			userText.setEnabled(true);

			String msg = null;
			do
			{
				try
				{
					msg = objectInputStream.readObject().toString();
					textArea.append("Server: " + msg + "\n");
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
			try
			{
				clientSocket.shutdownOutput();
				clientSocket.shutdownInput();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try
			{
				clientSocket.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}

	}

	public static void main(String... args)
	{
		new ClientMulticonnection(9000, "localhost");
	}
}
