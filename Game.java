/* AUTHORSHIP STATEMENT
Elizabeth Konstantin Kwek Jin Li (2287563K)
DAS(H) COMPSCI 4019
This is my own work as defined in the Academic Ethics agreement I have signed.
*/

import java.util.*;
import java.rmi.RemoteException;
import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

public class Game implements GameIf, Serializable {
  public static final double STARTCASH = 100.0;
  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 3;
  private int shares;
  

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
      GameServer.broadcast("===================== NEW GAME =====================");
      GameServer.broadcast("Welcome to stocks simulator! Where every 5 second shares will have movement!");
      // new NewsFlash(this, Watcher.MINOR).run();
      display();
      
      Thread t = new Thread()
      {
          public void run() 
          {
            try
            {
            //GameServer.broadcast("Stock name set to ");
            while(isOn())
            {

                Thread.sleep(10000);
                affectStock(watcher.type());
                GameServer.broadcast("===================== SHARE PRICE =====================");
                GameServer.broadcast(stock.toString());
            } 
          }
          catch (InterruptedException e) {
                System.out.println("interrupted");
                e.printStackTrace();
            }
           catch(Exception e){
              System.out.println("interrupted");      // Always must return something
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
      case 3: // quit
        quit();
        players.remove(p);
        if (players.size() == 1)
          endGame();
      }
  }
 /**
   * Get input from the player.
   * @return whether the player quit.
   */
 
 /**
   * Allows the player to sell their shares. If the player does not have enough
   * shares to sell, all remaining shares will be sold.
   * @param int sell   Number of shåares the player wishes to sell
   */
  public synchronized void sell(int sell, Player p) throws RemoteException
  {
    int sold = 0;
    while (sell > 0) {
      if (shares == 0)
        break;
      sell--;
      shares--;
      p.cash += stock.getPrice();
      stock.returned();
      sold++;
    }
    p.cash = Double.valueOf(Watcher.df.format(p.cash));
    GameServer.broadcast(p.name + " " + sold + " shares sold.");
  }


  public synchronized void buy(int buy, Player p) throws RemoteException
  {
    int bought = 0;
    while (buy > 0) {
      if (p.cash < stock.getPrice() || stock.getPrice() <= 0)
        break;
      p.cash -= stock.getPrice();
      stock.sell();
      shares++;
      bought++;
      buy--;
    }
    p.cash = Double.valueOf(Watcher.df.format(p.cash));
    GameServer.broadcast(p.name + " bought " + bought + " shares at " + stock.getPrice());
  }

  /**
   * Allows the player to leave the game, returning all shares back to the stock.
   */
  public synchronized void quit() throws RemoteException
  {
    stock.returned(shares);
    /*
    try {
      GameServer.broadcast(
        "Player " + name() + " has left the game. " +
        shares + " shares have been returned to the stock."
      );
    } catch (Exception e) {
      System.out.println(
        "Player " + name() + " has left the game. " +
        shares + " shares have been returned to the stock."
      );
    }
    */
  }


}
