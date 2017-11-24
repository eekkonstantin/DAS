import java.rmi.server.ServerNotActiveException;
import java.rmi.RemoteException;
import java.util.*;

public interface StocksServer extends java.rmi.Remote
{
	public long[] syncTime() throws java.rmi.RemoteException;
	public String getIP() throws java.rmi.RemoteException, ServerNotActiveException;
    public void NewPlayer() throws java.rmi.RemoteException;
}
