package boot;

import presenter.Presenter;
import view.MyServerWindow;
import model.MyClientHandler;
import model.MyServer;

public class Run 
{//asd
	public static void main(String[] args) 
	{
		MyClientHandler cl = new MyClientHandler();
		MyServerWindow win = new MyServerWindow("My Server",500,500);
		MyServer serv = new MyServer(cl);
		Presenter p = new Presenter(cl,win,serv);
		cl.addObserver(p);
		win.addObserver(p);
		serv.addObserver(p);
		win.Start();
	}
}
