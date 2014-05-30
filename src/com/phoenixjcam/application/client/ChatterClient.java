package com.phoenixjcam.application.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatterClient extends JFrame
{

	private static final long serialVersionUID = 1L;
	private final int width = 640;
	private final int height = 480;

	// connection
	private String serverIP;
	private int port;
	private Socket connectionSocket;

	// streams
	private ObjectOutputStream output;
	private ObjectInputStream input;

	// components
	private JScrollPane scrollPane;
	private JTextArea chatArea;
	private JTextField userText;

	private String message;
	private final static String NEWLINE = "\n";
	/** exit command */
	private final static String EXITCMD = "\nSERVER - EXIT";

	public ChatterClient(String serverIP, int port)
	{
		super("Chatter Client");

		this.serverIP = serverIP;
		this.port = port;

		scrollPane = new JScrollPane(getChatArea());
		add(scrollPane, BorderLayout.CENTER);

		add(getUserText(), BorderLayout.NORTH);

		frameSettings();
		runClient();
	}

	private void frameSettings()
	{
		setSize(width, height);
		Point centerPoint = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		setLocation(900, (centerPoint.y) - (height / 2));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private JTextArea getChatArea()
	{
		chatArea = new JTextArea();
		chatArea.setFont(new Font("Arial", 0, 20));
		return chatArea;
	}

	/** send client text to server and append client text to client chat area */
	private JTextField getUserText()
	{
		userText = new JTextField();
		userText.setFont(new Font("Arial", 0, 20));

		userText.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent event)
			{
				String serverMessage = event.getActionCommand();

				try
				{
					// send client text to server side
					output.writeObject(NEWLINE + "CLIENT - " + serverMessage);
					output.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				// append client text to area
				chatArea.append(NEWLINE + "CLIENT - " + serverMessage);

				userText.setText("");
			}
		});

		return userText;
	}

	public void runClient()
	{

		chatArea.append("Attempting connection" + NEWLINE);

		try
		{
			// InetAddress address = InetAddress.getByName(serverIP);
			connectionSocket = new Socket(serverIP, port);

			chatArea.append("connected to " + serverIP + NEWLINE);

			output = new ObjectOutputStream(connectionSocket.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connectionSocket.getInputStream());

			do
			{
				message = (String) input.readObject();

				chatArea.append(message);
			}
			while (!message.equals(EXITCMD));

			closeStreams();
			closeSocket();
			// ? dispose();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String hostName = connectionSocket.getInetAddress().getHostName();
		chatArea.setText("connected to " + hostName + NEWLINE);
	}

	private void closeStreams()
	{
		try
		{
			output.close();
			input.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void closeSocket()
	{
		try
		{
			connectionSocket.close();
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		String serverIP = "127.0.0.1";
		int port = 9002;

		new ChatterClient(serverIP, port);
	}
}
