package view;

import java.util.Set;


/**
 * The Interface View.
 */
public interface View 
{
	
	/**
	 * Start.
	 */
	public void Start();
	
	/**
	 * Update show u.
	 *
	 * @param h the h
	 */
	public void updateShowU(Set<String> h);
	
	/**
	 * Update show t.
	 *
	 * @param task the task
	 */
	public void updateShowT(String task);
	
	/**
	 * Gets the remove u.
	 *
	 * @return the string
	 */
	public String GetRemoveU();
	
	/**
	 * update the admin if needed
	 * 
	 * @param string update
	 */
	public void UpdateAdmin(String update);
}
