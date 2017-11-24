import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.util.Random;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StocksServerImpl extends java.rmi.server.UnicastRemoteObject implements StocksServer
{
    static ArrayList<String> IPAddresses = new ArrayList<String>();
    static ArrayList<Player> Players = new ArrayList<Player>();
    //Stock Apple = new Stock();
    int id = 1;
    
    
	protected StocksServerImpl() throws RemoteException
    {
		super();
	}
    
	public long[] syncTime() throws RemoteException
    {
		long[] time = new long[2];
		time[0] = System.currentTimeMillis() ;
		try
        {
			TimeUnit.SECONDS.sleep(1);
			time[1] = System.currentTimeMillis();
		}
        catch (InterruptedException e)
        {
			e.printStackTrace();
		}
		return time;
	}

	public String getIP() throws RemoteException, ServerNotActiveException
    {
		String IPAddress = RemoteServer.getClientHost();
		IPAddresses.add(IPAddress);
		return IPAddress;
	}
    
    public void NewPlayer() throws RemoteException
    {
         Player p = new Player(id, 100.0, 0);
         id++;
         Players.add(p);
        
    }
}
