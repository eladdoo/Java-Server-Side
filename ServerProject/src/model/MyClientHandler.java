package model;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import compression_algorithms.Bits;
import compression_algorithms.HuffmanAlg;
import config.HandelProperties;
import config.Properties;
import algorithms.demo.MazeDomain;
import algorithms.mazeGenerators.Cell;
import algorithms.mazeGenerators.DFSMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.RandomMazeGenerator;
import algorithms.search.AStar;
import algorithms.search.Action;
import algorithms.search.ActuallState;
import algorithms.search.AirDistance;
import algorithms.search.BFS;
import algorithms.search.ManhattanDistance;
import algorithms.search.Solution;
import Messages.MessageType;


/**
 * The Class MyClientHandler.
 */
public class MyClientHandler extends Observable implements ClientHandler,Model
{
	
	/** The problem. */
	private Problem pro;
	
	/** The saved m. */
	private HashMap<String,MazeDomain> savedM = new HashMap<String,MazeDomain>();
	/** The Tnum. */
	private int Tnum;
	
	/** The solution. */
	private Solution sol;
	
	/** The map. */
	private HashMap<Maze,Solution> map;
	
	/** The executor. */
	private ExecutorService executor;
	
	/** The new m. */
	private Queue<SolveProblem> soloQueue;
	
	/** The algo solution. */
	private String algoSolution;
	
	/** The algo maze. */
	private String algoMaze;
	
	/** The huris. */
	private String huris;
	
	/** The command. */
	private String command = "Default";
	
