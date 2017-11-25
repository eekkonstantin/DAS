import java.rmi.server.ServerNotActiveException;
import java.util.*;
import java.rmi.*;

public interface StocksServerIntf extends Remote
{
	public long[] syncTime() throws RemoteException;
	public String getIP() throws RemoteException, ServerNotActiveException;
    public int Players() throws RemoteException;
    public Player CreatePlayer(ClientIntf c) throws RemoteException;
    public void StartGame() throws RemoteException;
    public boolean GameStats() throws RemoteException;
    public void BroadCast(String s) throws RemoteException;
    public void MoveStocks()throws RemoteException;
}
