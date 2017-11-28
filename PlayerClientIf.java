import java.rmi.server.ServerNotActiveException;
import java.util.*;
import java.rmi.*;

public interface PlayerClientIf extends Remote {
  /**
   * Get name of PlayerClient
   * @return name of PlayerClient
   */
  public String getName() throws RemoteException;
  /**
   * Get ID of PlayerClient
   * @return PlayerClient ID
   */
  public int getID() throws RemoteException;
  /**
   * Call back function for server to print to client
   * @param String s text to print
   */
  public void callback(String s) throws RemoteException;
}