	/** The dealing. */
	private String dealing = "";
	
	
	/**
	 * Instantiates a new my client handler.
	 */
	public MyClientHandler()
	{
		this.map  = new HashMap<Maze,Solution>();
		this.soloQueue = new LinkedList<SolveProblem>();
		XMLDecoder decoder;
		try 
		{
			/**
			 * read server properties
			 */
			decoder = new XMLDecoder(new FileInputStream("ServPro.xml"));
			Properties p = (Properties)decoder.readObject();
			this.Tnum = p.getTnumForMaze();
			this.algoMaze = p.getAlgoCrMaze();
			this.algoSolution = p.getAlgoSolution();
			this.huris = p.getHuristic();
			executor = Executors. newFixedThreadPool(Tnum);
			this.LoadHuff();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * the main function to handel the client requestes
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleClient(ObjectInputStream in,ObjectOutputStream out)
	{
		try 
		{
			out.flush();
			while (this.command.equals("exit")==false)
			{
				HashMap<Bits, Character> dictRec=null;
				Bits byRec=null;
				/**
				 * read commands after coaded on client side with huffman
				 */
				while (dictRec==null && byRec==null)
				{
					try
					{
					dictRec= (HashMap<Bits, Character>) in.readObject(); 
					byRec = (Bits) in.readObject();
					}
					catch(SocketException s)
					{
						in.close();
						out.close();
					}
				}
				/**
				 * decoad the object problem to handel it
				 */
				pro = this.decoadHuff(dictRec,byRec);
				this.command = pro.getCommand();
				/**
				 * if the client ask to solve the maze
				 */
				if (pro.getCommand().equals("solveMaze"))
				{
					this.dealing = "Now Solving Maze For User: " + pro.getUser();
					this.setChanged();
					this.notifyObservers(MessageType.task);
					MazeDomain temp = new MazeDomain();
					temp.setDiagonal(savedM.get(pro.getName()).isDiagonal());
					temp.setM(pro.getM());
					this.solveMaze(pro.getName(),temp); //solve the maze and creat solution object
					ByteArrayInputStream b = this.coadHuff(this.soloQueue.poll()); //coad the solution with huffman
					ObjectInputStream getting = new ObjectInputStream(b);
					HashMap<Bits, Character> dict = (HashMap<Bits, Character>) getting.readObject(); 
					Bits bf = (Bits) getting.readObject();
					out.writeObject(dict);
					out.flush();
					out.writeObject(bf);
					out.flush();
				}
				/**
				 * if the client ask to generate new maze
				 */
				else if (pro.getCommand().equals("generateMaze"))
				{
					this.dealing = "Now Generating Maze For User: " + pro.getUser();
					this.setChanged();
					this.notifyObservers(MessageType.task);
					String[] res = pro.getName().split(" "); //split the client maze properties
					if (res[3].equals("Y"))
					{
						this.generateMaze(res[0],Integer.parseInt(res[1]), Integer.parseInt(res[2]),true);
					}
					else
					{
						this.generateMaze(res[0],Integer.parseInt(res[1]), Integer.parseInt(res[2]),false);
					}
					ByteArrayInputStream b = this.coadHuff(this.soloQueue.poll());
					ObjectInputStream getting = new ObjectInputStream(b);
					HashMap<Bits, Character> dict = (HashMap<Bits, Character>) getting.readObject(); 
					Bits bf = (Bits) getting.readObject();
					out.writeObject(dict);
					out.flush();
					out.writeObject(bf);
					out.flush();
				}
				/**
				 * if the client ask to display maze
				 */
				else if (pro.getCommand().equals("displayMaze"))
				{
					this.dealing = "Now Sending The Maze To Display It For User: " + pro.getUser();
					this.setChanged();
					this.notifyObservers(MessageType.task);
					if (savedM.containsKey(pro.getName())==false)// if the client asked to display maze that doesnt exist
					{
						SolveProblem slv = new SolveProblem();
						slv.setM(null);
						slv.setMessage("Error");
						slv.setSol(null);
						ByteArrayInputStream b = this.coadHuff(slv);
						ObjectInputStream getting = new ObjectInputStream(b);
						HashMap<Bits, Character> dict = (HashMap<Bits, Character>) getting.readObject(); 
						Bits bf = (Bits) getting.readObject();
						out.writeObject(dict);
						out.flush();
						out.writeObject(bf);
						out.flush();
					}
					else
					{
						SolveProblem solv = new SolveProblem();
						solv.setMessage(pro.getName());
						solv.setM(this.getMaze(pro)); //setting the maze that the user asked on solution object
						solv.setSol(null);
						ByteArrayInputStream b = this.coadHuff(solv);
						ObjectInputStream getting = new ObjectInputStream(b);
						HashMap<Bits, Character> dict = (HashMap<Bits, Character>) getting.readObject(); 
						Bits bf = (Bits) getting.readObject();
						out.writeObject(dict);
						out.flush();
						out.writeObject(bf);
						out.flush();
					}
					
				}
				/**
				 * if the user asked to load properties for server
				 */
				if (pro.getFname()!=null)
				{
					this.dealing = "Now Loading Properties For User: " + pro.getUser();
					this.setChanged();
					this.notifyObservers(MessageType.task);
					boolean flag = this.loadXmlFill(pro.getFname());
					SolveProblem solv = new SolveProblem();
					solv.setM(null);
					solv.setSol(null);
					if (flag)
					{
						solv.setMessage("loaded");
					}
					else
					{
						solv.setMessage("error");
					}
					ByteArrayInputStream b = this.coadHuff(solv);
					ObjectInputStream getting = new ObjectInputStream(b);
					HashMap<Bits, Character> dict = (HashMap<Bits, Character>) getting.readObject(); 
					Bits bf = (Bits) getting.readObject();
					out.writeObject(dict);
					out.flush();
					out.writeObject(bf);
					out.flush();
				}
				/**
				 * setting the loop readers null so they will wait for command again
				 */
				dictRec=null; 
				byRec=null;
			}
			this.setChanged();
			this.notifyObservers(MessageType.EXIT);
			
		} 
		catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		
	}
	/* 
	 * the generate maze method
	 */
	@Override
	public void generateMaze(String name, int rows, int colls, boolean diag)
	{
		Future<MazeDomain> f;
		if (this.algoMaze == "DFS")
		{
			f = executor.submit (new MazeCallable(rows,colls,new DFSMazeGenerator(),diag));
		}
		else
		{
			f = executor.submit (new MazeCallable(rows,colls,new RandomMazeGenerator(),diag));
		}
		
		try 
		{
			SolveProblem solut = new SolveProblem();
			this.savedM.put(name,f.get());
			solut.setMessage(name);
			solut.setM(null);
			solut.setSol(null);
			this.soloQueue.add(solut); //setting the solution in the queue 
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		} 
		catch (ExecutionException e) 
		{
			e.printStackTrace();
		}
	}

	/* 
	 * getting maze with the maze name
	 */
	@Override
	public MazeDomain getMaze(Problem p) 
	{
		return this.savedM.get(p.getName());
	}

	/**
	 * solve the maze method
	 */
	@Override
	public void solveMaze(String name, MazeDomain m) 
	{
			if (map.containsKey(m)) //if the solution is in cache
			{
				System.out.println("solving maze is ready already...");
				SolveProblem solv = new SolveProblem();
				solv.setSol(map.get(m));
				solv.setMessage(name);
				solv.setM(null);
				this.soloQueue.add(solv);
				return;
			}
		Future<Solution> f;
		if (this.algoSolution == "BFS")
		{
			f = executor.submit (new SolutionCallable(m,new BFS()));
		}
		else
		{
			if (this.huris == "AirDistance")
			{
				f = executor.submit(new SolutionCallable(m,new AStar(new AirDistance())));
			}
			else
			{
				f = executor.submit(new SolutionCallable(m,new AStar(new ManhattanDistance())));
			}
		}
		try
		{
			sol = f.get();
			SolveProblem solv = new SolveProblem();
			solv.setMessage(name);
			solv.setSol(sol);
			solv.setM(null);
			map.put(m.getM(),sol);
			this.soloQueue.add(solv); //putting the solution in queueu
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (ExecutionException e) 
		{
			e.printStackTrace();
		} 
	}

	/**
	 * stop model (client handler
	 */
	@Override
	public void stop() 
	{
		if (map.size()!=0) //save the cache to file if its not empty
		{
			this.SaveHuff();
		}
		this.executor.shutdown();
		this.setChanged();
	}

	/**
	 * load properties file
	 */
	@Override
	public boolean loadXmlFill(String file) 
	{
		HandelProperties h = new HandelProperties(file);
		Properties p = h.ReadProperties();
		if (p!=null)
		{
			this.Tnum = p.getTnumForMaze();
			this.algoMaze = p.getAlgoCrMaze();
			this.algoSolution = p.getAlgoSolution();
			this.huris = p.getHuristic();
			executor = Executors. newFixedThreadPool(Tnum);
			return true;
		}
		return false;
	}
	
	/**
	 * Decoad huff.
	 *
	 * @param dict the dict
	 * @param bit the bit
	 * @return the problem
	 */
	public Problem decoadHuff(HashMap<Bits, Character> dict,Bits bit)
	{
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		HuffmanAlg hff = new HuffmanAlg();
		try 
		{
				ObjectOutputStream out = new ObjectOutputStream(bs);
				out.writeObject(dict); //writing the dictionary to bytearray to send to the decompress algorithem
				out.writeObject(bit); //writing the code to bytearray to send to the decompress algorithem
				out.close();
				byte[] bi = hff.decompress(bs.toByteArray());
				ByteArrayInputStream fg = new ByteArrayInputStream(bi);
				ObjectInputStream hg = new ObjectInputStream(fg);
				String text = hg.readUTF();
				hg.close();
				String []res = text.split("]");
				Problem prob = new Problem();
				prob.setCommand(res[0]);
				prob.setName(res[1]);
				prob.setUser(res[2]);
				if (res.length==3)
				{
					return prob;
				}
				else if (res[3].equals("F"))
				{
					prob.setFname(res[4]);
				}
				else
				{
					Maze temp = new Maze();
					String []ForM = res[4].split(" "); //split in the maze itself to know its variables
					temp.setRows(Integer.parseInt(ForM[0])); 
					temp.setColls(Integer.parseInt(ForM[1]));
					temp.setStart(new ActuallState(Integer.parseInt(ForM[2]),Integer.parseInt(ForM[3])));
					temp.setGoal(new ActuallState(Integer.parseInt(ForM[4]),Integer.parseInt(ForM[5])));
					temp.setMaze(new Cell[temp.getRows()][temp.getColls()]);
					for (int x=0;x<temp.getRows();x++) //initilize matrix
					{
						for (int y=0;y<temp.getColls();y++)
						{
							temp.getMaze()[x][y] = new Cell(x,y);
						}
					}
					int r=0,c=0;
					for (int j=6;j<ForM.length-1;j+=2) //setting the walls
					{
						if (ForM[j].equals("true"))
						{
							temp.getMaze()[r][c].setHasDownWall(true);
						}
						else
						{
							temp.getMaze()[r][c].setHasDownWall(false);
						}
						if (ForM[j+1].equals("true"))
						{
							temp.getMaze()[r][c].setHasRightWall(true);
						}
						else
						{
							temp.getMaze()[r][c].setHasRightWall(false);
						}
						c++;
						if (c==temp.getColls())
						{
							r++;
							c=0;
						}
					}
					prob.setM(temp);
				}
				return prob; //return the object problem after decoading
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * Coad huff.
	 *
	 * @param s the s
	 * @return the byte array input stream
	 */
	public ByteArrayInputStream coadHuff(SolveProblem s)
	{
		HuffmanAlg hff = new HuffmanAlg();
		String res = s.toString();
		try 
		{
			byte[] b = hff.compress(res.getBytes());
			ByteArrayInputStream by = new ByteArrayInputStream(b);
			return by;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null; //just for function
	}
	
	/**
	 * Save huff.
	 * didnt had time to do it
	 */
	@SuppressWarnings("unchecked")
	public void SaveHuff()
	{
		String str = "";
		for (Maze temp:map.keySet()) //converting the map into string
		{
			str = str + temp.toString();
			str = str + map.get(temp).toString();
		}
		HuffmanAlg hff = new HuffmanAlg();
		try 
		{
			byte[] b = hff.compress(str.getBytes());
			ByteArrayInputStream by = new ByteArrayInputStream(b);
			ObjectInputStream f = new ObjectInputStream(by);
			HashMap<Bits, Character> dict;
			Bits bf;
			try 
			{
				dict = (HashMap<Bits, Character>) f.readObject();
				bf = (Bits) f.readObject();
				f.close();
				ObjectOutputStream c = new ObjectOutputStream(new FileOutputStream("Save.hff")); //writing the bits to a file
				c.writeObject(dict);
				c.writeObject(bf);
				c.close();
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			}
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Load huff.
	 * load from huff file 
	 * didnt had time to do it
	 */
	@SuppressWarnings("unchecked")
	public void LoadHuff()
	{
		File check = new File("Save.hff");
		if (check.length() == 0)
		{
			return;
		}
		else
		{
			HuffmanAlg hff = new HuffmanAlg();
			try 
			{
				ObjectInputStream o = new ObjectInputStream(new FileInputStream("Save.hff"));
				ByteArrayOutputStream by = new ByteArrayOutputStream();
				HashMap<Bits, Character> dict;
				try 
				{
					dict = (HashMap<Bits, Character>) o.readObject(); //reading from the compressed file the dictionary
					Bits bf = (Bits) o.readObject(); //reading from the compressed file the code
					o.close(); 
					ObjectOutputStream out = new ObjectOutputStream(by);
					out.writeObject(dict); //writing the dictionary to bytearray to send to the decompress algorithem
					out.writeObject(bf); //writing the code to bytearray to send to the decompress algorithem
					out.close();
					byte[] b = hff.decompress(by.toByteArray());
					ByteArrayInputStream fg = new ByteArrayInputStream(b);
					ObjectInputStream hg = new ObjectInputStream(fg);
					String text = hg.readUTF();
					hg.close();
					String []res = text.split(","); //split the string into mazes and solutions
					for (int i=0;i<res.length;i+=2)
					{
						Maze temp = new Maze();
						Solution temp2 = new Solution();
						String []ForM = res[i].split(" "); //split in the maze itself to know its variables
						String []ForS = res[i+1].split("/"); //split in the solution itself to know its variables
						temp.setRows(Integer.parseInt(ForM[0])); 
						temp.setColls(Integer.parseInt(ForM[1]));
						temp.setStart(new ActuallState(Integer.parseInt(ForM[2]),Integer.parseInt(ForM[3])));
						temp.setGoal(new ActuallState(Integer.parseInt(ForM[4]),Integer.parseInt(ForM[5])));
						temp.setMaze(new Cell[temp.getRows()][temp.getColls()]);
						for (int x=0;x<temp.getRows();x++) //initilize matrix
						{
							for (int y=0;y<temp.getColls();y++)
							{
								temp.getMaze()[x][y] = new Cell(x,y);
							}
						}
						int r=0,c=0;
						for (int j=6;j<ForM.length;j+=2) //setting the walls
						{
							if (ForM[j].equals("true"))
							{
								temp.getMaze()[r][c].setHasDownWall(true);
							}
							else
							{
								temp.getMaze()[r][c].setHasDownWall(false);
							}
							if (ForM[j+1].equals("true"))
							{
								temp.getMaze()[r][c].setHasRightWall(true);
							}
							else
							{
								temp.getMaze()[r][c].setHasRightWall(false);
							}
							c++;
							if (c==temp.getColls())
							{
								r++;
								c=0;
							}
						}
						List<Action> moves = new ArrayList<Action>();
						for (int j=0;j<ForS.length;j++) //creating the actions
						{
							String []ForA = ForS[j].split("_");
							double P = Double.parseDouble(ForA[0]);
							String TheMove = ForA[1];
							Action a = new Action(TheMove);
							a.setPrice(P);
							moves.add(a);
						}
						temp2.setMoves(moves);
						map.put(temp,temp2); //setting into the map
					}
				} 
				catch (ClassNotFoundException e) 
				{
					e.printStackTrace();
				} 
			}	 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return;
	}
	
	/**
	 * get the task that the model is doing
	 */
	@Override
	public String gettingTask()
	{
		return this.dealing;
	}
	
}
