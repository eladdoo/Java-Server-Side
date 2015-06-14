package presenter;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import view.View;
import Messages.MessageType;
import model.Model;
import model.MyServer;

/**
 * The Class Presenter.
 */
public class Presenter implements Observer
{
	
	/** The model. */
	private Model m;
	
	/** The view. */
	private View v;
	
	private MyServer server;
	
	/**
	 * Instantiates a new presenter.
	 *
	 * @param m the m
	 * @param v the v
	 */
	public Presenter(Model m, View v,MyServer s) 
	{
		super();
		this.m = m;
		this.v = v;
		this.server = s;
	}

	/**
	 * update for observer
	 */
	@SuppressWarnings("incomplete-switch")
	@Override
	public void update(Observable o, Object arg) 
	{
		if (o==v) //if its view
		{
			MessageType n = (MessageType)arg;
			switch (n)
			{
			case EXIT:
				m.stop();
				server.stopServer();
				break;
			case START:
				server.startServer();
				break;
			case Remove:
				if (server.getUsers().containsKey(v.GetRemoveU())==true)
				{
					Socket rmov = server.getUsers().get(v.GetRemoveU());
					try 
					{
						rmov.close();
						server.getUsers().remove(v.GetRemoveU());
						v.updateShowU(server.getUsers().keySet());
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				else
				{
					v.UpdateAdmin("The User Doesnt Exist");
				}
				break;
			}
		}
		else if (o==m)//its model
		{
			MessageType t = (MessageType)arg;
			switch (t)
			{
			case task:
				v.updateShowT(m.gettingTask());
				break;
			case EXIT:
				server.stopServer();
				break;
			}
		}
		else //its server
		{
			MessageType t = (MessageType)arg;
			switch (t)
			{
			case newU:
				v.updateShowU(server.getUsers().keySet());
				break;
			}
		}
	}

}
