package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Messages.MessageType;
import config.HandelProperties;
import config.Properties;


/**
 * The Class MyServer.
 */
public class MyServer extends Observable
{
	
	/** The port. */
	private int port;
	
	/** The time out of server. */
	private int timeOut;
	
	/** The num of clients. */
	private int numOfClients;
	
	/** The stopped. */
	private volatile boolean stopped;
	
	/** The client handler. */
	ClientHandler ch;
	
	/** The users. */
	private HashMap<String,Socket> users = new HashMap<String,Socket>();
	
	/**
	 * Instantiates a new my server.
	 *
	 * @param client handler the cli
	 */
	public MyServer(ClientHandler cli)
	{
		HandelProperties h = new HandelProperties();
		Properties p = h.ReadProperties();
		this.port = p.getPort();
		this.timeOut = p.getServTimeOut();
		this.numOfClients = p.getNumOfClients();
		this.ch = cli;
		this.stopped = false;
	}
	
	/**
	 * Start server.
	 */
	public void startServer()
	{
		ServerSocket server;
		try 
		{
			server = new ServerSocket(port);
			server.setSoTimeout(timeOut);
			ExecutorService threadPool = Executors.newFixedThreadPool(numOfClients);
			Thread t = new Thread(new Runnable() //putting the accept in thread so wont interrupt GUI
			{
				@Override
				public void run()
				{
					while (!stopped)
					{
						try
						{	
							final Socket someclient = server.accept(); //accept new client
							threadPool.execute(new Runnable()
							{
								@Override
								public void run() 
								{
									try 
									{
										ObjectOutputStream out = new ObjectOutputStream(someclient.getOutputStream());
										out.flush();
										ObjectInputStream in = new ObjectInputStream(someclient.getInputStream());
										String user = in.readUTF(); //reading user name
										boolean flag = false;
										if(users.containsKey(user)) //if user already exist
										{
											out.writeUTF("Error");
											out.flush();
										}
										else
										{
											users.put(user,someclient); //putting the new user in users map
											out.writeUTF("done");
											out.flush();
											flag=true;
										}
										if (flag==true) //if user logged in successfully
										{
											setChanged();
											notifyObservers(MessageType.newU); //for updating the View to show users with the new user
											ch.handleClient(in,out);
										}
										for (String u:users.keySet()) //remove user from map after done
										{
											if (users.get(u)==someclient)
											{
												users.remove(u);
												break;
											}
										}
										setChanged();
										notifyObservers(MessageType.newU); //for updating the View to show users without the user that disconnected
										someclient.close();
									} 
									catch (IOException e) 
									{
										e.printStackTrace();
									}
								}
							});
						}
						catch (IOException e) {}
					}
					stopServer(); //stop server after main loop
					threadPool.shutdown(); //close server
					try 
					{
						server.close();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				
			});
			t.start();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
	
	/**
	 * Stop server method.
	 */
	public void stopServer()
	{
		this.stopped = true;
	}

	/**
	 * Gets the users.
	 *
	 * @return the users
	 */
	public HashMap<String, Socket> getUsers() 
	{
		return users;
	}

	/**
	 * Sets the users.
	 *
	 * @param users the users
	 */
	public void setUsers(HashMap<String, Socket> users)
	{
		this.users = users;
	}
	
}
