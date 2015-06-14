package model;

import java.util.concurrent.Callable;

import algorithms.mazeGenerators.Searchable;
import algorithms.search.Searcher;
import algorithms.search.Solution;

/**
 * The Class SolutionCallable.
 */
public class SolutionCallable implements Callable<Solution>
{
	
	/** The m. */
	Searchable m;
	
	/** The s. */
	Searcher s;
	
	/**
	 * Instantiates a new solution callable.
	 *
	 * @param m the m
	 * @param s the s
	 */
	public SolutionCallable(Searchable m, Searcher s) 
	{
		super();
		this.m = m;
		this.s = s;
	}


	/* 
	 * making solution callabe (thread)
	 */
	@Override
	public Solution call() throws Exception 
	{
		return s.search(m);
	}

}
