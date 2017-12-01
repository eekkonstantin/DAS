/* AUTHORSHIP STATEMENT
* TEAM 13
* DAS(H) COMPSCI 4019
* This is our own work as defined in the Academic Ethics agreement we have signed.
*
* This Game.java contains all functions that were used to run the game on the server
*/

import java.util.*;
import java.rmi.RemoteException;
import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

public class Game implements GameIf, Serializable {
  // Initiate cash per client
  public static final double STARTCASH = 100.0;
  // Min and Max players
  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 3;
  // The duration of the game
  public static final int RUNTIME = 300000;
  // The interval of stocks movement
  public static final int INTERVAL = 10000;
  // Array to stall all commands for switch-case
  public static final ArrayList<String> COMMANDS = new ArrayList<>(
    Arrays.asList("buy", "sell", "pass", "status", "quit")
  );

  private int shares;
  private Stock stock;
  private Watcher watcher;
  // Players in game
  private ArrayList<Player> players;
  // Game stats: true if running, false if not running
  private boolean ongoing;
  // To increment the amount of players in this game
  private int counter = 0;

  /**
   * Class of Game to contain essential details in an ongoing game
   */
  public Game(String stockName) {
    this.stock = new Stock(stockName);
    this.watcher = new Watcher(stock);
    this.players = new ArrayList<>();
  }

/**
 *  setName changes the stocks name to first player stocks preference name
 *
 */
  public void setName(String stockName) throws RemoteException {
    if (!ongoing) {
      this.stock = new Stock(stockName);
      this.watcher = new Watcher(stock);
      this.players = new ArrayList<>();
      GameServer.broadcast("Stock name set to " + stockName + ".");
    }
  }

  /**
   * Check if the player numbers are not between {@code MIN_PLAYERS} and
   * {@code MAX_PLAYERS} Set ongoing to false. Else set ongoing to
   * true, broadcast to clients the start of game and status. Create new thread
   * to concurrently affect stock price every 10 seconds and broadcast to clients
   * while the game is ongoing. Stocks price will stop moving when the game ends
   *
   * @return ongoing as true to represent successfully running
   */

