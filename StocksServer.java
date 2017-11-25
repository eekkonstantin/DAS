import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class StocksServer extends UnicastRemoteObject implements StocksServerIntf
{

    public static final double STARTCASH = 100.0;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 3;

    static ArrayList<String> IPAddresses = new ArrayList<String>();
    static ArrayList<Player> Players = new ArrayList<Player>();
    static ArrayList<ClientIntf> Clients = new ArrayList<ClientIntf>();
    int id = 0;
    Boolean ongoing = false;
    Game game = new Game("Apple");
    
	protected StocksServer() throws RemoteException
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

    public void BroadCast(String s) throws RemoteException
    {
        for (ClientIntf Client: Clients) 
        {
            Client.CallBack(s);
        }
    }

    public void MoveStocks()throws RemoteException
    {
        game.affectStock(Watcher.type());
    }

	public String getIP() throws RemoteException, ServerNotActiveException
    {
		String IPAddress = RemoteServer.getClientHost();
		IPAddresses.add(IPAddress);
		return IPAddress;
	}

    public Player CreatePlayer(ClientIntf c) throws RemoteException
    {
        Player p = new Player(id, 100.0, null);
        id++;
        Players.add(p);
        Clients.add(c);
        c.CallBack("You are in ready stat now!");
        BroadCast("There are currently " + Clients.size() + " players that are ready!");
        return p;
    }
    
    public int Players() throws RemoteException
    {
        return Clients.size();
    }

    public boolean GameStats() 
    {
        return game.isOn();
    }

    public void StartGame() throws RemoteException
    {
        BroadCast("Host has started the game!");
        BroadCast("There are " + Players.size() + " players in game!");
        for (Player players: Players) 
        {
             players.setGame(game);
             game.addPlayer();
        }
       
        game.start();

    }

    public static void main(String args[]) {

        String host = "localhost";
        int port = 1099;
            
        try 
        {
            StocksServer ss = new StocksServer();

            Naming.rebind("//"+host+":"+port+"/StocksServer", ss);

            System.out.println("StocksServer is ready for action!");

        } 
        catch(RemoteException e) 
        {
            System.out.println("Exception in RMIServerImp.main " + e);
        } 
        catch(MalformedURLException ue) 
        {
            System.out.println("MalformedURLException in RMIServerImp.main " + ue);
        }
    }
}
