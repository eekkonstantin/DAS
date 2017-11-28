import java.rmi.server.ServerNotActiveException;
import java.util.*;
import java.rmi.*;

public interface PlayerClientIf extends Remote {
  public String getName() throws RemoteException;
  public void callback(String s) throws RemoteException;
}
