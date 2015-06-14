package config;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Properties.
 */

public class Properties implements Serializable
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The tnum. */
	private int TnumForMaze;
	
	/** The algo solution. */
	private String algoSolution;
	
	/** The algo cr maze. */
	private String algoCrMaze;
	
	/** The Huristic. */
	private String Huristic;
	
	/** The port. */
	private int port;
	
	/** The Num of clients. */
	private int NumOfClients;
	
	/** The ip. */
	private String ip;
	
	/** The Serv time out. */
	private int ServTimeOut;
	
	/**
	 * Instantiates a new properties.
	 */
	public Properties() 
	{
		super();
		this.TnumForMaze = 2;
		this.algoSolution = "BFS";
		this.algoCrMaze = "DFS";
		this.Huristic = "AirDistance";
		this.port = 1234;
		this.NumOfClients = 5;
		this.ip = "127.0.0.1";
		this.ServTimeOut = 5000;
	}
	
	/**
	 * Instantiates a new properties.
	 *
	 * @param TnumForMaze the tnum for maze
	 * @param algoSolution the algo solution
	 * @param algoCrMaze the algo cr maze
	 * @param huristic the huristic
	 * @param NumOfClients the num of clients
	 * @param port the port
	 * @param ip the ip
	 * @param ServTimeOut the serv time out
	 */
	public Properties(int TnumForMaze, String algoSolution, String algoCrMaze,
			String huristic,int NumOfClients,int port,String ip,int ServTimeOut) 
	{
		super();
		this.TnumForMaze = TnumForMaze;
		this.algoSolution = algoSolution;
		this.algoCrMaze = algoCrMaze;
		this.Huristic = huristic;
		this.NumOfClients = NumOfClients;
		this.port = port;
		this.ip = ip;
		this.ServTimeOut = ServTimeOut;
	}

	/**
	 * Gets the tnum for maze.
	 *
	 * @return the tnum for maze
	 */
	public int getTnumForMaze() 
	{
		return TnumForMaze;
	}

	/**
	 * Sets the tnum for maze.
	 *
	 * @param tnumForMaze the new tnum for maze
	 */
	public void setTnumForMaze(int tnumForMaze) 
	{
		TnumForMaze = tnumForMaze;
	}

	/**
	 * Gets the algo solution.
	 *
	 * @return the algo solution
	 */
	public String getAlgoSolution()
	{
		return algoSolution;
	}

	/**
	 * Sets the algo solution.
	 *
	 * @param algoSolution the new algo solution
	 */
	public void setAlgoSolution(String algoSolution)
	{
		this.algoSolution = algoSolution;
	}

	/**
	 * Gets the algo cr maze.
	 *
	 * @return the algo cr maze
	 */
	public String getAlgoCrMaze() 
	{
		return algoCrMaze;
	}

	/**
	 * Sets the algo cr maze.
	 *
	 * @param algoCrMaze the new algo cr maze
	 */
	public void setAlgoCrMaze(String algoCrMaze) 
	{
		this.algoCrMaze = algoCrMaze;
	}

	/**
	 * Gets the huristic.
	 *
	 * @return the huristic
	 */
	public String getHuristic() 
	{
		return Huristic;
	}

	/**
	 * Sets the huristic.
	 *
	 * @param huristic the new huristic
	 */
	public void setHuristic(String huristic) 
	{
		Huristic = huristic;
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() 
	{
		return port;
	}

	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * Gets the num of clients.
	 *
	 * @return the num of clients
	 */
	public int getNumOfClients()
	{
		return NumOfClients;
	}

	/**
	 * Sets the num of clients.
	 *
	 * @param numOfClients the new num of clients
	 */
	public void setNumOfClients(int numOfClients) 
	{
		NumOfClients = numOfClients;
	}

	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public String getIp() 
	{
		return ip;
	}

	/**
	 * Sets the ip.
	 *
	 * @param ip the new ip
	 */
	public void setIp(String ip) 
	{
		this.ip = ip;
	}

	/**
	 * Gets the serv time out.
	 *
	 * @return the serv time out
	 */
	public int getServTimeOut() 
	{
		return ServTimeOut;
	}

	/**
	 * Sets the serv time out.
	 *
	 * @param servTimeOut the new serv time out
	 */
	public void setServTimeOut(int servTimeOut) 
	{
		ServTimeOut = servTimeOut;
	}

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() 
	{
		return serialVersionUID;
	}

	
}
