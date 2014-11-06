package clientSide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSide
{
	private final static String NEW_LINE = "\n";
	private final static String END_OF_CONV = "END";

	private ClientGUI clientGUI;
	private Socket clientSocket;

	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	private boolean isOpen = false;

	public ClientSide(String host, int port, ClientGUI clientGUI)
	{
		try
		{
			this.clientGUI = clientGUI;
			clientSocket = new Socket(host, port);
			isOpen = true;

			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// ActionListener runs in separated thread so on each user typing it'll do the trick
		clientGUI.getUserText().addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String clientMsg = e.getActionCommand();
				clientGUI.getTextArea().append(clientMsg + NEW_LINE);

				if(clientMsg.equals(END_OF_CONV))
				{
					isOpen = false;
					clientGUI.getTextArea().append("Now you can close the application.");
					clientGUI.getUserText().setEnabled(false);
					clientGUI.getTextArea().setEnabled(false);
				}
				
				try
				{
					objectOutputStream.writeObject(clientMsg);
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});

		while (true)
		{
			// setup msg from other clients and append them to main txt area
			try
			{
				String broadcastMsg = objectInputStream.readObject().toString();

				clientGUI.getTextArea().append(broadcastMsg + NEW_LINE);
				
				if(!isOpen)
					break;
			
			}
			catch (ClassNotFoundException | IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}

}
