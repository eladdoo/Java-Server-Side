package view;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import Messages.MessageType;

/**
 * The Class MyServerWindow.
 */
public class MyServerWindow extends BasicWindow implements View
{
	
	/** The show u. */
	private Text showU;
	
	/** The message box. */
	private MessageBox messageBox = new MessageBox(shell,SWT.ICON_INFORMATION | SWT.OK);
	
	/** The show t. */
	private Text showT;
	
	/** The removed user. */
	private String rmUser;
	
	/**
	 * Instantiates a new my server window.
	 *
	 * @param title the title
	 * @param width the width
	 * @param height the height
	 */
	public MyServerWindow(String title, int width, int height)
	{
		super(title, width, height);
	}
	
	/**
	 * start the main loop in basic window
	 */
	@Override
	public void Start()
	{
		this.run();
	}

	/**
	 * shell init widget
	 */
	@Override
	void initWidgets() 
	{
		shell.setLayout(new GridLayout(2,false));
		Button st = new Button(shell,SWT.PUSH);
		st.setText("Start Server");
		st.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
		st.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				setChanged();
				notifyObservers(MessageType.START);
				st.setEnabled(false);
			}
		});
		Button rm = new Button(shell,SWT.PUSH);
		rm.setText("Remove User");
		rm.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
		rm.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {}

			@Override
			public void widgetSelected(SelectionEvent paramSelectionEvent) 
			{
				InputDialog dlg = new InputDialog(shell,SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL,"Remove Choose","Please Choose A User To Remove");
				rmUser = dlg.open();
				setChanged();
				notifyObservers(MessageType.Remove);
			}
		});
		Button stop = new Button(shell,SWT.PUSH);
		stop.setText("Stop Server");
		stop.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
		stop.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				setChanged();
				notifyObservers(MessageType.EXIT);
				shell.close();
			}
			
		});
		showU=new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		showU.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,1,2));
		showT=new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		showT.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,1,2));
	}
	
	/**
	 * update the users names in the shell
	 */
	@Override
	public void updateShowU(Set<String> h)
	{
		display.syncExec(new Runnable(){

			@Override
			public void run() 
			{
				String users = "";
				for (String s:h)
				{
					users = users + s + "\n";
				}
				showU.setText(users);
				showU.redraw();
				
			}
			
		});
	}
	
	/**
	 * update the task the model is doing right now
	 */
	@Override
	public void updateShowT(String task)
	{
		display.syncExec(new Runnable(){

			@Override
			public void run() 
			{
				showT.setText(task);
				showT.redraw();
			}	
		});
	}
	
	/**
	 * getting the chosen removed user
	 */
	@Override
	public String GetRemoveU()
	{
		return this.rmUser;
	}
	
	/**
	 * update the admin if needed
	 * 
	 * @param string update
	 */
	@Override
	public void UpdateAdmin(String update)
	{
		messageBox.setText("Information");
	    messageBox.setMessage(update);
	    messageBox.open();
	}

}
