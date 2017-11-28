import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.*;

public class PlayerClient extends UnicastRemoteObject implements PlayerClientIf {
  private static String host = "localhost";
  private static int port = 1099;
  public static final ArrayList<String> COMMANDS = new ArrayList<>(
    Arrays.asList("buy", "sell", "pass", "status", "quit")
  );

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

/**
   * Checks whether input conforms to the command syntax.
   * @param  String test  Input string to check.
   * @return              Whether input is valid.
   */
  public static boolean testInput(String test) {
    String[] t = test.split(" ");
    switch (t.length) {
      case 2:
        int ok = 0;
        if (t[0].equalsIgnoreCase("buy") || t[0].equalsIgnoreCase("sell"))
          ok++;
        if (t[1].matches("^[0-9]*([,]{1}[0-9]{0,2}){0,1}$"))
          ok++;
        return (ok == 2);
      case 1:
        return COMMANDS.indexOf(t[0]) > 1;
    }
    return false;
  }
  /**
   * Static method to display instructions.
   */
  public static void instructions() {
    try {
      GameServer.broadcast("To see current status of the game, use\t\t\t[ status ]");
      GameServer.broadcast("To buy or sell shares, use\t[ buy | sell ] [ No. of shares ]");
    //  GameServer.broadcast("To pass, use\t\t\t[ pass ]");
      GameServer.broadcast("To quit, use\t\t\t[ quit ]");
    } catch (Exception e) {
      System.out.println("To see current status of the game, use\t\t\t[ status ]");
      System.out.println("To buy or sell shares, use\t[ buy | sell ] [ No. of shares ]");
    //  System.out.println("To pass, use\t\t\t[ pass ]");
      System.out.println("To quit, use\t\t\t[ quit ]");
    }

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
      //boolean quit = false;
      me = gi.getPlayer(pID);
      instructions();
      System.out.print("Command: ");
      while (gi.isOn()) {
        Scanner scanner = new Scanner(System.in);
        // get input
        String out = scanner.nextLine();
        while (!testInput(out)) {
          instructions();
          System.out.print("Command: ");
          out = scanner.nextLine();
        }

        gi.action(me, out);
        
      }

    } catch(Exception e) {
      e.printStackTrace();
    }
  }

}
