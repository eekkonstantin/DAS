import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.*;

public class PlayerClient extends UnicastRemoteObject implements PlayerClientIf {
  private static String host = "localhost";
  private static int port = 1099;

  private Player me;
  private String name;

  public PlayerClient(String name) throws RemoteException {
    super();
    this.name = name;
  }

  public PlayerClient(String h, int p) throws RemoteException {
	  super();
	  host = h;
	  port = p;
    this.name = name;
	}

  public String getName() {
    return name;
  }

  public void callback(String s) {
    System.out.println(s);
  }



  public static void main(String[] args) {
    String name = "Undefined";
    if (args.length > 0)
      name = args[0];

    try {
      PlayerClient pc = new PlayerClient(name);
      GameIf gi = (GameIf) Naming.lookup("rmi://"+ host +":"+port+"/Game");
      GameServerIf gs = (GameServerIf) Naming.lookup("rmi://"+ host +":"+port+"/GameServer");

      // Connect player to broadcast channel
      gs.joinChannel(pc);

      String stockName = GameServer.lobby();
      // Check if first player. Set stock name if first.
      if (gi.players() == 0)
        gi.setName(stockName);

      // Try to join game
      int pID = gi.addPlayer(name, gi);
      while (pID == -1) {
        if (gi.isOn()) { // player limit exceeded.
          System.out.println("Too many players. Please wait for a player to quit.");
          stockName = GameServer.lobby();
        } else { // previous game ended, or game not started yet
          if (gi.players() == 0) // previous game ended
            gi.setName(stockName);
        }
        pID = gi.addPlayer(name, gi);
      }

      Player me = gi.getPlayer(pID);


      // Try to start game if not ongoing
      if (!gi.isOn()) {
        if (!gi.start()) { // insufficient players.
          GameServer.broadcast("Unable to start game with " +
            gi.players() + " players. Need " +
            Game.MIN_PLAYERS + " to " + Game.MAX_PLAYERS +
            " players to start."
          );
          while (!gi.isOn()) {
            if (gi.players() == 0)
              gi.setName(stockName);
            if (gi.start()) {
              break;
            }
          }
        } else { // game not started.
          if (gi.players() == 0)
            gi.setName(stockName);
        }
      }

      // ************* NEW GAME *************
      boolean quit = false;
      me = gi.getPlayer(pID);
      while (gi.isOn()) {
        quit = me.getInput();
        if (quit)
          System.exit(0);
      }

    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
