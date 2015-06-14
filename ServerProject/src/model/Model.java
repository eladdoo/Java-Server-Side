package model;

import algorithms.demo.MazeDomain;

/**
 * The Interface Model.
 */
public interface Model 
{
	
	/**
	 * Generate maze.
	 *
	 * @param name the name
	 * @param rows the rows
	 * @param colls the colls
	 * @param diag the diag
	 */
	public void generateMaze(String name,int rows,int colls,boolean diag);
	
	/**
	 * Gets the maze.
	 *
	 * @param p the p
	 * @return the maze
	 */
	public MazeDomain getMaze(Problem p);
	
	/**
	 * Solve maze.
	 *
	 * @param name the name
	 * @param m the m
	 */
	public void solveMaze(String name,MazeDomain m);

	/**
	 * Stop.
	 */
	public void stop();
	
	/**
	 * Load xml fill.
	 *
	 * @param file the file
	 * @return true, if successful
	 */
	public boolean loadXmlFill(String file);
	
	/**
	 * Gets the ting task.
	 *
	 * @return the ting task
	 */
	public String gettingTask();
}
