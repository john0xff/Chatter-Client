package com.phoenixjcam.application.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * branche v2.0-multiThread
 * 
 * @author Bart Bien
 * 
 */
public class ChatterClient extends JFrame
{
	private static final long serialVersionUID = 5797811107125726563L;
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

	// time
	private Calendar calendar;
	private SimpleDateFormat dateFormat;
	private String currentTime;

	public ChatterClient(String serverIP, int port)
	{
		super("Chatter Client");

		this.serverIP = serverIP;
		this.port = port;

		dateFormat = new SimpleDateFormat("HH:mm:ss");

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

		//ImageIO.read
		ImageIcon img = new ImageIcon(ChatterClient.class.getResource("res/icoB.png"));
		setIconImage(img.getImage());

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
					calendar = Calendar.getInstance();
					currentTime = dateFormat.format(calendar.getTime());

					// send client text to server side
					output.writeObject(NEWLINE + currentTime + " CLIENT - " + serverMessage);
					output.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				// append client text to area
				chatArea.append(NEWLINE + currentTime + " CLIENT - " + serverMessage);

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
			//output.flush();
			input = new ObjectInputStream(connectionSocket.getInputStream());

			do
			{
				message = (String) input.readObject();

				chatArea.append(message);
			}
			while (!message.equals(EXITCMD));

			// ? dispose();
		}
		catch (IOException | ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			closeStreams();
			closeSocket();
		}

//		String hostName = connectionSocket.getInetAddress().getHostName();
//		chatArea.setText("connected to " + hostName + NEWLINE);
	}
	
	private void closeObject(Closeable object)
	{
		try
		{
			object.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void closeStreams()
	{
		closeObject(output);
		closeObject(input);
	}

	private void closeSocket()
	{
		closeObject(connectionSocket);
	}

	public static void main(String[] args)
	{
		String serverIP = "127.0.0.1";
		int port = 9002;

		new ChatterClient(serverIP, port);
	}
	

}
