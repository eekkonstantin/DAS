/* AUTHORSHIP STATEMENT
* TEAM 13
* DAS(H) COMPSCI 4019
* This is our own work as defined in the Academic Ethics agreement we have signed.
*
* This GameServer.java contains method that manage clients on the server.
*/

import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.*;
import java.util.*;

public class GameServer extends UnicastRemoteObject implements GameServerIf {

  // Array of Client Interface
  public static ArrayList<PlayerClientIf> members = new ArrayList<>();

  public GameServer() throws RemoteException {
    super();
  }

  /**
   * Add new client into the array of clients
   * @param  PlayerClientIf  member Player Interface of new client
   */
  public void joinChannel(PlayerClientIf member) throws RemoteException {
    GameServer.broadcast(member.getName() + " has joined the channel.");
    members.add(member);
  }

/**
 * Remove client from the array of clients
 * @param  PlayerClientIf  member member Player Interface of new client
 */
  public void quitChannel(PlayerClientIf member) throws RemoteException {
      GameServer.broadcast(member.getName() + " has quit the channel.");
      int id = 0;
      for (int i=0; i<members.size(); i++) {
        if (members.get(i).getID() == member.getID())
          members.remove(member);
      }
  }

/**
 * To call the callback method in all clients interface
 * @param  String s String to be printed out to all clients
 */
  public static void broadcast(String s) throws RemoteException {
    System.out.println(s);
    for (PlayerClientIf member: GameServer.members) {
      member.callback(s);
    }
  }

/**
 * To call the callback method in a selected client
 * @param  String s  String to be printed out
 * @param  int pID   pID of the select player
 */
  public static void message(String s, int pID) throws RemoteException {
    for (PlayerClientIf member : GameServer.members) {
      if (member.getID() == pID) {
        member.callback(s);
        break;
      }
    }
  }

/**
 * Main method to bind game server to host and port
 */
  public static void main(String[] args) {
      String host = "localhost";
      int port = 1099;

      try {
        GameServer gs = new GameServer();
        Game game = new Game("Default");
        GameIf gameIf = (GameIf) UnicastRemoteObject.exportObject(game, 0);
        Naming.rebind("//"+host+":"+port+"/Game", gameIf);
        Naming.rebind("//"+host+":"+port+"/GameServer", gs);
        System.out.println("Server ready to establish connection with client.");
      } catch(Exception e) {
        e.printStackTrace();
      }
    }

/**
 * Add clients to lobby first before starting the game
 * Wait for client to enter ready
 * if more than 2 game will start, else hold the client in lobby
 * Holds the client in the lobby until there are more than 2 users
 * Allows the first connected client to change the stock name
 * @return user 2nd args if received else return Default to get default stock name
 */
  public static String lobby() {
    System.out.println(
      "When you're ready to start, type: [ ready ] [ stock name ]" +
      "\nThe stock name is optional. Only the first name received will be used."
    );
    String in = "";
    Scanner sc = new Scanner(System.in);
    while (!in.split(" ", 2)[0].equalsIgnoreCase("ready")) {
      System.out.print("Ready? ");
      in = sc.nextLine();
    }

    String[] s = in.split(" ", 2);
    if (s.length > 1)
      return s[1];

    return "Default";
  }

}
