package model;

import java.util.concurrent.Callable;

import algorithms.demo.MazeDomain;
import algorithms.mazeGenerators.MazeGenerator;

/**
 * The Class MazeCallable.
 */
public class MazeCallable implements Callable<MazeDomain>
{
	
	/** The row. */
	private int row;
	
	/** The coll. */
	private int coll;
	
	/** The m. */
	private MazeGenerator m;
	
	/** The dig. */
	private boolean dig;
	
	/**
	 * Instantiates a new maze callable.
	 *
	 * @param row the row
	 * @param coll the coll
	 * @param M the m
	 * @param dig the dig
	 */
	public MazeCallable(int row, int coll,MazeGenerator M,boolean dig)
	{
		super();
		this.row = row;
		this.coll = coll;
		this.m = M;
		this.dig = dig;
	}

	/* making maze callable
	 */
	@Override
	public MazeDomain call() throws Exception
	{
		MazeDomain md = new MazeDomain(m,row,coll,dig);
		return md;
	}

}
