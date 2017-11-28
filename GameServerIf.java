/* AUTHORSHIP STATEMENT
* TEAM 13
* DAS(H) COMPSCI 4019
* This is our own work as defined in the Academic Ethics agreement we have signed.
*
* This GameServerIf.java interface file contains all callable functions in GameServer.java.
* This file allows PlayerClient to connect and use functions in Game.java
*/


import java.rmi.server.ServerNotActiveException;
import java.util.*;
import java.rmi.*;

public interface GameServerIf extends Remote {
  /**
   * Add new client into the array of clients
   * @param  PlayerClientIf  member Player Interface of new client
   */
  public void joinChannel(PlayerClientIf member) throws RemoteException;
  /**
   * Remove client from the array of clients
   * @param  PlayerClientIf  member member Player Interface of new client
   */
  public void quitChannel(PlayerClientIf member) throws RemoteException;
}
