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
   * @param boolean quit whether to quit after broadcast
   */
  public void callback(String s, boolean quit) throws RemoteException;
}