  public boolean start() throws RemoteException {
    if (players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS) {
      ongoing = false;

    } else {
      ongoing = true;
      GameServer.broadcast("===================== NEW GAME =====================");
      GameServer.broadcast("Welcome to stocks simulator! Where every 5 second shares will have movement!");
      display();

      Thread t = new Thread()
      {
          public void run()
          {
            try
            {
            long duration = System.currentTimeMillis() + RUNTIME;
            boolean stat = isOn();
            while(stat)
            {
              stat = isOn();
              if(stat)
              {
                if (System.currentTimeMillis() > duration)
                {
                    endGame();
                    System.exit(0);
                }

                Thread.sleep(INTERVAL);
                affectStock(watcher.type());
                GameServer.broadcast("===================== SHARE PRICE =====================");
                GameServer.broadcast(stock.toString() + "\n");
              }
            }
            System.out.print(" end" + isOn());
          }
          catch (InterruptedException e) {
                System.out.println("interrupted");
                e.printStackTrace();
            }
           catch(Exception e){
              System.out.println("interrupted, ending game");
              try {
                endGame();
              } catch (Exception ex) {
                System.err.println("Error ending game");
                e.printStackTrace();
              }
            }
          }
      };
      t.start();
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
   * is full. Add player into players array, initialise new player class and
   * broadcast to all clients.
   * @return counter
   */
  public int addPlayer() throws RemoteException {
    int cursize = players.size();
    if (cursize == MAX_PLAYERS) {
      return -1;
    }
    players.add(new Player(++counter, STARTCASH, this));
    GameServer.broadcast("Player " + counter + " has joined the game.");
    return counter;
  }

  /**
   * Adds a player to the game if there is space available. Returns -1 if game
   * is full.
   * @param String name Player name to use and GameIf game interface
   * @return Player ID. -1 if not added.
   */
  public int addPlayer(String name, GameIf game) throws RemoteException {
    int cursize = players.size();
    if (cursize == MAX_PLAYERS) {
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
  public Player getPlayer(int id) throws RemoteException {
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

  /**
  * Get every player stock information and detail
  * @return stock detail of players
  */
  public String getInfo() throws RemoteException {
    String out = "\n" + stock;
    for (Player p : players)
      out += "\n" + p.toString(stock);
    return out + "\n";
  }

  /**
   * Triggers stock price fluctuation.
   * @param int type  {@code MINOR} or {@code MAJOR} change
   */
  public void affectStock(int type){
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
      out += "\nWINNER! " + players.get(0).toString(stock);
    else {
      double topScore = 0;
      for (Player p : players) {
        if (p.getWorth(stock) >= topScore)
          topScore = p.getWorth(stock);
      }

      for (Player p : players) {
        out += "\n" + (p.getWorth(stock) == topScore ? "WINNER! " : "") + p.toString(stock);
        System.out.println(p.getWorth(stock) + "================");
      }
    }
    GameServer.broadcastEnd(out);
    ongoing = false;
  }

/**
 * Get commands entered by client
 * @param  Player p    To know which player is sending commands
 * @param  String out  Values entered by client after validation
 */
  public void action(Player p, String out) throws RemoteException
  {

    String[] input = out.split(" ");
    switch (COMMANDS.indexOf(input[0])) {
      case 0: // buy
        buy(Integer.parseInt(input[1]), p);
        break;
      case 1: // sell
        sell(Integer.parseInt(input[1]), p);
        break;
      case 2: // pass
        break;
      case 3: // status
        GameServer.message(getInfo(), p.getID());
        break;
      case 4: // quit
        quit();
        players.remove(p);
        if (players.size() == 1)
          endGame();
        break;
      }
  }

  /**
   * Synchronised function that lock shares quantity of a stock
   * Allows the player to sell their shares. If the player entered a value that
   * is larger than what he is holding, all remaining shares will be sold
   * @param  int    sell  Number of shares the player wishes to sell
   * @param  Player p     Which player entered this command
   */
  public synchronized void sell(int sell, Player player) throws RemoteException
  {
    Player p = findPlayer(player);

    System.err.println(p.name() + " trying to sell " + sell);
    int sold = 0;
    while (sell > 0) {
      if (p.shares == 0)
        break;
      sell--;
      p.shares--;
      p.cash += stock.getPrice();
      stock.returned();
      sold++;
      System.err.println(p.name() + " trying to sell " + sell);
    }
    p.cash = Double.valueOf(Watcher.df.format(p.cash));
    // setPlayer(p);
    GameServer.broadcast(p.name() + " sold " + sold + " shares.");
  }

  private Player findPlayer(Player player) throws RemoteException {
    for (Player p : players)
      if (p.getID() == player.getID())
        return p;

    return null;
  }
/**
 * To iterate the array to the entered player
 * @param  Player p Desired player
 */
  private void setPlayer(Player p) throws RemoteException {
    for (int i=0; i<players.size(); i++) {
      if (players.get(i).getID() == p.getID()) {
        players.set(i, p);
        break;
      }
    }
  }

/**
 * Synchronised function that lock shares quantity of a stock
 * Allows the player to buy shares. If the player entered a value that
 * is larger than how much is can afford, he will purchase the max amount of
 * shares he can afford
 * @param  int buy quantity of shares to purchase
 * @param  Player p targeted player to change detail
 */
  public synchronized void buy(int buy, Player player) throws RemoteException
  {
    Player p = findPlayer(player);

    int bought = 0;
    while (buy > 0) {
      if (p.cash < stock.getPrice() || stock.getPrice() <= 0)
        break;
      p.cash -= stock.getPrice();
      stock.sell();
      p.shares++;
      bought++;
      buy--;
    }
    p.cash = Double.valueOf(Watcher.df.format(p.cash));
    // setPlayer(p);
    GameServer.broadcast(p.name() + " bought " + bought + " shares at $" + stock.getPrice());
  }

  /**
   * Return holding shares of client that quit back to the share
   */
  public void quit() throws RemoteException
  {
    stock.returned(shares);
  }
}
