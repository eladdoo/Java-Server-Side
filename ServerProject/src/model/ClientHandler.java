package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The Interface ClientHandler.
 */
public interface ClientHandler 
{
	
	/**
	 * Handle client.
	 *
	 * @param in the in
	 * @param out the out
	 */
	public void handleClient(ObjectInputStream in,ObjectOutputStream out);
}
