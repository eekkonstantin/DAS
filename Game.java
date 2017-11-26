/* AUTHORSHIP STATEMENT
Elizabeth Konstantin Kwek Jin Li (2287563K)
DAS(H) COMPSCI 4019
This is my own work as defined in the Academic Ethics agreement I have signed.
*/

import java.util.*;
import java.rmi.RemoteException;
import java.io.Serializable;

public class Game implements GameIf, Serializable {
  public static final double STARTCASH = 100.0;
  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 2;

  public static final ArrayList<String> COMMANDS = new ArrayList<>(
    Arrays.asList("buy", "sell", "pass", "status", "quit")
  );

  private Stock stock;
  private Watcher watcher;

  private ArrayList<Player> players;
  private boolean ongoing;
  private int counter = 0;

  public Game(String stockName) {
    this.stock = new Stock(stockName);
    this.watcher = new Watcher(stock);
    this.players = new ArrayList<>();
  }

  public void setName(String stockName) throws RemoteException {
    if (!ongoing) {
      this.stock = new Stock(stockName);
      this.watcher = new Watcher(stock);
      this.players = new ArrayList<>();
      GameServer.broadcast("Stock name set to " + stockName + ".");
    }
  }

  /**
   * Starts the game if player numbers are between {@code MIN_PLAYERS} and
   * {@code MAX_PLAYERS}.
   * @return Whether the game has been successfully started.
   */
  public boolean start() throws RemoteException {
    if (players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS) {
      ongoing = false;

    } else {
      ongoing = true;
      GameServer.broadcast("************* NEW GAME *************");
      display();
    }
    return ongoing;
  }

  /**
   * @return Whether the game is ongoing.
   */
  public boolean isOn() {
    return ongoing;
  }

  /**
   * Adds a player to the game if there is space available. Returns -1 if game
   * is full.
   * @return Player ID. -1 if not added.
   */
  public int addPlayer() throws RemoteException {
    int cursize = players.size();
    if (cursize == MAX_PLAYERS) {
      // GameServer.broadcast("Too many players. Please wait for a player to quit.");
      return -1;
    }
    players.add(new Player(++counter, STARTCASH, this));
    GameServer.broadcast("Player " + counter + " has joined the game.");
    return counter;
  }
  /**
   * Adds a player to the game if there is space available. Returns -1 if game
   * is full.
   * @param String name Player name to use
   * @return Player ID. -1 if not added.
   */
  public int addPlayer(String name, GameIf game) throws RemoteException {
    int cursize = players.size();
    if (cursize == MAX_PLAYERS) {
      // GameServer.broadcast("Too many players. Please wait for a player to quit.", name);
      return -1;
    }
    players.add(new Player(++counter, STARTCASH, game, name));
    GameServer.broadcast("Player " + name + " has joined the game.");
    return counter;
  }

  /**
   * Retrieves the player with the given id.
   * @param  int id Player ID
   * @return        Player from {@code players} list.
   */
  public Player getPlayer(int id) {
    for (Player p : players) {
      if (p.getID() == id)
        return p;
    }
    return null;
  }

  /**
   * @return Number of players currently in the game.
   */
  public int players() {
    return players.size();
  }

  /**
   * Displays the current game statistics: Stock availability and prices, and
   * each player's assets.
   */
  public void display() throws RemoteException {
    GameServer.broadcast(getInfo());
  }

  public String getInfo() throws RemoteException {
    String out = "\n" + stock;
    for (Player p : players)
      out += "\n" + p;
    return out + "\n";
  }

  /**
   * Triggers stock price fluctuation.
   * @param int type  {@code MINOR} or {@code MAJOR} change
   */
  public void affectStock(int type) throws RemoteException {
    GameServer.broadcast("");
    watcher.change(type);
  }

  /**
   * Interface implementation.
   * Removes a player when the player requests to `quit`.
   * @param Player player Player to remove
   */
  public void removePlayer(Player player) {
    players.remove(player);
    try {
      if (players.size() == 1)
        endGame();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Triggers end of game. If there is only one player left, the player wins by
   * default. Otherwise, this method is called when a timer or counter expires.
   */
  public void endGame() throws RemoteException {
    String out = "\n************* GAME ENDED *************";
    if (players.size() == 1)
      out += "\nWINNER! " + players.get(0);
    else {
      double topScore = 0;
      for (Player p : players) {
        if (p.getWorth() >= topScore)
          topScore = p.getWorth();
      }

      for (Player p : players)
        out += "\n" + (p.getWorth() == topScore ? "WINNER! " : "") + p;
    }
    GameServer.broadcast(out);
    ongoing = false;
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
      GameServer.broadcast("To pass, use\t\t\t[ pass ]");
      GameServer.broadcast("To quit, use\t\t\t[ quit ]");
    } catch (Exception e) {
      System.out.println("To see current status of the game, use\t\t\t[ status ]");
      System.out.println("To buy or sell shares, use\t[ buy | sell ] [ No. of shares ]");
      System.out.println("To pass, use\t\t\t[ pass ]");
      System.out.println("To quit, use\t\t\t[ quit ]");
    }
  }




}
